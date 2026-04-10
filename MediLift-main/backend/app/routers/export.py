from fastapi import APIRouter, Depends
from sqlalchemy.orm import Session
from fastapi.responses import StreamingResponse
from io import StringIO
import csv

from ..database import get_db
from ..models import Visit, Beneficiary

router = APIRouter(tags=["export"])


@router.get("/export")
def export_visits_csv(db: Session = Depends(get_db)):
    visits = db.query(Visit, Beneficiary).join(Beneficiary).all()
    output = StringIO()
    writer = csv.writer(output)
    writer.writerow(
        [
            "Visit ID",
            "Beneficiary Name",
            "Village",
            "Visit Type",
            "Risk Score",
            "Risk Level",
            "Specialist Flag",
            "Referred",
            "Assigned Doctor ID",
            "Notes",
            "Created At",
        ]
    )
    for visit, beneficiary in visits:
        writer.writerow(
            [
                visit.id,
                beneficiary.name,
                beneficiary.village or "",
                visit.visit_type,
                visit.risk_score or "",
                visit.risk_level or "",
                visit.specialist_flag or "",
                "yes" if visit.is_referred else "no",
                visit.assigned_doctor_id or "",
                visit.notes or "",
                visit.created_at,
            ]
        )

    output.seek(0)
    return StreamingResponse(
        output,
        media_type="text/csv",
        headers={"Content-Disposition": "attachment; filename=visits.csv"},
    )
