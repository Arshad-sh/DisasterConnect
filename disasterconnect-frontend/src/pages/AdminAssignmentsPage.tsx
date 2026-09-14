import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";

import {
  getAllAssignments,
  type AssignmentResponse,
} from "../services/assignmentService";

function AdminAssignmentsPage() {
  const navigate = useNavigate();

  const [assignments, setAssignments] = useState<
    AssignmentResponse[]
  >([]);

  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadAssignments = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await getAllAssignments();

      console.log(
        "Admin assignments response:",
        data
      );

      setAssignments(data);
    } catch (error) {
      console.error(
        "Failed to load assignments:",
        error
      );

      setAssignments([]);

      setError(
        "Failed to load assignments. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAssignments();
  }, []);

  if (loading) {
    return (
      <section>
        <h2>Manage Assignments</h2>

        <p>Loading assignments...</p>
      </section>
    );
  }

  return (
    <section>
      <h2>Manage Assignments</h2>

      <p>
        View and monitor volunteer assignments
        across the DisasterConnect platform.
      </p>

      <hr />

      {error && <p>{error}</p>}

      <h3>
        Registered Assignments ({assignments.length})
      </h3>

      {assignments.length === 0 ? (
        <p>No assignments found.</p>
      ) : (
        <div>
          {assignments.map((assignment) => (
            <article key={assignment.id}>
              <h3>
                Assignment #{assignment.id}
              </h3>

              <p>
                <strong>Assignment ID:</strong>{" "}
                {assignment.id}
              </p>

              <p>
                <strong>Request ID:</strong>{" "}
                {assignment.requestId}
              </p>

              <p>
                <strong>Volunteer ID:</strong>{" "}
                {assignment.volunteerId}
              </p>

              <p>
                <strong>Status:</strong>{" "}
                {assignment.status}
              </p>

              <p>
                <strong>Assigned At:</strong>{" "}
                {assignment.assignedAt}
              </p>

              <p>
                <strong>Notes:</strong>{" "}
                {assignment.notes || "No notes"}
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

export default AdminAssignmentsPage;