import api from "./api";

export interface AuditLogResponse {
  id: number;
  action: string;
  performedBy: number | null;
  entityType: string;
  entityId: number;
  details: string | null;
  createdAt: string;
}

export const getAllAuditLogs = async (): Promise<AuditLogResponse[]> => {
  const response = await api.get("/api/admin/audit-logs");
  return response.data;
};

export const getAuditLogById = async (
  id: number
): Promise<AuditLogResponse> => {
  const response = await api.get(`/api/admin/audit-logs/${id}`);
  return response.data;
};