import { useEffect, useState } from "react";
import {
  getAllRequests,
  type RequestResponse,
} from "../services/requestService";

function NGORequestsPage() {
  const [requests, setRequests] = useState<RequestResponse[]>([]);

  const [search, setSearch] = useState("");
  const [status, setStatus] = useState("");
  const [urgency, setUrgency] = useState("");

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadRequests = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await getAllRequests();

      let filteredRequests: RequestResponse[] =
        response.content;

      if (search.trim() !== "") {
        const searchValue = search
          .trim()
          .toLowerCase();

        filteredRequests = filteredRequests.filter(
          (request) =>
            request.title
              .toLowerCase()
              .includes(searchValue) ||
            request.description
              .toLowerCase()
              .includes(searchValue) ||
            request.location
              .toLowerCase()
              .includes(searchValue)
        );
      }

      if (status !== "") {
        filteredRequests = filteredRequests.filter(
          (request) =>
            request.status === status
        );
      }

      if (urgency !== "") {
        filteredRequests = filteredRequests.filter(
          (request) =>
            request.urgency === urgency
        );
      }

      setRequests(filteredRequests);
    } catch (error) {
      console.error(
        "Failed to load help requests:",
        error
      );

      setError(
        "Failed to load help requests."
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadRequests();
  }, []);

  const handleSearch = (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();

    loadRequests();
  };

  const handleClearFilters = () => {
    setSearch("");
    setStatus("");
    setUrgency("");

    loadRequests();
  };

  if (loading) {
    return (
      <section>
        <h2>Help Requests</h2>
        <p>Loading help requests...</p>
      </section>
    );
  }

  if (error) {
    return (
      <section>
        <h2>Help Requests</h2>
        <p>{error}</p>
      </section>
    );
  }

  return (
    <section>
      <h2>Help Requests</h2>

      <form onSubmit={handleSearch}>
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
            placeholder="Search title, description or location"
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
              All Urgencies
            </option>

            <option value="LOW">
              LOW
            </option>

            <option value="MEDIUM">
              MEDIUM
            </option>

            <option value="HIGH">
              HIGH
            </option>

            <option value="CRITICAL">
              CRITICAL
            </option>
          </select>
        </div>

        <button type="submit">
          Search
        </button>

        <button
          type="button"
          onClick={handleClearFilters}
        >
          Clear Filters
        </button>
      </form>

      <hr />

      <h3>
        Results: {requests.length}
      </h3>

      {requests.length === 0 ? (
        <p>
          No help requests match the selected
          filters.
        </p>
      ) : (
        <div>
          {requests.map((request) => (
            <article key={request.id}>
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
                <strong>Status:</strong>{" "}
                {request.status}
              </p>

              <hr />
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

export default NGORequestsPage;