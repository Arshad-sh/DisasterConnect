import api from "./api";

export interface CreateRequestData {
  title: string;
  description: string;
  location: string;
  urgency: string;
}

export interface RequestResponse {
  id: number;
  title: string;
  description: string;
  location: string;
  urgency: string;
  status: string;
  userId: number;
}

export const createRequest = async (
  requestData: CreateRequestData
) => {
  const response = await api.post(
    "/api/requests",
    requestData
  );

  return response.data;
};

export const getMyRequests = async () => {
  const response = await api.get(
    "/api/requests/my"
  );

  return response.data;
};

export const getRequestById = async (
  id: number
) => {
  const response = await api.get(
    `/api/requests/${id}`
  );

  return response.data;
};

export const getAllRequests = async () => {
  const response = await api.get(
    "/api/requests"
  );

  return response.data;
};