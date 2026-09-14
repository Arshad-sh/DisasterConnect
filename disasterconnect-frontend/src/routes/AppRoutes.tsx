import { BrowserRouter, Routes, Route, Link, Navigate, Outlet } from "react-router-dom";
import MainLayout from "../layouts/MainLayout";
import LoginPage from "../pages/LoginPage";
import RegisterPage from "../pages/RegisterPage";
import WebSocketTestPage from "../pages/WebSocketTestPage";
import CitizenDashboardPage from "../pages/CitizenDashboardPage";
import CreateHelpRequestPage from "../pages/CreateHelpRequestPage";
import MyRequestsPage from "../pages/MyRequestsPage";
import RequestDetailsPage from "../pages/RequestDetailsPage";
import NGODashboardPage from "../pages/NGODashboardPage";
import NGORequestsPage from "../pages/NGORequestsPage";
import NGOResourcesPage from "../pages/NGOResourcesPage";
import CreateResourcePage from "../pages/CreateResourcePage";
import EditResourcePage from "../pages/EditResourcePage";
import CreateReservationPage from "../pages/CreateReservationPage";
import NGOReservationsPage from "../pages/NGOReservationsPage";
import VolunteerDashboardPage from "../pages/VolunteerDashboardPage";
import VolunteerProfilePage from "../pages/VolunteerProfilePage";
import VolunteerAssignmentsPage from "../pages/VolunteerAssignmentsPage";
import VolunteerAssignmentDetailsPage from "../pages/VolunteerAssignmentDetailsPage";
import AdminDashboardPage from "../pages/AdminDashboardPage";
import AdminUsersPage from "../pages/AdminUsersPage";
import AdminRequestsPage from "../pages/AdminRequestsPage";
import AdminNGOsPage from "../pages/AdminNGOsPage";
import AdminVolunteersPage from "../pages/AdminVolunteersPage";
import AdminResourcesPage from "../pages/AdminResourcesPage";
import AdminReservationsPage from "../pages/AdminReservationsPage";
import AdminAssignmentsPage from "../pages/AdminAssignmentsPage";
import AdminAuditLogsPage from "../pages/AdminAuditLogsPage";
import ProtectedRoute from "./ProtectedRoute";
import { useAuth } from "../context/AuthContext";
import { AuthProvider } from "../context/AuthContext";

function HomePage() {
  return <section className="landing-page">
    <div className="landing-copy"><div className="landing-kicker"><span className="status-dot" /> LIVE RESPONSE NETWORK</div><h2>When every second matters, <em>connect faster.</em></h2><p>DisasterConnect brings citizens, NGOs, volunteers and resources together in one coordinated emergency response platform.</p><div className="hero-actions"><Link className="button button-danger" to="/register">Join the response network</Link><Link className="button button-secondary" to="/login">Sign in</Link></div></div>
    <div className="landing-visual"><div className="radar"><div className="radar-ring ring-one"/><div className="radar-ring ring-two"/><div className="radar-ring ring-three"/><span className="radar-core">✚</span><span className="radar-point point-a"/><span className="radar-point point-b"/><span className="radar-point point-c"/></div><div className="floating-card card-top"><strong>24/7</strong><span>Response coordination</span></div><div className="floating-card card-bottom"><span className="status-dot"/><div><strong>Network online</strong><small>Responders can coordinate now</small></div></div></div>
    <div className="landing-features"><div><span>01</span><strong>Report</strong><p>Capture emergency needs quickly.</p></div><div><span>02</span><strong>Match</strong><p>Connect requests with suitable resources.</p></div><div><span>03</span><strong>Respond</strong><p>Coordinate volunteers and deliveries.</p></div></div>
  </section>;
}

function RoleRoute({ allowed }: { allowed: string[] }) {
  const { role } = useAuth();
  if (!role || !allowed.includes(role)) {
    const target = role === "ADMIN" ? "/admin" : role === "NGO" ? "/ngo" : role === "VOLUNTEER" ? "/volunteer" : "/citizen";
    return <Navigate to={target} replace />;
  }
  return <Outlet />;
}

function AppRoutes() {
  return <AuthProvider><BrowserRouter><Routes><Route path="/" element={<MainLayout />}><Route index element={<HomePage />} /><Route path="login" element={<LoginPage />} /><Route path="register" element={<RegisterPage />} /><Route element={<ProtectedRoute />}>
    <Route path="request/:id" element={<RequestDetailsPage />} />
    <Route path="websocket-test" element={<WebSocketTestPage />} />
    <Route element={<RoleRoute allowed={["CITIZEN"]} />}><Route path="citizen" element={<CitizenDashboardPage />} /><Route path="citizen/create-request" element={<CreateHelpRequestPage />} /><Route path="citizen/requests" element={<MyRequestsPage />} /><Route path="citizen/requests/:id" element={<RequestDetailsPage />} /></Route>
    <Route element={<RoleRoute allowed={["NGO"]} />}><Route path="ngo" element={<NGODashboardPage />} /><Route path="ngo/requests" element={<NGORequestsPage />} /><Route path="ngo/resources" element={<NGOResourcesPage />} /><Route path="ngo/resources/create" element={<CreateResourcePage />} /><Route path="ngo/resources/edit/:id" element={<EditResourcePage />} /><Route path="ngo/reservations" element={<NGOReservationsPage />} /><Route path="ngo/reservations/create" element={<CreateReservationPage />} /></Route>
    <Route element={<RoleRoute allowed={["VOLUNTEER"]} />}><Route path="volunteer" element={<VolunteerDashboardPage />} /><Route path="volunteer/profile" element={<VolunteerProfilePage />} /><Route path="volunteer/assignments" element={<VolunteerAssignmentsPage />} /><Route path="volunteer/assignments/:id" element={<VolunteerAssignmentDetailsPage />} /></Route>
    <Route element={<RoleRoute allowed={["ADMIN"]} />}><Route path="admin" element={<AdminDashboardPage />} /><Route path="admin/users" element={<AdminUsersPage />} /><Route path="admin/requests" element={<AdminRequestsPage />} /><Route path="admin/ngos" element={<AdminNGOsPage />} /><Route path="admin/volunteers" element={<AdminVolunteersPage />} /><Route path="admin/resources" element={<AdminResourcesPage />} /><Route path="admin/reservations" element={<AdminReservationsPage />} /><Route path="admin/assignments" element={<AdminAssignmentsPage />} /><Route path="admin/audit-logs" element={<AdminAuditLogsPage />} /></Route>
  </Route></Route></Routes></BrowserRouter></AuthProvider>;
}
export default AppRoutes;
