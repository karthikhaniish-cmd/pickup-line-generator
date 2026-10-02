// RIZZAI — AI Pickup Line Generator Engine

// State Management
let currentLanguage = 'Tanglish';
let currentStyle = 'Funny';
let currentConfidence = 'Casual';
let currentCount = 5;

// LocalStorage Keys
const FAV_KEY = 'rizzai_favorites';
const HIST_KEY = 'rizzai_history';

document.addEventListener('DOMContentLoaded', () => {
  setupChips();
  setupForm();
  setupSituationChange();
  setupSurpriseButtons();
  setupMobileNav();
  loadFavorites();
  loadHistory();
});

// Setup Chip selection handlers
function setupChips() {
  // Language
  document.querySelectorAll('#languageGroup .chip').forEach(chip => {
    chip.addEventListener('click', () => {
      document.querySelectorAll('#languageGroup .chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
      currentLanguage = chip.dataset.value;
    });
  });

  // Style
  document.querySelectorAll('#styleGroup .chip').forEach(chip => {
    chip.addEventListener('click', () => {
      document.querySelectorAll('#styleGroup .chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
      currentStyle = chip.dataset.value;
    });
  });

  // Confidence
  document.querySelectorAll('#confidenceGroup .chip').forEach(chip => {
    chip.addEventListener('click', () => {
      document.querySelectorAll('#confidenceGroup .chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
      currentConfidence = chip.dataset.value;
    });
  });

  // Count
  document.querySelectorAll('#countGroup .chip').forEach(chip => {
    chip.addEventListener('click', () => {
      document.querySelectorAll('#countGroup .chip').forEach(c => c.classList.remove('active'));
      chip.classList.add('active');
      currentCount = parseInt(chip.dataset.value, 10);
    });
  });
}

// Situation dropdown custom input toggle
function setupSituationChange() {
  const sitSelect = document.getElementById('situationSelect');
  const customGroup = document.getElementById('customSituationGroup');
  sitSelect.addEventListener('change', () => {
    if (sitSelect.value === 'Custom') {
      customGroup.style.display = 'block';
    } else {
      customGroup.style.display = 'none';
    }
  });
}

// Surprise Me Buttons
function setupSurpriseButtons() {
  const surpriseHandler = () => {
    const styles = ['Funny', 'Cute', 'Romantic', 'Clever', 'Confident', 'Flirty', 'Smooth', 'Sarcastic', 'Nerdy', 'Short & Simple', 'Wholesome'];
    const confidences = ['Shy', 'Casual', 'Confident', 'Bold'];
    const situations = ['First time talking', 'Instagram DM', 'WhatsApp chat', 'College', 'Friend', 'Crush', 'Dating app'];
    const languages = ['Tanglish', 'English', 'Tamil + English'];

    currentStyle = styles[Math.floor(Math.random() * styles.length)];
    currentConfidence = confidences[Math.floor(Math.random() * confidences.length)];
    const chosenSit = situations[Math.floor(Math.random() * situations.length)];
    currentLanguage = languages[Math.floor(Math.random() * languages.length)];

    // Sync UI
    selectChip('#styleGroup', currentStyle);
    selectChip('#confidenceGroup', currentConfidence);
    selectChip('#languageGroup', currentLanguage);
    document.getElementById('situationSelect').value = chosenSit;
    document.getElementById('customSituationGroup').style.display = 'none';

    scrollToGenerator();
    generateLines();
  };

  document.getElementById('surpriseHeroBtn')?.addEventListener('click', surpriseHandler);
  document.getElementById('surpriseMeBtn')?.addEventListener('click', surpriseHandler);
}

function selectChip(groupId, value) {
  document.querySelectorAll(`${groupId} .chip`).forEach(c => {
    if (c.dataset.value.startsWith(value)) {
      c.classList.add('active');
    } else {
      c.classList.remove('active');
    }
  });
}

// Mobile Nav Toggle
function setupMobileNav() {
  const btn = document.getElementById('hamburgerBtn');
  const links = document.getElementById('navLinks');
  btn?.addEventListener('click', () => {
    links.classList.toggle('show');
  });
}

// Form Submission
function setupForm() {
  const form = document.getElementById('rizzForm');
  form.addEventListener('submit', (e) => {
    e.preventDefault();
    generateLines();
  });
}

// API Generation
async function generateLines() {
  const nameInput = document.getElementById('personName');
  const sitSelect = document.getElementById('situationSelect');
  const customSit = document.getElementById('customSituation');

  let situation = sitSelect.value;
  if (situation === 'Custom' && customSit.value.trim()) {
    situation = customSit.value.trim();
  }

  const payload = {
    name: nameInput.value.trim(),
    situation: situation,
    language: currentLanguage,
    style: currentStyle,
    confidence: currentConfidence,
    count: currentCount
  };

  // UI States
  const loading = document.getElementById('loadingState');
  const empty = document.getElementById('emptyState');
  const list = document.getElementById('resultsList');
  const genBtn = document.getElementById('generateBtn');

  loading.style.display = 'flex';
  empty.style.display = 'none';
  list.innerHTML = '';
  genBtn.disabled = true;
  genBtn.innerHTML = '<span>Cooking up some rizz... 🔥</span>';

  try {
    const res = await fetch('/api/generate', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify(payload)
    });

    const data = await res.json();
    loading.style.display = 'none';
    genBtn.disabled = false;
    genBtn.innerHTML = '<span>Generate Rizz 🔥</span>';

    if (data.success && data.lines && data.lines.length > 0) {
      renderResults(data.lines);
      saveToHistory(data.lines, payload);
      document.getElementById('resultsCountBadge').textContent = `${data.lines.length} Lines`;
      if (data.demo_mode) {
        showToast('✨ Curated Rizz Engine active');
      }
    } else {
      empty.style.display = 'flex';
      empty.querySelector('h4').textContent = "Oops! The AI got shy 😅";
      empty.querySelector('p').textContent = "Try generating again or select another style.";
    }
  } catch (err) {
    console.error('Generation error:', err);
    loading.style.display = 'none';
    genBtn.disabled = false;
    genBtn.innerHTML = '<span>Generate Rizz 🔥</span>';
    empty.style.display = 'flex';
    empty.querySelector('h4').textContent = "Connection hiccup!";
    empty.querySelector('p').textContent = "Please check your network and try again.";
    showToast('Failed to reach server');
  }
}

// Render Results
function renderResults(lines) {
  const container = document.getElementById('resultsList');
  container.innerHTML = '';

  const favorites = getFavorites();

  lines.forEach((line) => {
    const isFav = favorites.some(f => f.text === line.text);
    const card = document.createElement('div');
    card.className = 'result-card';

    card.innerHTML = `
      <div class="result-header">
        <span class="style-badge style-funny">${line.style.toUpperCase()}</span>
        <span class="result-meta">${line.tone || currentConfidence} • ${currentLanguage}</span>
      </div>
      <div class="result-text">“${escapeHtml(line.text)}”</div>
      <div class="result-footer">
        <span class="result-meta">AI Generated</span>
        <div class="card-actions">
          <button class="icon-btn ${isFav ? 'favorited' : ''}" onclick="toggleFavorite(this, '${escapeQuotes(line.text)}', '${escapeQuotes(line.style)}', '${escapeQuotes(line.tone || currentConfidence)}')" title="Favorite">
            ❤️
          </button>
          <button class="icon-btn" onclick="copyText('${escapeQuotes(line.text)}')" title="Copy Line">
            📋
          </button>
          <button class="icon-btn" onclick="shareLine('${escapeQuotes(line.text)}')" title="Share Line">
            ↗️
          </button>
        </div>
      </div>
    `;
    container.appendChild(card);
  });
}

// Quick Presets
window.applyPreset = function(style, situation) {
  currentStyle = style;
  selectChip('#styleGroup', style);
  document.getElementById('situationSelect').value = situation || 'Instagram DM';
  document.getElementById('customSituationGroup').style.display = 'none';
  scrollToGenerator();
  generateLines();
};

// Clipboard Copy
window.copyText = function(text) {
  navigator.clipboard.writeText(text).then(() => {
    showToast('Copied! ✓');
  }).catch(() => {
    // Fallback
    const input = document.createElement('textarea');
    input.value = text;
    document.body.appendChild(input);
    input.select();
    document.execCommand('copy');
    document.body.removeChild(input);
    showToast('Copied! ✓');
  });
};

// Share
window.shareLine = function(text) {
  const shareData = {
    title: 'RIZZAI Pickup Line',
    text: `Check out this pickup line generated by RIZZAI 😎\n\n"${text}"`
  };

  if (navigator.share) {
    navigator.share(shareData).catch(() => {});
  } else {
    window.copyText(`Check out this pickup line generated by RIZZAI 😎\n\n"${text}"`);
    showToast('Share link copied to clipboard!');
  }
};

// Toast notification
function showToast(message) {
  const toast = document.getElementById('toast');
  if (!toast) return;
  toast.textContent = message;
  toast.style.display = 'block';
  setTimeout(() => {
    toast.style.display = 'none';
  }, 2500);
}

// Navigation scroll
window.scrollToGenerator = function() {
  const gen = document.getElementById('generator');
  if (gen) gen.scrollIntoView({ behavior: 'smooth' });
};

// FAVORITES MANAGEMENT
function getFavorites() {
  try {
    return JSON.parse(localStorage.getItem(FAV_KEY)) || [];
  } catch (e) {
    return [];
  }
}

window.toggleFavorite = function(btn, text, style, tone) {
  let favs = getFavorites();
  const index = favs.findIndex(f => f.text === text);

  if (index >= 0) {
    favs.splice(index, 1);
    btn.classList.remove('favorited');
    showToast('Removed from favorites');
  } else {
    favs.unshift({ text, style, tone, timestamp: Date.now() });
    btn.classList.add('favorited');
    showToast('Added to favorites ❤️');
  }

  localStorage.setItem(FAV_KEY, JSON.stringify(favs));
  loadFavorites();
};

function loadFavorites() {
  const list = document.getElementById('favoritesList');
  const empty = document.getElementById('favoritesEmpty');
  const countBadge = document.getElementById('favCount');
  const favs = getFavorites();

  if (countBadge) countBadge.textContent = favs.length;

  if (!list || !empty) return;

  if (favs.length === 0) {
    list.innerHTML = '';
    empty.style.display = 'flex';
    return;
  }

  empty.style.display = 'none';
  list.innerHTML = '';

  favs.forEach((fav, i) => {
    const card = document.createElement('div');
    card.className = 'result-card';
    card.innerHTML = `
      <div class="result-header">
        <span class="style-badge style-funny">${fav.style.toUpperCase()}</span>
        <span class="result-meta">${fav.tone || 'Casual'}</span>
      </div>
      <div class="result-text">“${escapeHtml(fav.text)}”</div>
      <div class="result-footer">
        <span class="result-meta">${new Date(fav.timestamp).toLocaleDateString()}</span>
        <div class="card-actions">
          <button class="icon-btn favorited" onclick="removeFavorite(${i})" title="Remove">🗑️</button>
          <button class="icon-btn" onclick="copyText('${escapeQuotes(fav.text)}')" title="Copy">📋</button>
          <button class="icon-btn" onclick="shareLine('${escapeQuotes(fav.text)}')" title="Share">↗️</button>
        </div>
      </div>
    `;
    list.appendChild(card);
  });
}

window.removeFavorite = function(index) {
  let favs = getFavorites();
  favs.splice(index, 1);
  localStorage.setItem(FAV_KEY, JSON.stringify(favs));
  loadFavorites();
  showToast('Removed from favorites');
};

window.clearFavorites = function() {
  if (confirm('Clear all saved favorites?')) {
    localStorage.removeItem(FAV_KEY);
    loadFavorites();
    showToast('Favorites cleared');
  }
};

// HISTORY MANAGEMENT
function getHistory() {
  try {
    return JSON.parse(localStorage.getItem(HIST_KEY)) || [];
  } catch (e) {
    return [];
  }
}

function saveToHistory(newLines, payload) {
  let hist = getHistory();
  newLines.forEach(line => {
    hist.unshift({
      text: line.text,
      style: line.style,
      language: payload.language,
      situation: payload.situation,
      timestamp: Date.now()
    });
  });
  // Limit to 50 recent items
  hist = hist.slice(0, 50);
  localStorage.setItem(HIST_KEY, JSON.stringify(hist));
  loadHistory();
}

function loadHistory() {
  const list = document.getElementById('historyList');
  const empty = document.getElementById('historyEmpty');
  const hist = getHistory();

  if (!list || !empty) return;

  if (hist.length === 0) {
    list.innerHTML = '';
    empty.style.display = 'flex';
    return;
  }

  empty.style.display = 'none';
  list.innerHTML = '';

  hist.forEach(item => {
    const card = document.createElement('div');
    card.className = 'result-card';
    card.innerHTML = `
      <div class="result-header">
        <span class="style-badge style-funny">${item.style}</span>
        <span class="result-meta">${item.language} • ${item.situation}</span>
      </div>
      <div class="result-text">“${escapeHtml(item.text)}”</div>
      <div class="result-footer">
        <span class="result-meta">${new Date(item.timestamp).toLocaleTimeString([], { hour: '2-digit', minute: '2-digit' })}</span>
        <div class="card-actions">
          <button class="icon-btn" onclick="copyText('${escapeQuotes(item.text)}')" title="Copy">📋</button>
          <button class="icon-btn" onclick="shareLine('${escapeQuotes(item.text)}')" title="Share">↗️</button>
        </div>
      </div>
    `;
    list.appendChild(card);
  });
}

window.clearHistory = function() {
  localStorage.removeItem(HIST_KEY);
  loadHistory();
  showToast('History cleared');
};

// Helpers
function escapeHtml(str) {
  return str.replace(/&/g, "&amp;").replace(/</g, "&lt;").replace(/>/g, "&gt;");
}

function escapeQuotes(str) {
  return str.replace(/'/g, "\\'").replace(/"/g, '\\"');
}
