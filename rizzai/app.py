import os
import json
import random
import requests
from flask import Flask, render_template, request, jsonify
from dotenv import load_dotenv

# Load environment variables
load_dotenv()

app = Flask(__name__, template_folder='templates', static_folder='static')

GEMINI_API_KEY = os.getenv("GEMINI_API_KEY", "").strip()
GEMINI_TEXT_MODEL = os.getenv("GEMINI_TEXT_MODEL", "gemini-3.5-flash").strip()
DEMO_MODE = os.getenv("DEMO_MODE", "false").lower() == "true" or not GEMINI_API_KEY or GEMINI_API_KEY == "MY_GEMINI_API_KEY"

# Curated high-voltage fallback rizz lines
CURATED_LINES = {
    "Tanglish": [
        {"text": "Un kitta pesanum nu nenachen... aana en brain already loading screen la stuck aayiduchu 😂", "style": "Funny", "tone": "Casual"},
        {"text": "Un smile pathale enakku WiFi signal full bars varuthu 📶", "style": "Funny", "tone": "Casual"},
        {"text": "Un smile paatha udane en day motthama bright aayiduchu ✨", "style": "Cute", "tone": "Casual"},
        {"text": "First time pesuren, aana already romba familiar madhiri feel aaguthu 🥰", "style": "Cute", "tone": "Casual"},
        {"text": "Naan un kitta pesradhukku oru reason venum nu nenachen... ippo un smile-e periya reason ❤️", "style": "Romantic", "tone": "Confident"},
        {"text": "Un presence enakku favorite song on repeat kekra madhiri irukku ✨", "style": "Romantic", "tone": "Casual"},
        {"text": "En algorithm un profile paatha udane 100% match nu predict panniduchu 🧠", "style": "Clever", "tone": "Confident"},
        {"text": "Are you made of Copper and Tellurium? Because you are definitely Cu-Te!", "style": "Clever", "tone": "Casual"},
        {"text": "Enakkum theriyum, unakkum theriyum... namma conversation fire-ah irukka poguthu 🔥", "style": "Confident", "tone": "Bold"},
        {"text": "First move naan eduthuten, adutha reply un choice 😉", "style": "Confident", "tone": "Bold"},
        {"text": "Un smile romba dangerous ah irukku... fine potralama? 😉", "style": "Flirty", "tone": "Confident"},
        {"text": "I was looking for a good conversation, and universe pointed me right to you ✨", "style": "Smooth", "tone": "Confident"},
        {"text": "Are you always this charming or is today a special screening for me? 😏", "style": "Sarcastic", "tone": "Confident"},
        {"text": "Are you an uncaught exception? Because my system just halted 🤓", "style": "Nerdy", "tone": "Casual"},
        {"text": "Un smile: 10/10. Had to drop by and say hi ⚡", "style": "Short & Simple", "tone": "Casual"},
        {"text": "I hope your day is as bright and kind as your smile 🌸", "style": "Wholesome", "tone": "Shy"}
    ],
    "English": [
        {"text": "Are you Wi-Fi? Because I'm feeling a really strong connection 📶", "style": "Funny", "tone": "Casual"},
        {"text": "If you were a vegetable, you'd be a cute-cumber 🥒", "style": "Cute", "tone": "Casual"},
        {"text": "If a thousand artists painted for a thousand years, they couldn't capture your warmth ❤️", "style": "Romantic", "tone": "Confident"},
        {"text": "Are you a 90-degree angle? Because you are looking acute! 🧠", "style": "Clever", "tone": "Casual"},
        {"text": "I usually wait for people to say hi, but you're definitely worth breaking the rule 😎", "style": "Confident", "tone": "Bold"},
        {"text": "Do you believe in love at first swipe, or should I scroll past again? 😉", "style": "Flirty", "tone": "Confident"},
        {"text": "Life is too short for boring conversations—let's make this one interesting ✨", "style": "Smooth", "tone": "Confident"},
        {"text": "Are you a CSS file? Because you definitely give my day style 🤓", "style": "Nerdy", "tone": "Casual"},
        {"text": "10/10 smile. Just had to let you know ⚡", "style": "Short & Simple", "tone": "Casual"},
        {"text": "I hope your coffee is hot, your wifi is fast, and your day is great 🌸", "style": "Wholesome", "tone": "Shy"}
    ],
    "Tamil": [
        {"text": "உன் சிரிப்பை பார்த்தாலே என் நாள் முழுக்க வெளிச்சமாகி விடுகிறது ✨", "style": "Cute", "tone": "Casual"},
        {"text": "உன்னிடம் பேச ஒரு காரணம் தேடினேன், இப்போது அந்த காரணமே நீதான்! ❤️", "style": "Romantic", "tone": "Confident"},
        {"text": "நூறு பேர் பார்த்தாலும், உன் புன்னகை தரும் சந்தோஷம் தனி தான்!", "style": "Smooth", "tone": "Casual"}
    ]
}

@app.route('/')
def index():
    return render_template('index.html', demo_mode=DEMO_MODE)

@app.route('/api/generate', methods=['POST'])
def generate_pickup_lines():
    try:
        data = request.get_json() or {}
        name = data.get("name", "").strip()
        situation = data.get("situation", "First time talking").strip()
        language = data.get("language", "Tanglish").strip()
        style = data.get("style", "Funny").strip()
        confidence = data.get("confidence", "Casual").strip()
        count = int(data.get("count", 5))
        count = max(1, min(count, 10))

        # Check if Demo Mode is active
        if DEMO_MODE or not GEMINI_API_KEY:
            lines = get_demo_lines(name, situation, language, style, confidence, count)
            return jsonify({
                "success": True,
                "lines": lines,
                "demo_mode": True,
                "message": "Generated in Demo Mode (Curated Engine)"
            })

        # Call Gemini API
        prompt = f"""
Generate exactly {count} pickup lines.
Target Person Name: {name if name else "None specified"}
Situation: {situation}
Language: {language}
Style: {style}
Confidence Level: {confidence}

Crucial Requirements:
1. If language is Tanglish, write natural spoken Tamil in Latin English letters (e.g., 'Un kitta pesanum nu nenachen... aana en brain already loading screen la stuck aayiduchu 😂'). Do not mechanically translate English.
2. Strictly respectful and playful. NO sexual, explicit, coercive, derogatory, or harassment content.
3. Return ONLY a valid JSON object matching this schema:
{{
  "lines": [
    {{
      "text": "pickup line text",
      "style": "{style}",
      "tone": "{confidence}"
    }}
  ]
}}
"""

        system_instruction = (
            "You are RIZZAI, a creative pickup-line writer. "
            "Generate playful, respectful, original pickup lines based on the user's situation. "
            "Understand English, Tamil, Tanglish, and mixed Tamil-English naturally. "
            "When the requested language is Tanglish, write natural spoken Tamil using English/Latin letters. "
            "Do not mechanically translate English. Match the requested mood, style, confidence, and situation. "
            "Keep the lines concise and conversational. Avoid: sexual content, explicit content, harassment, "
            "manipulation, threats, degrading language. Return ONLY valid JSON."
        )

        url = f"https://generativelanguage.googleapis.com/v1beta/models/{GEMINI_TEXT_MODEL}:generateContent?key={GEMINI_API_KEY}"
        payload = {
            "system_instruction": {
                "parts": [{"text": system_instruction}]
            },
            "contents": [
                {
                    "parts": [{"text": prompt}]
                }
            ],
            "generationConfig": {
                "response_mime_type": "application/json",
                "temperature": 0.85
            }
        }

        resp = requests.post(url, json=payload, timeout=45)
        if resp.status_code != 200:
            # Fallback to demo lines if API fails
            fallback_lines = get_demo_lines(name, situation, language, style, confidence, count)
            return jsonify({
                "success": True,
                "lines": fallback_lines,
                "demo_mode": True,
                "message": "Gemini API temporarily busy. Loaded from native Rizz engine."
            })

        resp_json = resp.json()
        candidates = resp_json.get("candidates", [])
        if not candidates:
            fallback = get_demo_lines(name, situation, language, style, confidence, count)
            return jsonify({"success": True, "lines": fallback, "demo_mode": True})

        text_content = candidates[0].get("content", {}).get("parts", [{}])[0].get("text", "")
        # Clean potential markdown fences
        text_content = text_content.strip()
        if text_content.startswith("```json"):
            text_content = text_content[7:]
        elif text_content.startswith("```"):
            text_content = text_content[3:]
        if text_content.endswith("```"):
            text_content = text_content[:-3]
        text_content = text_content.strip()

        parsed = json.loads(text_content)
        lines = parsed.get("lines", [])

        # Validate lines
        valid_lines = []
        for item in lines:
            if isinstance(item, dict) and "text" in item and isinstance(item["text"], str):
                valid_lines.append({
                    "text": item["text"].strip(),
                    "style": item.get("style", style),
                    "tone": item.get("tone", confidence)
                })

        if not valid_lines:
            valid_lines = get_demo_lines(name, situation, language, style, confidence, count)
            return jsonify({"success": True, "lines": valid_lines, "demo_mode": True})

        return jsonify({
            "success": True,
            "lines": valid_lines,
            "demo_mode": False
        })

    except Exception as e:
        # Graceful fallback to curated engine
        fallback = get_demo_lines(
            request.json.get("name", "") if request.is_json else "",
            "Chat",
            "Tanglish",
            "Funny",
            "Casual",
            5
        )
        return jsonify({
            "success": True,
            "lines": fallback,
            "demo_mode": True,
            "message": "AI got a little shy 😅 Native engine delivered instead!"
        })

def get_demo_lines(name, situation, language, style, confidence, count):
    pool = CURATED_LINES.get(language, CURATED_LINES["Tanglish"])
    # Filter matching style if possible
    style_clean = style.split()[0]
    matched = [item for item in pool if style_clean.lower() in item["style"].lower()]
    if not matched:
        matched = pool

    # Sample without repetition
    sample_pool = matched if len(matched) >= count else pool
    selected = random.sample(sample_pool, min(count, len(sample_pool)))

    # Personalize with name if provided
    result = []
    for item in selected:
        txt = item["text"]
        if name and not txt.startswith(name):
            txt = f"{name}, {txt}"
        result.append({
            "text": txt,
            "style": item["style"],
            "tone": confidence
        })
    return result

if __name__ == '__main__':
    port = int(os.getenv("PORT", 5000))
    app.run(host='0.0.0.0', port=port, debug=True)
