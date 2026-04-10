# MediLift Design Specifications

## Color Scheme
- Primary Blue: #0052CC (used for buttons, active states, headers)
- Dark Red/Maroon: #8B0000 (used for alerts, critical states)
- Green: #2E7D32 (used for success, active operations)
- Light Gray: #F5F5F5 (backgrounds)
- Medium Gray: #E0E0E0 (borders, dividers)
- Text Dark: #1A1A1A (main text)
- Text Light: #666666 (secondary text)

## Typography
- Headers: Bold, large font sizes
- Body: Regular weight, readable sizes
- Labels: Blue color, uppercase

## Screens Overview

### 1. Login & Onboarding
- Header: "Medilift" with "Offline Ready" indicator
- Hero Text: "Care Without Boundaries."
- Subtitle: "Empowering healthcare workers in every environment, with or without a signal."
- Language Selection: Hindi, English (default selected), Bengali, Tamil
- Voice Guidance Toggle: Enabled/Disabled
- PIN Entry: 4-digit secure PIN with numeric keypad
- Continue Offline Button
- Footer: "SECURED BY MEDILIFT ENCRYPTION v4.2"

### 2. Home Dashboard
- Header: "Medilift" with back arrow and offline indicator
- Daily Focus Section:
  - "MORNING SHIFT" label
  - "Daily Focus" heading
  - "08 VISITS TODAY" (large number)
- Alert Card (Pink background):
  - "CRITICAL INTERVENTION REQUIRED"
  - "3 Patients in High-Risk Zone"
  - Description text
  - "VIEW ALERTS" button (dark red)
- Local Storage Section:
  - Icon with "Local Storage"
  - "14 Records pending cloud synchronization"
  - Sync Progress: 64%
- Action Buttons:
  - "Add New Patient" (Blue, with user+ icon)
  - "Start Visit" (Green, with play icon)
- Pending Follow-ups Section:
  - Patient cards with avatar, name, status, time
  - "SEE ALL" link
- Bottom Navigation: HOME (active), PATIENTS, ALERTS, SETTINGS

### 3. Add Beneficiary
- Header: "Medilift" with back arrow, auto-saving indicator, offline badge
- Title: "New Beneficiary"
- Subtitle: "Register a new patient to the regional healthcare registry."
- Form Fields:
  - Full Patient Name (text input with voice icon)
  - Age (Years) (numeric input)
  - Gender Identification (dropdown)
  - Pregnancy Status (toggle)
  - Community Cluster (dropdown/text)
- Save Button: "Save Patient" (Blue, with user+ icon)
- Bottom Navigation: HOME, PATIENTS (active), ALERTS, SETTINGS

### 4. Visit Recording
- Header: "Medilift" with back arrow, offline indicator
- Progress: "STEP 1 OF 3" with progress bar (33%)
- Title: "Visit Recording"
- Two Tabs: "Select Symptoms" (active), "Record Voice"
- Symptoms Grid:
  - Fever (selected, blue)
  - Cough
  - Pain
  - Breathing
  - Allergy
  - Other
- Patient Vitals Section:
  - Blood Pressure: 120/80 mmHg (with camera icon)
  - Body Weight: 68.5 kg (with weight icon)
- Input Measurement: Active numeric keypad (1-9, 0, decimal, delete)
- Next Step Button (Blue)

### 5. AI Risk Result
- Shows risk assessment results
- Risk score and level
- Recommendations

### 6. Patient History
- Patient list with history
- Past visits and records
- Follow-up information

### 7. Escalation & Referral
- Referral options
- Escalation workflow
- Doctor/specialist assignment

### 8. Notifications
- Notification list
- Alert types
- Timestamps

### 9. Settings
- User preferences
- Language settings
- Offline settings
- Account management

### 10. Sync & Offline State
- Sync status
- Offline indicators
- Data synchronization progress

## UI Components

### Buttons
- Primary (Blue): #0052CC, rounded, large padding
- Secondary (Green): #2E7D32, rounded, large padding
- Alert (Red): #8B0000, rounded, medium padding
- Disabled: Gray

### Input Fields
- Background: #F5F5F5
- Border: #E0E0E0
- Padding: Medium
- Rounded corners

### Cards
- White background
- Subtle shadow
- Rounded corners
- Padding

### Icons
- Microphone (voice input)
- Camera (photo capture)
- Weight scale
- User plus
- Play button
- Settings
- Alerts
- Home

### Status Indicators
- Offline: Brown/tan dot
- Online: Green dot
- Syncing: Blue indicator with percentage
- Auto-saving: Text indicator

### Navigation
- Bottom tab navigation
- Active state: Blue background, white icon
- Inactive state: Gray icon


## Additional Screen Details

### AI Risk Result
- Header: "Medilift" with back arrow, offline indicator
- Title: "Assessment Result"
- Patient ID: ML-992831
- Risk Card (Pink background):
  - Red circular icon with exclamation mark
  - "HIGH RISK" label
  - Risk description: "High-risk pregnancy detected"
  - Recommendation box: "Refer to doctor within 24 hours"
  - "Escalate Case" button (Blue with shield icon)
  - "Done" button (Gray)
- Vitals Display:
  - Blood Pressure: 158/98 (CRITICAL - red text)
  - Oxygen Sat: 94% (BORDERLINE - brown text)
- Footer: "AI CONFIDENCE: 98.4%"

### Patient History
- Header: Back arrow, "Patient History", offline indicator, "Medilift"
- Patient Profile Card:
  - Avatar with online indicator (green dot)
  - "PATIENT FILE #8812"
  - Name: "Evelyn Harper"
  - Age/Conditions: "74 years • Type 2 Diabetes • Hypertension"
  - Last BP: 138/84
  - Glucose: 104 mg/dL
  - Risk Level: Moderate (orange dot)
  - "New Entry" button (Blue)
- Clinical Timeline Section:
  - Emergency Stabilization (Red alert triangle) - Oct 24, 2023
  - Routine Wellness Check (Brown tag) - Sep 12, 2023
  - Initial Assessment (Green checkmark) - Aug 05, 2023
  - Each with detailed description and metadata
- Bottom Navigation: HOME, PATIENTS (active), ALERTS, SETTINGS

### Escalation & Referral
- Header: Back arrow, "Medilift", offline indicator
- Section Label: "CRITICAL CARE"
- Title: "Referral & Escalation"
- Doctor Card:
  - Doctor avatar
  - Name: "Dr. Silas Vance"
  - Specialty: "Senior Cardiologist"
  - Status badge: "AVAILABLE NOW" (Green)
  - Distance: "12KM AWAY" (Gray)
  - "Call Doctor Vance" button (Blue with phone icon)
- Escalation Notes Section:
  - Text area: "Describe patient's worsening condition..."
  - Microphone icon (Blue circle)
- Checkbox: "Mark as referred" with description
- "Confirm Referral" button (Green)
- Bottom Navigation: HOME, PATIENTS (active), ALERTS, SETTINGS

### Notifications (Alerts)
- Header: Back arrow, "Medilift", offline indicator
- Title: "Alerts."
- Subtitle: "Prioritized clinical notifications and system updates for your current shift in the Northern Region."
- "MARK ALL AS READ" button (Gray)
- Alert Categories:
  - HIGH RISK section with alert items (pink background)
    - "Abnormal Vi..." - 2M AGO - "VIEW PATIENT FILE" link
    - "Missed Dos..." - 15M AGO - "LOG MEDICATION" link
  - FOLLOW-UP REMINDERS section
    - "POST-OP REVIEW" - Sarah Jenkins - "SCHEDULE NOW" link
    - "LAB RESULTS" - Michael Chen - "REVIEW REPORT" link
  - SYSTEM UPDATES section
    - "Database Sync Successful" - 1h ago
    - "Protocol Update: v2.4.1" - 4h ago
- Bottom Navigation: HOME, PATIENTS, ALERTS (active), SETTINGS

### Settings
- Header: Back arrow, "Settings", offline indicator
- User Profile Card:
  - Avatar with "SAFE IS WORK" label
  - Name: "Dr. Arjan Singh"
  - Role: "Community Health Lead • Zone 4"
- APP PREFERENCES Section:
  - Voice Guidance (toggle enabled, blue icon)
  - Auto-Sync (toggle enabled, green icon)
  - App Language (currently Hindi, with arrow)
- SUPPORT CENTER Section:
  - "Need Clinical Support?" card (Blue background)
  - "Call Help & Support" button (White text)
- "Logout from Medilift" button (Pink background, red text)
- Version: "VERSION 2.4.1 (STABLE)"
- Bottom Navigation: HOME, PATIENTS, ALERTS, SETTINGS (active)

### Sync & Offline State
- Header: Back arrow, "Medilift", offline badge (tan/brown)
- Main Content:
  - "14 Records Saved Locally" (large blue text)
  - "Waiting for a stable connection. Current status: Weak Signal in the valley sector."
  - "Retry Sync" button (Blue with sync icon)
- Network Diagnostics Section:
  - Title: "NETWORK DIAGNOSTICS"
  - "Unstable Connection Detected"
  - Description: "The system is automatically holding your latest entries to prevent data corruption. Records will upload once signal strength exceeds 40%."
  - Signal Strength: 12% (with bar chart visualization)
- Storage Info:
  - Database icon
  - "248 MB PENDING UPLOAD SIZE"
- Pending Synchronizations Section:
  - Last check: 2 mins ago
  - Items with status badges:
    - "Patient Intake: Sarah Johnson" - Pending (orange)
    - "Dosage Update: Insulin Admin" - Pending (orange)
    - "Lab Results: Hematology" - Synced (green)
- Bottom Navigation: HOME, PATIENTS, ALERTS (active), SETTINGS
