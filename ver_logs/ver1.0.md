# Version 1.0 - Initial Seika Fork Migration

**Date:** June 5, 2026
**Project:** Seika (Fork of Mihon)
**Objective:** Establish the Seika fork with global rebranding and integrated spiritual disclaimer systems.

## 1. Global Rebranding
Transformed the application identity from "Mihon" to "Seika".

### Modified Files:
- **`i18n/src/commonMain/moko-resources/base/strings.xml`**: Updated `app_name` and project-specific strings.
- **`app/build.gradle.kts`**: Changed `applicationId` to `app.seika` for independent installation.
- **`app/src/main/AndroidManifest.xml`**:
    - Updated deep link schemes to `seika://`.
    - Updated launcher icon configurations (`android:icon` and `android:roundIcon`).
- **`settings.gradle.kts`**: Updated `rootProject.name` to `"Seika"`.
- **`README.md`**: Fully rewritten to reflect Seika's mission and new branding.

### Assets:
- Added new PNG app icon to `app/src/main/res/mipmap-xxxhdpi/ic_launcher_foreground.png`.
- Configured adaptive icons in `app/src/main/res/mipmap/ic_launcher.xml` and `ic_launcher_round.xml`.
- Created `app/src/main/res/values/launcher_colors.xml` for icon background consistency.

---

## 2. Spiritual Disclaimer System
Implemented a persistent reminder system based on Islamic values.

### Logic & Features:
- **First Launch Protection:** The app now automatically displays a scrollable spiritual disclaimer on the first run.
- **Persistent State:** User acknowledgment is saved via a new preference key.
- **On-Demand Access:** A Floating Action Button (FAB) was added to the Library screen for users to revisit the reminder at any time.

### New & Modified Files:
- **`app/src/main/java/eu/kanade/presentation/components/SeikaDisclaimerDialog.kt`**:
    - New Jetpack Compose component containing the full spiritual text and verses (Quran/Hadith).
    - Uses `AdaptiveSheet` for consistent UI across devices.
- **`app/src/main/java/eu/kanade/domain/base/BasePreferences.kt`**: Added `shownSeikaDisclaimer` preference.
- **`app/src/main/java/eu/kanade/tachiyomi/ui/main/MainActivity.kt`**: Added `ShowSeikaDisclaimer()` logic to the main entry point.
- **`app/src/main/java/eu/kanade/tachiyomi/ui/library/LibraryTab.kt`**: Integrated the info FAB into the Scaffold and managed the dialog visibility state.

---

## 3. Build & System Resilience
- **`gradle/build-logic/src/main/kotlin/mihon/gradle/Commands.kt`**:
    - Fixed a critical sync error where the build would fail if `git` was missing from the environment.
    - Added fallback logic for `COMMIT_COUNT`, `COMMIT_SHA`, and `BUILD_TIME`.

## Summary of Logic Used:
1. **Preference Store:** Utilized the existing Mihon/Tachiyomi Preference framework to store the disclaimer state.
2. **Scaffold Integration:** Appended the FAB to the `Scaffold` in `LibraryTab.kt` without interfering with existing UI elements.
3. **State Management:** Used `rememberSaveable { mutableStateOf(false) }` to ensure dialog visibility state persists across configuration changes (like rotation).
4. **Adaptive UI:** Used the `AdaptiveSheet` component to ensure the disclaimer displays correctly on both phones and tablets.
