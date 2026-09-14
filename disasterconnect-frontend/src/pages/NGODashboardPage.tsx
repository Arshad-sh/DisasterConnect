import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getAllRequests, type RequestResponse } from "../services/requestService";
import { getMyResources, type ResourceResponse } from "../services/resourceService";
import { getMyReservations, type ReservationResponse } from "../services/reservationService";

function NGODashboardPage() {
  const [requests, setRequests] = useState<RequestResponse[]>([]);
  const [resources, setResources] = useState<ResourceResponse[]>([]);
  const [reservations, setReservations] = useState<ReservationResponse[]>([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    Promise.all([
      getAllRequests(),
      getMyResources(),
      getMyReservations(),
    ])
      .then(([requestData, resourceData, reservationData]) => {
        setRequests(Array.isArray(requestData) ? requestData : requestData.content ?? []);
        setResources(resourceData);
        setReservations(reservationData);
      })
      .catch((error) => console.error("Failed to load NGO dashboard:", error))
      .finally(() => setLoading(false));
  }, []);

  const criticalRequests = requests.filter((request) => request.urgency === "CRITICAL").length;
  const availableResources = resources.filter((resource) => resource.available).length;
  const activeReservations = reservations.filter(
    (reservation) => reservation.status !== "CANCELLED" && reservation.status !== "COMPLETED"
  ).length;

  return <section>
    <div className="dashboard-hero">
      <div>
        <div className="section-kicker">NGO OPERATIONS</div>
        <h2>Operations overview</h2>
        <p>Coordinate requests, resources and reservations for your response team.</p>
      </div>
      <div className="hero-actions">
        <Link className="button button-danger" to="/ngo/resources/create">+ Add resource</Link>
        <Link className="button button-secondary" to="/ngo/requests">View requests</Link>
      </div>
    </div>

    <div className="stats-grid">
      <div className="stat-card"><div className="stat-top"><span>Incoming requests</span><span className="stat-icon">!</span></div><div className="stat-number">{loading ? "…" : requests.length}</div><div className="stat-meta">{criticalRequests} critical priority</div></div>
      <div className="stat-card"><div className="stat-top"><span>Resources</span><span className="stat-icon">▦</span></div><div className="stat-number">{loading ? "…" : resources.length}</div><div className="stat-meta">{availableResources} currently available</div></div>
      <div className="stat-card"><div className="stat-top"><span>Reservations</span><span className="stat-icon">◈</span></div><div className="stat-number">{loading ? "…" : reservations.length}</div><div className="stat-meta">{activeReservations} active allocations</div></div>
      <div className="stat-card"><div className="stat-top"><span>Network</span><span className="stat-icon">◎</span></div><div className="stat-number">Live</div><div className="stat-meta up">Response coordination active</div></div>
    </div>

    <div className="content-grid">
      <div className="panel">
        <div className="panel-header"><h3>Response operations</h3><span>Workspace</span></div>
        <div className="panel-body">
          <div className="quick-grid">
            <Link className="quick-card" to="/ngo/requests"><span className="quick-icon">!</span><strong>Help requests</strong><span>Review community needs and move support workflows forward.</span></Link>
            <Link className="quick-card" to="/ngo/resources"><span className="quick-icon">▦</span><strong>My resources</strong><span>Monitor resource availability and inventory.</span></Link>
            <Link className="quick-card" to="/ngo/resources/create"><span className="quick-icon">+</span><strong>Add resource</strong><span>Make supplies or services available to responders.</span></Link>
            <Link className="quick-card" to="/ngo/reservations"><span className="quick-icon">◈</span><strong>Reservations</strong><span>Track allocations and reservation history.</span></Link>
          </div>
        </div>
      </div>
      <div className="panel">
        <div className="panel-header"><h3>Response checklist</h3><span>Today</span></div>
        <div className="panel-body">
          <div className="alert-list">
            <div className="alert-item"><span className="alert-mark" style={{background:"#1e9b69"}}/><div><strong>Resources ready</strong><span>Keep quantities and availability current.</span></div></div>
            <div className="alert-item"><span className="alert-mark"/><div><strong>Review incoming requests</strong><span>Prioritize critical needs first.</span></div></div>
          </div>
        </div>
      </div>
    </div>
  </section>;
}
export default NGODashboardPage;
