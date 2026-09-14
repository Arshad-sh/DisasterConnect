import { useEffect, useState } from "react";
import { Link } from "react-router-dom";
import { getMyReservations, type ReservationResponse } from "../services/reservationService";

function NGOReservationsPage() {
  const [reservations, setReservations] = useState<ReservationResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    getMyReservations()
      .then(setReservations)
      .catch(() => setError("Failed to load reservation history. Please try again."))
      .finally(() => setLoading(false));
  }, []);

  const statusClass = (status: string) => `status-badge status-${status.toLowerCase()}`;

  return <section>
    <div className="dashboard-hero">
      <div>
        <div className="section-kicker">RESOURCE ALLOCATION</div>
        <h2>Reservation history</h2>
        <p>Track every allocation created by your NGO and monitor its current status.</p>
      </div>
      <div className="hero-actions">
        <Link className="button button-danger" to="/ngo/reservations/create">+ Create reservation</Link>
      </div>
    </div>

    {error && <div className="inline-alert">{error}</div>}

    <div className="panel">
      <div className="panel-header"><h3>Your reservations</h3><span>{loading ? "Loading…" : `${reservations.length} total`}</span></div>
      <div className="panel-body">
        {loading ? <div className="loading-state"><span className="spinner" /> Loading reservation history…</div> :
          reservations.length === 0 ? <div className="empty-state"><div className="empty-icon">◈</div><strong>No reservations yet</strong><span>Create a reservation to allocate one of your resources to an active request.</span><Link className="button button-secondary" to="/ngo/reservations/create">Create your first reservation</Link></div> :
          <div className="data-list">
            {reservations.map((reservation) => <article className="data-card" key={reservation.id}>
              <div className="data-card-head"><div><span className="muted-label">RESERVATION</span><h3>#{reservation.id}</h3></div><span className={statusClass(reservation.status)}>{reservation.status}</span></div>
              <div className="data-grid">
                <div><span className="muted-label">Help request</span><strong>#{reservation.requestId}</strong></div>
                <div><span className="muted-label">Resource</span><strong>#{reservation.resourceId}</strong></div>
                <div><span className="muted-label">Quantity</span><strong>{reservation.quantity}</strong></div>
                <div><span className="muted-label">Created</span><strong>{new Date(reservation.createdAt).toLocaleString()}</strong></div>
              </div>
            </article>)}
          </div>}
      </div>
    </div>
  </section>;
}
export default NGOReservationsPage;
