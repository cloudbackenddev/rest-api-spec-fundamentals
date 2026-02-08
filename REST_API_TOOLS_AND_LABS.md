# REST API Tools & Labs

This guide provides practical tools and exercises to master REST API testing and interaction.

---

## 1. Testing REST APIs

### Tools for Testing:

| Tool | Use Case |
|------|----------|
| **curl -k** | Command-line testing |
| **Postman** | GUI-based testing with collections |
| **Insomnia** | Lightweight alternative to Postman |
| **HTTPie** | Human-friendly curl -k alternative |

### curl -k Cheatsheet:

> 💡 **Tip:** Use `-v` (verbose) to see request/response headers for debugging!

```bash
# GET request (verbose to see headers)
curl -k -v -X GET "https://reqres.in/api/users"

# GET with headers
curl -k -v -X GET "https://reqres.in/api/users" \
  -H "Authorization: Bearer YOUR_TOKEN"

# POST with JSON body
curl -k -v -X POST "https://reqres.in/api/users" \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice", "email": "alice@example.com"}'

# PUT (update)
curl -k -v -X PUT "https://reqres.in/api/users/42" \
  -H "Content-Type: application/json" \
  -d '{"name": "Alice Updated"}'

# DELETE
curl -k -v -X DELETE "https://reqres.in/api/users/42"

# See response headers only (use -i instead of -v for cleaner output)
curl -k -i "https://reqres.in/api/users"

# Follow redirects
curl -k -v -L "https://httpbin.org/redirect-to?url=https://www.google.com"
```

### ⚠️ Windows (PowerShell/CMD) Users:
Single quotes don't work the same on Windows! Use escaped double quotes instead:

```powershell
# POST with JSON body (Windows)
curl -k -v -X POST "https://reqres.in/api/users" -H "Content-Type: application/json" -d "{\"name\": \"Alice\", \"email\": \"alice@example.com\"}"

# PUT (Windows)
curl -k -v -X PUT "https://reqres.in/api/users/42" -H "Content-Type: application/json" -d "{\"name\": \"Alice Updated\"}"
```

**Alternative: Use a JSON file**
```powershell
# Create data.json with your JSON, then:
curl -k -v -X POST "https://reqres.in/api/users" -H "Content-Type: application/json" -d @data.json
```

| Shell | JSON Quoting |
|-------|--------------|
| Linux/Mac (bash) | `'{"key": "value"}'` |
| Windows (PowerShell) | `"{\"key\": \"value\"}"` |

**Interactive Exercise:**
> Write a curl -k command to create a new product with `name: "Laptop"` and `price: 999.99`.
> **Answer (Linux/Mac):**
> ```bash
> curl -k -X POST "https://dummyjson.com/products/add" \
>   -H "Content-Type: application/json" \
>   -d '{"name": "Laptop", "price": 999.99}'
> ```
> **Answer (Windows):**
> ```powershell
> curl -k -X POST "https://dummyjson.com/products/add" -H "Content-Type: application/json" -d "{\"name\": \"Laptop\", \"price\": 999.99}"
> ```

---

## 2. Hands-On Exercises with HTTPBin
[httpbin.org](https://httpbin.org) is a free public API for testing HTTP requests. Use it to practice everything you've learned!

### What is HTTPBin?
HTTPBin echoes back whatever you send it, making it perfect for learning and debugging.

### Exercise 1: Basic GET Request
```bash
curl -k -v -X GET "https://httpbin.org/get"
```
**What to observe:** The `-v` flag shows request/response headers, origin IP, and URL.

---

### Exercise 2: GET with Query Parameters
```bash
curl -k -v -X GET "https://httpbin.org/get?name=Alice&role=developer"
```
**Task:** Find the `args` object in the response. What does it contain?

---

### Exercise 3: POST with JSON Body
```bash
curl -k -v -X POST "https://httpbin.org/post" \
  -H "Content-Type: application/json" \
  -d '{"username": "john", "email": "john@example.com"}'
```
**Task:** Find your JSON data in the `json` field of the response.

---

### Exercise 4: Custom Headers
```bash
curl -k -v -X GET "https://httpbin.org/headers" \
  -H "Authorization: Bearer my-secret-token" \
  -H "X-Custom-Header: HelloWorld"
```
**Task:** Verify your custom headers appear in the response.

---

### Exercise 5: HTTP Methods
Try different methods and observe the responses:
```bash
# PUT
curl -k -v -X PUT "https://httpbin.org/put" -d "data=updated"

# PATCH
curl -k -v -X PATCH "https://httpbin.org/patch" -d "field=patched"

# DELETE
curl -k -v -X DELETE "https://httpbin.org/delete"
```

---

### Exercise 6: Status Codes
HTTPBin can return any status code you want:
```bash
# Get a 404 response
curl -k -i "https://httpbin.org/status/404"

# Get a 500 response
curl -k -i "https://httpbin.org/status/500"

# Get a 201 response
curl -k -i "https://httpbin.org/status/201"
```
**Task:** Try status codes 200, 301, 401, 403, 429, and 503. Observe the responses.

---

### Exercise 7: Response Headers
See custom response headers:
```bash
curl -k -i "https://httpbin.org/response-headers?X-Custom=MyValue&Cache-Control=no-cache"
```

---

### Exercise 8: Delayed Response (Simulating Slow APIs)
```bash
# Wait 3 seconds before response
curl -k -v "https://httpbin.org/delay/3"
```
**Use case:** Test timeout handling in your client code.

---

### Exercise 9: Basic Authentication
```bash
curl -k -v -u "myuser:mypassword" "https://httpbin.org/basic-auth/myuser/mypassword"
```
**Task:** Try with wrong credentials and observe the 401 response.

---

### Exercise 10: Redirects
```bash
# Follow redirects
curl -k -v -L "https://httpbin.org/redirect/3"

# See redirect without following
curl -k -v -i "https://httpbin.org/redirect/1"
```

---

### Challenge Exercises:

**Challenge 1:** Send a POST request to `https://httpbin.org/post` with:
- Header: `Content-Type: application/json`
- Header: `Authorization: Bearer abc123`
- Body: `{"product": "Laptop", "price": 999}`

<details>
<summary>Solution</summary>

```bash
curl -k -v -X POST "https://httpbin.org/post" \
  -H "Content-Type: application/json" \
  -H "Authorization: Bearer abc123" \
  -d '{"product": "Laptop", "price": 999}'
```
</details>

**Challenge 2:** Make a request that returns a 429 status code and includes a `Retry-After` header.

<details>
<summary>Solution</summary>

```bash
curl -k -i "https://httpbin.org/status/429"
```
</details>

**Challenge 3:** Use HTTPBin to test what happens when a server takes 5 seconds to respond.

<details>
<summary>Solution</summary>

```bash
curl -k -v "https://httpbin.org/delay/5"
```
</details>

---

### HTTPBin Endpoints Reference:

| Endpoint | Description |
|----------|-------------|
| `/get` | Returns GET data |
| `/post` | Returns POST data |
| `/put` | Returns PUT data |
| `/patch` | Returns PATCH data |
| `/delete` | Returns DELETE data |
| `/status/{code}` | Returns given status code |
| `/headers` | Returns request headers |
| `/ip` | Returns origin IP |
| `/user-agent` | Returns user-agent |
| `/delay/{n}` | Delays response by n seconds |
| `/basic-auth/{user}/{pass}` | Tests HTTP Basic Auth |
| `/redirect/{n}` | Redirects n times |
| `/cookies` | Returns cookies |
| `/cookies/set?name=value` | Sets cookies |
