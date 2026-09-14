import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getMyAssignments, type AssignmentResponse } from "../services/assignmentService";
import { getMyVolunteerProfile, type VolunteerProfileResponse } from "../services/volunteerService";

function VolunteerDashboardPage() {
  const [assignments, setAssignments] = useState<AssignmentResponse[]>([]);
  const [profile, setProfile] = useState<VolunteerProfileResponse | null>(null);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([getMyAssignments(), getMyVolunteerProfile()])
      .then(([assignmentData, profileData]) => {
        setAssignments(assignmentData);
        setProfile(profileData);
      })
      .catch((error) => console.error("Failed to load volunteer dashboard:", error))
      .finally(() => setLoading(false));
  }, []);

  return <section>
    <div className="dashboard-hero"><div><h2>Ready to respond?</h2><p>See your assignments, availability and delivery progress.</p></div><div className="hero-actions"><Link className="button button-danger" to="/volunteer/assignments">View assignments</Link><Link className="button button-secondary" to="/volunteer/profile">My profile</Link></div></div>
    <div className="stats-grid">
      <div className="stat-card"><div className="stat-top"><span>Assignments</span><span className="stat-icon">✓</span></div><div className="stat-number">{loading ? "…" : assignments.length}</div><div className="stat-meta">Current response tasks</div></div>
      <div className="stat-card"><div className="stat-top"><span>Availability</span><span className="stat-icon">◉</span></div><div className="stat-number">{loading ? "…" : profile ? (profile.availability ? "Available" : "Unavailable") : "—"}</div><div className="stat-meta">Manage from your profile</div></div>
      <div className="stat-card"><div className="stat-top"><span>Priority</span><span className="stat-icon">!</span></div><div className="stat-number">Live</div><div className="stat-meta up">Assignment updates enabled</div></div>
      <div className="stat-card"><div className="stat-top"><span>Response network</span><span className="stat-icon">◎</span></div><div className="stat-number">Online</div><div className="stat-meta up">Coordination services active</div></div>
    </div>
    <div className="content-grid"><div className="panel"><div className="panel-header"><h3>Volunteer workspace</h3><span>Quick access</span></div><div className="panel-body"><div className="quick-grid"><Link className="quick-card" to="/volunteer/assignments"><span className="quick-icon">✓</span><strong>My assignments</strong><span>Open active tasks and update delivery progress.</span></Link><Link className="quick-card" to="/volunteer/profile"><span className="quick-icon">♙</span><strong>My profile</strong><span>Keep your contact details and availability current.</span></Link></div></div></div><div className="panel"><div className="panel-header"><h3>Before you respond</h3><span>Safety first</span></div><div className="panel-body"><div className="alert-list"><div className="alert-item"><span className="alert-mark" style={{background:"#e79b27"}}/><div><strong>Check assignment details</strong><span>Review the request, location and delivery workflow.</span></div></div><div className="alert-item"><span className="alert-mark" style={{background:"#1e9b69"}}/><div><strong>Keep availability updated</strong><span>This helps the system match responders efficiently.</span></div></div></div></div></div></div>
  </section>;
}
export default VolunteerDashboardPage;
