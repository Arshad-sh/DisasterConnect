import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  getAllReservations,
  type ReservationResponse,
} from "../services/reservationService";

function AdminReservationsPage() {
  const navigate = useNavigate();

  const [reservations, setReservations] = useState<
    ReservationResponse[]
  >([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadReservations = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await getAllReservations();

      console.log(
        "Admin reservations response:",
        data
      );

      setReservations(data);
    } catch (error) {
      console.error(
        "Failed to load reservations:",
        error
      );

      setReservations([]);

      setError(
        "Failed to load reservations. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadReservations();
  }, []);

  if (loading) {
    return (
      <section>
        <h2>Manage Reservations</h2>

        <p>Loading reservations...</p>
      </section>
    );
  }

  return (
    <section>
      <h2>Manage Reservations</h2>

      <p>
        View and monitor resource reservations
        across the DisasterConnect platform.
      </p>

      <hr />

      {error && <p>{error}</p>}

      <h3>
        Registered Reservations ({reservations.length})
      </h3>

      {reservations.length === 0 ? (
        <p>No reservations found.</p>
      ) : (
        <div>
          {reservations.map((reservation) => (
            <article key={reservation.id}>
              <h3>
                Reservation #{reservation.id}
              </h3>

              <p>
                <strong>Reservation ID:</strong>{" "}
                {reservation.id}
              </p>

              <p>
                <strong>Request ID:</strong>{" "}
                {reservation.requestId}
              </p>

              <p>
                <strong>Resource ID:</strong>{" "}
                {reservation.resourceId}
              </p>

              <p>
                <strong>Quantity:</strong>{" "}
                {reservation.quantity}
              </p>

              <p>
                <strong>Status:</strong>{" "}
                {reservation.status}
              </p>

              <p>
                <strong>Created At:</strong>{" "}
                {reservation.createdAt}
              </p>

              <hr />
            </article>
          ))}
        </div>
      )}

      <button
        type="button"
        onClick={() => navigate("/admin")}
      >
        Back to Admin Dashboard
      </button>
    </section>
  );
}

export default AdminReservationsPage;