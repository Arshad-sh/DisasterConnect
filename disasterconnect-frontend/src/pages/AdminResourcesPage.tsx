import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  getAllResources,
  type ResourceResponse,
} from "../services/resourceService";

function AdminResourcesPage() {
  const navigate = useNavigate();

  const [resources, setResources] = useState<ResourceResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadResources = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await getAllResources();

      console.log("Admin resources response:", data);

      setResources(data);
    } catch (error) {
      console.error(
        "Failed to load resources:",
        error
      );

      setResources([]);
      setError(
        "Failed to load resources. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadResources();
  }, []);

  if (loading) {
    return (
      <section>
        <h2>Manage Resources</h2>
        <p>Loading resources...</p>
      </section>
    );
  }

  return (
    <section>
      <h2>Manage Resources</h2>

      <p>
        View and monitor resources provided by NGOs
        on DisasterConnect.
      </p>

      <hr />

      {error && <p>{error}</p>}

      <h3>
        Registered Resources ({resources.length})
      </h3>

      {resources.length === 0 ? (
        <p>No resources found.</p>
      ) : (
        <div>
          {resources.map((resource) => (
            <article key={resource.id}>
              <h3>{resource.name}</h3>

              <p>
                <strong>Resource ID:</strong>{" "}
                {resource.id}
              </p>

              <p>
                <strong>NGO ID:</strong>{" "}
                {resource.ngoId}
              </p>

              <p>
                <strong>Description:</strong>{" "}
                {resource.description}
              </p>

              <p>
                <strong>Category:</strong>{" "}
                {resource.category}
              </p>

              <p>
                <strong>Quantity:</strong>{" "}
                {resource.quantity} {resource.unit}
              </p>

              <p>
                <strong>Availability:</strong>{" "}
                {resource.available
                  ? "Available"
                  : "Unavailable"}
              </p>

              <p>
                <strong>Location:</strong>{" "}
                {resource.location}
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

export default AdminResourcesPage;