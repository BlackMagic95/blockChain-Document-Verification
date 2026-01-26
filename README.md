# Blockchain Document Verification System

A blockchain-based platform for secure document registration and verification using cryptographic hashing and Ethereum smart contracts.

---

## 🚀 Overview

This project implements a **proof-of-existence and document integrity verification system** using blockchain technology.

Users can:

- Register documents by storing their SHA-256 hash on the Ethereum blockchain
- Verify documents by re-uploading and matching hashes
- Detect tampering using immutable on-chain records

The system ensures that documents cannot be altered without detection.

---

## ✨ Features

## 📸 Screenshots

### Dashboard (Verify Mode)

![Dashboard](ScreenShots/Dashboard.png)

### Register Document

![Register](ScreenShots/Register.png)

### Verification Result

![Verified](ScreenShots/Verify.png)

## 🧠 How It Works

1. A document is uploaded via the web interface
2. The backend computes a **SHA-256 cryptographic hash**
3. The hash is stored immutably on the Ethereum blockchain via a smart contract
4. For verification, the same document is uploaded again
5. A new hash is computed and compared against stored records
6. The system reports whether the document is **VERIFIED** or **TAMPERED**

---

## 🧱 System Architecture

- **Frontend:** User interface for uploading and verifying documents
- **Backend:** Handles hashing, persistence, and blockchain interaction
- **Blockchain:** Stores document hashes immutably using smart contracts

---

## 🛠 Tech Stack

- **Frontend:** React, CSS
- **Backend:** Spring Boot, Java, Web3j
- **Blockchain:** Ethereum (Sepolia Testnet)
- **Smart Contracts:** Solidity
- **Cryptography:** SHA-256
- **Database:** H2 (in-memory)

---

## ✨ Features

- Blockchain-backed document registration
- Cryptographic integrity verification
- Tamper detection
- Modern dashboard UI with Register / Verify toggle
- Blockchain explorer overview panel

---

## 🔐 Security

- Private keys are **not stored in the repository**
- Blockchain credentials are loaded via **environment variables**
- Only document hashes are stored on-chain (no raw files)

---

## ▶️ Run Locally

### Backend

```bash
cd backend
set PRIVATE_KEY=your_private_key_here
./gradlew bootRun
```

### Frontend

cd frontend
npm install
npm run dev
