import { useEffect, useState } from "react";
import axios from "axios";
import { useNavigate } from "react-router-dom";
import { createVolunteerProfile, getMyVolunteerProfile, updateVolunteerAvailability, type VolunteerProfileResponse } from "../services/volunteerService";

function VolunteerProfilePage() {
  const navigate = useNavigate();
  const [profile, setProfile] = useState<VolunteerProfileResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");
  const [profileMissing, setProfileMissing] = useState(false);
  const [skills, setSkills] = useState("");
  const [location, setLocation] = useState("");
  const [creating, setCreating] = useState(false);

  const load = () => {
    setLoading(true);
    setError("");
    setProfileMissing(false);
    getMyVolunteerProfile()
      .then(setProfile)
      .catch((requestError) => {
        const status = axios.isAxiosError(requestError) ? requestError.response?.status : undefined;
        if (status === 404 || status === 500) {
          setProfileMissing(true);
        } else {
          setError("Failed to load your volunteer profile. Please try again.");
        }
      })
      .finally(() => setLoading(false));
  };
  useEffect(load, []);

  const createProfile = async (event: React.FormEvent<HTMLFormElement>) => {
    event.preventDefault();
    setCreating(true);
    setError("");
    try {
      const created = await createVolunteerProfile({ skills: skills.trim(), location: location.trim() });
      setProfile(created);
      setProfileMissing(false);
    } catch {
      setError("Your responder profile could not be created. Please check the details and try again.");
    } finally {
      setCreating(false);
    }
  };

  const toggle = async () => {
    if (!profile) return;
    setSaving(true); setError("");
    try { setProfile(await updateVolunteerAvailability(!profile.availability)); }
    catch { setError("Availability could not be updated. Please try again."); }
    finally { setSaving(false); }
  };

  if (loading) return <section><h2>Volunteer profile</h2><div className="loading-state"><span className="spinner" /> Loading profile…</div></section>;

  return <section>
    <div className="dashboard-hero"><div><div className="section-kicker">RESPONDER PROFILE</div><h2>Volunteer profile</h2><p>Manage your response readiness and keep availability synchronized with the network.</p></div><button className="button-secondary" onClick={() => navigate("/volunteer")}>← Dashboard</button></div>
    {error && <div className="inline-alert">{error}</div>}
    {profileMissing && !profile && <div className="panel profile-setup-panel">
      <div className="panel-header"><div><h3>Complete your responder profile</h3><span>Your account is ready — add the details responders use for coordination.</span></div><span className="status-badge status-pending">SETUP</span></div>
      <div className="panel-body">
        <form className="profile-setup-form" onSubmit={createProfile}>
          <div className="form-grid">
            <label><span>Skills</span><input value={skills} onChange={(event) => setSkills(event.target.value)} placeholder="Medical support, delivery, logistics" required /></label>
            <label><span>Location</span><input value={location} onChange={(event) => setLocation(event.target.value)} placeholder="Pune, Maharashtra" required /></label>
          </div>
          <button type="submit" disabled={creating}>{creating ? "Creating profile…" : "Create responder profile"}</button>
        </form>
      </div>
    </div>}
    {profile && <div className="content-grid">
      <div className="panel"><div className="panel-header"><h3>Profile information</h3><span>#{profile.id}</span></div><div className="panel-body"><div className="profile-hero"><div className="avatar avatar-large">VO</div><div><strong>Volunteer responder</strong><span>Volunteer ID #{profile.userId}</span></div></div><div className="data-grid"><div><span className="muted-label">Skills</span><strong>{profile.skills || "Not provided"}</strong></div><div><span className="muted-label">Location</span><strong>{profile.location || "Not provided"}</strong></div><div><span className="muted-label">Verification</span><strong>{profile.verificationStatus}</strong></div></div></div></div>
      <div className="panel"><div className="panel-header"><h3>Availability</h3><span className={`status-badge ${profile.availability ? "status-available" : "status-unavailable"}`}>{profile.availability ? "AVAILABLE" : "UNAVAILABLE"}</span></div><div className="panel-body availability-panel"><div className={`availability-orb ${profile.availability ? "is-on" : ""}`}><span>●</span></div><strong>{profile.availability ? "Ready to respond" : "Currently unavailable"}</strong><p>Keep this status current so the matching system can consider you for suitable assignments.</p><button disabled={saving} onClick={toggle}>{saving ? "Saving…" : profile.availability ? "Set unavailable" : "Set available"}</button></div></div>
    </div>}
  </section>;
}
export default VolunteerProfilePage;
