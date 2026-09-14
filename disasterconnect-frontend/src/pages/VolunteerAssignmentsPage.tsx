import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getMyAssignments, type AssignmentResponse } from "../services/assignmentService";

function VolunteerAssignmentsPage() {
  const [assignments, setAssignments] = useState<AssignmentResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const load = () => {
    setLoading(true);
    getMyAssignments()
      .then(setAssignments)
      .catch(() => setError("Failed to load your assignments. Please try again."))
      .finally(() => setLoading(false));
  };

  useEffect(load, []);

  return <section>
    <div className="dashboard-hero">
      <div><div className="section-kicker">VOLUNTEER RESPONSE</div><h2>My assignments</h2><p>View tasks assigned to you and keep delivery progress synchronized.</p></div>
    </div>
    {error && <div className="inline-alert">{error}</div>}
    <div className="panel">
      <div className="panel-header"><h3>Assigned response work</h3><span>{loading ? "Loading…" : `${assignments.length} assignment${assignments.length === 1 ? "" : "s"}`}</span></div>
      <div className="panel-body">
        {loading ? <div className="loading-state"><span className="spinner" /> Loading assignments…</div> :
          assignments.length === 0 ? <div className="empty-state"><div className="empty-icon">✓</div><strong>No assignments yet</strong><span>New response tasks assigned to you will appear here.</span></div> :
          <div className="data-list">{assignments.map((assignment) => <article className="data-card" key={assignment.id}>
            <div className="data-card-head"><div><span className="muted-label">ASSIGNMENT</span><h3>#{assignment.id}</h3></div><span className={`status-badge status-${assignment.status.toLowerCase()}`}>{assignment.status}</span></div>
            <div className="data-grid">
              <div><span className="muted-label">Request</span><strong>#{assignment.requestId}</strong></div>
              <div><span className="muted-label">Assigned</span><strong>{new Date(assignment.assignedAt).toLocaleString()}</strong></div>
              <div><span className="muted-label">Notes</span><strong>{assignment.notes || "No notes"}</strong></div>
            </div>
            <Link className="button button-secondary" to={`/volunteer/assignments/${assignment.id}`}>Open assignment</Link>
          </article>)}</div>}
      </div>
    </div>
  </section>;
}
export default VolunteerAssignmentsPage;
