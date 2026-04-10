# FILE: backend/app/routers/auth.py
from fastapi import APIRouter, Depends, HTTPException, status
from sqlalchemy.orm import Session
from ..database import get_db
from ..models import User as UserModel
from ..schemas import UserCreate, User as UserOut, UserLogin, Token, RefreshRequest
from ..core.security import (
    get_password_hash,
    verify_password,
    create_access_token,
    create_refresh_token,
    verify_token,
)

router = APIRouter(prefix="/auth", tags=["auth"])

@router.post("/register", response_model=UserOut)
def register(user: UserCreate, db: Session = Depends(get_db)):
    db_user = db.query(UserModel).filter(UserModel.email == user.email).first()
    if db_user:
        raise HTTPException(status_code=400, detail="Email already registered")
    hashed_password = get_password_hash(user.password)
    db_user = UserModel(
        name=user.name,
        email=user.email,
        password_hash=hashed_password,
        role=user.role,
        district=user.district
    )
    db.add(db_user)
    db.commit()
    db.refresh(db_user)
    return db_user

@router.post("/login", response_model=Token)
def login(user_credentials: UserLogin, db: Session = Depends(get_db)):
    user = db.query(UserModel).filter(UserModel.email == user_credentials.email).first()
    if not user or not verify_password(user_credentials.password, user.password_hash):
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid credentials")
    access_token = create_access_token(data={"sub": user.id, "role": user.role})
    refresh_token = create_refresh_token(data={"sub": user.id, "role": user.role})
    return Token(
        access_token=access_token,
        refresh_token=refresh_token,
        token_type="bearer",
        user_id=user.id,
        role=user.role,
        name=user.name
    )


@router.post("/refresh", response_model=Token)
def refresh_tokens(payload: RefreshRequest, db: Session = Depends(get_db)):
    token_payload = verify_token(payload.refresh_token, token_type="refresh")
    if token_payload is None:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="Invalid refresh token")

    user_id = token_payload.get("sub")
    user = db.query(UserModel).filter(UserModel.id == user_id).first()
    if not user:
        raise HTTPException(status_code=status.HTTP_401_UNAUTHORIZED, detail="User not found")

    new_access = create_access_token(data={"sub": user.id, "role": user.role})
    new_refresh = create_refresh_token(data={"sub": user.id, "role": user.role})
    return Token(
        access_token=new_access,
        refresh_token=new_refresh,
        token_type="bearer",
        user_id=user.id,
        role=user.role,
        name=user.name,
    )
