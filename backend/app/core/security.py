# FILE: backend/app/core/security.py
from datetime import datetime, timedelta
from typing import Optional
from jose import JWTError, jwt
from passlib.context import CryptContext
from ..config import settings

# pbkdf2_sha256 avoids bcrypt backend issues across Python/build environments.
pwd_context = CryptContext(schemes=["pbkdf2_sha256"], deprecated="auto")

def verify_password(plain_password, hashed_password):
    return pwd_context.verify(plain_password, hashed_password)

def get_password_hash(password):
    return pwd_context.hash(password)

def _create_token(data: dict, token_type: str, expires_delta: timedelta):
    to_encode = data.copy()
    expire = datetime.utcnow() + expires_delta
    to_encode.update({"exp": expire, "type": token_type})
    encoded_jwt = jwt.encode(to_encode, settings.secret_key, algorithm=settings.algorithm)
    return encoded_jwt


def create_access_token(data: dict, expires_delta: Optional[timedelta] = None):
    return _create_token(
        data=data,
        token_type="access",
        expires_delta=expires_delta or timedelta(minutes=settings.access_token_expire_minutes),
    )


def create_refresh_token(data: dict, expires_delta: Optional[timedelta] = None):
    return _create_token(
        data=data,
        token_type="refresh",
        expires_delta=expires_delta or timedelta(days=7),
    )


def verify_token(token: str, token_type: Optional[str] = None):
    try:
        payload = jwt.decode(token, settings.secret_key, algorithms=[settings.algorithm])
        if token_type is not None and payload.get("type") != token_type:
            return None
        return payload
    except JWTError:
        return None
