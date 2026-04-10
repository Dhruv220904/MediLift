# Project Asha 2.0

Human + AI hybrid public-health stack for last-mile predictive care.

## Monorepo layout
- `backend/` FastAPI + SQLAlchemy + AI risk engine
- `dashboard/` Next.js command dashboard
- `mobile-app/` Android app placeholder and API contract

## Run backend
```bash
cd /Users/polaris/Asha/backend
python3 -m venv venv
source venv/bin/activate
pip install -r requirements.txt
uvicorn app.main:app --reload
```

Backend health check: `GET http://localhost:8000/ping`

## Run dashboard
```bash
cd /Users/polaris/Asha/dashboard
npm install
NEXT_PUBLIC_API_URL=http://localhost:8000 npm run dev
```

Dashboard URL: `http://localhost:3000`

## Demo flow (hackathon)
1. Register ASHA and doctor users via `/auth/register`.
2. Login via `/auth/login` to get JWT.
3. Create beneficiary, submit visit with observations.
4. Confirm AI risk score in `GET /visit/`.
5. Open dashboard and review `GET /stats/high-risk` entries.
6. Mark referral using doctor/admin token.
7. Export CSV from `/export`.

## Notes
- Existing local SQLite DBs are auto-upgraded on startup with referral columns.
- `/sync/batch` is provided for WorkManager batch sync compatibility.
