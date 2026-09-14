import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getMyRequests, type RequestResponse } from "../services/requestService";

function CitizenDashboardPage() {
  const [requests, setRequests] = useState<RequestResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    getMyRequests()
      .then((data) => setRequests(Array.isArray(data) ? data : data?.content ?? []))
      .catch((error) => console.error("Failed to load citizen dashboard:", error))
      .finally(() => setLoading(false));
  }, []);

  const activeSupportStatuses = ["VERIFIED", "MATCHED", "ASSIGNED", "IN_PROGRESS"];
  const activeSupport = requests.filter((request) => activeSupportStatuses.includes(request.status)).length;

  return <section>
    <div className="dashboard-hero"><div><h2>Good to see you. Stay safe.</h2><p>Report an emergency or keep track of support requests from one place.</p></div><div className="hero-actions"><Link className="button button-danger" to="/citizen/create-request">+ Report Emergency</Link><Link className="button button-secondary" to="/citizen/requests">View my requests</Link></div></div>
    <div className="stats-grid">
      <div className="stat-card"><div className="stat-top"><span>My requests</span><span className="stat-icon">▦</span></div><div className="stat-number">{loading ? "…" : requests.length}</div><div className="stat-meta">Track every submitted request</div></div>
      <div className="stat-card"><div className="stat-top"><span>Active support</span><span className="stat-icon">✓</span></div><div className="stat-number">{loading ? "…" : activeSupport}</div><div className="stat-meta">Live response status</div></div>
      <div className="stat-card"><div className="stat-top"><span>Priority</span><span className="stat-icon">!</span></div><div className="stat-number">24/7</div><div className="stat-meta up">Emergency response network</div></div>
      <div className="stat-card"><div className="stat-top"><span>Network</span><span className="stat-icon">◎</span></div><div className="stat-number">Live</div><div className="stat-meta up">Coordination services online</div></div>
    </div>
    <div className="content-grid">
      <div className="panel"><div className="panel-header"><h3>What do you need?</h3><span>Quick actions</span></div><div className="panel-body"><div className="quick-grid"><Link className="quick-card" to="/citizen/create-request"><span className="quick-icon">!</span><strong>Report an emergency</strong><span>Tell responders what happened and where help is needed.</span></Link><Link className="quick-card" to="/citizen/requests"><span className="quick-icon">✓</span><strong>Track my requests</strong><span>Check request status and response progress.</span></Link></div></div></div>
      <div className="panel"><div className="panel-header"><h3>Response network</h3><span>Live</span></div><div className="panel-body"><div className="alert-list"><div className="alert-item"><span className="alert-mark" style={{background:"#1e9b69"}}/><div><strong>Coordination services online</strong><span>NGOs and volunteers can receive requests.</span></div></div><div className="alert-item"><span className="alert-mark"/><div><strong>Need urgent help?</strong><span>Use Report Emergency for high-priority situations.</span></div></div></div></div></div>
    </div>
  </section>;
}
export default CitizenDashboardPage;
