# FILE: backend/app/models/__init__.py
from sqlalchemy import Column, Integer, String, Boolean, Float, Text, DateTime, ForeignKey
from sqlalchemy.dialects.sqlite import TEXT
from sqlalchemy.orm import relationship
from datetime import datetime
import uuid
from ..database import Base

class User(Base):
    __tablename__ = "users"

    id = Column(String, primary_key=True, default=lambda: str(uuid.uuid4()))
    name = Column(String, nullable=False)
    email = Column(String, unique=True, nullable=False)
    password_hash = Column(String, nullable=False)
    role = Column(String, nullable=False)  # asha / doctor / admin
    district = Column(String, nullable=True)
    created_at = Column(DateTime, default=datetime.utcnow)

class Beneficiary(Base):
    __tablename__ = "beneficiaries"

    id = Column(String, primary_key=True, default=lambda: str(uuid.uuid4()))
    asha_id = Column(String, ForeignKey("users.id"), nullable=False)
    name = Column(String, nullable=False)
    gender = Column(String, nullable=True)
    dob = Column(String, nullable=True)
    pregnancy_status = Column(Boolean, default=False)
    village = Column(String, nullable=True)
    created_at = Column(DateTime, default=datetime.utcnow)

class Visit(Base):
    __tablename__ = "visits"

    id = Column(String, primary_key=True, default=lambda: str(uuid.uuid4()))
    local_id = Column(String, nullable=True)
    beneficiary_id = Column(String, ForeignKey("beneficiaries.id"), nullable=False)
    visit_type = Column(String, nullable=False)
    risk_score = Column(Float, nullable=True)
    risk_level = Column(String, nullable=True)  # Low/Medium/High
    specialist_flag = Column(String, nullable=True)  # Maternal/TB/Nutrition/General
    is_referred = Column(Boolean, default=False)
    assigned_doctor_id = Column(String, ForeignKey("users.id"), nullable=True)
    referred_at = Column(DateTime, nullable=True)
    notes = Column(Text, nullable=True)
    synced = Column(Boolean, default=True)
    created_at = Column(DateTime, default=datetime.utcnow)

class VisitObservation(Base):
    __tablename__ = "visit_observations"

    id = Column(Integer, primary_key=True, autoincrement=True)
    visit_id = Column(String, ForeignKey("visits.id"), nullable=False)
    key = Column(String, nullable=False)
    value = Column(String, nullable=False)
