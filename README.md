\# DisasterConnect



> Community Emergency Response \& Resource Sharing Platform



DisasterConnect is a full-stack emergency support platform designed to connect \*\*Citizens, NGOs, Volunteers, and Administrators\*\* for coordinated disaster response and community resource sharing.



The platform supports the complete emergency-support lifecycle:



\*\*Request → Match → Support → Track\*\*



\---



\## 🚨 Problem Statement



During emergencies, people often struggle to find the right resources, volunteers, and organizations quickly.



DisasterConnect provides a centralized platform where:



\- Citizens can request emergency assistance.

\- NGOs can manage requests and share available resources.

\- Volunteers can view and manage assignments.

\- Administrators can monitor and coordinate the overall system.

\- The platform maintains request history, assignments, reservations, resources, and audit information.



\---



\## ✨ Key Features



\### 👤 Citizen



\- User registration and authentication

\- Create emergency help requests

\- View personal requests

\- Track request status

\- View request details and history

\- Create resource reservations where applicable



\### 🏢 NGO



\- NGO profile management

\- View and manage relevant requests

\- Manage emergency resources

\- Manage reservations

\- Support coordinated emergency response



\### 🦺 Volunteer



\- Volunteer profile management

\- View available assignments

\- View assignment details

\- Track assignment status

\- Participate in emergency response activities



\### 🛡️ Administrator



\- Administrative dashboard

\- User management

\- Request management

\- NGO management

\- Volunteer management

\- Resource management

\- Assignment management

\- Reservation management

\- Audit log monitoring



\---



\## ⚙️ Technical Highlights



\- Role-Based Access Control (RBAC)

\- JWT-based authentication

\- Spring Security

\- Request validation and business-rule handling

\- Request history tracking

\- Volunteer assignment management

\- Resource and reservation management

\- Emergency request matching

\- Audit logging

\- WebSocket-based real-time communication

\- RESTful APIs

\- Exception handling with centralized error responses

\- Transactional business operations

\- Automated backend tests



\---



\## 🏗️ Architecture



```text

&#x20;                   ┌──────────────────────┐

&#x20;                   │      React UI        │

&#x20;                   │   TypeScript / Vite  │

&#x20;                   └──────────┬───────────┘

&#x20;                              │

&#x20;                              │ REST API

&#x20;                              ▼

&#x20;                   ┌──────────────────────┐

&#x20;                   │    Spring Boot       │

&#x20;                   │      Backend         │

&#x20;                   ├──────────────────────┤

&#x20;                   │ Controllers          │

&#x20;                   │ Services             │

&#x20;                   │ DTOs                 │

&#x20;                   │ Security / JWT       │

&#x20;                   │ Exception Handling   │

&#x20;                   └──────────┬───────────┘

&#x20;                              │

&#x20;                              ▼

&#x20;                   ┌──────────────────────┐

&#x20;                   │     PostgreSQL       │

&#x20;                   │      Database        │

&#x20;                   └──────────────────────┘



&#x20;                   WebSocket

&#x20;                      │

&#x20;                      ▼

&#x20;             Real-Time Communication

