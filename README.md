# Java Networking Suite

A Java networking project demonstrating **socket programming and network communication** through three practical client-server applications: **TCP Chat, UDP Messaging, and UDP Multicast**.

The project provides hands-on examples of connection-oriented and connectionless communication, multi-client management, datagram exchange, and one-to-many network broadcasting.

## Project Overview

The project is divided into three independent networking applications:

```text id="7j7d1r"
                    Java Networking Suite
                           │
          ┌────────────────┼────────────────┐
          │                │                │
          ▼                ▼                ▼
      TCP Chat        UDP Messaging    UDP Multicast
          │                │                │
     Client/Server     Client/Server    One-to-Many
          │                │                │
    Multi-client       Datagrams       Multicast Group
```

## 1. TCP Chat

A multi-client chat application based on **TCP sockets**.

### Features

* Client-server communication
* Multiple simultaneous clients
* Public message broadcasting
* Private messaging between users
* Display of connected users
* Graceful client disconnection

### Available Commands

```text id="bq5qwi"
/msg <username> <message>   → Send a private message
/liste                      → Display connected users
/quit                       → Disconnect from the server
```

Any other text is treated as a public message.

### Communication Model

```text id="0t7ydx"
Client 1 ─────┐
Client 2 ─────┼────► TCP Server
Client 3 ─────┘          │
                         │
                    Message Handling
                         │
                 ┌───────┼───────┐
                 ▼       ▼       ▼
              Client 1 Client 2 Client 3
```

## 2. UDP Messaging

A client-server messaging application using **UDP datagrams**.

The server receives UDP packets from clients and forwards messages to connected clients, demonstrating connectionless communication and IP address/port management.

### Features

* UDP datagram communication
* Multiple clients
* Message broadcasting
* IP address and port tracking
* Connectionless communication

### Communication Model

```text id="6uy2e0"
Client 1 ─────┐
Client 2 ─────┼────► UDP Server
Client 3 ─────┘          │
                         ▼
                    Broadcast
                         │
                 ┌───────┼───────┐
                 ▼       ▼       ▼
              Client 1 Client 2 Client 3
```

## 3. UDP Multicast

A multicast communication application implementing **one-to-many communication**.

A server sends UDP datagrams to a multicast group, while multiple clients join the group and receive the same messages.

### Communication Model

```text id="q3uwc4"
                 Multicast Server
                       │
                       ▼
                 Multicast Group
                   230.0.0.1
                 ┌─────┼─────┐
                 ▼     ▼     ▼
              Client  Client  Client
                 1       2       3
```

This component demonstrates the use of multicast groups for distributing the same data to multiple receivers.

## Project Structure

```text id="j6p8bx"
java-networking-suite/
│
├── tcp/
│   ├── Serveur.java
│   └── Client.java
│
├── udp/
│   ├── Serveur_UDP.java
│   └── Client_UDP.java
│
├── multicast/
│   ├── AgentServer.java
│   └── AgentClient.java
│
├── screenshots/
│   └── ...
│
└── README.md
```

## Technologies

* **Java**
* **Java Socket Programming**
* **TCP/IP**
* **UDP**
* **UDP Multicast**
* **Client-Server Architecture**
* **Network Communication**
* **Datagrams**
* **IP Addressing & Ports**
* **Multithreading**

## Requirements

* **Java JDK 17+**
* IntelliJ IDEA or another Java IDE

The project was designed to be easily executed and tested using IntelliJ IDEA.

## How to Run

### TCP Chat

#### 1. Start the server

Run:

```text id="d1ef3q"
tcp/Serveur.java
```

#### 2. Start multiple clients

Run multiple instances of:

```text id="m2j4ah"
tcp/Client.java
```

Available commands:

```text id="l9j5dg"
/msg <username> <message>
/liste
/quit
```

### UDP Messaging

#### 1. Start the server

Run:

```text id="k1yq5j"
udp/Serveur_UDP.java
```

#### 2. Start multiple clients

Run multiple instances of:

```text id="nqkvk3"
udp/Client_UDP.java
```

To disconnect:

```text id="w9n0pj"
exit
```

or:

```text id="3v4o6p"
/quit
```

depending on the implementation.

### UDP Multicast

#### 1. Start the receivers

Run one or more instances of:

```text id="j5z6kq"
multicast/AgentClient.java
```

#### 2. Start the broadcaster

Run:

```text id="f8r2as"
multicast/AgentServer.java
```

The server sends messages to the multicast group and all subscribed clients receive them.

> Multicast communication can be restricted by some networks or routers. For testing, running the server and clients on the same machine is recommended.

## TCP vs UDP

| Feature     | TCP                    | UDP                               |
| ----------- | ---------------------- | --------------------------------- |
| Connection  | Connection-oriented    | Connectionless                    |
| Reliability | Reliable               | No delivery guarantee             |
| Ordering    | Ordered                | No ordering guarantee             |
| Overhead    | Higher                 | Lower                             |
| Typical use | Reliable communication | Fast datagram-based communication |

### Multicast

UDP Multicast enables **one-to-many communication**, where a sender transmits data to a multicast group and multiple receivers subscribed to that group can receive the same message.

## Learning Objectives

This project was developed to gain practical experience with:

* TCP/IP communication
* UDP datagrams
* Java socket programming
* Client-server architecture
* Multi-client communication
* IP addresses and port numbers
* Network message broadcasting
* Multicast groups
* Concurrent client handling
* Differences between TCP and UDP

## Author

**Farah Chaari**

Software Engineer | Artificial Intelligence Machine Learning & LLM | Full-Stack Development | Business Intelligence

📍 Tunisia

🔗 [LinkedIn](www.linkedin.com/in/farah-chaari-1b4aa1282)

🔗 [GitHub](https://github.com/farahchaari01)


---

## 📄 License

This project was created for academic and educational purposes.
