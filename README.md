# 🔐 Blockchain Document Verification System

A secure, tamper-proof **Blockchain Based Document Verification Platform** built using  
⚡ React + Spring Boot + MongoDB + Ethereum + Cloud Storage.

This system guarantees that once a document is registered, its integrity **cannot be altered or forged**.

---

## 🚀 Features

### 👤 Admin

- Google OAuth Login
- Upload & register documents
- SHA-256 hashing
- Store hash on Ethereum blockchain
- File storage on Cloudinary
- MongoDB history tracking
- Dashboard with live stats
- Toast notifications
- Light / Dark theme toggle
- Logout system

### 🔍 Public Verification

- No login required
- Upload any document
- Instant authenticity check
- Blockchain validation
- Tamper detection

---

## 🧠 How It Works

### Registration Flow

1. Upload file
2. Generate SHA-256 hash
3. Upload file to Cloudinary
4. Save metadata in MongoDB
5. Store hash on Ethereum blockchain

### Verification Flow

1. Upload document
2. Generate hash
3. Compare with database
4. Validate with blockchain
5. Show VERIFIED / NOT REGISTERED

---

## 🏗 Architecture

```
User → React Frontend
      ↓
Spring Boot Backend
      ↓
MongoDB (metadata/history)
Cloudinary (file storage)
Ethereum Blockchain (hash storage)
```

### Why this architecture?

| Component  | Purpose                |
| ---------- | ---------------------- |
| Blockchain | Tamper-proof integrity |
| MongoDB    | Fast history & stats   |
| Cloudinary | File hosting           |
| SHA-256    | Unique fingerprint     |

---

## 🖼 Screenshots

Create a folder:

```
/screenshots
```

Add:

```
login.png
verify.png
admin.png
```

They will appear below automatically.

### Login

![Login](./screenshots/login.png)

### Verify

![Verify](./screenshots/verify.png)

### Admin

![Admin](./screenshots/admin.png)

---

## ⚙️ Tech Stack

### Frontend

- React (Vite)
- React Router
- Axios
- React Hot Toast
- Google OAuth
- Modern CSS UI

### Backend

- Spring Boot
- MongoDB
- Web3j
- JWT Authentication
- Cloudinary Storage

### Blockchain

- Ethereum (Sepolia)
- Smart Contracts
- SHA-256 hashing

---

## 🛠 Installation Guide

### Clone

```bash
git clone https://github.com/BlackMagic95/blockchain-document-verification.git
cd blockchain-document-verification
```

---

### Backend

```bash
cd backend
./gradlew bootRun
```

Add in `application.properties`:

```
spring.data.mongodb.uri=YOUR_MONGO_URI

google.client-id=YOUR_GOOGLE_CLIENT_ID

blockchain.private-key=YOUR_PRIVATE_KEY
blockchain.contract=YOUR_CONTRACT_ADDRESS
blockchain.rpc=YOUR_RPC_URL

cloudinary.cloud-name=XXX
cloudinary.api-key=XXX
cloudinary.api-secret=XXX
```

---

### Frontend

```bash
cd frontend
npm install
npm run dev
```

Open:

```
http://localhost:5173
```

---

## 📊 Dashboard Stats

- Total Registered Documents
- Total Blockchain Hashes
- Total Verifications
- Real-time updates

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
- Docker deployment
- Cloud hosting
- Analytics dashboard

---

## 🎓 Research / IEEE Scope

This project demonstrates:

- Blockchain for document integrity
- Hybrid cloud + decentralized architecture
- Secure digital verification
- Tamper-proof systems
- Real-world scalability

✅ Suitable for:

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

- Star ⭐ the repo
- Fork 🍴 it
- Improve 🚀 it

---

Built with ❤️ using Blockchain + Full Stack
