import { useEffect, useState } from "react";
import { useParams } from "react-router-dom";
import {
  getRequestById,
  type RequestResponse,
} from "../services/requestService";

const requestStatuses = [
  "CREATED",
  "VERIFIED",
  "MATCHED",
  "ASSIGNED",
  "IN_PROGRESS",
  "COMPLETED",
];

function RequestDetailsPage() {
  const { id } = useParams<{ id: string }>();

  const [request, setRequest] = useState<RequestResponse | null>(null);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadRequest = async () => {
      if (!id) {
        setError("Request ID is missing.");
        setLoading(false);
        return;
      }

      try {
        const response = await getRequestById(Number(id));

        setRequest(response);
      } catch (error) {
        console.error(
          "Failed to load request details:",
          error
        );

        setError(
          "Failed to load request details."
        );
      } finally {
        setLoading(false);
      }
    };

    loadRequest();
  }, [id]);

  if (loading) {
    return (
      <section>
        <h2>Request Details</h2>
        <p>Loading request details...</p>
      </section>
    );
  }

  if (error) {
    return (
      <section>
        <h2>Request Details</h2>
        <p>{error}</p>
      </section>
    );
  }

  if (!request) {
    return (
      <section>
        <h2>Request Details</h2>
        <p>Request not found.</p>
      </section>
    );
  }

  const currentStatusIndex = requestStatuses.indexOf(
    request.status
  );

  return (
    <section>
      <h2>Request Details</h2>

      <article>
        <h3>{request.title}</h3>

        <p>
          <strong>Description:</strong>{" "}
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
          <strong>Current Status:</strong>{" "}
          {request.status}
        </p>
      </article>

      <div>
        <h3>Request Status Tracking</h3>

        {requestStatuses.map((status, index) => {
          const isCurrent = index === currentStatusIndex;
          const isCompleted =
            currentStatusIndex >= 0 &&
            index < currentStatusIndex;

          return (
            <div key={status}>
              <p>
                {isCompleted && "✓ "}
                {isCurrent && "→ "}

                <strong>{status}</strong>

                {isCurrent && " (Current)"}
              </p>

              {index < requestStatuses.length - 1 && (
                <p>↓</p>
              )}
            </div>
          );
        })}
      </div>
    </section>
  );
}

export default RequestDetailsPage;