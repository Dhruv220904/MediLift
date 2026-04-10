# FILE: backend/app/ai/predictor.py
import pickle
import os
from typing import List, Dict

# Load model once
model_path = os.path.join(os.path.dirname(__file__), 'model.pkl')
with open(model_path, 'rb') as f:
    model = pickle.load(f)

def predict_risk(observations: List[Dict[str, str]]) -> Dict:
    # Parse observations into features
    features = {
        'age': 30,
        'pregnancy_week': 0,
        'systolic_bp': 120,
        'diastolic_bp': 80,
        'hemoglobin': 12,
        'weight_kg': 60,
        'tb_cough': 0,
        'night_sweats': 0,
        'muac_cm': 22,
        'sdoh_score': 5
    }

    for obs in observations:
        if isinstance(obs, dict):
            key = obs.get("key")
            value = obs.get("value")
        else:
            key = getattr(obs, "key", None)
            value = getattr(obs, "value", None)
        if key is None or value is None:
            continue
        try:
            if key in ['age', 'pregnancy_week', 'systolic_bp', 'diastolic_bp', 'tb_cough', 'night_sweats']:
                features[key] = int(float(value))
            elif key in ['hemoglobin', 'weight_kg', 'muac_cm', 'sdoh_score']:
                features[key] = float(value)
        except ValueError:
            pass  # Keep default

    # Predict
    import numpy as np
    X = np.array([list(features.values())])
    risk_level = model.predict(X)[0]
    risk_score = model.predict_proba(X)[0].max() * 100  # Approximate score

    # Specialist flag
    specialist_flag = "General"
    if features['tb_cough'] == 1 and features['night_sweats'] == 1:
        specialist_flag = "TB"
    elif features['muac_cm'] < 19:
        specialist_flag = "Nutrition"
    elif features['pregnancy_week'] > 0:
        specialist_flag = "Maternal"

    return {
        "risk_score": round(risk_score, 2),
        "risk_level": risk_level,
        "specialist_flag": specialist_flag
    }
