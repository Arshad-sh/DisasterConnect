DisasterConnect
Community Emergency Response & Resource Sharing Platform

1. Project Overview
DisasterConnect is a full-stack emergency-response coordination platform that connects Citizens, NGOs, Volunteers, and Administrators in one system.
Citizens can create and track emergency help requests. NGOs can review requests, manage resources, and reserve available resources against requests. Volunteers can manage their availability and complete response assignments. Administrators can oversee users, profiles, requests, resources, reservations, assignments, and audit logs.
The project uses a React and TypeScript frontend with a Spring Boot backend, PostgreSQL persistence, JWT-based authentication, role-based access control, and a STOMP WebSocket test channel.

2. Problem Statement
During emergencies, coordination between people requesting help, organizations with resources, volunteers, and administrators can become fragmented. Requests may be hard to track, resources can be allocated without clear status, and response operations may lack visibility.
DisasterConnect centralizes these workflows so emergency requests, resources, reservations, volunteer assignments, and administrative monitoring can be managed in a single platform.

3. Solution
DisasterConnect provides role-specific workflows for emergency-response coordination:
- Citizens submit and manage emergency help requests.
- NGOs browse requests, maintain available resources, and reserve resources for requests.
- Volunteers manage their profile and availability, then act on assigned response work.
- Administrators monitor platform records, verify NGO and volunteer profiles, create assignments, and review audit logs.
The backend enforces authentication and role-based authorization through Spring Security and JWT bearer tokens.

4. Key Features
Citizen
- Register and log in.
- Create emergency help requests.
- View personal help requests.
- View request details and status history.
- Update or delete owned requests.
- Progress request status through validated transitions.

NGO
- Create and view an NGO profile.
- Browse requests with search, status filtering, urgency filtering, pagination, and sorting.
- Update request status.
- Create, view, edit, and delete NGO-owned resources.
- Create resource reservations for requests.
- View and update reservation status.

Volunteer
- Create and view a volunteer profile.
- Update availability.
- View assigned response work.
- View assignment details.
- Update assignment status through validated transitions.

Administrator
- View users, requests, NGOs, volunteers, resources, reservations, assignments, and audit logs.
- Verify NGO profiles.
- Verify volunteer profiles.
- Create volunteer assignments.

Platform / Technical Features
- JWT-based authentication.
- Spring Security authorization and RBAC.
- BCrypt password hashing.
- Request, reservation, and assignment status-transition validation.
- Request history tracking.
- Transactional resource reservation flow.
- Reservation audit logging.
- PostgreSQL persistence through JPA/Hibernate.
- STOMP WebSocket test messaging.
- React protected routes and role-aware client routing.

5. End-to-End Workflow

Citizen
  ↓
Create Emergency Help Request
  ↓
Request is stored with CREATED status
  ↓
NGO reviews available requests
  ↓
NGO manages resources and creates a reservation
  ↓
Administrator can create a volunteer assignment
  ↓
Volunteer accepts and progresses the assignment
  ↓
Completed assignment marks the related request as COMPLETED
  ↓
Administrators monitor records and reservation audit logs

6. Security
- Spring Security is configured with stateless session management.
- JWT tokens are issued after login and sent through Authorization: Bearer <token>.
- A custom JWT authentication filter validates tokens and populates the Spring Security context.
- User passwords are hashed using BCryptPasswordEncoder.
- Backend authorization uses role-based request matchers for ADMIN, CITIZEN, NGO, and VOLUNTEER.
- Public endpoints include /, /health, and /api/auth/**.
- CORS allows:
  - http://localhost:5173
  - https://disasterconnect-sooty.vercel.app
- CSRF is disabled for the stateless API.

7. Setup & Installation

Prerequisites
- Java 21
- Node.js and npm
- PostgreSQL
- Git

Clone repository:
git clone https://github.com/Arshad-sh/DisasterConnect.git
cd DisasterConnect

Backend setup:
cd disasterconnect-backend

Create a PostgreSQL database, then configure the required environment variables.

Database setup:

The backend reads PostgreSQL connection details from environment variables:

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/disasterconnect
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=

Hibernate schema generation is configured as:

spring.jpa.hibernate.ddl-auto=update

Environment variables

SPRING_DATASOURCE_URL=jdbc:postgresql://localhost:5432/disasterconnect
SPRING_DATASOURCE_USERNAME=
SPRING_DATASOURCE_PASSWORD=
JWT_SECRET=replace_with_a_secure_secret_of_sufficient_length
PORT=8080

Backend run
Windows:

.\mvnw.cmd spring-boot:run

The backend defaults to port 8080 when PORT is not provided.

Frontend setup:

cd ../disasterconnect-frontend
npm install

Create a local .env file:

VITE_API_BASE_URL=http://localhost:8080

Frontend run:

npm run dev

Additional frontend commands:
npm run build
npm run lint
npm run preview

12. Environment Variables
Backend
Variable	                             Purpose
SPRING_DATASOURCE_URL	           PostgreSQL JDBC connection URL.
SPRING_DATASOURCE_USERNAME	     PostgreSQL username.
SPRING_DATASOURCE_PASSWORD	     PostgreSQL password.
JWT_SECRET	                     JWT signing secret.
PORT	                           Backend server port; defaults to 8080.


Frontend
Variable	                            Purpose
VITE_API_BASE_URL              	Base URL for backend REST API requests.


8. Testing

Backend tests are implemented with Spring Boot and JUnit-based Spring test dependencies.

Confirmed test classes include:

- DisasterconnectBackendApplicationTests
- MatchingServiceTest
- ReservationServiceTest
- ReservationServiceConcurrencyTest
- ReservationServiceRollbackTest

Run backend tests with:

cd disasterconnect-backend
.\mvnw.cmd test

9. Deployment:
   
Frontend
https://disasterconnect-sooty.vercel.app/

The frontend includes a Vercel SPA rewrite configuration that routes requests to index.html.

Backend
https://disasterconnect-uucz.onrender.com/

Health Check
https://disasterconnect-uucz.onrender.com/health

The health endpoint returns OK.

10. Live Demo
🚀 https://disasterconnect-sooty.vercel.app/

11. Technical Highlights
- REST API architecture with Spring MVC controllers.
- DTO-based request and response handling for core workflows.
- JWT bearer-token authentication.
- Spring Security RBAC for four platform roles.
- BCrypt password hashing.
- JPA/Hibernate persistence with PostgreSQL.
- Validation through Jakarta Bean Validation.
- Transactional resource reservation operations.
- Resource quantity validation and locking during reservations.
- Request, reservation, and assignment status-transition rules.
- Request history tracking.
- Reservation audit logging.
- Spring WebSocket and STOMP test messaging.
- React protected routes and role-aware routing.
- React, TypeScript, Vite, Axios, and React Router.

12. Future Improvements
Future work ideas:
- Configure WebSocket URLs and allowed origins for production deployment.
- Connect the existing matching/scoring service to an operational matching API workflow.
- Add API request/response examples and OpenAPI documentation.
- Add frontend and end-to-end automated tests.
- Add CI validation for builds, linting, and tests.
- Add screenshots and a user-facing walkthrough.
- Externalize or remove development seed data before broader deployment.
- Add pagination or filtering support to administrative list views where needed.

13. Author
Arshad Shaikh
GitHub: https://github.com/Arshad-sh/DisasterConnect
