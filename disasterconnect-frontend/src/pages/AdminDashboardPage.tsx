import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import api from "../services/api";

function AdminDashboardPage() {
  const [counts, setCounts] = useState({ users: 0, requests: 0, resources: 0 });
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      api.get("/api/admin/users"),
      api.get("/api/requests"),
      api.get("/api/admin/resources"),
    ]).then(([users, requests, resources]) => {
      const requestData = requests.data;
      setCounts({
        users: Array.isArray(users.data) ? users.data.length : 0,
        requests: Array.isArray(requestData) ? requestData.length : requestData?.totalElements ?? requestData?.content?.length ?? 0,
        resources: Array.isArray(resources.data) ? resources.data.length : 0,
      });
    }).catch((error) => console.error("Failed to load admin dashboard:", error)).finally(() => setLoading(false));
  }, []);

  const items = [
    ["Requests","/admin/requests","Monitor emergency needs and response priority.","!"],
    ["Resources","/admin/resources","Review community resources and availability.","▦"],
    ["Volunteers","/admin/volunteers","Monitor responders and participation.","♙"],
    ["NGOs","/admin/ngos","Manage registered response organizations.","◎"],
    ["Users","/admin/users","View registered platform accounts and roles.","♟"],
    ["Assignments","/admin/assignments","Track delivery and volunteer workflows.","✓"],
    ["Audit logs","/admin/audit-logs","Review important platform activity.","◈"],
  ];

  return <section>
    <div className="dashboard-hero"><div><div className="section-kicker">ADMIN COMMAND CENTER</div><h2>Command center</h2><p>Monitor DisasterConnect operations and keep the response network moving.</p></div><div className="hero-actions"><Link className="button button-danger" to="/admin/requests">Review requests</Link></div></div>
    <div className="stats-grid">
      <div className="stat-card"><div className="stat-top"><span>Users</span><span className="stat-icon">♙</span></div><div className="stat-number">{loading ? "…" : counts.users}</div><div className="stat-meta">Registered platform users</div></div>
      <div className="stat-card"><div className="stat-top"><span>Requests</span><span className="stat-icon">!</span></div><div className="stat-number">{loading ? "…" : counts.requests}</div><div className="stat-meta">Emergency support requests</div></div>
      <div className="stat-card"><div className="stat-top"><span>Resources</span><span className="stat-icon">▦</span></div><div className="stat-number">{loading ? "…" : counts.resources}</div><div className="stat-meta">Resources in network</div></div>
      <div className="stat-card"><div className="stat-top"><span>System</span><span className="stat-icon">✓</span></div><div className="stat-number">Online</div><div className="stat-meta up">Core services operational</div></div>
    </div>
    <div className="content-grid">
      <div className="panel"><div className="panel-header"><h3>Platform management</h3><span>Administration</span></div><div className="panel-body"><div className="quick-grid">{items.map(([name,path,desc,icon])=><Link className="quick-card" to={path} key={path}><span className="quick-icon">{icon}</span><strong>{name}</strong><span>{desc}</span></Link>)}</div></div></div>
      <div className="panel"><div className="panel-header"><h3>System health</h3><span>Live</span></div><div className="panel-body"><div className="alert-list"><div className="alert-item"><span className="alert-mark" style={{background:"#1e9b69"}}/><div><strong>Response network online</strong><span>Authentication and coordination services are available.</span></div></div><div className="alert-item"><span className="alert-mark" style={{background:"#1e9b69"}}/><div><strong>Audit trail enabled</strong><span>Important platform actions can be reviewed.</span></div></div></div></div></div>
    </div>
  </section>;
}
export default AdminDashboardPage;
