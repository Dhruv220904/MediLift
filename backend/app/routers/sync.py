# FILE: backend/app/routers/sync.py
from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from ..database import get_db
from ..models import Visit, VisitObservation, Beneficiary
from ..schemas import SyncRequest, SyncResponse, SyncResult
from ..ai.predictor import predict_risk

router = APIRouter(prefix="/sync", tags=["sync"])

@router.post("/", response_model=SyncResponse)
def sync_visits(sync_request: SyncRequest, db: Session = Depends(get_db)):
    results = []
    synced_count = 0

    for visit_data in sync_request.visits:
        # Check if local_id already exists
        existing_visit = db.query(Visit).filter(Visit.local_id == visit_data.local_id).first()
        if existing_visit:
            results.append(SyncResult(
                local_id=visit_data.local_id,
                status="already_synced",
                visit_id=existing_visit.id,
                risk_score=existing_visit.risk_score,
                risk_level=existing_visit.risk_level,
                specialist_flag=existing_visit.specialist_flag
            ))
            continue

        # Check beneficiary exists
        beneficiary = db.query(Beneficiary).filter(Beneficiary.id == visit_data.beneficiary_id).first()
        if not beneficiary:
            results.append(SyncResult(local_id=visit_data.local_id, status="error: beneficiary not found"))
            continue

        # Create visit
        db_visit = Visit(
            local_id=visit_data.local_id,
            beneficiary_id=visit_data.beneficiary_id,
            visit_type=visit_data.visit_type,
            notes=visit_data.notes,
            synced=True
        )
        db.add(db_visit)
        db.commit()
        db.refresh(db_visit)

        # Add observations
        for obs in visit_data.observations:
            db_obs = VisitObservation(visit_id=db_visit.id, key=obs.key, value=obs.value)
            db.add(db_obs)
        db.commit()

        # Run AI prediction
        prediction = predict_risk(visit_data.observations)
        db_visit.risk_score = prediction["risk_score"]
        db_visit.risk_level = prediction["risk_level"]
        db_visit.specialist_flag = prediction["specialist_flag"]
        db.commit()

        results.append(SyncResult(
            local_id=visit_data.local_id,
            status="synced",
            visit_id=db_visit.id,
            risk_score=prediction["risk_score"],
            risk_level=prediction["risk_level"],
            specialist_flag=prediction["specialist_flag"]
        ))
        synced_count += 1

    return SyncResponse(synced=synced_count, results=results)


@router.post("/batch", response_model=SyncResponse)
def sync_visits_batch(sync_request: SyncRequest, db: Session = Depends(get_db)):
    return sync_visits(sync_request=sync_request, db=db)
