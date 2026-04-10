# FILE: backend/app/ai/train.py
import pandas as pd
import numpy as np
from sklearn.ensemble import RandomForestClassifier
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report
import pickle
import os

# Generate synthetic data
np.random.seed(42)
n_samples = 1500

data = {
    'age': np.random.randint(15, 80, n_samples),
    'pregnancy_week': np.random.choice([0] + list(range(1, 41)), n_samples, p=[0.7] + [0.3/40]*40),
    'systolic_bp': np.random.randint(90, 200, n_samples),
    'diastolic_bp': np.random.randint(60, 120, n_samples),
    'hemoglobin': np.random.uniform(5, 15, n_samples),
    'weight_kg': np.random.uniform(40, 100, n_samples),
    'tb_cough': np.random.choice([0, 1], n_samples, p=[0.9, 0.1]),
    'night_sweats': np.random.choice([0, 1], n_samples, p=[0.85, 0.15]),
    'muac_cm': np.random.uniform(15, 30, n_samples),
    'sdoh_score': np.random.uniform(0, 10, n_samples)
}

df = pd.DataFrame(data)

# Calculate risk scores
def calculate_risk(row):
    score = 0
    if row['systolic_bp'] > 160:
        score += 25
    if row['hemoglobin'] < 7:
        score += 20
    if row['pregnancy_week'] > 0 and row['systolic_bp'] > 140:
        score += 20
    if row['tb_cough'] == 1 and row['night_sweats'] == 1:
        score += 18
    if row['muac_cm'] < 18:
        score += 15
    score += row['sdoh_score'] * 3
    return min(score, 100)

df['risk_score'] = df.apply(calculate_risk, axis=1)

# Label risk levels
def get_risk_level(score):
    if score <= 30:
        return 'Low'
    elif score <= 60:
        return 'Medium'
    else:
        return 'High'

df['risk_level'] = df['risk_score'].apply(get_risk_level)

# Features and target
features = ['age', 'pregnancy_week', 'systolic_bp', 'diastolic_bp', 'hemoglobin', 'weight_kg', 'tb_cough', 'night_sweats', 'muac_cm', 'sdoh_score']
X = df[features]
y = df['risk_level']

# Train model
X_train, X_test, y_train, y_test = train_test_split(X, y, test_size=0.2, random_state=42)
model = RandomForestClassifier(n_estimators=200, max_depth=12, random_state=42)
model.fit(X_train, y_train)

# Evaluate
y_pred = model.predict(X_test)
print(classification_report(y_test, y_pred))

# Save model
os.makedirs('app/ai', exist_ok=True)
with open('app/ai/model.pkl', 'wb') as f:
    pickle.dump(model, f)

print("Model saved to app/ai/model.pkl")