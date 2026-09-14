import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import api from "../services/api";

interface AdminUser {
  id: number;
  name: string;
  email: string;
  phone: string | null;
  role: string;
}

function AdminUsersPage() {
  const navigate = useNavigate();

  const [users, setUsers] = useState<AdminUser[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");
  const [managedUserId, setManagedUserId] = useState<number | null>(null);

  const loadUsers = async () => {
    try {
      setLoading(true);
      setError("");

      const response = await api.get("/api/admin/users");

      console.log("Admin users response:", response.data);

      setUsers(response.data);
    } catch (error) {
      console.error("Failed to load admin users:", error);

      setUsers([]);
      setError(
        "Failed to load users. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadUsers();
  }, []);

  if (loading) {
    return (
      <section>
        <h2>Manage Users</h2>
        <p>Loading users...</p>
      </section>
    );
  }

  return (
    <section>
      <h2>Manage Users</h2>

      <p>
        View and manage users registered on the
        DisasterConnect platform.
      </p>

      <hr />

      {error && (
        <p>
          <strong>{error}</strong>
        </p>
      )}

      <h3>
        Registered Users ({users.length})
      </h3>

      {users.length === 0 ? (
        <p>No users found.</p>
      ) : (
        <div>
          {users.map((user) => (
            <article key={user.id}>
              <h3>{user.name}</h3>

              <p>
                <strong>User ID:</strong>{" "}
                {user.id}
              </p>

              <p>
                <strong>Email:</strong>{" "}
                {user.email}
              </p>

              <p>
                <strong>Phone:</strong>{" "}
                {user.phone || "Not provided"}
              </p>

              <p>
                <strong>Role:</strong>{" "}
                {user.role}
              </p>

              <p>
                <strong>Status:</strong>{" "}
                ACTIVE
              </p>

              <button
                type="button"
                onClick={() => setManagedUserId(managedUserId === user.id ? null : user.id)}
              >
                {managedUserId === user.id ? "Hide User Details" : "Manage User"}
              </button>

              {managedUserId === user.id && (
                <div className="manage-user-panel">
                  <div><span className="muted-label">ACCOUNT</span><strong>Active</strong></div>
                  <div><span className="muted-label">ROLE</span><strong>{user.role}</strong></div>
                  <div><span className="muted-label">CONTACT</span><strong>{user.phone || "Not provided"}</strong></div>
                </div>
              )}

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

export default AdminUsersPage;