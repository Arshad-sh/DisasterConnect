import { Link, NavLink, Outlet, useLocation, useNavigate } from "react-router-dom";
import { useAuth } from "../context/AuthContext";
import "../App.css";

const roleMeta = {
  ADMIN: { label: "Administrator", initials: "AD" },
  CITIZEN: { label: "Citizen", initials: "CT" },
  NGO: { label: "NGO Coordinator", initials: "NG" },
  VOLUNTEER: { label: "Volunteer", initials: "VO" },
} as const;

function MainLayout() {
  const navigate = useNavigate();
  const location = useLocation();
  const { isAuthenticated, role, logout } = useAuth();
  const meta = role ? roleMeta[role] : null;

  const handleLogout = () => {
    logout();
    navigate("/login");
  };

  const dashboardPath = role === "ADMIN" ? "/admin" : role === "CITIZEN" ? "/citizen" : role === "NGO" ? "/ngo" : "/volunteer";
  const navItems = role === "ADMIN"
    ? [
        ["Overview", "/admin"], ["Requests", "/admin/requests"], ["Resources", "/admin/resources"],
        ["Volunteers", "/admin/volunteers"], ["NGOs", "/admin/ngos"], ["Users", "/admin/users"], ["Assignments", "/admin/assignments"], ["Audit Logs", "/admin/audit-logs"],
      ]
    : role === "NGO"
      ? [["Overview", "/ngo"], ["Help Requests", "/ngo/requests"], ["My Resources", "/ngo/resources"], ["Add Resource", "/ngo/resources/create"], ["Reservations", "/ngo/reservations"]]
      : role === "VOLUNTEER"
        ? [["Overview", "/volunteer"], ["My Assignments", "/volunteer/assignments"], ["My Profile", "/volunteer/profile"]]
        : role === "CITIZEN"
          ? [["Overview", "/citizen"], ["My Requests", "/citizen/requests"], ["Report Emergency", "/citizen/create-request"]]
          : [];

  if (!isAuthenticated) {
    return (
      <div className="public-shell">
        <header className="public-header">
          <Link to="/" className="brand brand-public"><span className="brand-mark">✚</span><span>Disaster<span>Connect</span></span></Link>
          <nav className="public-nav"><Link to="/login">Sign in</Link><Link className="public-cta" to="/register">Get started</Link></nav>
        </header>
        <main className="public-main"><Outlet /></main>
        <footer className="public-footer">© 2026 DisasterConnect · Emergency support coordination platform</footer>
      </div>
    );
  }

  const currentLabel = navItems.find(([, path]) => path === location.pathname)?.[0] || "Overview";

  return (
    <div className="app-shell">
      <aside className="sidebar">
        <Link to={dashboardPath} className="brand sidebar-brand"><span className="brand-mark">✚</span><span>Disaster<span>Connect</span></span></Link>
        <div className="sidebar-section-label">Workspace</div>
        <nav className="side-nav">
          {navItems.map(([label, path]) => (
            <NavLink key={path} to={path} end={path === dashboardPath} className={({ isActive }) => `side-link ${isActive ? "active" : ""}`}>
              <span className="side-icon">{label === "Overview" ? "⌂" : label.includes("Request") || label.includes("Emergency") ? "!" : label.includes("Resource") ? "▦" : label.includes("Volunteer") ? "♙" : label.includes("Assignment") ? "✓" : label.includes("NGO") ? "◎" : "◈"}</span>
              <span>{label}</span>
            </NavLink>
          ))}
        </nav>
        <div className="sidebar-bottom">
          <div className="response-card"><span className="status-dot" /> Response network online<p>Live coordination active</p></div>
          <div className="sidebar-profile">
            <div className="sidebar-profile-main">
              <span className="avatar">{meta?.initials}</span>
              <span className="sidebar-profile-copy"><strong>{meta?.label}</strong><small>{role}</small></span>
            </div>
            <button className="sidebar-signout" onClick={handleLogout}>↪ <span>Sign out</span></button>
          </div>
        </div>
      </aside>

      <div className="main-shell">
        <header className="topbar">
          <div><div className="eyebrow">DISASTER RESPONSE PLATFORM</div><div className="breadcrumb"><Link to={dashboardPath}>{meta?.label}</Link> <span>/</span> {currentLabel}</div></div>
          <div className="top-actions"></div>
        </header>
        <main className="page-content"><Outlet /></main>
      </div>
    </div>
  );
}

export default MainLayout;
