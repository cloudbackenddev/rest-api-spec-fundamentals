# REST API Assignment: Conference Session Management

## 1. Problem Statement
You are tasked with building a **Conference Session Management System** backend. This system will allow conference organizers to manage the schedule of sessions (talks).

You need to:
1.  **Design a REST API** for managing sessions.
2.  **Create an OpenAPI Specification** for your design.
3.  **Implement a REST API Server** in Java (using standard libraries).
4.  **Implement a REST API Client** in Java to test your server.

## 2. Requirements

### 2.1 Data Model
A **Session** resource should have the following properties:
-   `id`: String (Unique Identifier, e.g., UUID)
-   `title`: String (Title of the talk)
-   `speaker`: String (Name of the speaker)
-   `time`: String (ISO-8601 format, e.g., "10:00")
-   `duration`: Integer (Duration in minutes)

### 2.2 API Requirements
Your API must support the following operations:

| Operation | HTTP Method | Endpoint | Description |
| :--- | :--- | :--- | :--- |
| **List Sessions** | `GET` | `/sessions` | specific filtering optional (e.g., by speaker) |
| **Get Session** | `GET` | `/sessions/{id}` | Get details of a single session |
| **Create Session** | `POST` | `/sessions` | Create a new session. Return 201 Created. |
| **Delete Session** | `DELETE` | `/sessions/{id}` | Remove a session. Return 204 No Content. |
| **Update Session**| `PUT` | `/sessions/{id}` | (Optional) Update session details. |

### 2.3 Implementation Constraints
-   **Language**: Java (JDK 11+)
-   **Server Library**: `com.sun.net.httpserver.HttpServer` (No Spring/Jakarta EE)
-   **Client Library**: `java.net.http.HttpClient`
-   **Storage**: In-memory `Map` is sufficient (stateless server not required for this simple lab).

## 3. Deliverables

### Part A: API Design & OpenAPI Spec
Create a file named `conference-api-spec.yaml`.
-   Define `info`, `servers`, `paths`, and `components`.
-   Use proper HTTP status codes.

### Part B: Java Server Implementation
Create `ConferenceServer.java`.
-   Start an HTTP server on port `8080`.
-   Implement handlers for the `/sessions` endpoints.
-   Parse JSON manually (e.g., simplistic string manipulation) or use a tiny library if available (for this assignment, manual string splitting for simple JSON is acceptable to keep it dependency-free).

### Part C: Java Client Implementation
Create `ConferenceClient.java`.
-   Write a main method that:
    1.  Creates 2-3 new sessions.
    2.  Lists all sessions.
    3.  Gets a specific session by ID.
    4.  Deletes a session.
    5.  Verifies the deletion.

## 4. Reference Implementation (Hints)

**Files Provided:**
- `src/com/example/rest/model/Session.java` (Data Model - **Complete**)
- `src/com/example/rest/ConferenceServer.java` (Server - **Skeleton Provided**)
- `src/com/example/rest/ConferenceClient.java` (Client - **Skeleton Provided**)

**Your Task:**
The provided `ConferenceServer.java` and `ConferenceClient.java` files contain `TODO` comments. You need to fill in the missing logic to make the application work.

**Simple Server/Client Example:**
Refer to `SimpleRestServer.java` and `SimpleRestClient.java` to understand the basics of setting up the server and client.

## 5. Bonus Challenge
-   Add error handling: Return `404 Not Found` if a session ID doesn't exist.
-   Add input validation: Ensure `title` is not empty.
