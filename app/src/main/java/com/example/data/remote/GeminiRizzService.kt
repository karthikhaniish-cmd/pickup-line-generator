package com.example.data.remote

import android.util.Log
import com.example.BuildConfig
import com.example.data.model.GenerationRequest
import com.example.data.model.PickupLine
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.util.concurrent.TimeUnit

class GeminiRizzService {

    private val client = OkHttpClient.Builder()
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    data class GenerationResult(
        val lines: List<PickupLine>,
        val isDemoMode: Boolean,
        val errorMessage: String? = null
    )

    suspend fun generatePickupLines(request: GenerationRequest): GenerationResult = withContext(Dispatchers.IO) {
        val apiKey = try {
            BuildConfig.GEMINI_API_KEY
        } catch (e: Exception) {
            ""
        }

        // If no API key is provided, or placeholder, immediately use Demo Mode
        if (apiKey.isBlank() || apiKey == "MY_GEMINI_API_KEY") {
            Log.d("GeminiRizzService", "Using Demo Mode (no API key configured)")
            val demoLines = CuratedRizzEngine.generateCuratedLines(request)
            return@withContext GenerationResult(lines = demoLines, isDemoMode = true)
        }

        val prompt = buildPrompt(request)
        val systemInstructionText = """
            You are RIZZAI, a creative pickup-line writer.
            Generate playful, respectful, original pickup lines based on the user's situation.
            Understand English, Tamil, Tanglish, and mixed Tamil-English naturally.
            When the requested language is Tanglish, write natural spoken Tamil using English/Latin letters.
            Do not mechanically translate English.
            Match the requested mood, style, confidence, and situation.
            Keep the lines concise and conversational.
            Avoid:
            - sexual content
            - explicit content
            - harassment
            - insults targeting protected groups
            - manipulation
            - threats
            - degrading language
            - pressure or coercion
            Prefer:
            - humor
            - clever wordplay
            - harmless compliments
            - confidence
            - friendly teasing
            - creativity
            Return ONLY a valid JSON object matching this schema:
            {
              "lines": [
                {
                  "text": "pickup line string",
                  "style": "${request.style}",
                  "tone": "${request.confidence}"
                }
              ]
            }
        """.trimIndent()

        try {
            val rootJson = JSONObject().apply {
                put("system_instruction", JSONObject().apply {
                    put("parts", JSONArray().apply {
                        put(JSONObject().apply { put("text", systemInstructionText) })
                    })
                })
                put("contents", JSONArray().apply {
                    put(JSONObject().apply {
                        put("parts", JSONArray().apply {
                            put(JSONObject().apply { put("text", prompt) })
                        })
                    })
                })
                put("generationConfig", JSONObject().apply {
                    put("response_mime_type", "application/json")
                    put("temperature", 0.85)
                })
            }

            val requestBody = rootJson.toString().toRequestBody("application/json".toMediaType())
            val url = "https://generativelanguage.googleapis.com/v1beta/models/gemini-3.5-flash:generateContent?key=$apiKey"

            val httpRequest = Request.Builder()
                .url(url)
                .post(requestBody)
                .build()

            val response = client.newCall(httpRequest).execute()
            val responseBody = response.body?.string()

            if (!response.isSuccessful || responseBody == null) {
                Log.w("GeminiRizzService", "Gemini API failed with code ${response.code}: $responseBody")
                val fallbackLines = CuratedRizzEngine.generateCuratedLines(request)
                return@withContext GenerationResult(
                    lines = fallbackLines,
                    isDemoMode = true,
                    errorMessage = "Gemini server busy (Demo Mode active)"
                )
            }

            val parsedLines = parseGeminiResponse(responseBody, request)
            if (parsedLines.isNotEmpty()) {
                GenerationResult(lines = parsedLines, isDemoMode = false)
            } else {
                val fallbackLines = CuratedRizzEngine.generateCuratedLines(request)
                GenerationResult(lines = fallbackLines, isDemoMode = true)
            }
        } catch (e: Exception) {
            Log.e("GeminiRizzService", "Exception calling Gemini API: ${e.message}", e)
            val fallbackLines = CuratedRizzEngine.generateCuratedLines(request)
            GenerationResult(
                lines = fallbackLines,
                isDemoMode = true,
                errorMessage = "Network error: AI got shy 😅 Demo mode loaded"
            )
        }
    }

    private fun buildPrompt(request: GenerationRequest): String {
        return buildString {
            append("Generate exactly ${request.count} pickup lines.\n")
            if (request.name.isNotBlank()) {
                append("Target Person Name: ${request.name.trim()}\n")
            }
            append("Situation/Context: ${request.situation}\n")
            append("Language: ${request.language}\n")
            append("Style/Mood: ${request.style}\n")
            append("Confidence Level: ${request.confidence}\n")
            append("Important: Respectful, witty, conversational. If language is Tanglish, use colloquial spoken Tamil in English letters like: 'Un smile paathale enakku WiFi full bars varudhu' or 'First time pesuren aana already connection feel aagudhu'.")
        }
    }

    private fun parseGeminiResponse(rawJson: String, request: GenerationRequest): List<PickupLine> {
        val resultList = mutableListOf<PickupLine>()
        try {
            val root = JSONObject(rawJson)
            val candidates = root.optJSONArray("candidates") ?: return emptyList()
            if (candidates.length() == 0) return emptyList()

            val candidate = candidates.getJSONObject(0)
            val content = candidate.optJSONObject("content") ?: return emptyList()
            val parts = content.optJSONArray("parts") ?: return emptyList()
            if (parts.length() == 0) return emptyList()

            var textContent = parts.getJSONObject(0).optString("text", "")
            textContent = cleanMarkdownFences(textContent)

            val parsedOutput = JSONObject(textContent)
            val linesArray = parsedOutput.optJSONArray("lines") ?: return emptyList()

            for (i in 0 until linesArray.length()) {
                val item = linesArray.optJSONObject(i) ?: continue
                val text = item.optString("text", "").trim()
                if (text.isNotBlank()) {
                    val style = item.optString("style", request.style)
                    val tone = item.optString("tone", request.confidence)
                    resultList.add(
                        PickupLine(
                            text = text,
                            style = style,
                            tone = tone,
                            language = request.language
                        )
                    )
                }
            }
        } catch (e: Exception) {
            Log.e("GeminiRizzService", "Error parsing Gemini JSON: ${e.message}")
        }
        return resultList
    }

    private fun cleanMarkdownFences(text: String): String {
        var cleaned = text.trim()
        if (cleaned.startsWith("```json")) {
            cleaned = cleaned.removePrefix("```json")
        } else if (cleaned.startsWith("```")) {
            cleaned = cleaned.removePrefix("```")
        }
        if (cleaned.endsWith("```")) {
            cleaned = cleaned.removeSuffix("```")
        }
        return cleaned.trim()
    }
}
