/* =============================================
   APPLICATION JS — Portfolio Stéphane PYRAM-LOUIS
   ============================================= */

document.addEventListener('DOMContentLoaded', () => {
  initNavigation();
  initMobileMenu();
  renderCompetences();
  renderProjects();
  renderMatieres();
  renderMatrix();
  initFilters();
  initProgressBars();
  initModal();
});

/* ============ NAVIGATION ============ */
function initNavigation() {
  const navItems = document.querySelectorAll('.nav-item');
  const sections = document.querySelectorAll('.section');
  const quickNavBtns = document.querySelectorAll('[data-nav]');

  function activateSection(target) {
    navItems.forEach(item => {
      item.classList.toggle('active', item.dataset.target === target);
    });
    sections.forEach(sec => {
      sec.classList.toggle('active', sec.id === target);
    });
    // Scroll top
    window.scrollTo({ top: 0, behavior: 'smooth' });
    // Mobile : ferme le menu
    document.getElementById('sidebar').classList.remove('open');
    // Update hash
    history.replaceState(null, '', `#${target}`);
    // Re-animate progress bars when entering home
    if (target === 'home') {
      setTimeout(initProgressBars, 100);
    }
  }

  navItems.forEach(item => {
    item.addEventListener('click', () => activateSection(item.dataset.target));
  });

  quickNavBtns.forEach(btn => {
    btn.addEventListener('click', (e) => {
      const t = btn.dataset.nav;
      if (t) activateSection(t);
    });
  });

  // Hash sur load
  const initial = window.location.hash.replace('#', '');
  if (initial && document.getElementById(initial)) {
    activateSection(initial);
  }
}

/* ============ MENU MOBILE ============ */
function initMobileMenu() {
  const toggle = document.getElementById('menuToggle');
  const sidebar = document.getElementById('sidebar');
  toggle.addEventListener('click', () => {
    sidebar.classList.toggle('open');
  });
  document.addEventListener('click', (e) => {
    if (window.innerWidth <= 768 &&
        !sidebar.contains(e.target) &&
        !toggle.contains(e.target) &&
        sidebar.classList.contains('open')) {
      sidebar.classList.remove('open');
    }
  });
}

/* ============ COMPÉTENCES RNCP ============ */
function renderCompetences() {
  const grid = document.getElementById('competenceGrid');
  if (!grid) return;

  grid.innerHTML = COMPETENCES.map(c => {
    const bars = Array.from({ length: 4 }, (_, i) =>
      `<div class="level-bar ${i < c.level ? 'active' : ''}"></div>`
    ).join('');

    const acsHTML = c.acs.map(ac => `
      <div class="ac-block">
        <div class="ac-title">${escapeHTML(ac.title)}</div>
        <div class="ac-desc">${escapeHTML(ac.desc)}</div>
        <div class="reflexion-grid">
          <div class="reflexion-item">
            <div class="reflexion-label">✓ Ce que j'ai fait</div>
            <div class="reflexion-text">${escapeHTML(ac.fait)}</div>
          </div>
          <div class="reflexion-item">
            <div class="reflexion-label">? Pourquoi</div>
            <div class="reflexion-text">${escapeHTML(ac.pourquoi)}</div>
          </div>
          <div class="reflexion-item">
            <div class="reflexion-label">⚙ Comment (méthode &amp; outils)</div>
            <div class="reflexion-text">${escapeHTML(ac.comment)}</div>
          </div>
          <div class="reflexion-item">
            <div class="reflexion-label">⚠ Difficultés rencontrées</div>
            <div class="reflexion-text">${escapeHTML(ac.difficultes)}</div>
          </div>
          <div class="reflexion-item">
            <div class="reflexion-label">💡 Ce que j'en ai appris</div>
            <div class="reflexion-text">${escapeHTML(ac.appris)}</div>
          </div>
          <div class="reflexion-item">
            <div class="reflexion-label">↻ Ce que je ferais autrement</div>
            <div class="reflexion-text">${escapeHTML(ac.autrement)}</div>
          </div>
        </div>
      </div>
    `).join('');

    return `
      <article class="competence-card" data-type="${c.type}">
        <header class="competence-header" data-toggle="${c.id}">
          <span class="competence-num">${c.num}</span>
          <div class="competence-info">
            <h3 class="competence-title">${escapeHTML(c.title)}</h3>
            <p class="competence-subtitle">${escapeHTML(c.subtitle)}</p>
          </div>
          <div class="competence-level">
            <div class="level-bars">${bars}</div>
            <span class="level-text">${escapeHTML(c.levelText)}</span>
          </div>
          <svg class="competence-chevron" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
            <polyline points="6 9 12 15 18 9"/>
          </svg>
        </header>
        <div class="competence-body">
          <div class="competence-content">
            <p style="font-size: 14px; color: var(--text-secondary); line-height: 1.7; margin-bottom: 20px;">
              ${escapeHTML(c.description)}
            </p>
            ${acsHTML}
            <div class="competence-section">
              <div class="competence-section-title">📚 Matières liées</div>
              <div class="linked-items">
                ${c.matieres.map(m => `<span class="tag cyan">${escapeHTML(m)}</span>`).join('')}
              </div>
            </div>
            <div class="competence-section">
              <div class="competence-section-title">🎯 SAÉ &amp; projets</div>
              <div class="linked-items">
                ${c.saes.map(s => `<span class="tag green">${escapeHTML(s)}</span>`).join('')}
              </div>
            </div>
            <div class="competence-section">
              <div class="competence-section-title">⚙ Technologies / Outils</div>
              <div class="linked-items">
                ${c.technos.map(t => `<span class="tag violet">${escapeHTML(t)}</span>`).join('')}
              </div>
            </div>
            <div class="competence-section">
              <div class="competence-section-title">🏆 Preuves concrètes</div>
              <p style="font-size: 13px; color: var(--text-secondary); line-height: 1.6;">
                ${escapeHTML(c.preuves)}
              </p>
            </div>
          </div>
        </div>
      </article>
    `;
  }).join('');

  // Accordion
  grid.querySelectorAll('.competence-header').forEach(h => {
    h.addEventListener('click', () => {
      const card = h.closest('.competence-card');
      card.classList.toggle('open');
    });
  });
}

/* ============ PROJETS ============ */
function renderProjects() {
  const grid = document.getElementById('projectsGrid');
  if (!grid) return;

  grid.innerHTML = PROJETS.map(p => {
    const gradeClass = parseFloat(p.grade) >= 14 ? '' : 'medium';
    const tagsHTML = p.tags.map(t => `<span class="tag">${tagLabel(t)}</span>`).join('');
    return `
      <article class="project-card" data-id="${p.id}" data-tags="${p.tags.join(',')}">
        <div class="project-header">
          <span class="project-code">${escapeHTML(p.code)} · ${escapeHTML(p.semestre)}</span>
          <span class="project-grade ${gradeClass}">${escapeHTML(p.grade)}</span>
        </div>
        <h3 class="project-title">${escapeHTML(p.titre)}</h3>
        <p class="project-desc">${escapeHTML(p.description)}</p>
        <div class="project-tags">
          ${p.technos.slice(0, 4).map(t => `<span class="tag">${escapeHTML(t)}</span>`).join('')}
        </div>
        <div class="project-footer">
          <span>${p.competences.map(c => c).join(' · ')}</span>
          <span class="project-link">Voir détails →</span>
        </div>
      </article>
    `;
  }).join('');

  // Click → modal
  grid.querySelectorAll('.project-card').forEach(card => {
    card.addEventListener('click', () => openProjectModal(card.dataset.id));
  });
}

function tagLabel(tag) {
  const map = { cyber: 'Cyber', reseau: 'Réseaux', telecom: 'Télécoms', dev: 'Dev' };
  return map[tag] || tag;
}

/* ============ FILTRES ============ */
function initFilters() {
  // Filtres compétences
  document.querySelectorAll('[data-filter]').forEach(btn => {
    btn.addEventListener('click', () => {
      const f = btn.dataset.filter;
      document.querySelectorAll('[data-filter]').forEach(b => b.classList.toggle('active', b === btn));
      document.querySelectorAll('.competence-card').forEach(card => {
        if (f === 'all') card.style.display = '';
        else card.style.display = card.dataset.type === f ? '' : 'none';
      });
    });
  });

  // Filtres projets
  document.querySelectorAll('[data-projfilter]').forEach(btn => {
    btn.addEventListener('click', () => {
      const f = btn.dataset.projfilter;
      document.querySelectorAll('[data-projfilter]').forEach(b => b.classList.toggle('active', b === btn));
      document.querySelectorAll('.project-card').forEach(card => {
        const tags = (card.dataset.tags || '').split(',');
        if (f === 'all') card.style.display = '';
        else card.style.display = tags.includes(f) ? '' : 'none';
      });
    });
  });
}

/* ============ MODAL PROJET ============ */
function initModal() {
  const backdrop = document.getElementById('modalBackdrop');
  const close = document.getElementById('modalClose');
  close.addEventListener('click', closeModal);
  backdrop.addEventListener('click', (e) => {
    if (e.target === backdrop) closeModal();
  });
  document.addEventListener('keydown', (e) => {
    if (e.key === 'Escape') closeModal();
  });
}

function openProjectModal(id) {
  const p = PROJETS.find(x => x.id === id);
  if (!p) return;
  const content = document.getElementById('modalContent');
  const gradeClass = parseFloat(p.grade) >= 14 ? '' : 'medium';

  content.innerHTML = `
    <h2>${escapeHTML(p.titre)}</h2>
    <div class="modal-meta">
      <span class="tag cyan">${escapeHTML(p.code)}</span>
      <span class="tag">${escapeHTML(p.semestre)}</span>
      <span class="project-grade ${gradeClass}">${escapeHTML(p.grade)} / 20</span>
      ${p.competences.map(c => `<span class="tag violet">${escapeHTML(c)}</span>`).join('')}
    </div>

    <div class="modal-section">
      <h3>📋 Contexte</h3>
      <p>${escapeHTML(p.contexte)}</p>
    </div>

    <div class="modal-section">
      <h3>🎯 Objectifs</h3>
      <ul>
        ${p.objectifs.map(o => `<li>${escapeHTML(o)}</li>`).join('')}
      </ul>
    </div>

    <div class="modal-section">
      <h3>⚙ Réalisation</h3>
      <p>${escapeHTML(p.realisation)}</p>
    </div>

    <div class="modal-section">
      <h3>🛠 Technologies &amp; outils</h3>
      <div style="display:flex;gap:8px;flex-wrap:wrap;">
        ${p.technos.map(t => `<span class="tag">${escapeHTML(t)}</span>`).join('')}
      </div>
    </div>

    <div class="modal-section">
      <h3>💡 Ce que j'en ai appris</h3>
      <p>${escapeHTML(p.appris)}</p>
    </div>

    <div class="modal-section">
      <h3>⚠ Difficultés rencontrées</h3>
      <p>${escapeHTML(p.difficulte)}</p>
    </div>
  `;

  document.getElementById('modalBackdrop').classList.add('active');
  document.body.style.overflow = 'hidden';
}

function closeModal() {
  document.getElementById('modalBackdrop').classList.remove('active');
  document.body.style.overflow = '';
}

/* ============ MATIÈRES ============ */
function renderMatieres() {
  const container = document.getElementById('matieresContent');
  if (!container) return;

  container.innerHTML = MATIERES_PAR_SEMESTRE.map(year => `
    <div class="year-section">
      <div class="year-header">
        <h2 class="year-title">${escapeHTML(year.semestre)}</h2>
        <span class="year-meta">${escapeHTML(year.annee)} · ${escapeHTML(year.moyennes)}</span>
      </div>
      <div class="matieres-grid">
        ${year.matieres.map(m => `
          <div class="matiere-card">
            <div class="matiere-header">
              <span class="matiere-code">${escapeHTML(m.code)}</span>
              <span class="matiere-grade">${formatNote(m.note)} / 20</span>
            </div>
            <h4 class="matiere-title">${escapeHTML(m.titre)}</h4>
            <div class="progress-bar">
              <div class="progress-fill" style="width: ${m.percent}%;"></div>
            </div>
          </div>
        `).join('')}
      </div>
    </div>
  `).join('');
}

function formatNote(n) {
  return Number(n).toFixed(2);
}

/* ============ MATRICE ============ */
function renderMatrix() {
  const tbody = document.getElementById('matrixBody');
  if (!tbody) return;

  tbody.innerHTML = MATRICE.map(row => {
    const gradeClass = parseFloat(row.note) >= 14 ? 'green' : 'amber';
    return `
      <tr>
        <td class="col-code">${escapeHTML(row.code)}</td>
        <td>${row.isTop ? '⭐ ' : ''}${escapeHTML(row.intitule)}</td>
        <td>
          <div style="display:flex;gap:4px;flex-wrap:wrap;">
            ${row.comps.map(c => `<span class="tag cyan" style="font-size:10px;padding:2px 7px;">${escapeHTML(c)}</span>`).join('')}
          </div>
        </td>
        <td><span class="tag ${gradeClass}" style="font-weight:600;">${escapeHTML(row.note)}</span></td>
      </tr>
    `;
  }).join('');
}

/* ============ PROGRESS BARS ============ */
function initProgressBars() {
  document.querySelectorAll('.semester-fill').forEach(bar => {
    const w = bar.getAttribute('data-width');
    bar.style.width = '0%';
    requestAnimationFrame(() => {
      setTimeout(() => { bar.style.width = w + '%'; }, 100);
    });
  });
}

/* ============ UTILS ============ */
function escapeHTML(str) {
  if (str == null) return '';
  return String(str)
    .replace(/&/g, '&amp;')
    .replace(/</g, '&lt;')
    .replace(/>/g, '&gt;')
    .replace(/"/g, '&quot;')
    .replace(/'/g, '&#39;');
}
