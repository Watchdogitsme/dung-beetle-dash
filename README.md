# Dung Beetle Dash: 100 Tiers

**Play Online (Desktop & Mobile PWA):** https://watchdogitsme.github.io/dung-beetle-dash/

A single-file, zero-dependency HTML5 Canvas arcade game featuring 100 tiers, 5 dynamic biomes plus unlockable scenery packs, boss encounters, stackable mutations, and an Evolution Lab for permanent upgrades—packaged as both an offline PWA and a Google Play-ready Android app.

## Controls

- **Desktop:** `WASD` or `Arrow Keys` to steer, `Esc` or `P` to pause, sidebar toggles for `SFX` / `Music` / `Pause`.
- **Mobile & Android:** Dedicated bottom virtual analog thumbstick **or** touch-drag anywhere on the arena to steer in 360°; tap mutation chips in the top strip to inspect bonuses; hardware/gesture Back pauses, closes menus, or prompts twice to exit.

## Mobile / Android App

### Install Offline PWA (Zero Build Needed)
Open https://watchdogitsme.github.io/dung-beetle-dash/ in Chrome on Android and tap **Install app** / **Add to Home screen**.

### Download Installable Android APK (GitHub Actions)
Every push to `main` or `feat/*` builds `dung-beetle-dash-debug.apk` automatically via `.github/workflows/android.yml`. Open the latest workflow run under the repository **Actions** tab and download the `dung-beetle-dash-debug-apk` artifact.

### Google Play Store Release Signing (`.aab` & Signed `.apk`)
The Android app (`io.github.watchdogitsme.dungbeetledash`, `minSdk 24`, `targetSdk 35`) requires zero network permissions (`VIBRATE` only) and automatically builds a minified, signed Play Store `.aab` when these 4 GitHub Repository Secrets are configured:

1. Generate an upload keystore locally (never commit `.jks` files to Git):
   ```bash
   keytool -genkey -v -keystore upload-keystore.jks -keyalg RSA -keysize 2048 -validity 10000 -alias upload
   base64 -w 0 upload-keystore.jks > keystore.b64.txt
   ```
2. Add these secrets in **Settings → Secrets and variables → Actions**:
   - `ANDROID_KEYSTORE_BASE64` — contents of `keystore.b64.txt`
   - `ANDROID_KEYSTORE_PASSWORD` — keystore password
   - `ANDROID_KEY_ALIAS` — `upload`
   - `ANDROID_KEY_PASSWORD` — key password
3. Push a version tag (e.g., `git tag v1.0.0 && git push origin v1.0.0`) or trigger the workflow manually to produce `dung-beetle-dash-release.aab` and `dung-beetle-dash-release.apk`.

## Project Structure

- **`Beetal dash.html`** — Single self-contained HTML/CSS/JS game source (works offline, via `file://`, on GitHub Pages PWA, and bundled into Android `assets/www/index.html` at build time).
- **`pwa/`** — Web App Manifest, Service Worker, and Golden Scarab Crest icons for offline GitHub Pages install.
- **`android/`** — Native Kotlin + `WebViewAssetLoader` Android wrapper (`io.github.watchdogitsme.dungbeetledash`) with edge-to-edge immersive portrait display, lifecycle auto-pause, and Back-button bridge.
- **`.github/workflows/pages.yml`** — Deploys `Beetal dash.html` + `pwa/` to GitHub Pages on pushes to `main`.
- **`.github/workflows/android.yml`** — Validates JS syntax, compiles/lints the Android APK, and builds signed Play Store AABs when signing secrets exist.
