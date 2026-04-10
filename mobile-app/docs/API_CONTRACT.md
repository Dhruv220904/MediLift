# Project Asha 2.0 Mobile API Contract

## Auth
- `POST /auth/register`
- `POST /auth/login` -> returns `access_token` + `refresh_token`
- `POST /auth/refresh`

## Beneficiary
- `POST /beneficiary/` (JWT required)
- `GET /beneficiary/` (JWT required)
- `GET /beneficiary/{id}` (JWT required)

## Visits
- `POST /visit/` (JWT required)
- `GET /visit/` (JWT required)
- `GET /visit/{id}` (JWT required)
- `POST /visit/{id}/refer` (doctor/admin JWT)

## Offline Sync
- `POST /sync/`
- `POST /sync/batch` (alias)

## Dashboard
- `GET /stats/`
- `GET /stats/high-risk?district=&specialist_flag=&is_referred=`
- `GET /export`

## Observation keys expected by AI model
- `age`
- `pregnancy_week`
- `systolic_bp`
- `diastolic_bp`
- `hemoglobin`
- `weight_kg`
- `tb_cough`
- `night_sweats`
- `muac_cm`
- `sdoh_score`
