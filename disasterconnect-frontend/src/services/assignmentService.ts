import api from "./api";

export interface AssignmentResponse {
  id: number;
  requestId: number;
  volunteerId: number;
  status: string;
  assignedAt: string;
  notes: string | null;
}

export interface CreateAssignmentData {
  requestId: number;
  volunteerId: number;
  notes?: string;
}

export const createAssignment = async (
  assignmentData: CreateAssignmentData
): Promise<AssignmentResponse> => {
  const response = await api.post("/api/assignments", assignmentData);
  return response.data;
};

export const updateAssignmentStatus = async (
  id: number,
  status: string
): Promise<AssignmentResponse> => {
  const response = await api.put(`/api/assignments/${id}/status`, null, {
    params: { status },
  });
  return response.data;
};

export const getMyAssignments = async (): Promise<AssignmentResponse[]> => {
  const response = await api.get("/api/assignments/my");
  return response.data;
};

export const getMyAssignmentById = async (
  id: number
): Promise<AssignmentResponse> => {
  const response = await api.get(`/api/assignments/${id}`);
  return response.data;
};

export const getAllAssignments = async (): Promise<AssignmentResponse[]> => {
  const response = await api.get("/api/admin/assignments");
  return response.data;
};

export const getAssignmentById = async (
  id: number
): Promise<AssignmentResponse> => {
  const response = await api.get(`/api/admin/assignments/${id}`);
  return response.data;
};
