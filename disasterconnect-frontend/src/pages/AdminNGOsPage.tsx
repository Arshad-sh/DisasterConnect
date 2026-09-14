import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { getAllNGOs, verifyNGO, type NGOProfileResponse } from "../services/ngoService";

function AdminNGOsPage() {
  const navigate = useNavigate();
  const [ngos, setNGOs] = useState<NGOProfileResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [verifyingId, setVerifyingId] = useState<number | null>(null);
  const [error, setError] = useState("");

  const loadNGOs = async () => {
    try {
      setLoading(true);
      setError("");
      setNGOs(await getAllNGOs());
    } catch (requestError) {
      console.error("Failed to load NGOs:", requestError);
      setNGOs([]);
      setError("Failed to load NGOs. Please try again.");
    } finally { setLoading(false); }
  };

  useEffect(() => { loadNGOs(); }, []);

  const handleVerify = async (id: number) => {
    try {
      setVerifyingId(id);
      setError("");
      const updated = await verifyNGO(id);
      setNGOs((current) => current.map((item) => item.id === id ? updated : item));
    } catch (requestError) {
      console.error("Failed to verify NGO:", requestError);
      setError("NGO verification could not be completed. Please try again.");
    } finally { setVerifyingId(null); }
  };

  return (
    <section>
      <div className="dashboard-hero">
        <div><div className="section-kicker">ADMINISTRATION</div><h2>Manage NGOs</h2><p>View and monitor NGO and resource provider profiles registered on DisasterConnect.</p></div>
      </div>
      {error && <div className="inline-alert">{error}</div>}
      <div className="panel">
        <div className="panel-header"><h3>Registered NGOs ({ngos.length})</h3><span>Verification</span></div>
        <div className="panel-body">
          {loading ? <div className="loading-state"><span className="spinner" /> Loading NGOs…</div> : ngos.length === 0 ? <div className="empty-state"><div className="empty-icon">◎</div><strong>No NGO profiles found</strong></div> :
            <div className="data-list">{ngos.map((ngo) => <article className="data-card" key={ngo.id}>
              <div className="data-card-head"><div><span className="muted-label">NGO PROFILE</span><h3>{ngo.organizationName}</h3></div><span className={`status-badge status-${ngo.verificationStatus.toLowerCase()}`}>{ngo.verificationStatus}</span></div>
              <div className="data-grid">
                <div><span className="muted-label">NGO Profile ID</span><strong>#{ngo.id}</strong></div>
                <div><span className="muted-label">User ID</span><strong>{ngo.userId}</strong></div>
                <div><span className="muted-label">Contact Number</span><strong>{ngo.contactNumber}</strong></div>
                <div><span className="muted-label">Address</span><strong>{ngo.address}</strong></div>
              </div>
              <p className="data-description">{ngo.description}</p>
              {ngo.verificationStatus === "PENDING" && <button type="button" className="button-danger verify-button" disabled={verifyingId === ngo.id} onClick={() => handleVerify(ngo.id)}>{verifyingId === ngo.id ? "Verifying…" : "Verify NGO"}</button>}
            </article>)}</div>}
        </div>
      </div>
      <button type="button" className="button-secondary back-button" onClick={() => navigate("/admin")}>Back to Admin Dashboard</button>
    </section>
  );
}

export default AdminNGOsPage;
