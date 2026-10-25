# DroidHub

A clean Android phone companion with a standards-based Android Auto **media** service.

## Build APK
GitHub Actions is already configured. Upload this project to the repository, then open **Actions → Build DroidHub APK → Run workflow**. The APK is published as the `DroidHub-APK` workflow artifact.

## Android Auto
DroidHub declares the supported `media` automotive capability and exposes a browsable MediaBrowser service with MediaSession playback. Sideloaded apps may require Android Auto developer settings / Unknown sources on the test phone.

This project intentionally does not bypass Android Auto driving-safety restrictions or disguise browser/video functionality as a supported app category.
