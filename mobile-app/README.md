# Project Asha Mobile App (Phase 4 Scaffold)

Android scaffold for the hackathon roadmap with:
- Room offline database
- Retrofit API client with JWT auth interceptor
- Encrypted token storage
- WorkManager offline sync
- ViewModel layer for login/beneficiary/visit flows

## Folder highlights
- `app/src/main/java/com/projectasha/mobile/data/local/` Room DB, entities, DAOs
- `app/src/main/java/com/projectasha/mobile/network/` Retrofit, DTOs, auth interceptor
- `app/src/main/java/com/projectasha/mobile/repository/` Offline-first repository
- `app/src/main/java/com/projectasha/mobile/sync/` WorkManager sync worker and scheduler
- `app/src/main/java/com/projectasha/mobile/ui/` Main activity and ViewModels

## Open and run
1. Open `/Users/polaris/Asha/mobile-app` in Android Studio.
2. Let Gradle sync.
3. Set backend URL if needed in `app/build.gradle.kts` (`API_BASE_URL`).
4. Run app on emulator/device.

## Backend expectation
- Backend running at `http://10.0.2.2:8000/` for Android emulator.
- API endpoints documented in `docs/API_CONTRACT.md`.

## Next implementation tasks
1. Add dedicated Login, Beneficiary Form, and Visit Form screens.
2. Wire real user input into `BeneficiaryViewModel` and `VisitViewModel`.
3. Add token refresh retry in networking layer when 401 occurs.
4. Show synced risk score and risk level in UI after background sync.
