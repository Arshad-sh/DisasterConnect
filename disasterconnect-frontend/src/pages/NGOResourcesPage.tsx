import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getMyResources,
  deleteResource,
  type ResourceResponse,
} from "../services/resourceService";

function NGOResourcesPage() {
  const navigate = useNavigate();

  const [resources, setResources] = useState<
    ResourceResponse[]
  >([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadResources = async () => {
      try {
        const data = await getMyResources();

        setResources(data);
      } catch (error) {
        console.error(
          "Failed to load resources:",
          error
        );

        setError(
          "Failed to load your resources."
        );
      } finally {
        setLoading(false);
      }
    };

    loadResources();
  }, []);

  const handleDeleteResource = async (
    resourceId: number
  ) => {
    const confirmed = window.confirm(
      "Are you sure you want to delete this resource?"
    );

    if (!confirmed) {
      return;
    }

    try {
      await deleteResource(resourceId);

      setResources((currentResources) =>
        currentResources.filter(
          (resource) =>
            resource.id !== resourceId
        )
      );

      console.log(
        "Resource deleted successfully"
      );
    } catch (error) {
      console.error(
        "Failed to delete resource:",
        error
      );

      setError(
        "Failed to delete resource. Please try again."
      );
    }
  };

  if (loading) {
    return (
      <section>
        <h2>My Resources</h2>
        <p>Loading resources...</p>
      </section>
    );
  }

  if (error && resources.length === 0) {
    return (
      <section>
        <h2>My Resources</h2>
        <p>{error}</p>
      </section>
    );
  }

  return (
    <section>
      <h2>My Resources</h2>

      {error && <p>{error}</p>}

      <button
        type="button"
        onClick={() =>
          navigate("/ngo/resources/create")
        }
      >
        Add Resource
      </button>

      {resources.length === 0 ? (
        <p>
          You have not added any resources yet.
        </p>
      ) : (
        <div>
          {resources.map((resource) => (
            <article key={resource.id}>
              <h3>{resource.name}</h3>

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
                {resource.quantity}{" "}
                {resource.unit}
              </p>

              <p>
                <strong>Location:</strong>{" "}
                {resource.location}
              </p>

              <p>
                <strong>Availability:</strong>{" "}
                {resource.available
                  ? "Available"
                  : "Unavailable"}
              </p>

              <button
                type="button"
                onClick={() =>
                  navigate(
                    `/ngo/resources/edit/${resource.id}`
                  )
                }
              >
                Edit
              </button>

              <button
                type="button"
                onClick={() =>
                  handleDeleteResource(resource.id)
                }
              >
                Delete
              </button>

              <hr />
            </article>
          ))}
        </div>
      )}
    </section>
  );
}

export default NGOResourcesPage;