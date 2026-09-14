import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  createReservation,
  type CreateReservationData,
} from "../services/reservationService";
import {
  getAllRequests,
  type RequestResponse,
} from "../services/requestService";
import {
  getMyResources,
  type ResourceResponse,
} from "../services/resourceService";

function CreateReservationPage() {
  const navigate = useNavigate();

  const [requests, setRequests] = useState<RequestResponse[]>([]);
  const [resources, setResources] = useState<ResourceResponse[]>([]);

  const [requestId, setRequestId] = useState("");
  const [resourceId, setResourceId] = useState("");
  const [quantity, setQuantity] = useState("");

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadData = async () => {
      try {
        const [requestResponse, resourceResponse] =
          await Promise.all([
            getAllRequests(),
            getMyResources(),
          ]);

        setRequests(requestResponse.content);
        setResources(resourceResponse);
      } catch (error) {
        console.error(
          "Failed to load reservation data:",
          error
        );

        setError(
          "Failed to load help requests or resources."
        );
      } finally {
        setLoading(false);
      }
    };

    loadData();
  }, []);

  const handleSubmit = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();

    if (!requestId || !resourceId || !quantity) {
      setError("Please complete all fields.");
      return;
    }

    const reservationData: CreateReservationData = {
      requestId: Number(requestId),
      resourceId: Number(resourceId),
      quantity: Number(quantity),
    };

    setSaving(true);
    setError("");

    try {
      const response = await createReservation(
        reservationData
      );

      console.log(
        "Reservation created successfully"
      );

      console.log(
        "Reservation response:",
        response
      );

      navigate("/ngo");
    } catch (error) {
      console.error(
        "Failed to create reservation:",
        error
      );

      setError(
        "Failed to create reservation. Please try again."
      );
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <section>
        <h2>Create Reservation</h2>
        <p>Loading requests and resources...</p>
      </section>
    );
  }

  return (
    <section>
      <h2>Create Reservation</h2>

      {error && <p>{error}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="request">
            Help Request
          </label>

          <select
            id="request"
            value={requestId}
            onChange={(event) =>
              setRequestId(event.target.value)
            }
            required
          >
            <option value="" disabled>
              Select help request
            </option>

            {requests.map((request) => (
              <option
                key={request.id}
                value={request.id}
              >
                #{request.id} - {request.title} (
                {request.urgency})
              </option>
            ))}
          </select>
        </div>

        <div>
          <label htmlFor="resource">
            Resource
          </label>

          <select
            id="resource"
            value={resourceId}
            onChange={(event) =>
              setResourceId(event.target.value)
            }
            required
          >
            <option value="" disabled>
              Select resource
            </option>

            {resources.map((resource) => (
              <option
                key={resource.id}
                value={resource.id}
                disabled={!resource.available}
              >
                #{resource.id} - {resource.name} (
                {resource.quantity} {resource.unit})
              </option>
            ))}
          </select>
        </div>

        <div>
          <label htmlFor="quantity">
            Reservation Quantity
          </label>

          <input
            id="quantity"
            type="number"
            min="1"
            value={quantity}
            onChange={(event) =>
              setQuantity(event.target.value)
            }
            placeholder="Enter quantity"
            required
          />
        </div>

        <button
          type="submit"
          disabled={saving}
        >
          {saving
            ? "Creating..."
            : "Create Reservation"}
        </button>

        <button
          type="button"
          onClick={() => navigate("/ngo")}
        >
          Cancel
        </button>
      </form>
    </section>
  );
}

export default CreateReservationPage;