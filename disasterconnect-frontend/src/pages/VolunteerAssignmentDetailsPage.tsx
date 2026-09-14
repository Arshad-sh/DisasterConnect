import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import { getMyAssignmentById, updateAssignmentStatus, type AssignmentResponse } from "../services/assignmentService";

const deliveryStatuses = ["PENDING", "ACCEPTED", "IN_PROGRESS", "COMPLETED"];

function VolunteerAssignmentDetailsPage() {
  const navigate = useNavigate();
  const { id } = useParams<{ id: string }>();
  const assignmentId = Number(id);
  const [assignment, setAssignment] = useState<AssignmentResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  const load = () => {
    setLoading(true);
    getMyAssignmentById(assignmentId)
      .then(setAssignment)
      .catch(() => setError("Assignment could not be loaded."))
      .finally(() => setLoading(false));
  };

  useEffect(() => { if (Number.isFinite(assignmentId)) load(); }, [assignmentId]);

  const handleStatusUpdate = async (newStatus: string) => {
    setSaving(true); setError("");
    try {
      const updated = await updateAssignmentStatus(assignmentId, newStatus);
      setAssignment(updated);
    } catch {
      setError("That status transition could not be completed. Please try again.");
    } finally { setSaving(false); }
  };

  if (loading) return <section><h2>Assignment details</h2><div className="loading-state"><span className="spinner" /> Loading assignment…</div></section>;
  if (!assignment) return <section><h2>Assignment details</h2><div className="empty-state"><strong>Assignment not found</strong><span>{error}</span><button onClick={() => navigate("/volunteer/assignments")}>Back to assignments</button></div></section>;

  const currentIndex = deliveryStatuses.indexOf(assignment.status);
  const nextStatus = assignment.status === "PENDING" ? "ACCEPTED" : assignment.status === "ACCEPTED" ? "IN_PROGRESS" : assignment.status === "IN_PROGRESS" ? "COMPLETED" : null;

  return <section>
    <div className="dashboard-hero"><div><div className="section-kicker">LIVE DELIVERY WORKFLOW</div><h2>Assignment #{assignment.id}</h2><p>Request #{assignment.requestId} · assigned {new Date(assignment.assignedAt).toLocaleString()}</p></div><button className="button-secondary" onClick={() => navigate("/volunteer/assignments")}>← Back</button></div>
    {error && <div className="inline-alert">{error}</div>}
    <div className="content-grid">
      <div className="panel"><div className="panel-header"><h3>Assignment overview</h3><span className={`status-badge status-${assignment.status.toLowerCase()}`}>{assignment.status}</span></div><div className="panel-body">
        <div className="data-grid">
          <div><span className="muted-label">Request</span><strong>#{assignment.requestId}</strong></div>
          <div><span className="muted-label">Volunteer</span><strong>#{assignment.volunteerId}</strong></div>
          <div><span className="muted-label">Assigned</span><strong>{new Date(assignment.assignedAt).toLocaleString()}</strong></div>
          <div><span className="muted-label">Notes</span><strong>{assignment.notes || "No notes provided"}</strong></div>
        </div>
      </div></div>
      <div className="panel"><div className="panel-header"><h3>Delivery workflow</h3><span>Step {Math.max(currentIndex + 1, 1)} / {deliveryStatuses.length}</span></div><div className="panel-body">
        <div className="workflow">{deliveryStatuses.map((step, index) => <div className={`workflow-step ${index < currentIndex ? "done" : ""} ${index === currentIndex ? "current" : ""}`} key={step}><span>{index < currentIndex ? "✓" : index + 1}</span><strong>{step.replace("_", " ")}</strong></div>)}</div>
        {nextStatus ? <button disabled={saving} onClick={() => handleStatusUpdate(nextStatus)}>{saving ? "Updating…" : nextStatus === "COMPLETED" ? "Mark delivery completed" : nextStatus === "IN_PROGRESS" ? "Start delivery" : "Accept assignment"}</button> : <div className="success-callout">✓ Delivery completed successfully.</div>}
      </div></div>
    </div>
  </section>;
}
export default VolunteerAssignmentDetailsPage;
