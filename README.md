# java-networking-suite
Java networking suite showcasing TCP chat, UDP messaging, and UDP multicast broadcasting with multi-client console demos (IntelliJ-ready).


# 🌐 Java Networking Suite — TCP • UDP • Multicast

A single repository showcasing **Java socket programming** with three mini-projects:
1) **TCP Chat** (multi-client)
2) **UDP Messaging** (client/server broadcast-style)
3) **UDP Multicast Broadcast** (one sender → many receivers)

This repo includes runnable examples (IntelliJ-ready) and console screenshots.

---

## ✅ What’s Inside

### 1) TCP Chat (Client/Server)
- Multi-client TCP chat
- Public messages (broadcast)
- Private messages: `/msg <user> <message>`
- List connected users: `/liste`
- Clean disconnect (optional `/quit`)

### 2) UDP Messaging (Client/Server)
- UDP server receives datagrams and broadcasts messages to connected clients
- Multiple UDP clients can send and receive
- Demonstrates connectionless messaging + address/port tracking

### 3) UDP Multicast Broadcast
- `AgentServer` sends UDP datagrams to a multicast group
- Multiple `AgentClient` instances join the group and receive the same message
- Demonstrates group communication (one-to-many)

---

## 📁 Project Layout

├── tcp/ # TCP chat (server + client)
├── udp/ # UDP messaging (server + client)
├── multicast/ # UDP multicast broadcast (server + client)
└── screenshots/ # Console screenshots used in this README


---

## ⚙️ Requirements
- **Java JDK 17+** (tested with JDK 21)
- IntelliJ IDEA (recommended) or any Java IDE

---

## ▶️ How to Run (IntelliJ)

### General Tip: Allow multiple instances
To run multiple clients at the same time:
- **Run → Edit Configurations…**
- Select your client configuration
- **Modify options → Allow multiple instances**
- Apply → OK

---

## 1) Run TCP Chat

### Step 1 — Start the server
Run:
- `tcp/Serveur.java`

### Step 2 — Start clients (2 or more)
Run multiple times:
- `tcp/Client.java`

Commands:
- Public message: type any text
- Private message: `/msg <username> <message>`
- List users: `/liste`
- Quit: `/quit`

---

## 2) Run UDP Messaging

### Step 1 — Start the server
Run:
- `udp/Serveur_UDP.java`

### Step 2 — Start clients (2 or more)
Run multiple times:
- `udp/Client_UDP.java`

Quit:
- `exit` (or `/quit` depending on your code)

---

## 3) Run UDP Multicast Broadcast

### Step 1 — Start receivers first
Run multiple times:
- `multicast/AgentClient.java`

### Step 2 — Start the broadcaster
Run:
- `multicast/AgentServer.java`

Quit (server):
- `exit`

> Note: On some networks/routers multicast may be filtered. If testing between 2 PCs doesn’t work, test on the same machine first.

---

## 🖼️ Screenshots

### TCP — Server & Clients
![TCP Server](screenshots/tcp-server.png)
![TCP Client 01](screenshots/tcp-client-01.png)
![TCP Client 02](screenshots/tcp-client-02.png)

### UDP — Server & Clients
![UDP Server](screenshots/udp-server.png)
![UDP Client 01](screenshots/udp-client-01.png)
![UDP Client 02](screenshots/udp-client-02.png)

### Multicast — Server & Clients
![Multicast Server](screenshots/multicast-server.png)
![Multicast Client 01](screenshots/multicast-client-01.png)
![Multicast Client 02](screenshots/multicast-client-02.png)
![Multicast Client 03](screenshots/multicast-client-03.png)

---

## 🧹 Recommended `.gitignore`
Add a `.gitignore` at the repo root to avoid committing IDE/build artifacts:
- `.idea/`, `*.iml`
- `out/`, `target/`, `build/`
- `*.class`

---

## 📌 Notes
- TCP is **connection-oriented** (reliable, ordered).
- UDP is **connectionless** (fast, no delivery guarantee).
- Multicast is **one-to-many** via a group address (e.g., `230.0.0.1`).

---

## 👤 Author
Mohamed Abdelkader Ketata  
