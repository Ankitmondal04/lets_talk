# Lets_Talk

Lets_Talk is a real-time chat platform enabling dynamic room creation and instant messaging, built entirely in Kotlin with Jetpack Compose for Android and a Ktor backend.

## Features

- 🗨️ **Real-time Chat:** Live messaging powered by WebSockets.
- 🏗️ **Dynamic Room Creation:** Instantly create and join chat rooms.
- 🛡️ **Secure Authentication:** JWT token-based authentication with password hashing (BCrypt).
- 📱 **Modern Android UI:** Built from the ground up with Jetpack Compose.
- 🗄️ **Scalable Backend:** Ktor server with MongoDB for persistent chat history.

## Project Structure

### Modules

- **app/** — Android client (Jetpack Compose, Ktor client, WebSockets)
- **server/** — Backend server (Ktor Server, MongoDB, JWT auth, WebSockets)

### Tech Stack

| Layer        | Technology              |
|--------------|------------------------|
| Android UI   | Jetpack Compose        |
| Networking   | Ktor Client            |
| Backend      | Ktor Server + Netty    |
| Database     | MongoDB                |
| Auth         | JWT + BCrypt           |
| Real-time    | WebSockets             |

## Getting Started

For instructions to run and contribute to Lets_Talk:

- See the detailed [Server Guide](server/README.md)
- See the [App Setup](app/README.md)

---

**License:** MIT (or your license if different – specify here).
