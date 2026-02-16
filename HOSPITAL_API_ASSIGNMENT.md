# REST API Assignment: Hospital Patient Management

## 1. Problem Statement

You are the Lead Backend Engineer for a new **Hospital Management System**. The hospital needs a way to digitize patient records, manage admissions, and schedule appointments.

Your task is to:
1.  **Design a REST API** for managing patients, their medical records, and doctor appointments.
2.  **Create an OpenAPI Specification** (YAML/JSON) for your design.

The system needs to track patients, their basic information, their medical history (e.g., diagnoses, treatments, visit notes), and manage appointment scheduling.

## 2. Requirements

### 2.1 Domain Knowledge (The "Business Logic")
-   **Patients**: Individuals receiving care. We need to store their name, date of birth, gender, and contact info.
-   **Medical Records**: Every time a patient visits or is treated, a record is created. A patient can have multiple medical records over time (1-to-Many relationship).
-   **Appointments**: Patients need to schedule visits with doctors. An appointment has a date, time, doctor, and status (e.g., SCHEDULED, COMPLETED, CANCELLED).
-   **Admissions**: (Optional/Advanced) Patients can be admitted to the hospital. THis part is optional

### 2.2 Core Features
The API must support:
-   Registering a new patient.
-   Retrieving patient details.
-   **Scheduling a new appointment for a patient.**
-   **Viewing upcoming appointments.**
-   **Canceling an appointment.**
-   Adding a new medical record for a patient (after a visit).
-   Viewing a patient's medical history (all records).
-   Discharging a patient (updating status).

### 2.3 Deliverables
A single file named `hospital-api-spec.yaml` containing the complete OpenAPI 3.0 definition for your designed API.

## 3. The Design Process (4 Steps)

Follow these steps to design your API before writing any YAML.

### Step 1: Identify Resources
*   Information that needs to be stored and accessed.
*   **Hint**: Look for nouns in the requirements (e.g., "Patient", "Appointment").

### Step 2: Define Relationships
*   How do resources connect?
*   Does an `Appointment` belong to a `Patient`?
*   Does a `MedicalRecord` link to an `Appointment`?

### Step 3: Design Endpoints
*   Map your resources to URIs.
*   Use HTTP methods (`GET`, `POST`, `PUT`, `DELETE`, `PATCH`) for actions.
*   Example: `GET /resource`, `POST /resource`.

### Step 4: Plan Request/Response Data
*   What data comes in? (e.g., when scheduling an appointment)
*   What data goes out? (e.g., the appointment confirmation with an ID)

---

## 4. Submitting Your Work

Once you have completed your design and OpenAPI specification, compare it with the provided solution file `HOSPITAL_API_SOLUTION.md`.
