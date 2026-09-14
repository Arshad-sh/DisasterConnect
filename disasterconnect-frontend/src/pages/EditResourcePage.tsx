import { useEffect, useState } from "react";
import { useNavigate, useParams } from "react-router-dom";
import {
  getResourceById,
  updateResource,
  type CreateResourceData,
  type ResourceResponse,
} from "../services/resourceService";

function EditResourcePage() {
  const { id } = useParams<{ id: string }>();
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("");
  const [quantity, setQuantity] = useState("");
  const [unit, setUnit] = useState("");
  const [location, setLocation] = useState("");

  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);
  const [error, setError] = useState("");

  useEffect(() => {
    const loadResource = async () => {
      if (!id) {
        setError("Resource ID is missing.");
        setLoading(false);
        return;
      }

      try {
        const response: ResourceResponse =
          await getResourceById(Number(id));

        setName(response.name);
        setDescription(response.description);
        setCategory(response.category);
        setQuantity(String(response.quantity));
        setUnit(response.unit);
        setLocation(response.location);
      } catch (error) {
        console.error(
          "Failed to load resource:",
          error
        );

        setError(
          "Failed to load resource details."
        );
      } finally {
        setLoading(false);
      }
    };

    loadResource();
  }, [id]);

  const handleSubmit = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();

    if (!id) {
      setError("Resource ID is missing.");
      return;
    }

    setSaving(true);
    setError("");

    const resourceData: CreateResourceData = {
      name,
      description,
      category,
      quantity: Number(quantity),
      unit,
      location,
    };

    try {
      await updateResource(
        Number(id),
        resourceData
      );

      console.log("Resource updated successfully");

      navigate("/ngo/resources");
    } catch (error) {
      console.error(
        "Failed to update resource:",
        error
      );

      setError(
        "Failed to update resource. Please try again."
      );
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <section>
        <h2>Edit Resource</h2>
        <p>Loading resource...</p>
      </section>
    );
  }

  if (error && !name) {
    return (
      <section>
        <h2>Edit Resource</h2>
        <p>{error}</p>
      </section>
    );
  }

  return (
    <section>
      <h2>Edit Resource</h2>

      {error && <p>{error}</p>}

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="name">
            Resource Name
          </label>

          <input
            id="name"
            type="text"
            value={name}
            onChange={(event) =>
              setName(event.target.value)
            }
            required
          />
        </div>

        <div>
          <label htmlFor="description">
            Description
          </label>

          <textarea
            id="description"
            value={description}
            onChange={(event) =>
              setDescription(event.target.value)
            }
            rows={4}
            required
          />
        </div>

        <div>
          <label htmlFor="category">
            Category
          </label>

          <select
            id="category"
            value={category}
            onChange={(event) =>
              setCategory(event.target.value)
            }
            required
          >
            <option value="" disabled>
              Select category
            </option>

            <option value="MEDICAL">
              Medical
            </option>

            <option value="FOOD">
              Food
            </option>

            <option value="WATER">
              Water
            </option>

            <option value="SHELTER">
              Shelter
            </option>

            <option value="CLOTHING">
              Clothing
            </option>

            <option value="OTHER">
              Other
            </option>
          </select>
        </div>

        <div>
          <label htmlFor="quantity">
            Quantity
          </label>

          <input
            id="quantity"
            type="number"
            min="1"
            value={quantity}
            onChange={(event) =>
              setQuantity(event.target.value)
            }
            required
          />
        </div>

        <div>
          <label htmlFor="unit">
            Unit
          </label>

          <input
            id="unit"
            type="text"
            value={unit}
            onChange={(event) =>
              setUnit(event.target.value)
            }
            required
          />
        </div>

        <div>
          <label htmlFor="location">
            Location
          </label>

          <input
            id="location"
            type="text"
            value={location}
            onChange={(event) =>
              setLocation(event.target.value)
            }
            required
          />
        </div>

        <button
          type="submit"
          disabled={saving}
        >
          {saving
            ? "Saving..."
            : "Update Resource"}
        </button>

        <button
          type="button"
          onClick={() =>
            navigate("/ngo/resources")
          }
        >
          Cancel
        </button>
      </form>
    </section>
  );
}

export default EditResourcePage;