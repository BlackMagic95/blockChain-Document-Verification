# 🔐 Blockchain Document Verification System (IPFS + Blockchain Powered)

A secure, tamper-proof **Web3 Document Verification Platform** built using:

⚡ React + Spring Boot + MongoDB + Ethereum + IPFS (Pinata)

This system ensures that once a document is registered, its integrity **cannot be altered, forged, or tampered with**.

Instead of traditional cloud storage, files are stored on **decentralized IPFS**, and their **SHA-256 hash is permanently stored on blockchain** for trustless verification.

Perfect for:
• Certificates
• Academic records
• Legal documents
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
- MongoDB metadata/history
- Dashboard with stats
- CSV export
- File validation (PDF/JPG/PNG/DOC)
- 5MB file limit
- Light/Dark theme
- Toast notifications

## 🔍 Public Verification

- No login required
- Upload document
- Instant authenticity check
- Blockchain validation
- Tamper detection
- Real-time result

---

# 🧠 How It Works

## Registration Flow

1. Upload file
2. Generate SHA-256 hash
3. Upload file → IPFS (Pinata)
4. Receive CID
5. Save CID + metadata → MongoDB
6. Store hash → Ethereum blockchain

## Verification Flow

1. Upload document
2. Generate hash
3. Compare with database
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

# 🌐 Why IPFS Instead of Cloud?

Traditional Cloud Storage ❌  
Decentralized Storage (IPFS) ✅

Benefits:

- Tamper-proof
- Content-addressable (CID)
- No vendor lock-in
- Permanent storage
- Works even if backend is offline
- Ideal for blockchain apps

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
- JUnit

## Blockchain

- Ethereum (Sepolia)
- Solidity Smart Contract
- SHA-256 hashing

---

# 📡 API Endpoints

POST /upload → Register document  
POST /verify → Verify document  
GET /docs → List documents  
GET /stats → Dashboard stats

Swagger:
http://localhost:8080/swagger-ui.html

---

# 🌐 Access Files via IPFS

After upload you receive:

CID

Open in browser:

https://gateway.pinata.cloud/ipfs/<CID>

Files remain accessible even if backend is stopped.

---

# ⚙️ Installation Guide

## Clone

git clone https://github.com/BlackMagic95/blockChain-Document-Verification.git  
cd blockChain-Document-Verification

---

## Backend Setup

Create `.env` inside backend:

BLOCKCHAIN_PRIVATE_KEY=xxx  
BLOCKCHAIN_CONTRACT=xxx  
BLOCKCHAIN_RPC=xxx

PINATA_JWT=xxx

MONGO_URI=xxx  
JWT_SECRET=xxx  
GOOGLE_CLIENT_ID=xxx

Run backend:

cd backend  
./gradlew bootRun

Server:
http://localhost:8080

---

## Frontend Setup

cd frontend  
npm install  
npm run dev

Open:
http://localhost:5173

---

# 📊 Dashboard Stats

- Total Documents
- Blockchain Hashes
- Total Verifications
- Real-time updates
- CSV export

---

# 🔒 Security

- JWT protected APIs
- File validation
- Private keys stored in .env
- No secrets committed
- Immutable IPFS storage
- Blockchain integrity guarantee

---

# 🔮 Future Improvements

- Self-hosted IPFS node
- File encryption
- Drag & drop upload
- Bulk upload
- Docker deployment
- CI/CD
- Smart contract auto-verification

---

# 👨‍💻 Author

Rohan Kumar  
B.Tech ECE – BIT Mesra

GitHub: https://github.com/BlackMagic95  
LinkedIn: https://linkedin.com/in/rkrohankumar

---

# ⭐ Support

If you like this project:
Star ⭐  
Fork 🍴  
Contribute 🚀

---

Built with ❤️ using Spring Boot + IPFS + Blockchain
