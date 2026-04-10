# FILE: backend/app/main.py
from fastapi import FastAPI
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy import text
from .database import engine, Base
from .routers import auth, beneficiary, visit, sync, stats, export

# Create tables
Base.metadata.create_all(bind=engine)


def _ensure_visit_columns() -> None:
    # Lightweight schema upgrade path for existing local SQLite DBs.
    with engine.begin() as conn:
        columns = {
            row[1]
            for row in conn.execute(text("PRAGMA table_info(visits)")).fetchall()
        }
        if "is_referred" not in columns:
            conn.execute(text("ALTER TABLE visits ADD COLUMN is_referred BOOLEAN DEFAULT 0"))
        if "assigned_doctor_id" not in columns:
            conn.execute(text("ALTER TABLE visits ADD COLUMN assigned_doctor_id VARCHAR"))
        if "referred_at" not in columns:
            conn.execute(text("ALTER TABLE visits ADD COLUMN referred_at DATETIME"))


_ensure_visit_columns()

app = FastAPI(title="Asha Health App Backend")

# CORS
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

# Include routers
app.include_router(auth.router)
app.include_router(beneficiary.router)
app.include_router(visit.router)
app.include_router(sync.router)
app.include_router(stats.router)
app.include_router(export.router)

@app.get("/ping")
def ping():
    return {"message": "pong"}
