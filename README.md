# 🔐 Blockchain Document Verification System

![License](https://img.shields.io/badge/license-MIT-green)
![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen)
![React](https://img.shields.io/badge/React-18-blue)
![Blockchain](https://img.shields.io/badge/Blockchain-Ethereum-purple)
![IPFS](https://img.shields.io/badge/Storage-IPFS-black)
![Docker](https://img.shields.io/badge/Deploy-Docker-blue)
![Status](https://img.shields.io/badge/Build-Stable-success)

A secure, tamper-proof **Web3 Document Verification Platform** using:

⚡ React + Spring Boot + MongoDB + Ethereum + IPFS + AES Encryption

---

## 🌟 What makes this project special?

This system provides:

✅ Blockchain integrity  
✅ Decentralized storage (IPFS)  
✅ AES encryption (privacy protection)  
✅ JWT security  
✅ Admin-only uploads  
✅ Secure backend decryption downloads

---

# 🌐 Live Demo

### Frontend (Vercel)

Hosted on :contentReference[oaicite:0]{index=0}  
https://block-chain-document-verification-phi.vercel.app

### Backend (Render)

Hosted on :contentReference[oaicite:1]{index=1}  
https://blockchain-document-verification.onrender.com/docs

> Free tier may take ~30 seconds to wake

---

# 🏗 System Architecture

::contentReference[oaicite:2]{index=2}

### Flow

```
User
   ↓
React Frontend
   ↓
Spring Boot Backend
   ↓
MongoDB (metadata)
IPFS (encrypted file)
Ethereum (hash)
```

---

# ✨ Features

## 👤 Admin Panel

- Google OAuth login
- JWT authentication
- Upload & register documents
- SHA-256 hashing
- AES encryption before IPFS upload 🔐
- Store files on IPFS (Pinata)
- Store hash on Ethereum blockchain
- CSV export
- Stats dashboard
- Responsive UI

---

## 🔍 Public Verification

- No login required
- Upload document
- Blockchain validation
- Tamper detection
- Secure decrypted download

---

# 🔐 Security (Major Highlight)

### Encryption Layer

```
Original File
     ↓
AES Encrypt
     ↓
Upload to IPFS
     ↓
CID (encrypted only)
```

Even if someone knows CID:

❌ cannot read file  
✅ only backend decrypts

👉 prevents data leaks

---

# 🧠 How It Works

## Registration

1. Upload file
2. Generate SHA-256
3. Encrypt (AES)
4. Upload encrypted → IPFS
5. Save metadata → MongoDB
6. Store hash → Blockchain

## Verification

1. Upload file
2. Hash match
3. Check blockchain
4. If valid → secure backend download

---

# 🖼 Screenshots

## 🔐 Login Page

![Login](./screenshots/login.png)

## 🔍 Verification Page

![Verify](./screenshots/verify.png)

## 👤 Admin Dashboard

![Dashboard](./screenshots/admin.png)

---

# 🛠 Tech Stack

## Frontend

- :contentReference[oaicite:6]{index=6}
- Vite
- Axios
- React Router
- Toast notifications

## Backend

- :contentReference[oaicite:7]{index=7}
- :contentReference[oaicite:8]{index=8}
- JWT Security
- Web3j
- AES Encryption
- Swagger

## Blockchain

- :contentReference[oaicite:9]{index=9} (Sepolia)
- Solidity
- SHA-256

## Storage

- :contentReference[oaicite:10]{index=10} (Pinata)

## DevOps

- Docker
- Render
- Vercel
- GitHub

---

# 📡 API Endpoints

| Method | Endpoint       | Description               |
| ------ | -------------- | ------------------------- |
| POST   | /upload        | Register document (Admin) |
| POST   | /verify        | Verify document           |
| GET    | /download/{id} | Secure decrypted download |
| GET    | /docs          | List documents            |
| GET    | /stats         | Stats                     |

Swagger:

```
http://localhost:8080/swagger-ui.html
```

---

# ⚙️ Local Setup

## Clone

```bash
git clone https://github.com/BlackMagic95/blockChain-Document-Verification.git
cd blockChain-Document-Verification
```

---

## Backend

Create `.env`

```env
BLOCKCHAIN_PRIVATE_KEY=xxx
BLOCKCHAIN_CONTRACT=xxx
BLOCKCHAIN_RPC=xxx
PINATA_JWT=xxx
MONGO_URI=xxx
JWT_SECRET=xxx
AES_SECRET=your_32_char_secret_key
```

Run:

```bash
cd backend
./gradlew bootRun
```

---

## Frontend

```bash
cd frontend
npm install
npm run dev
```

---

# 🐳 Docker

```bash
docker build -t verify-backend .
docker run -p 8080:8080 --env-file .env verify-backend
```

---

# 🔒 Security Model

- JWT protected APIs
- Admin-only uploads
- AES encrypted IPFS files
- Blockchain immutability
- Environment-based secrets
- Secure download gateway

---

# 🔮 Future Scope

- Multi-admin roles (RBAC)
- Bulk verification
- Merkle tree batching
- Smart contract events
- Self-hosted IPFS
- CI/CD

---

# 👨‍💻 Author

Rohan Kumar  
GitHub: https://github.com/BlackMagic95

---

⭐ If this project helped you, please star the repo!
