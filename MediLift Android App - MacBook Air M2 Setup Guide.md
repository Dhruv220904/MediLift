# MediLift Android App - MacBook Air M2 Setup Guide

## Prerequisites

Your MacBook Air M2 is perfect for Android development! Here's everything you need to get started.

---

## Step 1: Install Java Development Kit (JDK)

Android development requires Java 11 or later. The M2 chip requires ARM64 compatible versions.

### Option A: Using Homebrew (Recommended)

```bash
# Install Homebrew if not already installed
/bin/bash -c "$(curl -fsSL https://raw.githubusercontent.com/Homebrew/install/HEAD/install.sh)"

# Install Java 17 (recommended for Android)
brew install openjdk@17

# Set JAVA_HOME environment variable
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc
source ~/.zshrc

# Verify installation
java -version
```

### Option B: Download from Oracle
- Visit: https://www.oracle.com/java/technologies/downloads/
- Download JDK 17 for macOS ARM64
- Follow the installer

---

## Step 2: Install Android Studio (M2 Compatible)

### Download & Install

1. **Download Android Studio for Mac (ARM64)**
   - Visit: https://developer.android.com/studio
   - Download the macOS version (it auto-detects ARM64)
   - Drag to Applications folder

2. **Launch Android Studio**
   ```bash
   open /Applications/Android\ Studio.app
   ```

3. **Complete Initial Setup**
   - Accept license agreements
   - Choose "Standard" installation
   - Select default settings for SDK location

### Configure Android SDK

1. Open Android Studio → Preferences (or Android Studio → Settings on newer versions)
2. Navigate to: **Appearance & Behavior → System Settings → Android SDK**
3. Install required components:
   - **SDK Platforms**: Android 13, 14 (latest)
   - **SDK Tools**:
     - Android SDK Build-Tools (latest)
     - Android Emulator
     - Android SDK Platform-Tools
     - Intel x86 Emulator Accelerator (HAXM) - **Skip this for M2, use native emulator**

---

## Step 3: Set Up Environment Variables

Add these to your shell profile (`~/.zshrc` for M2 Macs):

```bash
# Add these lines to ~/.zshrc
export ANDROID_HOME=$HOME/Library/Android/sdk
export PATH=$PATH:$ANDROID_HOME/emulator
export PATH=$PATH:$ANDROID_HOME/tools
export PATH=$PATH:$ANDROID_HOME/tools/bin
export PATH=$PATH:$ANDROID_HOME/platform-tools

# Java Home (if not already set)
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
```

Apply changes:
```bash
source ~/.zshrc
```

---

## Step 4: Set Up the MediLift Project

### Clone/Open the Project

```bash
# Navigate to your project directory
cd /path/to/MediLift

# Or if you have it in a zip file, extract it first
unzip MediLift-main.zip
cd MediLift-main/mobile-app
```

### Open in Android Studio

1. Launch Android Studio
2. Click **File → Open**
3. Navigate to the `MediLift/mobile-app` folder
4. Click **Open**
5. Wait for Gradle to sync (first time takes 2-5 minutes)

---

## Step 5: Create Android Virtual Device (Emulator)

### Create a Virtual Device

1. In Android Studio, click **Tools → Device Manager**
2. Click **Create Device**
3. Select a device (e.g., "Pixel 6 Pro")
4. Choose API Level 33 or 34
5. For M2 Mac, select **ARM64** architecture (native support)
6. Click **Next** and **Finish**

### Alternative: Use Physical Device

If you have an Android phone:
1. Enable Developer Mode: Settings → About Phone → Tap Build Number 7 times
2. Enable USB Debugging: Settings → Developer Options → USB Debugging
3. Connect via USB
4. Trust the computer when prompted

---

## Step 6: Build and Run the App

### Method 1: Using Android Studio (Easiest)

1. **Sync Gradle Files**
   - Click **File → Sync Now** (or wait for auto-sync)
   - Wait for build to complete

2. **Build the App**
   - Click **Build → Make Project**
   - Or use keyboard shortcut: `Cmd + B`

3. **Run the App**
   - Click **Run → Run 'app'** (or press `Ctrl + R`)
   - Select your emulator or connected device
   - Click **OK**

### Method 2: Using Terminal

```bash
# Navigate to project directory
cd MediLift/mobile-app

# Build the APK
./gradlew build

# Install on emulator/device
./gradlew installDebug

# Run the app
adb shell am start -n com.projectasha.mobile/.ui.LoginActivity
```

---

## Step 7: Troubleshooting Common Issues

### Issue 1: Gradle Sync Fails

**Solution:**
```bash
# Clean and rebuild
cd MediLift/mobile-app
./gradlew clean
./gradlew build
```

### Issue 2: Java Version Mismatch

**Solution:**
```bash
# Verify Java version
java -version

# Should show Java 11 or higher
# If not, update JAVA_HOME in ~/.zshrc
```

### Issue 3: Emulator Won't Start

**Solution:**
```bash
# Kill existing emulator processes
pkill -f emulator

# Start emulator from terminal
emulator -avd Pixel_6_Pro_API_34 -cores 4 -memory 2048
```

### Issue 4: Build Tools Not Found

**Solution:**
1. Open Android Studio → Preferences → Android SDK
2. Click **SDK Tools** tab
3. Check "Show Package Details"
4. Install latest Build-Tools version

### Issue 5: M2 Emulator Performance

**Optimization Tips:**
```bash
# Use more cores and memory for emulator
emulator -avd Pixel_6_Pro_API_34 -cores 4 -memory 2048 -gpu on
```

---

## Step 8: Backend Setup (FastAPI)

Your MediLift app needs the FastAPI backend to function properly.

### Install Python and Dependencies

```bash
# Install Python 3.9+ (if not already installed)
brew install python@3.11

# Create virtual environment
python3.11 -m venv venv
source venv/bin/activate

# Install FastAPI and dependencies
pip install fastapi uvicorn sqlalchemy pydantic

# Navigate to backend
cd MediLift/backend

# Run the backend server
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

### Update App Configuration

In your Android app, update the API base URL:
- File: `app/src/main/java/com/projectasha/mobile/network/ApiClient.kt`
- Update: `BASE_URL = "http://YOUR_MAC_IP:8000/api/"`
- Find your Mac's IP: `ifconfig | grep "inet " | grep -v 127.0.0.1`

---

## Step 9: Running the Complete Stack

### Terminal 1: Backend Server
```bash
cd MediLift/backend
source venv/bin/activate
uvicorn main:app --reload --host 0.0.0.0 --port 8000
```

### Terminal 2: Android Emulator
```bash
emulator -avd Pixel_6_Pro_API_34 -cores 4 -memory 2048 -gpu on
```

### Terminal 3: Android Studio
- Run the app from Android Studio (Ctrl + R)

---

## Step 10: Useful Commands

```bash
# List available emulators
emulator -list-avds

# Start specific emulator
emulator -avd Pixel_6_Pro_API_34

# View app logs
adb logcat | grep MediLift

# Install APK manually
adb install app/build/outputs/apk/debug/app-debug.apk

# Uninstall app
adb uninstall com.projectasha.mobile

# Clear app data
adb shell pm clear com.projectasha.mobile

# Take screenshot
adb shell screencap -p /sdcard/screenshot.png
adb pull /sdcard/screenshot.png ~/Desktop/

# Check connected devices
adb devices
```

---

## Performance Tips for M2 MacBook Air

1. **Close unnecessary apps** - Frees up RAM for emulator
2. **Use native ARM64 emulator** - Much faster than x86
3. **Allocate adequate memory** - At least 2GB for emulator
4. **Enable GPU acceleration** - Use `-gpu on` flag
5. **Use SSD storage** - Faster build times
6. **Disable live templates** - In Android Studio preferences

---

## M2 Specific Notes

✅ **Advantages:**
- Native ARM64 emulator support (very fast)
- Excellent battery life during development
- Sufficient RAM for emulator + IDE
- Good thermal management

⚠️ **Considerations:**
- Some older libraries might not support ARM64
- x86 emulator images won't work (use ARM64 only)
- Some plugins might need updates

---

## Next Steps

1. ✅ Install Android Studio
2. ✅ Set up emulator
3. ✅ Open MediLift project
4. ✅ Run the app
5. ✅ Start backend server
6. ✅ Test the app

---

## Additional Resources

- **Android Studio Docs**: https://developer.android.com/studio/intro
- **Kotlin Documentation**: https://kotlinlang.org/docs/
- **FastAPI Documentation**: https://fastapi.tiangolo.com/
- **Android Emulator Guide**: https://developer.android.com/studio/run/emulator

---

## Quick Start Command (Copy & Paste)

```bash
# One-time setup
brew install openjdk@17
export JAVA_HOME=$(/usr/libexec/java_home -v 17)
echo 'export JAVA_HOME=$(/usr/libexec/java_home -v 17)' >> ~/.zshrc

# Open Android Studio
open /Applications/Android\ Studio.app

# In Android Studio:
# 1. File → Open → Select MediLift/mobile-app
# 2. Wait for Gradle sync
# 3. Tools → Device Manager → Create Device (ARM64)
# 4. Run → Run 'app'
```

---

## Support

If you encounter issues:
1. Check Android Studio logs: **View → Tool Windows → Logcat**
2. Run: `./gradlew --info build` for detailed build info
3. Check Java version: `java -version`
4. Verify SDK installation: `$ANDROID_HOME/tools/bin/sdkmanager --list`

Good luck! 🚀
