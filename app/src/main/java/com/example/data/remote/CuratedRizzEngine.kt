package com.example.data.remote

import com.example.data.model.GenerationRequest
import com.example.data.model.PickupLine
import kotlin.random.Random

object CuratedRizzEngine {

    fun generateCuratedLines(request: GenerationRequest): List<PickupLine> {
        val name = request.name.trim()
        val namePrefix = if (name.isNotBlank()) "$name, " else ""

        val rawPool = when (request.language) {
            "Tanglish", "Tamil + English" -> getTanglishPool(name, request.style, request.situation)
            "Tamil" -> getTamilPool(name, request.style)
            "Malayalam" -> getMalayalamPool(name, request.style)
            "Hindi" -> getHindiPool(name, request.style)
            else -> getEnglishPool(name, request.style, request.situation)
        }

        // Shuffle and pick requested count
        val shuffled = rawPool.shuffled(Random(System.currentTimeMillis()))
        val selected = if (shuffled.size >= request.count) {
            shuffled.take(request.count)
        } else {
            // supplement with high-vibe generic rizz
            val bonus = getTanglishPool(name, "Smooth", request.situation) + getEnglishPool(name, "Smooth", request.situation)
            (shuffled + bonus.shuffled()).distinct().take(request.count)
        }

        return selected.map { text ->
            PickupLine(
                text = text,
                style = request.style,
                tone = request.confidence,
                language = request.language
            )
        }
    }

    private fun getTanglishPool(name: String, style: String, situation: String): List<String> {
        val n = if (name.isNotBlank()) name else "Hey"
        return when (style) {
            "Funny 😂" -> listOf(
                "Un kitta pesanum nu nenachen... aana en brain already loading screen la stuck aayiduchu 😂",
                "Un smile pathale enakku WiFi signal full bars varuthu 📶",
                "Naan Google-la search pannala... but I think I just found my favorite search result!",
                "Un smile-ku separate ah oru notification sound irukkanum, heard once and addicted!",
                "En phone battery 1% irundhalum un profile scroll panna ready ah irukken 😂",
                "Doctor sonnaru sugar kammiya sapdanum nu, aana un story paathale sweet overload aagudhu!",
                "Unkitta pesradhuku oru excuse thedunen, but honestly en rizz battery konjam low ah irukku."
            )
            "Cute 🥰" -> listOf(
                "Un smile paatha udane en day motthama bright aayiduchu ✨",
                "Ellarum stars paaka vaanam paapanga, naan un profile paathaale podhum 🥰",
                "First time pesuren, aana already romba familiar madhiri feel aaguthu.",
                "En playlist-la un favorite song add panna oru chance tharuviya?",
                "Un kitta pesum podhu time speed-ah poradhu physics mistake illa, un charm magic!",
                "World-la evvalavo beautiful things irukku, but un smile hits different."
            )
            "Romantic ❤️" -> listOf(
                "Naan un kitta pesradhukku oru reason venum nu nenachen... ippo un smile-e periya reason.",
                "Un presence enakku favorite song on repeat kekra madhiri irukku ❤️",
                "Kanneer illa, kovam illa, unkitta pesunaale oru peace kedaikkudhu.",
                "En thought process mottham un kitta mattum dhaan loop aagi odudhu.",
                "Un kooda spend panra ovvoru second-um oru memory snapshot madhiri."
            )
            "Clever 🧠" -> listOf(
                "Are you a high refresh rate display? Because you just made everything so smooth 🧠",
                "En algorithm un profile paatha udane 100% match nu predict panniduchu.",
                "Are you made of Copper and Tellurium? Because you are definitely Cu-Te!",
                "Neenga math problem madhiri illa, neat solution madhiri irukkeenga.",
                "En thought cache clear panna kooda un memory wipe aaga maatengudhu."
            )
            "Confident 😎" -> listOf(
                "Enakkum theriyum, unakkum theriyum... namma conversation fire-ah irukka poguthu 🔥",
                "First move naan eduthuten, adutha reply un choice 😉",
                "People say perfection doesn't exist, but clearly they haven't met you yet.",
                "Un DMs-la 100 per irukalaam, but best vibe inga dhaan start aaga pogudhu.",
                "I don't need luck today, en kitta un attention kedaichaale podhum 😎"
            )
            "Flirty 😉" -> listOf(
                "Un smile romba dangerous ah irukku... fine potralama? 😉",
                "En notifications-la un name varum podhu heartbeat skip aaguthu.",
                "Un kitta oru question kekkanum... why are you so illegally attractive?",
                "Naan normally introvert dhaan, aana un smile en extrovert side-ah unlock panniduchu.",
                "Un kitta pesinaale my day gets a 10x upgrade."
            )
            "Smooth ✨" -> listOf(
                "Un kitta oru hi sollanum nu nenachen, but wanted to make it memorable ✨",
                "I was looking for a good conversation, and universe pointed me right to you.",
                "Un vibe matches the aesthetic of everything I like.",
                "Coffee share pannalama nu keta clichéd ah irukkum, so oru smile share pannalama?",
                "Some people bring good energy, but you bring the whole vibe."
            )
            "Sarcastic 😏" -> listOf(
                "I was trying to ignore you, but you insisted on looking this good 😏",
                "Great, ippo naan en work concentrate pannama un DM paathutu irukkanuma?",
                "Are you always this charming or is today a special screening for me?",
                "En expectations romba high nu sonnanga, and then you just walked in."
            )
            "Nerdy 🤓" -> listOf(
                "Are you an uncaught exception? Because my system just halted 🤓",
                "If we were variables, our scope would definitely be global.",
                "En CPU utilization 100% aayiduchu looking at your profile!",
                "Are you Git? Because I want to commit to this conversation."
            )
            "Short & Simple ⚡" -> listOf(
                "Just wanted to say: your vibe is unreal ⚡",
                "Un smile: 10/10 ✨",
                "Coffee, or should we start with a hi?",
                "You just made my day better by existing.",
                "Hands down best energy on my feed today."
            )
            "Wholesome 🌸" -> listOf(
                "I hope your day is as bright and kind as your smile 🌸",
                "Sending you good vibes and a warm hello!",
                "You have that rare energy that genuinely makes people smile.",
                "Just thought someone should remind you today that you're awesome."
            )
            else -> listOf(
                "Un smile paatha udane my brain literally stopped working 😂",
                "First time pesuren, aana already connection feel aaguthu.",
                "Un presence gives major main character energy ✨",
                "Are you Wi-Fi? Because I feel a strong connection 📶"
            )
        }
    }

    private fun getEnglishPool(name: String, style: String, situation: String): List<String> {
        val n = if (name.isNotBlank()) name else "there"
        return when (style) {
            "Funny 😂" -> listOf(
                "Are you Wi-Fi? Because I'm feeling a really strong connection 📶",
                "I'm not a photographer, but I can definitely picture us having a great conversation!",
                "Do you have a map? Because I just got lost in your smile 😂",
                "Are you a keyboard? Because you're definitely my type!",
                "I was going to say something smooth, but you smiled and my RAM cleared."
            )
            "Cute 🥰" -> listOf(
                "If you were a vegetable, you'd be a cute-cumber 🥒",
                "I must be a snowflake, because I've fallen for your vibe ❄️",
                "Are you made of stardust? Because your smile lights up this whole screen.",
                "Is your name Google? Because you have everything I’ve been searching for 🥰",
                "My mom told me not to talk to strangers, but for you I'll make an exception."
            )
            "Romantic ❤️" -> listOf(
                "If a thousand artists painted for a thousand years, they couldn't capture your warmth ❤️",
                "I didn't believe in serendipity until I stumbled across your profile.",
                "You make every ordinary moment feel like a scene from a movie.",
                "My heart skipped a beat, and honestly, you have all the credit for it."
            )
            "Clever 🧠" -> listOf(
                "Are you made of copper and tellurium? Because you are undeniably Cu-Te 🧠",
                "Are you a 90 degree angle? Because you are looking acute!",
                "If we were binary code, we'd definitely be a 10/10.",
                "You must have a gravitational pull, because I'm orbiting your orbit."
            )
            "Confident 😎" -> listOf(
                "I usually wait for people to say hi, but you're definitely worth breaking the rule 😎",
                "They said good things come to those who wait, but I'd rather just say hello right now.",
                "I knew my day was missing something—turns out it was a chat with you.",
                "Let’s skip the small talk: what’s your favorite track to blast in the car?"
            )
            "Flirty 😉" -> listOf(
                "Do you believe in love at first swipe, or should I scroll past again? 😉",
                "Are you a magician? Because whenever I look at your photos, everyone else disappears.",
                "On a scale of 1 to America, how free are you to chat tonight?",
                "Is it hot in here, or did you just send an absolute radiant vibe?"
            )
            "Smooth ✨" -> listOf(
                "I was having a pretty regular day until your profile popped up ✨",
                "I’m not trying to impress you, but your playlist is probably amazing.",
                "Life is too short for boring conversations—let's make this one interesting.",
                "Some people bring good vibes, but you bring the entire soundtrack."
            )
            "Sarcastic 😏" -> listOf(
                "Are you always this distractingly cool, or is today an audition? 😏",
                "I was going to be productive today, but then you had to go and look like that.",
                "Congratulations, you’ve officially won the 'Most Likely to Distract Me' award."
            )
            "Nerdy 🤓" -> listOf(
                "Are you a CSS file? Because you definitely give my day style 🤓",
                "You must be an SSD, because you make everything in my mind run so much faster.",
                "Are you Git commit? Because I want to push my changes right to your branch."
            )
            "Short & Simple ⚡" -> listOf(
                "10/10 smile. Had to tell you ⚡",
                "Just dropping by to say: great vibe!",
                "Coffee or chai person? Let's settle this.",
                "Unmatched aura ✨"
            )
            "Wholesome 🌸" -> listOf(
                "I hope your coffee is hot, your wifi is fast, and your day is as sweet as you 🌸",
                "Just wanted to pass along some genuine positive energy!",
                "Your smile looks like it has healed bad days before."
            )
            else -> listOf(
                "Are you Wi-Fi? Because I'm feeling a connection 📶",
                "Your vibe is so refreshing, I had to say hello!",
                "You look like someone who knows all the best coffee spots."
            )
        }
    }

    private fun getTamilPool(name: String, style: String): List<String> {
        return listOf(
            "உன் சிரிப்பை பார்த்தாலே என் நாள் முழுக்க வெளிச்சமாகி விடுகிறது ✨",
            "உன்னிடம் பேச ஒரு காரணம் தேடினேன், இப்போது அந்த காரணமே நீதான்!",
            "வானத்தில் நிலவு உண்டு, ஆனால் என் திரையில் உன் புன்னகை தான் அழகு.",
            "உன் பார்வை பட்டதும் என் மனசுக்குள் ஒரு பாசிட்டிவ் வைப் பரவுது 🥰",
            "நூறு பேர் பார்த்தாலும், உன் புன்னகை தரும் சந்தோஷம் தனி தான் ❤️"
        )
    }

    private fun getMalayalamPool(name: String, style: String): List<String> {
        return listOf(
            "നിന്റെ ചിരി കണ്ടപ്പോൾ തന്നെ എൻ്റെ മനസ്സ് നിറഞ്ഞു ✨",
            "സംസാരിക്കാൻ ഒരു കാരണം തിരഞ്ഞതായിരുന്നു... ഇപ്പോൾ ആ കാരണം നീ തന്നെ!",
            "ഒരു പാട്ട് കേൾക്കുമ്പോൾ ഉള്ള സന്തോഷം നിന്റെ പ്രൊഫൈൽ കാണുമ്പോൾ കിട്ടുന്നുണ്ട് 🥰",
            "നിന്റെ ആ വൈബ് വളരെ പോസിറ്റീവ് ആണ്, ഹലോ പറയാതിരിക്കാൻ പറ്റിയില്ല!"
        )
    }

    private fun getHindiPool(name: String, style: String): List<String> {
        return listOf(
            "आपकी मुस्कान देख कर ही पूरा दिन बेहतरीन हो गया ✨",
            "आपसे बात करने का कोई बहाना ढूंढ रहा था, पर आपकी एक स्माइल ही काफी थी!",
            "क्या आप गूगल हैं? क्योंकि जो मुझे चाहिए था वो सब आपमें मिल गया 🥰",
            "आपका वाइब इतना कमाल का है कि बिना हाय बोले रहा ही नहीं गया 😎",
            "दुनिया में सब कुछ टेम्परेरी है, पर आपकी स्माइल का जादू परमानेंट है!"
        )
    }
}
