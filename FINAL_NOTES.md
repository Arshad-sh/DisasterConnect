# DisasterConnect Final

This package contains the updated frontend and backend.

## Included fixes
- Role-protected frontend routes for Admin / NGO / Volunteer / Citizen.
- Backend RBAC tightened for the affected API areas.
- NGO dashboard live counts.
- NGO reservation history.
- Volunteer assignments loaded from the backend and status updates persisted.
- Volunteer availability loaded/saved through the backend.
- Admin dashboard live counts.
- Admin Users navigation restored.
- Admin request urgency includes CRITICAL.
- Admin breadcrumb links back to the command center.
- Audit log heading now reflects the number of returned events.
- Polished loading, empty, status, workflow, availability and interaction UI.

## Run
Frontend:
```powershell
cd disasterconnect-frontend
npm install
npm run dev
```

Backend:
```powershell
cd disasterconnect-backend
.\mvnw.cmd spring-boot:run
```

The frontend `.env` points to `http://localhost:8080`.
