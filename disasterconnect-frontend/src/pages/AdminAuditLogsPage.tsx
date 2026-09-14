import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import {
  getAllAuditLogs,
  type AuditLogResponse,
} from "../services/auditLogService";

function AdminAuditLogsPage() {
  const navigate = useNavigate();

  const [auditLogs, setAuditLogs] = useState<AuditLogResponse[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState("");

  const loadAuditLogs = async () => {
    try {
      setLoading(true);
      setError("");

      const data = await getAllAuditLogs();

      console.log("Admin audit logs response:", data);

      setAuditLogs(data);
    } catch (error) {
      console.error("Failed to load audit logs:", error);

      setAuditLogs([]);
      setError(
        "Failed to load audit logs. Please try again."
      );
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadAuditLogs();
  }, []);

  if (loading) {
    return (
      <section>
        <h2>Manage Audit Logs</h2>

        <p>Loading audit logs...</p>
      </section>
    );
  }

  return (
    <section>
      <h2>Manage Audit Logs</h2>

      <p>
        View and monitor important system activities and
        history across the DisasterConnect platform.
      </p>

      <hr />

      {error && <p>{error}</p>}

      <h3>
        Showing {auditLogs.length} audit events
      </h3>

      {auditLogs.length === 0 ? (
        <p>No audit logs found.</p>
      ) : (
        <div>
          {auditLogs.map((log) => (
            <article key={log.id}>
              <h3>
                Audit Log #{log.id}
              </h3>

              <p>
                <strong>Audit Log ID:</strong>{" "}
                {log.id}
              </p>

              <p>
                <strong>Action:</strong>{" "}
                {log.action}
              </p>

              <p>
                <strong>Performed By:</strong>{" "}
                {log.performedBy ?? "System"}
              </p>

              <p>
                <strong>Entity Type:</strong>{" "}
                {log.entityType}
              </p>

              <p>
                <strong>Entity ID:</strong>{" "}
                {log.entityId}
              </p>

              <p>
                <strong>Details:</strong>{" "}
                {log.details ?? "No details available"}
              </p>

              <p>
                <strong>Created At:</strong>{" "}
                {log.createdAt}
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

export default AdminAuditLogsPage;