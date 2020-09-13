# Changelog

## [Unreleased]

## [0.16] - 2020-09-13
### Features
- User friendly uptime in app

### Fixes
- Resolve crash related to log file sharing
- Resolve crash related to widget notifications update

### Code/Structure changes
- Build with SDK 29
- Switch Crashlytics dependencies to Firebase
- Remove Fabric
- Depend on AndroidX solution for permissions, resolved known crashes
- Migrate from Evernote android-job to AndroidX WorkManager API
- Use latest stable libraries and not alpha versions where possible

### Other changes
- Update Privacy Policy, Terms and Conditions

## [0.15] - 2019-05-12
### Fixes
- Resolve crash on Print

## [0.14] - 2019-05-05
### Fixes
- Remove extra spacing from settings screen
- Fix theme issue on OSS licenses
- Fix flickering issue on developer options
- Open links in Credits outside application

### Code/Structure changes
- Migrate to AndroidX
- Code Quality improvements
- Proguard optimizations

### Known issues
- Extra spacing under uptime timer

## [0.13] - 2018-12-08
### Fixes
- Recalculate the restart notification date on application start
- Recalculate the restart notification date regularly when the widget is used
- Recalculate the restart notification date on device Quick boot events

### API dependency changes
- Update SDK to 28
- Update support libraries to 28.0.0
- Update crashlytics to 2.9.6

## [0.12] - 2018-05-28
### Notable changes
- New UI for showing the Uptime
- New UI for a restart Notification

### Code/Structure changes
- Use ModelView to handle UI data
- Enable APK minify

## [0.11] - 2018-05-17
### Features
- Widget to show Uptime
- Configuration for the widget Name
- Uptime inside the application
- Setup notifications for restart
- About screen with contents:
    - Open Source Licenses
    - Privacy Policy
    - Terms & Conditions
    - Credits
- Acceptance screen for crash and analytics data

[Unreleased]: https://github.com/madlymad/uptime/compare/v0.16...HEAD
[0.16]: https://github.com/madlymad/uptime/compare/v0.15...v0.16
[0.15]: https://github.com/madlymad/uptime/compare/v0.14...v0.15
[0.14]: https://github.com/madlymad/uptime/compare/v0.13...v0.14
[0.13]: https://github.com/madlymad/uptime/compare/v0.12...v0.13
[0.12]: https://github.com/madlymad/uptime/compare/v0.11...v0.12
[0.11]: https://github.com/madlymad/uptime/tree/v0.11

