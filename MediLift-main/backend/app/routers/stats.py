# FILE: backend/app/routers/stats.py
from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from sqlalchemy import func
from typing import List, Optional
from ..database import get_db
from ..models import Visit, Beneficiary, User
from ..schemas import StatsResponse, HighRiskVisit

router = APIRouter(prefix="/stats", tags=["stats"])

@router.get("/", response_model=StatsResponse)
def get_stats(db: Session = Depends(get_db)):
    today = func.date(func.now())

    total_visits_today = db.query(Visit).filter(func.date(Visit.created_at) == today).count()
    high_risk_today = db.query(Visit).filter(
        func.date(Visit.created_at) == today,
        Visit.risk_level == "High"
    ).count()
    total_beneficiaries = db.query(Beneficiary).count()
    active_ashas = db.query(User).filter(User.role == "asha").count()

    return StatsResponse(
        total_visits_today=total_visits_today,
        high_risk_today=high_risk_today,
        total_beneficiaries=total_beneficiaries,
        active_ashas=active_ashas
    )

@router.get("/high-risk", response_model=List[HighRiskVisit])
def get_high_risk_visits(
    district: Optional[str] = None,
    specialist_flag: Optional[str] = None,
    is_referred: Optional[bool] = None,
    db: Session = Depends(get_db),
):
    query = (
        db.query(Visit, Beneficiary, User)
        .join(Beneficiary, Visit.beneficiary_id == Beneficiary.id)
        .join(User, Beneficiary.asha_id == User.id)
        .filter(Visit.risk_level == "High")
    )
    if district:
        query = query.filter(func.lower(User.district) == district.lower())
    if specialist_flag:
        query = query.filter(func.lower(Visit.specialist_flag) == specialist_flag.lower())
    if is_referred is not None:
        query = query.filter(Visit.is_referred == is_referred)

    visits = query.order_by(Visit.risk_score.desc(), Visit.created_at.desc()).all()
    result = []
    for visit, beneficiary, asha_user in visits:
        result.append(HighRiskVisit(
            id=visit.id,
            beneficiary_name=beneficiary.name,
            district=asha_user.district,
            village=beneficiary.village,
            specialist_flag=visit.specialist_flag,
            is_referred=visit.is_referred,
            assigned_doctor_id=visit.assigned_doctor_id,
            risk_score=visit.risk_score,
            created_at=visit.created_at
        ))
    return result

@router.get("/export/csv")
def export_csv(db: Session = Depends(get_db)):
    import csv
    from io import StringIO
    from fastapi.responses import StreamingResponse

    visits = db.query(Visit, Beneficiary).join(Beneficiary).all()
    output = StringIO()
    writer = csv.writer(output)
    writer.writerow(["Visit ID", "Beneficiary Name", "Village", "Visit Type", "Risk Score", "Risk Level", "Specialist Flag", "Notes", "Created At"])

    for visit, beneficiary in visits:
        writer.writerow([
            visit.id,
            beneficiary.name,
            beneficiary.village or "",
            visit.visit_type,
            visit.risk_score or "",
            visit.risk_level or "",
            visit.specialist_flag or "",
            visit.notes or "",
            visit.created_at
        ])

    output.seek(0)
    return StreamingResponse(
        output,
        media_type="text/csv",
        headers={"Content-Disposition": "attachment; filename=visits.csv"}
    )
