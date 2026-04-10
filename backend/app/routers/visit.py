# FILE: backend/app/routers/visit.py
from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List, Optional
from datetime import datetime
from ..database import get_db
from ..models import Visit as VisitModel, VisitObservation, Beneficiary, User
from ..schemas import VisitCreate, Visit as VisitOut, ReferVisitRequest
from ..core.dependencies import get_current_user
from ..ai.predictor import predict_risk

router = APIRouter(prefix="/visit", tags=["visit"])

@router.post("/", response_model=VisitOut)
def create_visit(visit: VisitCreate, current_user: User = Depends(get_current_user), db: Session = Depends(get_db)):
    # Check if beneficiary exists and user has access
    beneficiary = db.query(Beneficiary).filter(Beneficiary.id == visit.beneficiary_id).first()
    if not beneficiary:
        raise HTTPException(status_code=404, detail="Beneficiary not found")
    if current_user.role != "admin" and beneficiary.asha_id != current_user.id:
        raise HTTPException(status_code=403, detail="Not authorized")

    # Create visit
    db_visit = VisitModel(
        local_id=visit.local_id,
        beneficiary_id=visit.beneficiary_id,
        visit_type=visit.visit_type,
        notes=visit.notes,
        synced=True
    )
    db.add(db_visit)
    db.commit()
    db.refresh(db_visit)

    # Add observations
    for obs in visit.observations:
        db_obs = VisitObservation(visit_id=db_visit.id, key=obs.key, value=obs.value)
        db.add(db_obs)
    db.commit()

    # Run AI prediction
    prediction = predict_risk(visit.observations)
    db_visit.risk_score = prediction["risk_score"]
    db_visit.risk_level = prediction["risk_level"]
    db_visit.specialist_flag = prediction["specialist_flag"]
    db.commit()
    db.refresh(db_visit)

    return db_visit

@router.get("/", response_model=List[VisitOut])
def list_visits(risk_level: Optional[str] = Query(None), current_user: User = Depends(get_current_user), db: Session = Depends(get_db)):
    query = db.query(VisitModel)
    if risk_level:
        query = query.filter(VisitModel.risk_level == risk_level)
    if current_user.role != "admin":
        # ASHA sees only their beneficiaries' visits
        query = query.join(Beneficiary).filter(Beneficiary.asha_id == current_user.id)
    return query.all()

@router.get("/{visit_id}", response_model=VisitOut)
def get_visit(visit_id: str, current_user: User = Depends(get_current_user), db: Session = Depends(get_db)):
    visit = db.query(VisitModel).filter(VisitModel.id == visit_id).first()
    if not visit:
        raise HTTPException(status_code=404, detail="Visit not found")
    if current_user.role != "admin":
        beneficiary = db.query(Beneficiary).filter(Beneficiary.id == visit.beneficiary_id).first()
        if beneficiary.asha_id != current_user.id:
            raise HTTPException(status_code=403, detail="Not authorized")
    return visit


@router.post("/{visit_id}/refer", response_model=VisitOut)
def refer_visit(
    visit_id: str,
    payload: ReferVisitRequest,
    current_user: User = Depends(get_current_user),
    db: Session = Depends(get_db),
):
    if current_user.role not in ("admin", "doctor"):
        raise HTTPException(status_code=403, detail="Only doctor/admin can mark referrals")

    visit = db.query(VisitModel).filter(VisitModel.id == visit_id).first()
    if not visit:
        raise HTTPException(status_code=404, detail="Visit not found")

    assigned_doctor_id = payload.doctor_id or current_user.id
    doctor_user = db.query(User).filter(User.id == assigned_doctor_id).first()
    if not doctor_user or doctor_user.role not in ("doctor", "admin"):
        raise HTTPException(status_code=400, detail="doctor_id must belong to a doctor/admin user")

    visit.is_referred = True
    visit.assigned_doctor_id = doctor_user.id
    visit.referred_at = datetime.utcnow()
    db.commit()
    db.refresh(visit)
    return visit
