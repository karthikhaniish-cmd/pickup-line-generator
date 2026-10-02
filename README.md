# RIZZAI — AI Pickup Line Generator

> **Your AI Wingman for Better Conversations.**  
> Generate funny, cute, confident, clever, wholesome, and creative conversation starters in seconds with native **Tanglish**, English, and Tamil support.

---

## ⚡ Core Features

- **Native Tanglish Nuance**: Unlike basic translators, RIZZAI understands and generates spoken colloquial Tamil written in Latin letters (e.g., *"Un smile paathale enakku WiFi signal full bars varudhu 📶"*).
- **Multi-Language Support**:
  - Tanglish
  - English
  - Tamil
  - Tamil + English
  - Malayalam
  - Hindi
- **11+ Styles & Moods**:
  - Funny 😂
  - Cute 🥰
  - Romantic ❤️
  - Clever 🧠
  - Confident 😎
  - Flirty 😉
  - Smooth ✨
  - Sarcastic 😏
  - Nerdy 🤓
  - Short & Simple ⚡
  - Wholesome 🌸
- **4 Confidence Calibrations**: Shy, Casual, Confident, Bold.
- **Context & Situations**: First time talking, Instagram DM, WhatsApp chat, College, Friend, Crush, Dating app, Birthday, Compliment, Custom.
- **1-Click Quick Presets**: *Make Me Laugh*, *Cute Line*, *Smooth Line*, *Flirty Line*, *Clever Line*, *Wholesome Line*, *Confident Line*.
- **Surprise Me 🎲**: Instant randomized cocktail of style, context, confidence, and language.
- **Offline Vault & Room Database**: Save favorite lines and browse full generation history.
- **Clipboard & Share**: 1-click copy with toast feedback and native Web / Android share intents.
- **Curated Demo Mode**: Works out-of-the-box even without an API key configured!

---

## 📱 Dual-Platform Support

This project includes both:
1. **Native Android Application** (Kotlin + Jetpack Compose + Material Design 3 + Room DB)
2. **Full-Stack Web Application** (Python + Flask + Modern Responsive HTML5/CSS3/JS)

---

## 🌐 Running the Web Application Locally

### 1. Requirements
- Python 3.9+
- `pip`

### 2. Setup Virtual Environment
```bash
# Linux / macOS
python3 -m venv venv
source venv/bin/activate

# Windows
python -m venv venv
venv\Scripts\activate
```

### 3. Install Dependencies
```bash
pip install -r requirements.txt
```

### 4. Configure Environment
Create or edit `.env`:
```env
GEMINI_API_KEY=your_gemini_api_key_here
GEMINI_TEXT_MODEL=gemini-3.5-flash
DEMO_MODE=false
```
*(If `GEMINI_API_KEY` is not provided or left as placeholder, Demo Mode will automatically activate with the built-in curated rizz engine).*

### 5. Run Server
```bash
python app.py
```
Open **`http://127.0.0.1:5000`** in your browser.

---

## 📲 Android Project Architecture

- **`app/src/main/java/com/example/MainActivity.kt`**: Main activity with edge-to-edge support, dynamic theme, and back handling.
- **`com.example.ui.theme`**: Custom dark Gen-Z theme (`#080B14`, `#8B5CF6`, `#EC4899`, `#3B82F6`).
- **`com.example.ui.components`**:
  - `RizzCard`: Interactive line card with style badges, favorite toggle, copy, share, and regenerate actions.
  - `CookingLoader`: Animated pulsing flame loader with skeleton placeholders.
  - `RizzNavbar`: Top bar with status and bottom navigation tabs.
- **`com.example.ui.screens`**:
  - `HomeScreen`: Hero banner, quick presets, feature highlights, and sample card.
  - `GeneratorScreen`: Interactive form + live card results.
  - `FavoritesScreen`: Offline saved lines stored in Room DB.
  - `HistoryScreen`: Chronological log with clear action.
  - `AboutScreen`: Mission, Tanglish philosophy, and safety pledge.
- **`com.example.data.local`**: Room Database (`RizzDatabase`, `RizzDao`) with reactive `Flow` queries.
- **`com.example.data.remote`**:
  - `GeminiRizzService`: Gemini 3.5 Flash REST client via OkHttp with 60s timeout and JSON parsing.
  - `CuratedRizzEngine`: Offline and Demo Mode engine with hundreds of curated Tanglish, Tamil, English, and regional lines.

---

## 🔒 Safety & Respect Guidelines

RIZZAI strictly adheres to respectful conversation rules:
- **No sexual or explicit content**
- **No harassment, threats, manipulation, or degradation**
- **No insults targeting protected groups**
- **Emphasis on humor, clever wordplay, friendly teasing, and genuine compliments**
