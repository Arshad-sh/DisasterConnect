import api from "./api";

export interface CreateReservationData {
  requestId: number;
  resourceId: number;
  quantity: number;
}

export interface ReservationResponse {
  id: number;
  requestId: number;
  resourceId: number;
  quantity: number;
  status: string;
  createdAt: string;
}

export const createReservation = async (
  reservationData: CreateReservationData
): Promise<ReservationResponse> => {
  const response = await api.post("/api/ngo/reservations", reservationData);
  return response.data;
};

export const getMyReservations = async (): Promise<ReservationResponse[]> => {
  const response = await api.get("/api/ngo/reservations");
  return response.data;
};

export const updateReservationStatus = async (
  id: number,
  status: string
): Promise<ReservationResponse> => {
  const response = await api.put(`/api/ngo/reservations/${id}/status`, null, {
    params: { status },
  });
  return response.data;
};

export const getAllReservations = async (): Promise<ReservationResponse[]> => {
  const response = await api.get("/api/admin/reservations");
  return response.data;
};

export const getReservationById = async (
  id: number
): Promise<ReservationResponse> => {
  const response = await api.get(`/api/admin/reservations/${id}`);
  return response.data;
};
