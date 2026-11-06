# AAA Browser (v1.1)

**AAA Browser** is a pitch-black AMOLED (`#000000`), privacy-focused Android web browser built with Kotlin and Jetpack Compose (Material 3 Expressive Segmented UI), featuring **YouTube Ad-Free Audio-Only Music Mode** (for Browser & Android Auto), **Reader Mode**, **Live Search Suggestions**, **Google Maps** integration, and **English (India) (`en-IN`)** as its single default language.

---

## Direct APK Download from GitHub Releases
Open the **Releases** section on the right side of this public GitHub repository (or use **Settings → System update (v1.1)** inside the app) to download:
- **`AAA-Browser-release.apk`** (Signed Release APK)
- **`AAA-Browser-debug.apk`** (Signed Debug APK)

---

## Key Features (v1.1 Update)
1. **Pitch-Black AMOLED (`#000000`) & Segmented Card UI**:
   - True `#000000` background with `#1C1D24` segmented card groups (`24.dp` outer corners, `6.dp` inner corners).
   - **Far-Left Circular Home Button** (`www.google.com`), **Center Floating Search & Mode Capsule**, and **Far-Right Cute Circular Page Info (`i`) Button**.
2. **YouTube Ad-Free Audio-Only Music Mode (Browser & Android Auto)**:
   - Play any song from YouTube like Spotify with **zero video ads**.
   - Toggle **Stop Video (Audio Only)** to hide the video display while listening to the music in the background — both in the main browser and inside the **Android Auto Driving Console**.
   - Includes 1-tap quick stations (*Punjabi Hits*, *Arijit Singh*, *Bollywood Lo-Fi*, *Workout Mix*) and transport controls (`Play/Pause`, `-10s`, `+10s`, `Next Song`).
3. **Distraction-Free Reader Mode**:
   - Format any article or webpage into clean typography with `A-` / `A+` font sizing, `AMOLED` / `Dark` / `Warm` themes, and **Read Aloud (`en-IN`)**.
4. **Live Search Suggestions**:
   - Real-time search completions, direct YouTube Ad-Free Music search, bookmark matches, and browsing history suggestions as you type.
5. **Cute Page Info (`i`) Sheet**:
   - Shows active page title, full URL, HTTPS status, ads/trackers blocked on the page, **Find in Page**, **Page Text Zoom (`85%`–`150%`)**, **Copy/Share Clean Link**, and pinned bookmarks.
6. **Pinned Bookmarks**:
   - `www.youtube.com`
   - `www.google.com`
   - `www.netflix.com`
   - `Android Auto settings` (Redirects directly to Android Auto settings on your phone)
7. **In-App System Update Center (OTA)**:
   - Manage and download new APK updates directly inside **Settings → System update (v1.1)**.
8. **Signature Footer on Every Page**:
   - Displays **`Made by Yug Chhabra In India With❤️`** at the bottom of every screen, modal sheet, and webpage.

---

## Build & Git Commands Reference

### 1. Build Debug & Release APKs Locally
```bash
gradle assembleDebug assembleRelease
```

### 2. Run Unit Tests
```bash
gradle :app:testDebugUnitTest --tests com.example.ExampleUnitTest
```

### 3. Push Entire Project to a New Public GitHub Repository
If pushing from a terminal or cloning into a new public GitHub repository (`https://github.com/<your-username>/<new-public-repo>.git`):
```bash
git init
git branch -M main
git add -A
git commit -m "Release AAA Browser v1.1 with Echo UI, Reader Mode, YouTube Audio-Only Mode & OTA Updater"
git remote add origin https://github.com/ychhabra453/AAA-Browser.git
git push -u origin main --force
```

---

**Made by Yug Chhabra In India With❤️**
