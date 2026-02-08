# 🔐 Blockchain Document Verification System (IPFS + Blockchain Powered)

A secure, tamper-proof **Web3 Document Verification Platform** built using:

⚡ React + Spring Boot + MongoDB + Ethereum + IPFS (Pinata)

Files are stored on **decentralized IPFS**, and their **SHA-256 hash is permanently stored on blockchain** for trustless verification.

Once registered, documents **cannot be altered or forged**.

Perfect for:
• Certificates  
• Academic records  
• Legal docs  
• Enterprises  
• Research projects

---

# 🚀 Features

## 👤 Admin Panel

- Google OAuth login
- JWT authentication
- Upload & register documents
- SHA-256 hashing
- Store files on IPFS (Pinata)
- Store hash on Ethereum blockchain
- MongoDB history tracking
- Dashboard with stats
- CSV export
- File validation + 5MB limit
- Light/Dark theme

## 🔍 Public Verification

- No login required
- Upload document
- Instant authenticity check
- Blockchain validation
- Tamper detection

---

# 🧠 How It Works

## Registration Flow

1. Upload file
2. Generate SHA-256 hash
3. Upload file → IPFS (Pinata)
4. Receive CID
5. Save CID → MongoDB
6. Store hash → Blockchain

## Verification Flow

1. Upload file
2. Generate hash
3. Compare with DB
4. Validate with blockchain
5. Show VERIFIED / NOT REGISTERED / TAMPERED

---

# 🏗 Architecture

User  
↓  
React Frontend  
↓  
Spring Boot Backend  
↓  
MongoDB (metadata/history)  
IPFS (file storage)  
Ethereum Blockchain (hash storage)

---

# 🖼 Screenshots

## 🔐 Login Page

![Login](./screenshots/login.png)

## 🔍 Document Verification

![Verify](./screenshots/verify.png)

## 👤 Admin Dashboard

![Dashboard](./screenshots/admin.png)

---

# 🌐 IPFS File Access

After upload you receive:

CID

Open in browser:

https://gateway.pinata.cloud/ipfs/<CID>

Files remain accessible even if backend is offline.

---

# 🛠 Tech Stack

## Frontend

- React (Vite)
- Axios
- React Router
- Google OAuth
- React Hot Toast

## Backend

- Spring Boot
- MongoDB
- Web3j
- JWT Security
- Pinata IPFS integration
- Swagger (OpenAPI)

## Blockchain

- Ethereum (Sepolia)
- Solidity Smart Contract
- SHA-256 hashing

---

# 📡 API Endpoints

POST /upload → Register  
POST /verify → Verify  
GET /docs → List  
GET /stats → Stats

Swagger:
http://localhost:8080/swagger-ui.html

---

# ⚙️ Installation Guide

## Clone

git clone https://github.com/BlackMagic95/blockChain-Document-Verification.git  
cd blockChain-Document-Verification

---

## Backend

Create `.env`:

BLOCKCHAIN_PRIVATE_KEY=xxx  
BLOCKCHAIN_CONTRACT=xxx  
BLOCKCHAIN_RPC=xxx

PINATA_JWT=xxx

MONGO_URI=xxx  
JWT_SECRET=xxx  
GOOGLE_CLIENT_ID=xxx

Run:

cd backend  
./gradlew bootRun

---

## Frontend

cd frontend  
npm install  
npm run dev

Open:
http://localhost:5173

---

# 🔒 Security

- JWT protected APIs
- Private keys hidden in .env
- File validation
- Immutable IPFS storage
- Blockchain integrity guarantee

---

# 🔮 Future Improvements

- Self-hosted IPFS node
- File encryption
- Drag & drop upload
- Docker deployment
- CI/CD

---

# 👨‍💻 Author

Rohan Kumar  
GitHub: https://github.com/BlackMagic95  
LinkedIn: https://linkedin.com/in/rkrohankumar

---

Built with ❤️ using Spring Boot + IPFS + Blockchain
