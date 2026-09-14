import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getAllRequests,
  type RequestResponse,
} from "../services/requestService";

function AdminRequestsPage() {
  const navigate = useNavigate();

  const [requests, setRequests] = useState<RequestResponse[]>([]);
  const [search, setSearch] = useState("");
  const [status, setStatus] = useState("");
  const [urgency, setUrgency] = useState("");
  const [loading, setLoading] = useState(true);

  const loadRequests = async () => {
    try {
      setLoading(true);

      const data = await getAllRequests();

      // Backend returns a paginated response.
      // The actual requests are inside data.content.
      if (Array.isArray(data)) {
        setRequests(data);
      } else if (Array.isArray(data.content)) {
        setRequests(data.content);
      } else {
        setRequests([]);
      }
    } catch (error) {
      console.error(
        "Failed to load admin requests:",
        error
      );

      setRequests([]);
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadRequests();
  }, []);

  const filteredRequests = requests.filter(
    (request) => {
      const searchText = search
        .trim()
        .toLowerCase();

      const matchesSearch =
        searchText === "" ||
        request.title
          .toLowerCase()
          .includes(searchText) ||
        request.description
          .toLowerCase()
          .includes(searchText) ||
        request.location
          .toLowerCase()
          .includes(searchText);

      const matchesStatus =
        status === "" ||
        request.status === status;

      const matchesUrgency =
        urgency === "" ||
        request.urgency === urgency;

      return (
        matchesSearch &&
        matchesStatus &&
        matchesUrgency
      );
    }
  );

  return (
    <section>
      <h2>Manage Help Requests</h2>

      <p>
        Monitor and manage emergency help requests
        submitted by citizens.
      </p>

      <div>
        <h3>Search and Filters</h3>

        <div>
          <label htmlFor="search">
            Search
          </label>

          <input
            id="search"
            type="text"
            value={search}
            onChange={(event) =>
              setSearch(event.target.value)
            }
            placeholder="Search by title, description or location"
          />
        </div>

        <div>
          <label htmlFor="status">
            Status
          </label>

          <select
            id="status"
            value={status}
            onChange={(event) =>
              setStatus(event.target.value)
            }
          >
            <option value="">
              All Statuses
            </option>

            <option value="CREATED">
              CREATED
            </option>

            <option value="VERIFIED">
              VERIFIED
            </option>

            <option value="MATCHED">
              MATCHED
            </option>

            <option value="ASSIGNED">
              ASSIGNED
            </option>

            <option value="IN_PROGRESS">
              IN_PROGRESS
            </option>

            <option value="COMPLETED">
              COMPLETED
            </option>
          </select>
        </div>

        <div>
          <label htmlFor="urgency">
            Urgency
          </label>

          <select
            id="urgency"
            value={urgency}
            onChange={(event) =>
              setUrgency(event.target.value)
            }
          >
            <option value="">
              All Urgency Levels
            </option>

            <option value="CRITICAL">
              CRITICAL
            </option>

            <option value="HIGH">
              HIGH
            </option>

            <option value="MEDIUM">
              MEDIUM
            </option>

            <option value="LOW">
              LOW
            </option>
          </select>
        </div>
      </div>

      <hr />

      <h3>
        Help Requests ({filteredRequests.length})
      </h3>

      {loading ? (
        <p>Loading help requests...</p>
      ) : filteredRequests.length === 0 ? (
        <p>No help requests found.</p>
      ) : (
        <div>
          {filteredRequests.map((request) => (
            <article key={request.id}>
              <h3>
                Request #{request.id}
              </h3>

              <p>
                <strong>Title:</strong>{" "}
                {request.title}
              </p>

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
                <strong>Status:</strong>{" "}
                {request.status}
              </p>

              <p>
                <strong>Citizen ID:</strong>{" "}
                {request.userId}
              </p>

              <button
                type="button"
                onClick={() =>
                  navigate(
                    `/request/${request.id}`
                  )
                }
              >
                View Request Details
              </button>

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

export default AdminRequestsPage;