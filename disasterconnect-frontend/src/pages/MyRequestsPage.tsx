import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getMyRequests,
  type RequestResponse,
} from "../services/requestService";

function MyRequestsPage() {
  const navigate = useNavigate();

  const [requests, setRequests] = useState<RequestResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadMyRequests = async () => {
      try {
        const response = await getMyRequests();

        setRequests(response.content);
      } catch (error) {
        console.error(
          "Failed to load my requests:",
          error
        );

        setError(
          "Failed to load your help requests."
        );
      } finally {
        setLoading(false);
      }
    };

    loadMyRequests();
  }, []);

  const handleViewDetails = (requestId: number) => {
    navigate(`/citizen/requests/${requestId}`);
  };

  if (loading) {
    return (
      <section>
        <h2>My Requests</h2>
        <p>Loading your requests...</p>
      </section>
    );
  }

  if (error) {
    return (
      <section>
        <h2>My Requests</h2>
        <p>{error}</p>
      </section>
    );
  }

  return (
    <section>
      <h2>My Requests</h2>

      {requests.length === 0 ? (
        <p>
          You have not created any help requests yet.
        </p>
      ) : (
        <div>
          {requests.map((request) => (
            <article key={request.id}>
              <h3>{request.title}</h3>

              <p>
                {request.description}
              </p>

              <p>
                <strong>Location:</strong>{" "}
                {request.location}
              </p>

              <p>
                <strong>Urgency:</strong>{" "}
                {request.urgency}
              </p>

              <p>
                <strong>Status:</strong>{" "}
                {request.status}
              </p>

              <button
                type="button"
                onClick={() =>
                  handleViewDetails(request.id)
                }
              >
                View Details
              </button>

              <hr />
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

export default MyRequestsPage;