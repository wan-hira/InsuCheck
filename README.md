# InsuCheck - Diabetic Monitoring App

**Mobile Computing Project - Master 1 Computer Science**
**CY Cergy Paris Université**

## 1. Team Members
* **AWAN Waneeza Hira**
* **BELKACIMI Yassine**

## 2. Project Description
**InsuCheck** is an interactive health assistant designed for diabetic patients. It streamlines the management of insulin and glucose levels by combining manual data entry with real-time mobile features.

### Key Features:
* **Health Logging**: Users can manually log glycemia and hemoglobin levels.
* **Multimedia Integration**: For every entry, users can capture a photo (e.g., of their meal or medical device) which is stored in the app's internal storage.
* **Geo-Location**: Automatically captures GPS coordinates (Latitude/Longitude) for each health entry to track environmental contexts.
* **Emergency Alert System**: If a critical glucose level is detected, the app automatically sends an emergency SMS to a pre-configured trusted contact.
* **Smart Interaction**: Uses a `BroadcastReceiver` to listen for a "OK" reply from the emergency contact to automatically silence alerts.
* **Data Persistence**: All entries, including time, location, and image file paths, are stored in a local **SQLite** database.

## 3. Technical Specifications
* **Android Studio Version**: Ladybug 2024.2.1 (latest)
* **Minimum SDK**: API 24
* **Target/Max SDK**: API 30 (Android 11.0)
    * *Note: The app is optimized for API 30 to ensure compatibility with standard permission handling and background services as discussed in class.*
* **Language**: Java
* **Database**: SQLite (OpenHelper)

## 4. Components Used
This project implements core concepts from the curriculum:
* **Activities & Intents**: Comprehensive navigation stack between Profile, Dashboard, Entry, and History screens.
* **Shared Preferences**: Permanent storage for user identity and emergency contact details.
* **SQLite Database**: Implementation of a structured database to manage `Entry` objects with multiple data types.
* **Broadcast Receivers**: Background listening for system SMS events to trigger app logic.
* **Hardware API**: Integration of **Camera** and **LocationManager/GPS**.
* **Telephony**: Use of `SmsManager` for automated emergency communication.

## 5. Required Permissions
To function as intended, the app requires:
* `SEND_SMS` & `RECEIVE_SMS`
* `ACCESS_FINE_LOCATION`
* `CAMERA`
* `READ_EXTERNAL_STORAGE` / `WRITE_EXTERNAL_STORAGE` (for image management)

## 6. How to Run
1. Clone the repository or extract the ZIP file.
2. Open the project in Android Studio (Ladybug).
3. Build the project against API 30.
4. Run on an emulator or physical device (ensure SMS and Location permissions are granted).
