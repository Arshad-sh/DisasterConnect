import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  getAllVolunteers,
  verifyVolunteer,
  type VolunteerProfileResponse,
} from "../services/volunteerService";

function AdminVolunteersPage() {
  const navigate = useNavigate();
  const [volunteers, setVolunteers] = useState<VolunteerProfileResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [verifyingId, setVerifyingId] = useState<number | null>(null);
  const [error, setError] = useState("");

  const loadVolunteers = async () => {
    try {
      setLoading(true);
      setError("");
      setVolunteers(await getAllVolunteers());
    } catch (requestError) {
      console.error("Failed to load volunteers:", requestError);
      setVolunteers([]);
      setError("Failed to load volunteers. Please try again.");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => { loadVolunteers(); }, []);

  const handleVerify = async (id: number) => {
    try {
      setVerifyingId(id);
      setError("");
      const updated = await verifyVolunteer(id);
      setVolunteers((current) => current.map((item) => item.id === id ? updated : item));
    } catch (requestError) {
      console.error("Failed to verify volunteer:", requestError);
      setError("Volunteer verification could not be completed. Please try again.");
    } finally {
      setVerifyingId(null);
    }
  };

  return (
    <section>
      <div className="dashboard-hero">
        <div><div className="section-kicker">ADMINISTRATION</div><h2>Manage Volunteers</h2><p>View and monitor volunteer profiles registered on DisasterConnect.</p></div>
      </div>
      {error && <div className="inline-alert">{error}</div>}
      <div className="panel">
        <div className="panel-header"><h3>Registered Volunteers ({volunteers.length})</h3><span>Verification</span></div>
        <div className="panel-body">
          {loading ? <div className="loading-state"><span className="spinner" /> Loading volunteers…</div> : volunteers.length === 0 ? <div className="empty-state"><div className="empty-icon">♙</div><strong>No volunteer profiles found</strong></div> :
            <div className="data-list">{volunteers.map((volunteer) => <article className="data-card" key={volunteer.id}>
              <div className="data-card-head"><div><span className="muted-label">VOLUNTEER PROFILE</span><h3>#{volunteer.id}</h3></div><span className={`status-badge status-${volunteer.verificationStatus.toLowerCase()}`}>{volunteer.verificationStatus}</span></div>
              <div className="data-grid">
                <div><span className="muted-label">User ID</span><strong>{volunteer.userId}</strong></div>
                <div><span className="muted-label">Availability</span><strong>{volunteer.availability ? "Available" : "Unavailable"}</strong></div>
                <div><span className="muted-label">Skills</span><strong>{volunteer.skills}</strong></div>
                <div><span className="muted-label">Location</span><strong>{volunteer.location}</strong></div>
              </div>
              {volunteer.verificationStatus === "PENDING" && <button type="button" className="button-danger verify-button" disabled={verifyingId === volunteer.id} onClick={() => handleVerify(volunteer.id)}>{verifyingId === volunteer.id ? "Verifying…" : "Verify Volunteer"}</button>}
            </article>)}</div>}
        </div>
      </div>
      <button type="button" className="button-secondary back-button" onClick={() => navigate("/admin")}>Back to Admin Dashboard</button>
    </section>
  );
}

export default AdminVolunteersPage;
