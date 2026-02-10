# Digital Signatures and Self-Signed Certificates

This document explains the theory behind digital signatures and certificates, followed by a hands-on guide to creating self-signed certificates using OpenSSL.

## 1. Theory: Digital Signatures

### What is a Digital Signature?
A digital signature is a cryptographic mechanism used to verify the authenticity and integrity of digital data. It is the digital equivalent of a handwritten signature or a stamped seal, but it offers far more inherent security.

Digital signatures rely on **Asymmetric Cryptography** (Public-Key Cryptography), which uses a pair of keys:
*   **Private Key**: Kept secret by the owner. Used for *signing*.
*   **Public Key**: Shared with everyone. Used for *verifying*.

### Quick Reference: Signing vs. Encryption

| Goal | Action | Sender Uses | Receiver Uses |
| :--- | :--- | :--- | :--- |
| **Digital Signature** | Prove Authenticity | **Private Key** (Sign) | **Public Key** (Verify) |
| **Encryption** | Keep it Secret | **Public Key** (Encrypt) | **Private Key** (Decrypt) |

### The Goals
1.  **Authentication**: Verifies that the message came from the claimed sender (since only they have the private key).
2.  **Integrity**: Verifies that the message was not altered in transit (any change to the data invalidates the signature).
3.  **Non-Repudiation**: The signer cannot deny having signed the message (as only their private key could have created the specific signature).

### How It Works

#### The Signing Process (Sender)
1.  **Hash**: A cryptographic hash function (like SHA-256) is applied to the original data to create a fixed-size **Digest**. This ensures efficiency and integrity.
2.  **Encrypt**: The Digest is encrypted using the sender's **Private Key**.
3.  **Result**: This encrypted digest is the **Digital Signature**. It is attached to the original data.

#### The Verification Process (Receiver)
1.  **Decrypt**: The receiver takes the digital signature and decrypts it using the sender's **Public Key**. This reveals the *original* Digest.
2.  **Hash**: The receiver takes the received data and applies the *same* hash function to create a *new* Digest.
3.  **Compare**: The receiver compares the *decrypted* Digest with the *calculated* Digest.
    *   If they match, the signature is **valid**.
    *   If they differ, the data was tampered with or not signed by the claimed key.

### Visualization (Mermaid)

```mermaid
sequenceDiagram
    %% Accessible Colors
    %% Sender: Blue #E6F3FF
    %% Receiver: Green #E6FFFA
    %% Notes/Actions: Yellow #FFFFE0 / Gray #F5F5F5
    
    actor Sender
    actor Receiver
    
    rect rgb(230, 243, 255)
    Note over Sender: Signing Phase
    Sender->>Sender: Hash(Data) -> Digest
    Sender->>Sender: Encrypt(Digest, PrivateKey) -> Signature
    Sender->>Receiver: Sends Data + Signature
    end
    
    rect rgb(230, 255, 250)
    Note over Receiver: Verification Phase
    Receiver->>Receiver: Decrypt(Signature, PublicKey) -> DecryptedDigest
    Receiver->>Receiver: Hash(ReceivedData) -> CalculatedDigest
    
    alt Digests Match
        Receiver->>Receiver: VALID: Data is authentic & unchanged
    else Digests Do Not Match
        Receiver->>Receiver: INVALID: Data corrupted or fake sender
    end
    end
```

---

## 2. Practical: Hashing in Action

Before diving into certificates, it's crucial to understand hashing, as it's the foundation of digital signatures.

### What is a Hash?
A hash function takes input data of any size and produces a fixed-size string of characters (the hash). 
*   **Deterministic**: Same input always produces the same hash.
*   **One-way**: You cannot reverse the hash to get the original data.
*   **Avalanche Effect**: A tiny change in input produces a completely different hash.

### Hands-On: Hashing with OpenSSL

Try these commands in your terminal to see hashing in action.

**1. Create a simple text file:**
```bash
echo "Hello World" > message.txt
```

**2. Generate a SHA-256 hash:**
```bash
openssl dgst -sha256 message.txt
# Output example: SHA256(message.txt)= a591a6d40bf420404a011733cfb7b190d62c65bf0bcda32b57b277d9ad9f146e
```

**3. Modify the file slightly:**
```bash
echo "Hello World." > message.txt
# Added a period
```

**4. Generate the hash again:**
```bash
openssl dgst -sha256 message.txt
# Output example: SHA256(message.txt)= f960d705c74323c6d74112e457F89786a34559c3a07205166f272a2752119932
```
*Notice completely different the hash is! This demonstrates the "Avalanche Effect".*

### Student Exercise 1: Hashing
1.  Create a file named `contract.txt` with the text: "I agree to pay $100".
2.  Generate the SHA-256 hash of this file. Record the first 4 characters.
3.  Change "$100" to "$1000".
4.  Generate the hash again. 
5.  **Question**: Did the hash change significantly or just slightly? What does this tell you about the reliability of hashing for detecting tampering?

---

## 3. Theory: Digital Certificates & CAs

While a digital signature proves *ownership* of a private key, it doesn't prove *who* the owner is. This is where **Digital Certificates** come in.

### What is a Digital Certificate?
A digital certificate is like a digital passport. It binds a **Public Key** to an **Identity** (like a person, organization, or domain name).

It contains:
*   **Subject Info**: Name, Organization, Domain (CN).
*   **Public Key**: The subject's public key.
*   **Issuer Signature**: A digital signature from a trusted third party (Certificate Authority) verifying this binding.

### Certificate Authority (CA)
A CA is a trusted entity that issues digital certificates.
1.  **Root CA**: The highest level of trust. Root certificates are pre-installed in browsers and OSs.
2.  **Intermediate CA**: Trusted by the Root CA to issue certificates on its behalf.
3.  **Leaf Certificate**: The end-entity certificate (e.g., for `google.com`).

### Chain of Trust
When a browser visits a website, it verifies the certificate by checking its issuer's signature. It follows the chain up until it reaches a trusted Root CA.

### Self-Signed Certificates
A **Self-Signed Certificate** is a certificate where the issuer is the same as the subject.
*   **Issuer**: Same as Subject.
*   **Signature**: Signed with its own Private Key.
*   **Trust**: Not trusted by browsers/OSs by default (because anyone can sign their own certificate).
*   **Use Cases**: Internal testing, development environments, intranets where you can manually distribute the certificate.

---

## 4. Student Exercise 2: Analyzing Real-World Certificates (Google.com)

**Goal**: Inspect the certificate of a major website (google.com) to understand how the Chain of Trust works in the real world.

### Step 1: Fetch the Certificate Chain
We will use `openssl s_client` to connect to google.com and retrieve its certificate information.

Run the following command in your terminal:

```bash
openssl s_client -connect google.com:443 -showcerts
```
*   `s_client`: A generic SSL/TLS client tool.
*   `-connect google.com:443`: Connect to Google on the standard HTTPS port.
*   `-showcerts`: Display the entire certificate chain sent by the server.

### Step 2: Analyze the Output (Key Findings)
The output is long, but here is a "Cheat Sheet" for what you just saw in the terminal:

1.  **The Chain of Trust**: Look for `Certificate chain`.
    - `0 s:CN=*.google.com`: The **Leaf** (website) certificate.
    - `1 s:CN=WE2`: The **Intermediate CA** that signed Google's cert.
    - `2 s:CN=GTS Root R4`: The **Root CA** (or a link to it).
    *Check the `i:` (Issuer) and `s:` (Subject) of each level to see how the "Chain" is linked.*

2.  **Subject Alternative Name (SAN)**: 
    - Search for `X509v3 Subject Alternative Name`.
    - Notice that one certificate for `*.google.com` also covers `youtube.com`, `android.com`, and hundreds of other domains!

3.  **Validity**:
    - Look for `Not Before` and `Not After`.
    - Big sites like Google often use short-lived certificates (e.g., 3 months) for better security through rotation.

4.  **Public Key Info**:
    - Search for `Public Key Algorithm`.
    - Modern sites use `id-ecPublicKey` (Elliptic Curve) which is much faster and uses smaller keys than older RSA keys for the same security.

### Step 3: Deep Dive into Details (Optional)
To see the certificate details in a readable format, we can pipe the output to `openssl x509`.

Run this command:
```bash
echo | openssl s_client -connect google.com:443 | openssl x509 -noout -text
```

**Find the following information:**
1.  **Issuer**: Who issued this certificate? (e.g., Google Trust Services).
2.  **Validity**:
    *   `Not Before`: When did it become valid?
    *   `Not After`: When does it expire? (Notice how short the validity period usually is for big sites!).
3.  **Subject Alternative Name (SAN)**:
    *   Scroll down to `X509v3 Subject Alternative Name`.
    *   See how many other domains this one certificate covers (e.g., `youtube.com`, `android.com`).

---

## 5. Hands-On: Self-Signing with OpenSSL

In this section, we will generate a self-signed certificate. This is essentially acting as your own Certificate Authority.

### Prerequisites
*   **OpenSSL**: Ensure OpenSSL is installed. It comes pre-installed on macOS and most Linux distros. On Windows, use Git Bash or WSL.

### Step 1: Generate a Private Key
First, we need a private key. This key will be used to sign the certificate.

```bash
# Generate a 2048-bit RSA private key and save it to 'my-key.pem'
openssl genrsa -out my-key.pem 2048
```
*   `genrsa`: Command to generate RSA private key.
*   `-out my-key.pem`: Output file.
*   `2048`: Key size in bits (2048 is standard secure).

### Step 2: Create a Certificate Signing Request (CSR)
A CSR contains your identity information (Country, Org, Domain) and your Public Key. Normally, you send this to a CA (like VeriSign or Let's Encrypt). For self-signing, we just process it ourselves.

```bash
# Create a CSR using the private key
openssl req -new -key my-key.pem -out my-request.csr
```
*   `req -new`: Create a new request.
*   `-key my-key.pem`: Use the key we just generated.
*   Interactive prompts will appear. You can enter `.` to leave fields empty. The most important field is **Common Name (CN)** (e.g., `localhost` or `mydomain.local`).

### Step 3: Generate the Self-Signed Certificate
Now, we act as the CA and sign our own request using our own private key.

```bash
# Sign the CSR with the private key to create a certificate valid for 365 days
openssl x509 -req -days 365 -in my-request.csr -signkey my-key.pem -out my-cert.crt
```
*   `x509`: Standard for Public Key Infrastructure (PKI).
*   `-req`: Data is a CSR.
*   `-days 365`: Validity period.
*   `-in`: Input CSR.
*   `-signkey`: Key used to sign the certificate (self-signing).
*   `-out`: Output certificate file.

### Workflow Visualization

```mermaid
flowchart LR
    %% Accessible Colors
    %% Nodes: Light Gray #F0F0F0 with High Contrast Text
    %% Self-Signed Path: Orange #FFD700
    %% CA Path: Blue #87CEEB
    
    classDef default fill:#F0F0F0,stroke:#333,stroke-width:1px,color:#000;
    classDef selfSigned fill:#FFD700,stroke:#d4b106,stroke-width:2px,color:#000;
    classDef caPath fill:#87CEEB,stroke:#5cadcf,stroke-width:2px,color:#000;

    A[Start] --> B[Generate Private Key]
    B --> C[Create CSR]
    C --> D{Signing Authority?}
    D -- Trusted CA --> E[Send CSR to CA]:::caPath
    E --> F[CA Signs with CA Key]:::caPath
    D -- Self-Signed --> G[Sign CSR with OWN Key]:::selfSigned
    F --> H[Receive Trusted Cert]:::caPath
    G --> I[Receive Self-Signed Cert]:::selfSigned
```

### Verifying the Contents
You can inspect the contents of your new certificate:

```bash
openssl x509 -in my-cert.crt -text -noout
```
Look for the **Issuer** and **Subject** fields. In a self-signed certificate, they will be identical.

---

## 6. Student Exercise 3: Be Your Own CA
**Goal**: Create a valid self-signed certificate for a fictional local website development environment.

**Scenario**: You are developing a website locally called `dev.my-awesome-site.local`. You need to enable HTTPS for testing, so you need a certificate.

**Tasks**:
1.  **Generate Key**: Create a private key named `dev-site.key`.
2.  **Create CSR**: Generate a Certificate Signing Request named `dev-site.csr`.
    *   *Important*: When asked for "Common Name", enter `dev.my-awesome-site.local`.
3.  **Self-Sign**: Generate the certificate `dev-site.crt` valid for **30 days**.
4.  **Verification**: 
    - Run the command to inspect the certificate.
    - Confirm the **Issuer** is the same as the **Subject**.
    - Confirm the **Validity** shows it expires in 30 days.

**Bonus Challenge**:
Find the command to view the "Modulus" (Public Key) of both the Private Key and the Certificate. They should match.
*Hint: `openssl rsa -noout -modulus -in ...` and `openssl x509 -noout -modulus -in ...`*
