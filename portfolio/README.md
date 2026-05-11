# Portfolio académique — Stéphane PYRAM-LOUIS

> **BUT3 Réseaux & Télécommunications — Parcours Cybersécurité**
> IUT de Béthune · Université d'Artois · 2025–2026

Portfolio web académique réalisé dans le cadre de la SAÉ Portfolio du BUT3 R&T. Il retrace mon parcours, mes compétences (référentiel RNCP), mes projets/SAÉ, mes expériences professionnelles, et mon projet d'avenir avec une démarche réflexive de niveau critique et professionnel.

---

## 🌐 Site en ligne

À déployer sur GitHub Pages — voir section **Déploiement** ci-dessous.

---

## 📂 Structure du projet

```
portfolio/
├── index.html              # Page unique (SPA simulée par sections)
├── css/
│   └── style.css           # Design system complet (cyber/tech sophistiqué)
├── js/
│   ├── data.js             # Toutes les données (compétences, SAÉ, matières, matrice)
│   └── app.js              # Logique : navigation, rendu, filtres, modales
├── assets/
│   ├── img/
│   │   └── profile.png     # Photo de profil
│   └── docs/
│       └── CV_Stephane_PYRAM_LOUIS.pdf
├── .nojekyll               # Force GitHub Pages à ne pas traiter le site comme Jekyll
├── .gitignore
└── README.md
```

---

## 🚀 Tester en local

Tu ne peux pas simplement ouvrir `index.html` avec un double-clic : les navigateurs bloquent certaines ressources en `file://`. Sers le site via un mini-serveur HTTP :

### Option 1 — Python (le plus simple, déjà installé)

```bash
cd portfolio
python3 -m http.server 8000
```

Puis ouvre http://localhost:8000 dans ton navigateur.

### Option 2 — Node.js

```bash
npx serve portfolio
```

### Option 3 — Extension VS Code

Installe **Live Server** dans VS Code, clic droit sur `index.html` → "Open with Live Server".

---

## 🌍 Déploiement sur GitHub Pages

Voici la procédure complète pour mettre le site en ligne gratuitement.

### Étape 1 — Créer le dépôt GitHub

1. Va sur https://github.com et crée un compte si tu n'en as pas
2. Clique sur **New** pour créer un nouveau dépôt
3. Nomme-le par exemple `portfolio` ou mieux : `stephane-pyram-louis.github.io` (le nom remplaçant `stephane-pyram-louis` par ton vrai username GitHub)
4. Laisse-le **public**
5. **Ne coche RIEN** (pas de README, pas de .gitignore, pas de license — tu en as déjà un)
6. Clique **Create repository**

### Étape 2 — Pousser le code

Dans un terminal, à la racine du dossier `portfolio/` :

```bash
git init
git add .
git commit -m "Initial portfolio commit"
git branch -M main
git remote add origin https://github.com/TON-USERNAME/portfolio.git
git push -u origin main
```

> Remplace `TON-USERNAME` par ton vrai username GitHub.

### Étape 3 — Activer GitHub Pages

1. Va dans ton dépôt sur GitHub
2. Clique **Settings** (onglet en haut)
3. Dans le menu de gauche, clique **Pages**
4. Section **Source** : choisis **Deploy from a branch**
5. Sélectionne la branche **main** et le dossier **/ (root)**
6. Clique **Save**

Attends 1–2 minutes. GitHub te donne l'URL en haut de la page Pages, du type :
```
https://TON-USERNAME.github.io/portfolio/
```

Ou si tu as nommé le dépôt `TON-USERNAME.github.io` :
```
https://TON-USERNAME.github.io/
```

### Étape 4 — Vérification

Ouvre l'URL, le site doit s'afficher. Si ça ne marche pas tout de suite, attends 5 minutes (le déploiement initial peut prendre un peu de temps).

---

## ✏️ Personnaliser le contenu

Tout le contenu modifiable se trouve dans **`js/data.js`** — c'est volontaire pour qu'on ne touche jamais le HTML.

### Modifier les compétences RNCP

Édite le tableau `COMPETENCES` dans `js/data.js`. Chaque compétence a une structure réflexive en 6 questions (conforme aux consignes BUT3) :
- `fait` — Ce que j'ai fait
- `pourquoi` — Pourquoi
- `comment` — Comment (méthode & outils)
- `difficultes` — Difficultés rencontrées
- `appris` — Ce que j'en ai appris
- `autrement` — Ce que je ferais autrement

### Modifier les projets / SAÉ

Édite le tableau `PROJETS` dans `js/data.js`. Pour chaque projet : titre, note, description, contexte, objectifs, réalisation, technologies, etc.

### Modifier les notes des matières

Édite le tableau `MATIERES_PAR_SEMESTRE` dans `js/data.js`.

### Modifier la matrice RNCP

Édite le tableau `MATRICE` dans `js/data.js`.

### Modifier le texte d'accueil, parcours, contact

Édite directement `index.html` — chaque section est clairement balisée par des commentaires.

---

## 🎨 Personnaliser le design

Le design system est centralisé dans `css/style.css`, en haut du fichier dans les variables CSS (`:root`). Tu peux changer :
- Les **couleurs** : `--accent-cyan`, `--accent-green`, `--accent-violet`, etc.
- Les **polices** : `--font-display`, `--font-body`, `--font-mono`
- Les **rayons d'arrondi**, **transitions**, **largeur de sidebar**…

---

## 🧰 Stack technique

- **HTML5 / CSS3 / JavaScript ES6+** — zéro dépendance, zéro build
- **Polices** : Space Grotesk (titres), Inter (corps), JetBrains Mono (tech), chargées depuis Google Fonts
- **Animations** : CSS pures (`@keyframes`, transitions)
- **Architecture** : SPA (Single Page Application) simulée en pur JS — navigation par sections cachées/affichées
- **Compatibilité** : navigateurs modernes (Chrome, Firefox, Safari, Edge)
- **Responsive** : desktop / tablette / mobile (menu hamburger sous 768 px)

---

## 📋 Consignes BUT3 respectées

Le portfolio suit la trame officielle de la SAÉ Portfolio :

✅ **Présentation personnelle** (Accueil + Parcours)
✅ **Compétences RNCP** détaillées avec AC et réflexion à 6 niveaux
✅ **Projets et SAÉ** documentés avec contexte, objectifs, difficultés, apprentissages
✅ **Expériences professionnelles** (stage, CDD, alternance)
✅ **Démarche réflexive** de niveau critique/professionnel
✅ **Matrice de correspondance** entre compétences, SAÉ et notes
✅ **Projet d'avenir** structuré court terme / moyen terme
✅ **CV téléchargeable**

---

## 📞 Contact

- **Email** : stephane.pyramlouis972@gmail.com
- **LinkedIn** : [stéphane-pyram-louis](https://www.linkedin.com/in/stéphane-pyram-louis-5a45652b5/)
- **Téléphone** : 06 15 95 56 53
- **Localisation** : Béthune (62)

---

## 📄 Licence

Ce portfolio est un travail académique personnel. Le code est libre de réutilisation pour des fins pédagogiques avec mention de l'auteur.

© 2026 Stéphane PYRAM-LOUIS
