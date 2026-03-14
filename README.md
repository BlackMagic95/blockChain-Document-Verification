<div align="center">

# 🔐 Blockchain Document Verification System

### Trustless · Tamper-Proof · Decentralized

[![Java](https://img.shields.io/badge/Java-17-orange?style=flat-square&logo=openjdk)](https://openjdk.org/)
[![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-brightgreen?style=flat-square&logo=springboot)](https://spring.io/projects/spring-boot)
[![React](https://img.shields.io/badge/React-18-61DAFB?style=flat-square&logo=react)](https://react.dev/)
[![MongoDB](https://img.shields.io/badge/MongoDB-Database-47A248?style=flat-square&logo=mongodb)](https://www.mongodb.com/)
[![Ethereum](https://img.shields.io/badge/Ethereum-Sepolia-purple?style=flat-square&logo=ethereum)](https://sepolia.etherscan.io/)
[![IPFS](https://img.shields.io/badge/IPFS-Pinata-65C2CB?style=flat-square&logo=ipfs)](https://pinata.cloud/)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg?style=flat-square)](LICENSE)
[![Live Demo](https://img.shields.io/badge/Live_Demo-Vercel-black?style=flat-square&logo=vercel)](https://your-app.vercel.app)

**A secure Web3 document verification platform** that ensures authenticity of certificates and official documents using blockchain immutability, decentralized storage, and cryptographic hashing.

[🚀 Live Demo](https://block-chain-document-verification-phi.vercel.app/) · [📖 API Docs (Swagger)](https://blockchain-document-verification.onrender.com/swagger-ui.html) · [📜 Smart Contract on Etherscan](https://sepolia.etherscan.io/address/0xB56D9F4309CAF0C20f52FBF3C14e0653A9Ed50AC)

</div>

---

## 📌 Problem

Traditional document verification systems suffer from:

- **Centralized databases** — single point of failure and trust
- **Easily forgeable certificates** — no cryptographic proof of authenticity
- **Manual verification processes** — slow, expensive, institution-dependent
- **High trust dependency** — relies on goodwill of a central authority
- **Slow institutional validation** — days or weeks for certificate checks

Fake certificates are increasingly common and costly to detect at scale.

---

## 💡 Solution

This platform introduces **trustless document verification** by combining:

| Technology  | Purpose                          |
| ----------- | -------------------------------- |
| Blockchain  | Immutable document hash registry |
| IPFS        | Decentralized encrypted storage  |
| AES-256     | Secure document encryption       |
| SHA-256     | Cryptographic fingerprint        |
| MongoDB     | Metadata & access management     |
| JWT + OAuth | Secure session management        |

This eliminates the need for a central authority to verify documents.

---

## 🌐 Live Demo

> **Frontend:** [https://your-app.vercel.app](https://block-chain-document-verification-phi.vercel.app/)
> **Backend API:** [https://your-backend.onrender.com](https://blockchain-document-verification.onrender.com)
> **Swagger UI:** [https://your-backend.onrender.com/swagger-ui.html](https://blockchain-document-verification.onrender.com)

> ⚠️ The demo uses the **Ethereum Sepolia Testnet**. No real ETH is required.

---

## ⚙️ Core Workflow

### Document Registration Flow

```
Admin Uploads Document
        ↓
SHA-256 Hash Generated
        ↓
AES-256 Encryption
        ↓
Encrypted File Uploaded to IPFS (via Pinata)
        ↓
IPFS CID Stored in MongoDB
        ↓
Hash Registered on Ethereum Sepolia (Smart Contract)
        ↓
Document Permanently Verifiable on Blockchain
```

### Document Verification Flow

```
User Uploads Document
        ↓
System Generates SHA-256 Hash
        ↓
Check MongoDB Database
        ↓
Verify Hash on Blockchain (Smart Contract Call)
        ↓
Compare Hashes
        ↓
Return Verification Result
```

**Possible results:**

| Status            | Meaning                                                        |
| ----------------- | -------------------------------------------------------------- |
| `VERIFIED`        | Hash matches in both MongoDB and Blockchain ✅                 |
| `TAMPERED_DB`     | Blockchain hash exists but DB record is modified or missing ⚠️ |
| `BLOCKCHAIN_ONLY` | Hash exists only on blockchain, DB record not found 🔍         |
| `NOT_REGISTERED`  | Document not found in either system ❌                         |

---

## 🏗 System Architecture

```
                ┌───────────────┐
                │     User      │
                └───────┬───────┘
                        │
                        ▼
              ┌─────────────────┐
              │  React Frontend │  (Vite + TailwindCSS)
              └────────┬────────┘
                       │ REST API
                       ▼
             ┌───────────────────┐
             │ Spring Boot Server│  (Java 17, JWT Auth)
             └───────┬───────────┘
                     │
        ┌────────────┼─────────────┐
        ▼            ▼             ▼
   MongoDB        IPFS           Ethereum
  (Metadata)   (Pinata CDN)    (Sepolia Testnet)
                Encrypted       Hash Registry
                Documents       Smart Contract
```

---

## 🔐 Security Architecture

Security is implemented in **multiple independent layers**:

| Layer                  | Implementation                                   |
| ---------------------- | ------------------------------------------------ |
| Authentication         | Google OAuth 2.0 + JWT tokens                    |
| File Encryption        | AES-256 before upload to IPFS                    |
| Document Fingerprint   | SHA-256 hash (unique per document)               |
| Blockchain Integrity   | Ethereum smart contract — immutable hash storage |
| Decentralized Storage  | IPFS via Pinata — no single point of failure     |
| Multi-Tenant Isolation | collegeId-scoped MongoDB queries + JWT claims    |

---

## 📸 Screenshots

### Login Page

![Login](./screenshots/login.png)

### Document Verification

![Verify](./screenshots/verify.png)

### Admin Dashboard

![Admin](./screenshots/admin.png)

### Super Admin Panel

![SuperAdmin](./screenshots/superadmin.png)

### College Request Form

![CollegeForm](./screenshots/college-request.png)

---

## 🏫 College Onboarding System

Institutions must apply before gaining access. This prevents unauthorized organizations from issuing verifiable documents.

```
College submits access request
        ↓
Status stored as PENDING
        ↓
Super Admin reviews request
        ↓
APPROVED or REJECTED
        ↓
Approved college receives portal access
```

---

## 🏢 Multi-Tenant College Isolation

```
College A → sees only College A documents
College B → sees only College B documents
Super Admin → sees everything across all colleges
```

Implemented using `collegeId` in JWT claims + scoped MongoDB queries. No cross-tenant data leakage.

---

## 📜 Smart Contract

> **Network:** Ethereum Sepolia Testnet
> **Contract Address:** [`0xB56D9F4309CAF0C20f52FBF3C14e0653A9Ed50AC`](https://sepolia.etherscan.io/address/0xB56D9F4309CAF0C20f52FBF3C14e0653A9Ed50AC)

The Solidity smart contract exposes two functions:

```solidity
// Register a document hash on-chain
function registerDocument(string memory docHash) public onlyAuthorized

// Check if a document hash is registered
function verifyDocument(string memory docHash) public view returns (bool)
```

---

## 🧰 Tech Stack

### Frontend

- **React 18** + **Vite**
- **TailwindCSS** for styling
- **Axios** for API calls

### Backend

- **Spring Boot 3.x** (Java 17)
- **JWT Authentication**
- **Swagger / OpenAPI** — [View Docs](https://your-backend.onrender.com/swagger-ui.html)

### Blockchain

- **Solidity** Smart Contract
- **Web3j** (Java Ethereum library)
- **Ethereum Sepolia Testnet**

### Storage & Database

- **IPFS via Pinata** — decentralized file storage
- **MongoDB** — metadata, access control, college records

### Deployment

- **Frontend** → Vercel (auto-deploy on push to `main`)
- **Backend** → Render (auto-deploy on push to `main`)

---

## 🚀 Quick Start

### Prerequisites

- Java 17+
- Node.js 18+
- MongoDB (local or Atlas)
- A [Pinata](https://pinata.cloud/) account (free tier works)
- A [Google Cloud](https://console.cloud.google.com/) OAuth 2.0 app
- An Ethereum wallet with Sepolia testnet ETH ([faucet](https://sepoliafaucet.com/))

---

### 1. Clone the Repository

```bash
git clone https://github.com/BlackMagic95/blockChain-Document-Verification.git
cd blockChain-Document-Verification
```

---

### 2. Environment Setup

Copy the example env file and fill in your credentials:

```bash
cp backend/.env.example backend/.env
```

**`backend/.env`** — all required variables:

```env
# MongoDB
MONGO_URI=mongodb+srv://<user>:<password>@cluster.mongodb.net/docverify

# JWT
JWT_SECRET=your_super_secret_key_at_least_32_chars
JWT_EXPIRY_MS=86400000

# Google OAuth
GOOGLE_CLIENT_ID=your_google_client_id.apps.googleusercontent.com
GOOGLE_CLIENT_SECRET=your_google_client_secret

# IPFS / Pinata
PINATA_API_KEY=your_pinata_api_key
PINATA_SECRET_KEY=your_pinata_secret_key

# AES Encryption
AES_SECRET_KEY=your_32_char_aes_encryption_key!!

# Ethereum / Web3j
WEB3J_RPC_URL=https://sepolia.infura.io/v3/your_infura_project_id
CONTRACT_ADDRESS=0xYOUR_DEPLOYED_CONTRACT_ADDRESS
WALLET_PRIVATE_KEY=your_ethereum_wallet_private_key

# Super Admin
SUPER_ADMIN_EMAIL=admin@yourdomain.com
```

---

### 3. Backend Setup

```bash
cd backend
./gradlew bootRun
```

Backend runs at: `http://localhost:8080`
Swagger UI: `http://localhost:8080/swagger-ui.html`

---

### 4. Frontend Setup

```bash
cd frontend
npm install
npm run dev
```

Frontend runs at: `http://localhost:5173`

---

### 5. Deploy Smart Contract (optional — already deployed on Sepolia)

```bash
cd blockchain
npm install
npx hardhat run scripts/deploy.js --network sepolia
```

Update `CONTRACT_ADDRESS` in your `.env` with the newly deployed address.

---

## 🐳 Docker (Optional)

```bash
docker build -t doc-verification .
docker run -p 8080:8080 --env-file backend/.env doc-verification
```

---

## 📡 API Reference

Full interactive docs: [`/swagger-ui.html`](https://your-backend.onrender.com/swagger-ui.html)

| Method | Endpoint              | Auth Required  | Description                      |
| ------ | --------------------- | -------------- | -------------------------------- |
| POST   | `/upload`             | ✅ Admin       | Register and encrypt a document  |
| POST   | `/verify`             | ❌ Public      | Verify a document's authenticity |
| GET    | `/docs`               | ✅ Admin       | List all registered documents    |
| GET    | `/download/{id}`      | ✅ Admin       | Download decrypted document      |
| GET    | `/stats`              | ✅ Admin       | Platform-wide statistics         |
| POST   | `/access/request`     | ❌ Public      | Submit college access request    |
| GET    | `/admin/requests`     | ✅ Super Admin | View all pending requests        |
| POST   | `/admin/approve/{id}` | ✅ Super Admin | Approve a college request        |
| POST   | `/admin/reject/{id}`  | ✅ Super Admin | Reject a college request         |

---

## 📊 Project Highlights

This project demonstrates end-to-end proficiency in:

- **Web3 / Blockchain integration** — Solidity + Web3j + Sepolia testnet
- **Cryptographic security** — AES-256 encryption + SHA-256 hashing
- **Decentralized storage** — IPFS via Pinata
- **Multi-tenant SaaS architecture** — college-scoped data isolation
- **Modern full-stack development** — React + Spring Boot
- **OAuth2 + JWT** — production-grade auth flow
- **CI/CD pipelines** — Vercel + Render auto-deployment

---

## 🔮 Known Limitations & Future Improvements

- **Gas fees**: Each document registration costs a small amount of Sepolia ETH. On mainnet this would require real ETH — a meta-transaction or L2 solution (e.g. Polygon) would reduce costs significantly.
- **IPFS pinning**: Free Pinata tier has upload limits. Production would need a paid plan or self-hosted IPFS node.
- **Batch registration**: Currently registers one document at a time. A batch endpoint would improve admin UX for large institutions.
- **Mobile app**: Verification could be done via a simple mobile app using QR codes on printed certificates.
- **ZK Proofs**: Future versions could use zero-knowledge proofs to verify document properties without revealing the document itself.

---

## 📄 License

This project is licensed under the **MIT License** — see the [LICENSE](LICENSE) file for details.

---

## 👨‍💻 Author

**Rohan Kumar**

[![GitHub](https://img.shields.io/badge/GitHub-BlackMagic95-181717?style=flat-square&logo=github)](https://github.com/BlackMagic95)

---

## ⭐ Support

If you found this project useful, please consider giving it a **star ⭐ on GitHub** — it helps others discover it!

---

<div align="center">
<sub>Built with ☕ Java, ⚛️ React, and ⛓️ Ethereum</sub>
</div>
