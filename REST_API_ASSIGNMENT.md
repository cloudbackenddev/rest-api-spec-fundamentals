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

## 6. Solution

<details>
<summary>Click to reveal the solution</summary>

### 1. API Design

Following the 4-step design process:

#### Step 1: Identify Resources (Nouns)
*   **Session**: Represents a conference talk or presentation.

#### Step 2: Define Relationships
*   In this simple system, `Session` is a standalone resource (1-to-None). There are no other resources like `Speakers` or `Rooms` to relate to yet.

#### Step 3: Design Endpoints
*   `GET /sessions`: Retrieve a list of all sessions.
*   `GET /sessions/{id}`: Retrieve a specific session by its unique ID.
*   `POST /sessions`: Create a new session.
*   `DELETE /sessions/{id}`: Delete a specific session.

#### Step 4: Plan Request/Response
*   **Session JSON Structure**:
    ```json
    {
      "id": "uuid-string",
      "title": "String",
      "speaker": "String",
      "time": "10:00",
      "duration": 45
    }
    ```
*   **Responses**:
    *   `200 OK`: Successful retrieval.
    *   `201 Created`: Successful creation.
    *   `204 No Content`: Successful deletion.
    *   `404 Not Found`: Resource not found.

### 2. OpenAPI Specification (`conference-api-spec.yaml`)

```yaml
openapi: 3.0.0
info:
  title: Conference Session Management API
  description: API for managing conference sessions.
  version: 1.0.0
servers:
  - url: http://localhost:8080
    description: Local development server

paths:
  /sessions:
    get:
      summary: List all sessions
      operationId: listSessions
      responses:
        '200':
          description: A list of sessions
          content:
            application/json:
              schema:
                type: array
                items:
                  $ref: '#/components/schemas/Session'
    post:
      summary: Create a new session
      operationId: createSession
      requestBody:
        required: true
        content:
          application/json:
            schema:
              $ref: '#/components/schemas/SessionInput'
      responses:
        '201':
          description: Session created successfully
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Session'

  /sessions/{id}:
    get:
      summary: Get a session by ID
      operationId: getSession
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
          description: The ID of the session to retrieve
      responses:
        '200':
          description: Session details
          content:
            application/json:
              schema:
                $ref: '#/components/schemas/Session'
        '404':
          description: Session not found
    delete:
      summary: Delete a session
      operationId: deleteSession
      parameters:
        - name: id
          in: path
          required: true
          schema:
            type: string
          description: The ID of the session to delete
      responses:
        '204':
          description: Session deleted successfully
        '404':
          description: Session not found

components:
  schemas:
    Session:
      type: object
      properties:
        id:
          type: string
          format: uuid
        title:
          type: string
        speaker:
          type: string
        time:
          type: string
          pattern: '^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$'
        duration:
          type: integer
      required:
        - id
        - title
        - speaker
        - time
        - duration

    SessionInput:
      type: object
      properties:
        title:
          type: string
        speaker:
          type: string
        time:
          type: string
          pattern: '^([0-1]?[0-9]|2[0-3]):[0-5][0-9]$'
        duration:
          type: integer
      required:
        - title
        - speaker
        - time
        - duration
```

### 3. Java Implementation

### Session.java
```java
package com.example.rest.model;

public class Session {
    private String id;
    private String title;
    private String speaker;
    private String time;
    private int duration;

    // Constructors, Getters, Setters
    public Session() {}

    public Session(String id, String title, String speaker, String time, int duration) {
        this.id = id;
        this.title = title;
        this.speaker = speaker;
        this.time = time;
        this.duration = duration;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }
    public String getSpeaker() { return speaker; }
    public void setSpeaker(String speaker) { this.speaker = speaker; }
    public String getTime() { return time; }
    public void setTime(String time) { this.time = time; }
    public int getDuration() { return duration; }
    public void setDuration(int duration) { this.duration = duration; }

    @Override
    public String toString() {
        return "Session{id='" + id + "', title='" + title + "'}";
    }
}
```

### ConferenceServer.java
```java
package com.example.rest;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.io.InputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.UUID;

public class ConferenceServer {
    // Map to store sessions in memory
    private static Map<String, String> sessions = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/sessions", new SessionHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("Server started on port 8080");
    }

    static class SessionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();
            // Splitting path to check for ID
            String[] pathParts = path.split("/");

            try {
                if ("GET".equals(method)) {
                    if (pathParts.length == 3) {
                         // GET /sessions/{id}
                        handleGetSession(exchange, pathParts[2]);
                    } else {
                        // GET /sessions
                        handleListSessions(exchange);
                    }
                } else if ("POST".equals(method)) {
                    // POST /sessions
                    handleCreateSession(exchange);
                } else if ("DELETE".equals(method)) {
                    if (pathParts.length == 3) {
                         // DELETE /sessions/{id}
                        handleDeleteSession(exchange, pathParts[2]);
                    } else {
                        sendResponse(exchange, 400, "Bad Request");
                    }
                } else {
                    sendResponse(exchange, 405, "Method Not Allowed");
                }
            } catch (Exception e) {
                e.printStackTrace();
                sendResponse(exchange, 500, "Internal Server Error");
            }
        }

        private void handleListSessions(HttpExchange exchange) throws IOException {
            // Manually building JSON array
            StringBuilder json = new StringBuilder("[");
            int i = 0;
            for (String s : sessions.values()) {
                json.append(s);
                if (++i < sessions.size()) json.append(",");
            }
            json.append("]");
            sendResponse(exchange, 200, json.toString());
        }

        private void handleGetSession(HttpExchange exchange, String id) throws IOException {
            if (sessions.containsKey(id)) {
                sendResponse(exchange, 200, sessions.get(id));
            } else {
                sendResponse(exchange, 404, "Session not found");
            }
        }

        private void handleCreateSession(HttpExchange exchange) throws IOException {
            InputStream is = exchange.getRequestBody();
            String body = new String(is.readAllBytes(), StandardCharsets.UTF_8);
            
            // Simple string manipulation to behave like JSON
            String id = UUID.randomUUID().toString();
            String storedJson = body.trim();
            
            // Quick hack to insert ID into JSON string if it's an object
            if (storedJson.endsWith("}")) {
                storedJson = storedJson.substring(0, storedJson.length() - 1) + ", \"id\": \"" + id + "\"}";
            }
            
            sessions.put(id, storedJson);
            sendResponse(exchange, 201, "Created: " + id);
        }

        private void handleDeleteSession(HttpExchange exchange, String id) throws IOException {
             if (sessions.containsKey(id)) {
                sessions.remove(id);
                sendResponse(exchange, 204, "");
            } else {
                sendResponse(exchange, 404, "Session not found");
            }
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            exchange.sendResponseHeaders(statusCode, response.length());
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
```

### ConferenceClient.java
```java
package com.example.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class ConferenceClient {
    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        String baseUrl = "http://localhost:8080/sessions";

        try {
            // 1. Create a Session
            String json = "{ \"title\": \"Java Concurrency\", \"speaker\": \"Dr. Thread\", \"time\": \"10:00\", \"duration\": 60 }";
            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(json))
                    .build();
            
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("Create Response: " + response.statusCode() + " - " + response.body());

            // 2. List Sessions
            request = HttpRequest.newBuilder()
                    .uri(URI.create(baseUrl))
                    .GET()
                    .build();
            response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println("List Response: " + response.body());

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
```

</details>

