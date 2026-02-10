# OAuth2, OpenID Connect, and JWT Masterclass

This guide demystifies modern authentication and authorization protocols. We will cover the theory, manually inspect tokens, and perform a real-world integration with Google APIs.

## 1. Theory: The Big Three

### 1.1 OAuth2 (Delegated Authorization)
**Analogy**: Valet Key.
You give a valet driver a key that opens the car and starts the engine, but doesn't open the trunk. You (Resource Owner) delegate specific access to the Valet (Client) to use your Car (Resource).

*   **Goal**: "Allow this app to access my photos."
*   **Result**: An **Access Token**.
*   **Roles**:
    *   **Resource Owner**: You (the user).
    *   **Client**: The application (e.g., a website).
    *   **Authorization Server**: The system verifying your identity (e.g., Google, Auth0).
    *   **Resource Server**: The API holding your data (e.g., Google Photos API).

### 1.2 OpenID Connect - OIDC (Authentication)
**Analogy**: ID Card.
OAuth2 was only about *access*. It didn't tell the app *who* you are. OIDC adds an identity layer on top of OAuth2.

*   **Goal**: "Log in with Google."
*   **Result**: An **ID Token** (usually a JWT).
*   **Info**: Contains user details like `name`, `email`, `sub` (subject/user ID).

### 1.3 JWT (JSON Web Token)
**Analogy**: A sealed, transparent envelope.
A standardized container for sending data between parties. It is signed, so if anyone changes the data, the seal (signature) breaks.

---

## 2. JWT Anatomy

A JWT is just a long string comprising three parts separated by dots (`.`):
`Header.Payload.Signature`

### Part 1: Header (Algorithm & Token Type)
Describes *how* the token is signed.
```json
{
  "alg": "HS256",
  "typ": "JWT"
}
```

### Part 2: Payload (Claims)
The actual data. Standard claims include:
*   `sub` (Subject): User ID (e.g., "1234567890").
*   `name`: "John Doe".
*   `exp` (Expiration): Timestamp when the token dies.
*   `iat` (Issued At): Timestamp when it was created.

```json
{
  "sub": "1234567890",
  "name": "John Doe",
  "admin": true
}
```

### Part 3: Signature
Used to verify the token hasn't been tampered with.
`HMACSHA256(base64UrlEncode(header) + "." + base64UrlEncode(payload), secret)`

---

## 3. Hands-On: Using jwt.io

**Goal**: Inspect and understand a JWT without writing code.

1.  Go to **[jwt.io](https://jwt.io)**.
2.  Scroll to the "Debugger" section.
3.  **Experiment**:
    *   Look at the **Encoded** side (Left). Change a single character.
    *   Look at the **Decoded** side (Right). Notice the "Signature Verified" box at the bottom turns invalid/red.
    *   This proves that you cannot fake a token without the private key/secret.

---

## 4. Practical Lab: Accessing Google APIs

We will use the **Google OAuth 2.0 Playground** to simulate a real application flow. We will get an ID Token (who we are) and an Access Token (permission to access data).

### Step 1: Configure Scope
1.  Open [Google OAuth 2.0 Playground](https://developers.google.com/oauthplayground/).
2.  On the left, scroll (or searching) for **"Google OAuth2 API v2"**.
3.  Select `https://www.googleapis.com/auth/userinfo.email` and `https://www.googleapis.com/auth/userinfo.profile`.
    *   *This asks Google: "I want to access the user's email and profile".*
4.  Click **Authorize APIs** (Blue Button).
5.  Log in with your Google Account and "Allow" access.

### Step 2: Exchange Code for Tokens
1.  You will be redirected back. You now have an **Authorization Code** (Step 1 of OAuth flow complete).
2.  Click **"Exchange authorization code for tokens"**.
3.  You will receive:
    *   **Refresh Token**: To get new tokens later.
    *   **Access Token**: The key to the API.
    *   **ID Token**: The simplified version of your profile (JWT format).

### Step 3: Inspect the ID Token (JWT)
1.  Copy the long string labeled **id_token**.
2.  Paste it into [jwt.io](https://jwt.io).
3.  **Analyze**:
    *   See `iss` (Issuer): `accounts.google.com`.
    *   See `sub`: Your unique Google User ID.
    *   See `email`: Your email address.
    *   *Note: This specific token proves your identity.*

### Step 4: Call a Protected API
Now let's use the **Access Token** to actually request data from Google's servers.

1.  In the Playground, Step 3 is "Configure request to API".
2.  The Request URI should be: `https://www.googleapis.com/oauth2/v2/userinfo`
3.  Click **Send the request**.

**Response Analysis**:
```json
{
  "id": "112233445566778899",
  "email": "yourname@gmail.com",
  "verified_email": true,
  "name": "Your Name",
  "given_name": "Your",
  "family_name": "Name",
  "picture": "https://lh3.googleusercontent.com/..."
}
```

### Step 5: Verify with CURL
You can use the same Access Token in your terminal to prove it works outside the web browser.

Copy the **Access Token** from the playground and run:

```bash
curl -H "Authorization: Bearer YOUR_ACCESS_TOKEN_HERE" \
     https://www.googleapis.com/oauth2/v2/userinfo
```

*   **Result**: You get the same JSON response.
*   **Note**: Access tokens expire (usually in 1 hour). If it fails, refresh the token in the playground.

---

## 5. Summary Flow

```mermaid
sequenceDiagram
    participant User
    participant App (Client)
    participant Google (Auth Server)
    participant GoogleAPI (Resource Server)

    User->>App: Click "Log in with Google"
    App->>Google: Redirect with Scopes (email, profile)
    Google->>User: "Allow App to access your info?"
    User->>Google: Yes
    Google->>App: Authorization Code
    App->>Google: Exchange Code for Tokens
    Google->>App: ID Token (JWT) + Access Token
    
    Note right of App: App validates ID Token (OIDC)<br/>to know WHO the user is.

    App->>GoogleAPI: GET /userinfo<br/>Header: Authorization: Bearer {Access Token}
    GoogleAPI-->>App: JSON {email: "bob@gmail.com", ...}
```
