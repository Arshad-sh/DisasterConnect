import { useState } from "react";
import { createRequest } from "../services/requestService";

function CreateHelpRequestPage() {
  const [title, setTitle] = useState("");
  const [description, setDescription] = useState("");
  const [location, setLocation] = useState("");
  const [urgency, setUrgency] = useState("");

  const handleSubmit = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();

    try {
      const response = await createRequest({
        title,
        description,
        location,
        urgency,
      });

      console.log("Help request created successfully");
      console.log("Request response:", response);

      setTitle("");
      setDescription("");
      setLocation("");
      setUrgency("");
    } catch (error) {
      console.error("Failed to create help request:", error);
    }
  };

  return (
    <section>
      <h2>Create Help Request</h2>

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="title">Title</label>
          <input
            id="title"
            type="text"
            value={title}
            onChange={(event) => setTitle(event.target.value)}
            placeholder="Enter help request title"
            required
          />
        </div>

        <div>
          <label htmlFor="description">Description</label>
          <textarea
            id="description"
            value={description}
            onChange={(event) => setDescription(event.target.value)}
            placeholder="Describe the emergency need"
            rows={5}
            required
          />
        </div>

        <div>
          <label htmlFor="location">Location</label>
          <input
            id="location"
            type="text"
            value={location}
            onChange={(event) => setLocation(event.target.value)}
            placeholder="Enter location"
            required
          />
        </div>

        <div>
          <label htmlFor="urgency">Urgency</label>
          <select
            id="urgency"
            value={urgency}
            onChange={(event) => setUrgency(event.target.value)}
            required
          >
            <option value="" disabled>
              Select urgency
            </option>
            <option value="LOW">Low</option>
            <option value="MEDIUM">Medium</option>
            <option value="HIGH">High</option>
            <option value="CRITICAL">Critical</option>
          </select>
        </div>

        <button type="submit">
          Create Help Request
        </button>
      </form>
    </section>
  );
}

export default CreateHelpRequestPage;