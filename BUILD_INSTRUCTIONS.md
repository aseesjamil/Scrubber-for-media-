# Build Instructions for Scrubber AI

## Prerequisites
- Android Studio Arctic Fox or later
- Android SDK 34 or later
- Gradle 8.0+
- JDK 11+
- Min SDK: Android 8.0 (API 26)
- Target SDK: Android 14 (API 34)

## Building the APK

### Debug APK (for testing)
1. Open the project in Android Studio
2. Go to **Build** > **Build Bundle(s)/APK(s)** > **Build APK(s)**
3. Wait for the build to complete
4. The APK will be located at: `app/build/outputs/apk/debug/app-debug.apk`

### Release APK (for production)
1. Open the project in Android Studio
2. Go to **Build** > **Build Bundle(s)/APK(s)** > **Build Signed Bundle/APK**
3. Select **APK** and click **Next**
4. Create or select an existing keystore
5. Fill in your signing details
6. Select **Release** build variant
7. Click **Finish**
8. The APK will be located at: `app/build/outputs/apk/release/app-release.apk`

## Installation on Device

### Via Android Studio
1. Connect your Android phone via USB
2. Enable **Developer Options** and **USB Debugging** on your phone
3. Click **Run** (green play button) in Android Studio
4. Select your device and click **OK**

### Via Command Line
```bash
# Install debug APK
adb install app/build/outputs/apk/debug/app-debug.apk

# Install release APK
adb install app/build/outputs/apk/release/app-release.apk
```

### Via APK File
1. Transfer the APK to your phone
2. Open File Manager on your phone
3. Locate and tap the APK file
4. Tap **Install**
5. Grant necessary permissions

## Required Permissions

When you first launch Scrubber AI, the app will request:

### Android 13+ (API 33+)
- `READ_MEDIA_IMAGES` - Access to photos
- `READ_MEDIA_VIDEO` - Access to videos

### Android 12 and Below (API ≤ 31)
- `READ_EXTERNAL_STORAGE` - Access to device storage
- `WRITE_EXTERNAL_STORAGE` - Ability to delete files

## Features to Test

### Home Screen
- View storage statistics (total, used, free)
- See photo and video counts
- Access all scan options

### Duplicate Scanner
- Scan for duplicate photos and videos
- Review duplicate groups with file details
- Select files to delete
- See space savings calculation

### Large Files Scanner
- Find files larger than 50MB, 100MB, 500MB, or 1GB
- Sort by file size
- Multi-select for deletion

### Junk Files Scanner
- Detect temporary, log, and cache files
- Safe deletion with confirmation

### APK Cleaner
- Find unused APK files
- Check if APK is currently installed
- Safe removal of unused APKs

### Recycle Bin
- View deleted files
- Restore files within 30 days
- Permanently delete files

### Settings
- Adjust scan thresholds
- Configure auto-scan frequency
- Set recycle bin retention period
- Dark/Light mode toggle

## Troubleshooting

### Permission Issues
- Ensure you grant storage permissions when prompted
- For Android 12+, grant per-app permissions
- Go to **Settings > Apps > Scrubber AI > Permissions** to manage permissions manually

### Scanning Issues
- Ensure you have sufficient storage space
- Close other apps that might be using storage
- Restart the app if scanning seems stuck

### Installation Issues
- Ensure USB Debugging is enabled on your device
- Update Android SDK Platform Tools
- Try "adb kill-server" and "adb start-server"

## File Structure

```
app/
├── src/main/
│   ├── java/com/scrubberai/
│   │   ├── MainActivity.kt
│   │   ├── di/
│   │   │   ├── AppModule.kt
│   │   │   └── ScrubberAIApp.kt
│   │   ├── data/
│   │   │   ├── local/
│   │   │   ├── repository/
│   │   │   └── scanner/
│   │   ├── domain/
│   │   │   ├── model/
│   │   │   └── usecase/
│   │   ├── presentation/
│   │   │   ├── navigation/
│   │   │   ├── ui/
│   │   │   ├── theme/
│   │   │   └── viewmodel/
│   │   └── utils/
│   ├── res/
│   │   ├── values/
│   │   ├── xml/
│   │   └── mipmap/
│   └── AndroidManifest.xml
├── build.gradle.kts
└── proguard-rules.pro
```

## Performance Tips

1. **First Scan**: The initial scan may take longer as the app builds its index
2. **Background Scanning**: Use background scans during off-hours
3. **Storage**: Ensure at least 500MB free space for optimal scanning
4. **Memory**: Close unused apps before scanning large libraries

## Support

For issues or feature requests, please check the GitHub repository issues section.

## License

MIT License - Feel free to use and modify this application.
