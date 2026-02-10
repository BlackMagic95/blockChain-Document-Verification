# 🔐 Blockchain Document Verification System

A secure, tamper-proof **Web3 Document Verification Platform** built using:

⚡ React + Spring Boot + MongoDB + Ethereum + IPFS

Documents are stored on **decentralized IPFS**, while their **SHA-256 hash is permanently stored on the blockchain** for trustless verification.

Once registered, files **cannot be altered or forged**.

Perfect for:
• Certificates  
• Academic records  
• Legal documents  
• Enterprises  
• Research projects

---

# 🌐 Live Demo

Frontend (Vercel)  
https://block-chain-document-verification-phi.vercel.app

Backend API (Render)  
https://blockchain-document-verification.onrender.com/docs

> Note: Backend may take ~30 seconds to wake up (free tier sleep)

---

# ✨ Features

## 👤 Admin Panel

- Google OAuth login
- JWT authentication
- Upload & register documents
- SHA-256 hashing
- Store files on IPFS (Pinata)
- Store hash on Ethereum blockchain
- MongoDB history tracking
- Dashboard with live stats
- CSV export
- File validation (5MB limit)
- Responsive UI (mobile friendly)
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
5. Save metadata → MongoDB
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
React Frontend (Vercel)  
↓  
Spring Boot Backend (Render)  
↓  
MongoDB + IPFS + Ethereum Blockchain

---

# 🖼 Screenshots

## 🔐 Login Page

![Login](./screenshots/login.png)

## 🔍 Verification Page

![Verify](./screenshots/verify.png)

## 👤 Admin Dashboard

![Dashboard](./screenshots/admin.png)

---

# 🌐 IPFS Access

After upload you receive a CID.

Open directly:
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
- Custom Glass UI CSS

## Backend

- Spring Boot
- MongoDB
- JWT Security
- Web3j
- Pinata IPFS
- Swagger / OpenAPI

## Blockchain

- Ethereum (Sepolia)
- Solidity Smart Contract
- SHA-256 hashing

## DevOps

- Docker
- Vercel
- Render
- GitHub

---

# 📡 API Endpoints

POST /upload → Register  
POST /verify → Verify  
GET /docs → List documents  
GET /stats → System stats

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

Backend runs at:
http://localhost:8080

---

## Frontend

cd frontend  
npm install  
npm run dev

Frontend runs at:
http://localhost:5173

---

# 🐳 Docker (Optional)

docker build -t verify-backend .  
docker run -p 8080:8080 --env-file backend/.env verify-backend

---

# 🔒 Security

- JWT protected APIs
- Private keys stored in environment variables
- File size validation
- Immutable IPFS storage
- Blockchain integrity guarantee
- Admin-only upload endpoints

---

# 🔮 Future Improvements

- Self-hosted IPFS node
- File encryption
- Multi-admin roles
- Smart contract events
- Drag & drop upload
- CI/CD pipeline

---

# 👨‍💻 Author

Rohan Kumar  
GitHub: https://github.com/BlackMagic95  
LinkedIn: https://linkedin.com/in/rkrohankumar

---

⭐ If you like this project, give it a star!
