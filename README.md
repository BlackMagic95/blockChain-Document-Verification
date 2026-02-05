# 🔐 Blockchain Document Verification System

<p align="center">

![React](https://img.shields.io/badge/Frontend-React-blue)
![Spring](https://img.shields.io/badge/Backend-SpringBoot-green)
![MongoDB](https://img.shields.io/badge/Database-MongoDB-darkgreen)
![Ethereum](https://img.shields.io/badge/Blockchain-Ethereum-purple)
![License](https://img.shields.io/badge/License-MIT-orange)

</p>

---

## 🚀 Overview

<<<<<<< HEAD
A *decentralized, tamper-proof document verification platform* that uses:
=======
A _decentralized, tamper-proof document verification platform_ that uses:
>>>>>>> 410db3b (UI polish + verify styling + final fixes)

- SHA-256 hashing
- Blockchain anchoring (Ethereum)
- MongoDB metadata
- Secure file storage
- Google OAuth admin access

This system ensures:

✅ No document tampering  
✅ Immutable proof  
✅ Transparent verification  
<<<<<<< HEAD
✅ Trustless architecture  
=======
✅ Trustless architecture
>>>>>>> 410db3b (UI polish + verify styling + final fixes)

---

## ✨ Features

### 👨‍💼 Admin
<<<<<<< HEAD
- Google login
- Upload & register documents
- Duplicate detection (hash-based)
- Blockchain anchoring
- View history dashboard
- Live stats

### 🌍 Public
- Verify documents without login
- Instant authenticity check
- Timestamped blockchain proof

### 📊 Analytics
=======

- Google login
- Upload & register documents
- Duplicate detection (hash-based)
- Blockchain anchoring
- View history dashboard
- Live stats

### 🌍 Public

- Verify documents without login
- Instant authenticity check
- Timestamped blockchain proof

### 📊 Analytics

>>>>>>> 410db3b (UI polish + verify styling + final fixes)
- Total registered documents
- Total verifications
- Real-time stats

---

# 📸 Screenshots

## 🔐 Login Page

<p align="center">
  <img src="screenshots/login.png" width="800"/>
</p>

---

## ✅ Verify Page

<p align="center">
  <img src="screenshots/verify.png" width="800"/>
</p>

---

## 📊 Admin Dashboard

<p align="center">
  <img src="screenshots/admin.png" width="800"/>
</p>

---

# 🏗️ Architecture
<<<<<<< HEAD


React Frontend
      ↓
Spring Boot Backend
      ↓
SHA-256 Hash
      ↓
MongoDB (metadata)
      ↓
Ethereum Smart Contract (hash stored)
      ↓
Cloudinary / IPFS (file storage)

=======

React Frontend
↓
Spring Boot Backend
↓
SHA-256 Hash
↓
MongoDB (metadata)
↓
Ethereum Smart Contract (hash stored)
↓
Cloudinary / IPFS (file storage)
>>>>>>> 410db3b (UI polish + verify styling + final fixes)

---

# 🛠️ Tech Stack

## Frontend
<<<<<<< HEAD
- ReactJS
- Axios
- Google OAuth
- Modern Glass UI

## Backend
=======

- ReactJS
- Axios
- Google OAuth
- Modern Glass UI

## Backend

>>>>>>> 410db3b (UI polish + verify styling + final fixes)
- Spring Boot
- MongoDB
- JWT Auth
- Cloudinary Storage

## Blockchain
<<<<<<< HEAD
=======

>>>>>>> 410db3b (UI polish + verify styling + final fixes)
- Ethereum (Sepolia)
- Web3j
- Smart Contracts

---

# ⚙️ How It Works

## 📌 Register Flow

Upload file
<<<<<<< HEAD
   ↓
Generate SHA-256 hash
   ↓
Save metadata (MongoDB)
   ↓
Store hash on Blockchain
   ↓
Success


## 🔍 Verify Flow

Upload file
   ↓
Generate hash
   ↓
Compare with DB
   ↓
Validate on blockchain
   ↓
Verified / Tampered


=======
↓
Generate SHA-256 hash
↓
Save metadata (MongoDB)
↓
Store hash on Blockchain
↓
Success

## 🔍 Verify Flow

Upload file
↓
Generate hash
↓
Compare with DB
↓
Validate on blockchain
↓
Verified / Tampered

>>>>>>> 410db3b (UI polish + verify styling + final fixes)
---

# 🔧 Local Setup

## 1️⃣ Clone
<<<<<<< HEAD
=======

>>>>>>> 410db3b (UI polish + verify styling + final fixes)
bash
git clone https://github.com/BlackMagic95/blockChain-Document-Verification.git
cd blockChain-Document-Verification

<<<<<<< HEAD

---

## 2️⃣ Backend
=======
---

## 2️⃣ Backend

>>>>>>> 410db3b (UI polish + verify styling + final fixes)
bash
cd backend
./gradlew bootRun

<<<<<<< HEAD

Runs at:

http://localhost:8080


---

## 3️⃣ Frontend
=======
Runs at:

http://localhost:8080

---

## 3️⃣ Frontend

>>>>>>> 410db3b (UI polish + verify styling + final fixes)
bash
cd frontend
npm install
npm start

<<<<<<< HEAD

=======
>>>>>>> 410db3b (UI polish + verify styling + final fixes)
Runs at:

http://localhost:3000

<<<<<<< HEAD

=======
>>>>>>> 410db3b (UI polish + verify styling + final fixes)
---

# 🔑 Environment Variables

### application.properties

properties
spring.data.mongodb.uri=YOUR_MONGO_URL

blockchain.private-key=YOUR_PRIVATE_KEY
blockchain.contract=YOUR_CONTRACT_ADDRESS
blockchain.rpc=YOUR_RPC_URL

google.client-id=YOUR_GOOGLE_CLIENT_ID

<<<<<<< HEAD

=======
>>>>>>> 410db3b (UI polish + verify styling + final fixes)
---

# 📡 API Endpoints

## Auth
<<<<<<< HEAD
POST /auth/google

## Admin
=======

POST /auth/google

## Admin

>>>>>>> 410db3b (UI polish + verify styling + final fixes)
POST /upload
GET /docs

## Public
<<<<<<< HEAD
=======

>>>>>>> 410db3b (UI polish + verify styling + final fixes)
POST /verify
GET /stats

---

# 📈 Project Highlights

✔ Blockchain anchored hashes  
✔ Duplicate prevention  
✔ Immutable verification  
✔ Clean dashboard UI  
✔ Real-time stats  
✔ Ready for IPFS integration  
<<<<<<< HEAD
✔ IEEE research ready  
=======
✔ IEEE research ready
>>>>>>> 410db3b (UI polish + verify styling + final fixes)

---

# 🎯 Use Cases

- Academic certificates
- Government records
- Legal contracts
- HR onboarding
- Compliance audits
- Digital identity

---

# 🔮 Future Improvements

- IPFS decentralized storage
- Filecoin/Arweave support
- Batch blockchain anchoring
- Merkle tree optimization
- Zero-knowledge proofs
- Enterprise deployment
- IEEE publication

---

# 👨‍💻 Author

<<<<<<< HEAD
*Rohan Kumar*

GitHub → https://github.com/BlackMagic95  
LinkedIn → https://linkedin.com/in/rkrohankumar  
=======
_Rohan Kumar_

GitHub → https://github.com/BlackMagic95  
LinkedIn → https://linkedin.com/in/rkrohankumar
>>>>>>> 410db3b (UI polish + verify styling + final fixes)

---

# ⭐ Support

If you like this project:

⭐ Star the repo  
🍴 Fork it  
<<<<<<< HEAD
🚀 Share it  
=======
🚀 Share it
>>>>>>> 410db3b (UI polish + verify styling + final fixes)

---

# 📜 License

MIT License
