import { useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  createResource,
  type CreateResourceData,
} from "../services/resourceService";

function CreateResourcePage() {
  const navigate = useNavigate();

  const [name, setName] = useState("");
  const [description, setDescription] = useState("");
  const [category, setCategory] = useState("");
  const [quantity, setQuantity] = useState("");
  const [unit, setUnit] = useState("");
  const [location, setLocation] = useState("");

  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const handleSubmit = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();

    setLoading(true);
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
      await createResource(resourceData);

      console.log("Resource created successfully");

      navigate("/ngo/resources");
    } catch (error) {
      console.error(
        "Failed to create resource:",
        error
      );

      setError(
        "Failed to create resource. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  return (
    <section>
      <h2>Add Resource</h2>

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
            placeholder="Enter resource name"
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
            placeholder="Describe the resource"
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
            placeholder="Enter quantity"
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
            placeholder="e.g. boxes, bottles, kg"
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
            placeholder="Enter resource location"
            required
          />
        </div>

        <button
          type="submit"
          disabled={loading}
        >
          {loading
            ? "Creating..."
            : "Create Resource"}
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

export default CreateResourcePage;