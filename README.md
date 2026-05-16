# Lets_Talk
Real-time chat platform supporting dynamic room creation and live messaging, developed entirely in Kotlin using Jetpack Compose and a Ktor backend.

# Project Structure
## Modules

- **app** — Android client built with Jetpack Compose, Ktor client, WebSockets
- **server** — Ktor server with MongoDB, JWT authentication, WebSocket real-time chat

## Tech Stack

| Layer | Technology |
|-------|-----------|
| Android UI | Jetpack Compose |
| Networking | Ktor Client |
| Backend | Ktor Server + Netty |
| Database | MongoDB |
| Auth | JWT + BCrypt |
| Real-time | WebSockets |

## Getting Started

See [Server README](server/README.md) and [App README](app/README.md) for setup instructions.
