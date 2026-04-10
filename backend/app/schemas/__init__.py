# FILE: backend/app/schemas/__init__.py
from pydantic import BaseModel
from typing import List, Optional
from datetime import datetime

# User schemas
class UserBase(BaseModel):
    name: str
    email: str
    role: str  # asha / doctor / admin
    district: Optional[str] = None

class UserCreate(UserBase):
    password: str

class User(UserBase):
    id: str
    created_at: datetime

    class Config:
        from_attributes = True

class UserLogin(BaseModel):
    email: str
    password: str

class Token(BaseModel):
    access_token: str
    refresh_token: str
    token_type: str
    user_id: str
    role: str
    name: str


class RefreshRequest(BaseModel):
    refresh_token: str

# Beneficiary schemas
class BeneficiaryBase(BaseModel):
    name: str
    gender: Optional[str] = None
    dob: Optional[str] = None
    pregnancy_status: bool = False
    village: Optional[str] = None

class BeneficiaryCreate(BeneficiaryBase):
    pass

class Beneficiary(BeneficiaryBase):
    id: str
    asha_id: str
    created_at: datetime

    class Config:
        from_attributes = True

# Visit schemas
class VisitObservationBase(BaseModel):
    key: str
    value: str

class VisitBase(BaseModel):
    beneficiary_id: str
    visit_type: str
    notes: Optional[str] = None
    observations: List[VisitObservationBase] = []

class VisitCreate(VisitBase):
    local_id: Optional[str] = None

class Visit(VisitBase):
    id: str
    local_id: Optional[str] = None
    risk_score: Optional[float] = None
    risk_level: Optional[str] = None
    specialist_flag: Optional[str] = None
    is_referred: bool = False
    assigned_doctor_id: Optional[str] = None
    referred_at: Optional[datetime] = None
    synced: bool = True
    created_at: datetime

    class Config:
        from_attributes = True

# Sync schemas
class SyncVisit(BaseModel):
    local_id: str
    beneficiary_id: str
    visit_type: str
    notes: Optional[str] = None
    observations: List[VisitObservationBase] = []

class SyncRequest(BaseModel):
    visits: List[SyncVisit]

class SyncResult(BaseModel):
    local_id: str
    status: str  # "synced" or "already_synced"
    visit_id: Optional[str] = None
    risk_score: Optional[float] = None
    risk_level: Optional[str] = None
    specialist_flag: Optional[str] = None

class SyncResponse(BaseModel):
    synced: int
    results: List[SyncResult]

# Stats schemas
class StatsResponse(BaseModel):
    total_visits_today: int
    high_risk_today: int
    total_beneficiaries: int
    active_ashas: int

class HighRiskVisit(BaseModel):
    id: str
    beneficiary_name: str
    district: Optional[str] = None
    village: Optional[str] = None
    specialist_flag: Optional[str] = None
    is_referred: bool = False
    assigned_doctor_id: Optional[str] = None
    risk_score: float
    created_at: datetime


class ReferVisitRequest(BaseModel):
    doctor_id: Optional[str] = None
