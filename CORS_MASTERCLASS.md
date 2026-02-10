# CORS Masterclass (Cross-Origin Resource Sharing)

CORS is essential when your API is consumed by browser-based JavaScript applications. This guide covers the theory and provides a hands-on laboratory to master CORS.

## 1. Theory: Understanding CORS

### What is CORS?
Browsers block requests from one origin (e.g., `https://frontend.com`) to a different origin (e.g., `https://api.backend.com`) unless the server explicitly allows it. This is a security feature implemented by browsers, known as the **Same-Origin Policy**.

### CORS Headers
The server controls CORS via specific HTTP headers.

```http
HTTP/1.1 200 OK
Access-Control-Allow-Origin: https://frontend.com
Access-Control-Allow-Methods: GET, POST, PUT, DELETE
Access-Control-Allow-Headers: Content-Type, Authorization
Access-Control-Max-Age: 86400
```

| Header | Description |
|--------|-------------|
| `Access-Control-Allow-Origin` | Which origins can access the resource. Use `*` for public APIs (allows everyone), or a specific origin like `https://myapp.com`. |
| `Access-Control-Allow-Methods` | Allowed HTTP methods (e.g., `GET, POST`). |
| `Access-Control-Allow-Headers` | Allowed custom headers (e.g., `Authorization`, `X-Custom-Header`). |
| `Access-Control-Max-Age` | How long the browser should cache the preflight response (in seconds). |

### Preflight Request (OPTIONS)
For "non-simple" requests (e.g., requests with custom headers like `Authorization` or Content-Type `application/json`), the browser acts as a scout. It sends a preliminary `OPTIONS` request to check if the actual request is safe to send.

**The Preflight Request:**
```http
OPTIONS /api/users HTTP/1.1
Origin: https://frontend.com
Access-Control-Request-Method: POST
Access-Control-Request-Headers: Content-Type, Authorization
```

**The Preflight Response (Success):**
```http
HTTP/1.1 204 No Content
Access-Control-Allow-Origin: https://frontend.com
Access-Control-Allow-Methods: POST, OPTIONS
Access-Control-Allow-Headers: Content-Type, Authorization
```

---

## 2. Practical Lab: The CORS Experiment

Let's see CORS in action using a simple Java server and an HTML file. We will create a "Bad" server that fails CORS, and then fix it.

### Prerequisites
- **JDK 8+**: To compile and run the Java server.
- **Node.js & npm**: To serve the client file. [Download here](https://nodejs.org/).

### Step 1: Create a "Bad" Server (Java)
Create a file named `SimpleServer.java`. This server allows `GET` requests but **does not** send CORS headers.

**Note**: This example uses the built-in `com.sun.net.httpserver` package (available in JDK 8+).

```java
import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

public class SimpleServer {
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8080), 0);
        server.createContext("/", new MyHandler());
        server.setExecutor(null); // creates a default executor
        server.start();
        System.out.println("API Server running on port 8080...");
    }

    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            // Handle Preflight (OPTIONS)
            if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                // Return 204 but NO CORS headers (Fail case)
                t.sendResponseHeaders(204, -1);
                return;
            }

            String response = "{\"message\": \"Hello from API!\"}";
            t.getResponseHeaders().set("Content-Type", "application/json");
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
}
```

### Step 2: Create a Client (HTML)
Create a file named `index.html`. This assumes you will open it in your browser (which acts as a different origin if opened as a file or on a different local port).

```html
<!DOCTYPE html>
<html>
<body>
    <h1>CORS Test</h1>
    <button onclick="callApi()">Call API</button>
    <p id="output" style="margin-top: 20px; font-weight: bold;"></p>

    <script>
        async function callApi() {
            const output = document.getElementById('output');
            output.innerText = "Calling API...";
            output.style.color = "black";

            try {
                // Attempt to fetch with a CUSTOM HEADER (Triggers Preflight)
                const response = await fetch('http://localhost:8080/', {
                    headers: { 'X-Custom-Header': 'PreflightTest' }
                });
                const data = await response.json();
                output.innerText = "Success: " + data.message;
                output.style.color = "green";
            } catch (error) {
                output.innerText = "Error (Check Console F12): " + error;
                output.style.color = "red";
                console.error('CORS Error Details:', error);
            }
        }
    </script>
</body>
</html>
```

### Step 3: Run the Experiment (Fail Case)
1.  **Compile and Run the Java server**:
    ```bash
    javac SimpleServer.java
    java SimpleServer
    ```
2.  **Open `index.html`**:
    *   **Option A (Easiest)**: Just double-click `index.html` in your file explorer. Your browser will treat the origin as `null` or `file://`.
    *   **Option B (Better)**: Serve it using `http-server` on a different port:
        ```bash
        # In a separate terminal
        npx http-server -p 3000
        ```
        Then visit `http://localhost:3000/index.html`.
3.  **Click "Call API"**.
4.  **Observe**: You will see an error!
    *   **Browser Console**:
        > *Access to fetch at 'http://localhost:8080/' from origin 'http://localhost:3000' has been blocked by CORS policy: No 'Access-Control-Allow-Origin' header is present on the requested resource.*

### Step 4: Fix the Server (Success Case)
Update `SimpleServer.java` to send the correct headers in the `handle` method.

```java
    static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange t) throws IOException {
            // FIX: Set CORS Headers for ALL requests (including preflight)
            t.getResponseHeaders().set("Access-Control-Allow-Origin", "http://localhost:8081");
            t.getResponseHeaders().set("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
            t.getResponseHeaders().set("Access-Control-Allow-Headers", "X-Custom-Header, Content-Type");

            // Handle Preflight
            if (t.getRequestMethod().equalsIgnoreCase("OPTIONS")) {
                t.sendResponseHeaders(204, -1);
                return;
            }

            String response = "{\"message\": \"Hello from API!\"}";
            t.getResponseHeaders().set("Content-Type", "application/json");
            
            t.sendResponseHeaders(200, response.length());
            OutputStream os = t.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
    }
```

5.  **Recompile and Restart**:
    *   Stop the server (Ctrl+C).
    *   `javac SimpleServer.java`
    *   `java SimpleServer`
6.  **Click "Call API"** again.
7.  **Result**: The text "Hello from API!" should appear in green.

---

## 3. Interactive FAQ

**Q: My React app on `localhost:3000` can't call my backend on `localhost:8080`. Why?**
**A:** Even though they are both "localhost", the **ports are different** (3000 vs 8080). This makes them different origins. You must enable CORS on port 8080.

**Q: Can I just use `Access-Control-Allow-Origin: *` everywhere?**
**A:** Only for public APIs (like a weather API). For private user data, you should specify the exact origin (e.g., `https://my-app.com`) to prevent malicious sites from reading your users' data if they are logged in.

**Q: How do I handle CORS in Production?**
**A:** In production, your backend should dynamically check the `Origin` header of the request against a whitelist of allowed domains (e.g., `['https://myapp.com', 'https://admin.myapp.com']`) and mirror that origin back in the `Access-Control-Allow-Origin` header.
