# FILE: backend/app/routers/beneficiary.py
from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from typing import List
from ..database import get_db
from ..models import Beneficiary as BeneficiaryModel, User
from ..schemas import BeneficiaryCreate, Beneficiary as BeneficiaryOut
from ..core.dependencies import get_current_user

router = APIRouter(prefix="/beneficiary", tags=["beneficiary"])

@router.post("/", response_model=BeneficiaryOut)
def create_beneficiary(beneficiary: BeneficiaryCreate, current_user: User = Depends(get_current_user), db: Session = Depends(get_db)):
    db_beneficiary = BeneficiaryModel(
        asha_id=current_user.id,
        name=beneficiary.name,
        gender=beneficiary.gender,
        dob=beneficiary.dob,
        pregnancy_status=beneficiary.pregnancy_status,
        village=beneficiary.village
    )
    db.add(db_beneficiary)
    db.commit()
    db.refresh(db_beneficiary)
    return db_beneficiary

@router.get("/", response_model=List[BeneficiaryOut])
def list_beneficiaries(current_user: User = Depends(get_current_user), db: Session = Depends(get_db)):
    if current_user.role == "admin":
        return db.query(BeneficiaryModel).all()
    else:
        return db.query(BeneficiaryModel).filter(BeneficiaryModel.asha_id == current_user.id).all()

@router.get("/{beneficiary_id}", response_model=BeneficiaryOut)
def get_beneficiary(beneficiary_id: str, current_user: User = Depends(get_current_user), db: Session = Depends(get_db)):
    beneficiary = db.query(BeneficiaryModel).filter(BeneficiaryModel.id == beneficiary_id).first()
    if not beneficiary:
        raise HTTPException(status_code=404, detail="Beneficiary not found")
    if current_user.role != "admin" and beneficiary.asha_id != current_user.id:
        raise HTTPException(status_code=403, detail="Not authorized")
    return beneficiary
