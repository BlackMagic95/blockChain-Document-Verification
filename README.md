# 🔐 Blockchain Document Verification System

A secure, tamper-proof **Blockchain Based Document Verification Platform** built using  
⚡ React + Spring Boot + MongoDB + Ethereum + Cloud Storage

This system guarantees that once a document is registered, its integrity **cannot be altered, forged, or tampered with**.

Designed for colleges, universities, certificates, legal docs, and enterprise verification.

---

## 🚀 Features

### 👤 Admin Panel

- Google OAuth Login
- Secure JWT authentication
- Upload & register documents
- SHA-256 hashing
- Store hash on Ethereum blockchain
- File storage on Cloudinary
- MongoDB history tracking
- Dashboard with live stats
- CSV export of registered documents
- File type validation (PDF/JPG/PNG/DOC)
- File size limit protection (5MB)
- Toast notifications
- Light / Dark theme toggle
- Logout system

---

### 🔍 Public Verification

- No login required
- Upload any document
- Instant authenticity check
- Blockchain validation
- Tamper detection
- Real-time result display

---

## 🔐 Security Enhancements

- Private keys moved to .env
- Secrets not committed to GitHub
- JWT protected APIs
- Backend file validation
- Try/Catch for blockchain failures
- Clean .gitignore (node_modules removed)

---

## 🧪 Testing & Dev Tools

- JUnit tests (controller endpoints)
- Swagger / OpenAPI documentation
- API testing UI
- Structured error handling
- Clean logging

Swagger URL:
http://localhost:8080/swagger-ui.html

---

## 🧠 How It Works

### 📌 Registration Flow

1. Upload file
2. Generate SHA-256 hash
3. Upload file → Cloudinary
4. Save metadata → MongoDB
5. Store hash → Ethereum blockchain
6. Return success response

---

### 📌 Verification Flow

1. Upload document
2. Generate hash
3. Compare with database
4. Validate with blockchain
5. Show VERIFIED / NOT REGISTERED / TAMPERED

---

## 🏗 Architecture

```
User → React Frontend
      ↓
Spring Boot Backend (REST APIs)
      ↓
MongoDB (metadata/history)
Cloudinary (file storage)
Ethereum Blockchain (hash storage)
```

---

### Why this architecture?

| Component  | Purpose                |
| ---------- | ---------------------- |
| Blockchain | Tamper-proof integrity |
| MongoDB    | Fast queries & history |
| Cloudinary | Secure file hosting    |
| SHA-256    | Unique fingerprint     |
| JWT        | Secure authentication  |
| Swagger    | API documentation      |

---

## 🖼 Screenshots

### 🔐 Login Page

![Login](./screenshots/login.png)

---

### 🔍 Document Verification

![Verify](./screenshots/verify.png)

---

### 👤 Admin Dashboard

![Admin](./screenshots/admin.png)

---

## ⚙️ Tech Stack

### 🎨 Frontend

- React (Vite)
- React Router
- Axios
- React Hot Toast
- Google OAuth
- Modern CSS UI

### ⚙️ Backend

- Spring Boot
- MongoDB
- Web3j
- JWT Authentication
- Cloudinary Storage
- Swagger (OpenAPI)
- JUnit Testing

### ⛓ Blockchain

- Ethereum (Sepolia Testnet)
- Smart Contracts (Solidity)
- SHA-256 hashing

---

## 🛠 Installation Guide

### Clone

```bash
git clone https://github.com/BlackMagic95/blockchain-document-verification.git
cd blockchain-document-verification
```

---

## 🔐 Environment Variables (IMPORTANT)

Create `.env` inside backend:

```
BLOCKCHAIN_PRIVATE_KEY=xxx
BLOCKCHAIN_CONTRACT=xxx
BLOCKCHAIN_RPC=xxx

CLOUDINARY_NAME=xxx
CLOUDINARY_KEY=xxx
CLOUDINARY_SECRET=xxx

MONGO_URI=xxx
JWT_SECRET=xxx
GOOGLE_CLIENT_ID=xxx
```

---

## ▶ Backend

```bash
cd backend
./gradlew bootRun
```

Runs on:
http://localhost:8080

Swagger:
http://localhost:8080/swagger-ui.html

---

## ▶ Frontend

```bash
cd frontend
npm install
npm run dev
```

Open:
http://localhost:5173

---

## 📊 Dashboard Stats

- Total Registered Documents
- Total Blockchain Hashes
- Total Verifications
- Real-time updates
- CSV Export

---

## ✨ UI Highlights

- Glassmorphism cards
- Smooth animations
- Light/Dark theme
- Toast notifications
- Responsive design
- Professional dashboard layout

---

## 🔮 Future Improvements

- IPFS decentralized storage
- Drag & Drop upload
- File preview
- Audit logs
- Bulk upload
- Docker deployment
- CI/CD pipeline
- Cloud hosting

---

## 🎓 Research / IEEE Scope

This project demonstrates:

- Blockchain for document integrity
- Hybrid cloud + decentralized architecture
- Secure digital verification
- Tamper-proof systems
- Real-world scalability

Suitable for:

- IEEE conference paper
- Research publication
- Final year project
- Resume showcase

---

## 👨‍💻 Author

Rohan Kumar  
B.Tech Electronics & Communication Engineering – BIT Mesra

GitHub: https://github.com/BlackMagic95  
LinkedIn: https://linkedin.com/in/rkrohankumar

---

## ⭐ Support

If you like this project:

Star ⭐ the repo  
Fork 🍴 it  
Improve 🚀 it

---

Built with ❤️ using Blockchain + Full Stack
