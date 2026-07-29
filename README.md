# 📱 Web Screensaver for Android (DreamService)

A lightweight, native Kotlin Android application that wraps any Web URL (such as a digital clock, dashboard, or web app) and serves it as an official Android Screensaver (**DreamService / Daydream**).

---

## ✨ Features

- 📱 **Official Android Screensaver:** Integrates natively into Android system settings (**Settings > Display > Screensaver**).
- 🌐 **Modern WebView Engine:** Supports modern Web Standards, ES Modules, JavaScript, LocalStorage, and CSS dynamic viewports (`100dvh`).
- 🎨 **Minimalist Material 3 UI:** Clean dark AMOLED settings interface (`#090A0F`) with status bar inset handling and responsive layout.
- 🖼️ **Native System Preview:** Custom thumbnail preview icon displayed in the Android screensaver picker.
- ⚙️ **Configurable Target URL:** Built-in settings activity to set custom URLs with validation and instant browser preview.
- 🚪 **UI Exit Bridge:** Exposes `window.AndroidBridge.exitScreensaver()` to JavaScript, allowing web interface back buttons to terminate the screensaver natively.
- ⚡ **Performance & Battery Optimized:** Pure native Kotlin without heavy cross-platform framework overhead.

---

## 🚀 Quick Start & Usage

### 1. Building the APK
Build the debug APK via terminal:
```powershell
.\gradlew assembleDebug
```
The compiled file will be generated at:
`app/build/outputs/apk/debug/app-debug.apk`

### 2. Installing to Phone
Install using ADB or copy the APK directly to your phone:
```powershell
adb install app/build/outputs/apk/debug/app-debug.apk
```

### 3. Configuration & Activation
1. **Open App:** Launch **Clock Screensaver** from your app drawer.
2. **Set URL:** Enter your hosted clock URL (e.g. `https://yourusername.github.io/digital-clock/?mode=android`) and tap **Save Settings**.
3. **Activate Screensaver:**
   - Tap **Open Android Screensaver Settings** (or open **Settings > Display > Screensaver**).
   - Select **Clock Screensaver**.
   - Set activation trigger: *While Charging* or *While Docked*.

---

## 🌐 Supported URL Query Parameters

Optimize your web app layout based on the route mode:

| URL Query Parameter | Mode | Behavior |
|---|---|---|
| **`?mode=android`** | **Android Screensaver** | Hides domain label, enables exit button, locks full dynamic viewport (`100dvh`). |
| **`?mode=windows`** | **Windows Screensaver** | Hides domain label, optimizes desktop mouse physics & parallax. |
| *(None)* | **Standard Web** | Displays full web interface with hostname label. |

---

## 🚪 How to Exit the Screensaver

When the screensaver is running on your phone, you can exit anytime using:
1. **Web Back Button:** Tap the **`←`** exit button in the web UI.
2. **Android System Gestures:** Swipe up from the bottom edge or perform the back gesture.
3. **Power Button:** Press the physical Power/Lock button.

---

## 🛠️ Project Structure & Tech Stack

```
webscreensaverandroid/
├── app/
│   ├── src/main/
│   │   ├── java/com/web/screensaver/
│   │   │   ├── ClockDreamService.kt      # Native DreamService & AndroidBridge
│   │   │   └── DreamSettingsActivity.kt # Settings UI Activity
│   │   ├── res/
│   │   │   ├── layout/                   # Activity XML layouts
│   │   │   ├── drawable/                 # Custom surface drawables & icons
│   │   │   ├── xml/dream_info.xml        # DreamService metadata & previewImage
│   │   │   └── values/                   # Strings, theme colors
│   │   └── AndroidManifest.xml           # Service & Activity declarations
│   └── build.gradle.kts                  # App dependencies & SDK configs
├── build.gradle.kts
└── settings.gradle.kts
```

- **Language:** Kotlin
- **Min SDK:** 21 (Android 5.0 Lollipop)
- **Target SDK:** 35 (Android 15)
- **Architecture:** Android `DreamService` + `WebView` + `ViewBinding` + `Material 3`

---

## 📄 License
MIT License. Free to use and modify.