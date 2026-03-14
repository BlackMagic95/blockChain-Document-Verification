<div align="center">

# 🔐 Blockchain Document Verification System

### Trustless • Tamper-Proof • Decentralized

[![License](https://img.shields.io/badge/license-MIT-green.svg)](LICENSE)
[![Java](https://img.shields.io/badge/Java-17-orange.svg)](https://www.java.com)
[![Spring Boot](https://img.shields.io/badge/SpringBoot-3.x-brightgreen.svg)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-blue.svg)](https://reactjs.org)
[![MongoDB](https://img.shields.io/badge/Database-MongoDB-darkgreen.svg)](https://www.mongodb.com)
[![Ethereum](https://img.shields.io/badge/Blockchain-Ethereum-purple.svg)](https://ethereum.org)
[![IPFS](https://img.shields.io/badge/Storage-IPFS-black.svg)](https://ipfs.io)
[![Status](https://img.shields.io/badge/Build-Stable-success.svg)](#)

A **production-ready Web3 document verification platform** ensuring document authenticity using **blockchain, decentralized storage, and strong cryptography.**

</div>

---

# 🎯 Overview

Traditional document verification relies on centralized authorities that can be manipulated or compromised.

This system removes trust requirements using blockchain and cryptography.

```
Document
   ↓
SHA-256 Hash
   ↓
AES-256 Encryption
   ↓
IPFS Storage
   ↓
Ethereum Blockchain Registry
   ↓
Trustless Verification
```

Even if someone accesses the **IPFS CID**, the document remains unreadable because it is encrypted.

---

# ✨ Features

## 👨‍💼 Admin Portal

- Google OAuth login
- JWT authentication
- Upload & register documents
- SHA-256 hashing
- AES-256 encryption
- Blockchain registration
- CSV export
- Responsive dashboard

---

## 🛡️ Super Admin Panel

Super Admin manages platform access.

Capabilities:

- Approve college applications
- Reject suspicious requests
- Monitor system usage
- Manage issuer access

---

## 🏫 College Registration System

Institutions must apply before using the platform.

```
College submits request
        ↓
Stored as PENDING
        ↓
Super Admin reviews
        ↓
APPROVED / REJECTED
        ↓
Approved college can login
```

This ensures only **verified institutions** can issue documents.

---

## 🏢 Multi-Tenant College Isolation

Each college sees only its own documents.

```
College A → documents of A
College B → documents of B
Super Admin → all documents
```

Isolation is implemented using:

- `collegeId`
- JWT authentication
- MongoDB scoped queries

---

# 🔍 Public Verification

Anyone can verify documents without login.

```
Upload document
      ↓
Generate SHA-256
      ↓
Check MongoDB
      ↓
Validate on blockchain
      ↓
Return result
```

Possible results:

- VERIFIED
- TAMPERED_DB
- BLOCKCHAIN_ONLY
- NOT_REGISTERED

---

# 🏗 System Architecture

```
User
 ↓
React Frontend
 ↓
Spring Boot Backend
 ↓
MongoDB (metadata)
 ↓
IPFS (encrypted storage)
 ↓
Ethereum Blockchain
```

---

# 🔐 Security Model

| Layer          | Technology         | Purpose                  |
| -------------- | ------------------ | ------------------------ |
| Authentication | Google OAuth + JWT | secure login             |
| Encryption     | AES-256            | document confidentiality |
| Hashing        | SHA-256            | document fingerprint     |
| Blockchain     | Ethereum           | tamper proof registry    |
| Storage        | IPFS               | decentralized storage    |

---

# 📸 Screenshots

## Login Page

![Login](./screenshots/login.png)

## Document Verification

![Verify](./screenshots/verify.png)

## Admin Dashboard

![Admin](./screenshots/admin.png)

## Super Admin Dashboard

![SuperAdmin](./screenshots/superadmin.png)

## College Request Form

![CollegeForm](./screenshots/college-request.png)

---

# 🌐 Live Demo

| Platform | Link                                                                  |
| -------- | --------------------------------------------------------------------- |
| Frontend | https://block-chain-document-verification-phi.vercel.app              |
| Backend  | https://blockchain-document-verification.onrender.com                 |
| API Docs | https://blockchain-document-verification.onrender.com/swagger-ui.html |

---

# 🛠 Tech Stack

### Frontend

- React
- Vite
- Axios
- TailwindCSS

### Backend

- Spring Boot
- Java 17
- JWT Security
- Swagger OpenAPI

### Blockchain

- Solidity
- Web3j
- Ethereum Sepolia

### Storage

- IPFS (Pinata)

### Database

- MongoDB

### Deployment

- Vercel
- Render

---

# 📡 API Endpoints

| Method | Endpoint        | Description       |
| ------ | --------------- | ----------------- |
| POST   | /upload         | register document |
| POST   | /verify         | verify document   |
| GET    | /docs           | list documents    |
| GET    | /download/{id}  | download file     |
| GET    | /stats          | system statistics |
| POST   | /access/request | college request   |

---

# ⚡ Quick Start

Clone repository

```bash
git clone https://github.com/BlackMagic95/blockChain-Document-Verification.git
cd blockChain-Document-Verification
```

Backend setup

```bash
cd backend
./gradlew bootRun
```

Frontend setup

```bash
cd frontend
npm install
npm run dev
```

---

# 🐳 Docker (Optional)

```bash
docker build -t doc-verification .
docker run -p 8080:8080 doc-verification
```

---

# 👨‍💻 Author

Rohan Kumar  
GitHub: https://github.com/BlackMagic95

---

# ⭐ Support

If you like this project please give it a **star ⭐ on GitHub**.
