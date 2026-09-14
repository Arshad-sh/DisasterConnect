import api from "./api";

export interface NGOProfileResponse {
  id: number;
  organizationName: string;
  description: string;
  contactNumber: string;
  address: string;
  verificationStatus: string;
  userId: number;
}

// =========================================================
// NGO PROFILE - LOGGED-IN NGO
// =========================================================

export interface CreateNGOProfileData {
  organizationName: string;
  description: string;
  contactNumber: string;
  address: string;
}

export const createNGOProfile = async (
  profileData: CreateNGOProfileData
) => {
  const response = await api.post(
    "/api/ngo/profile",
    profileData
  );

  return response.data;
};

export const getMyNGOProfile = async () => {
  const response = await api.get(
    "/api/ngo/profile"
  );

  return response.data;
};

// =========================================================
// ADMIN - NGO MANAGEMENT
// =========================================================

export const getAllNGOs = async (): Promise<
  NGOProfileResponse[]
> => {
  const response = await api.get(
    "/api/admin/ngos"
  );

  return response.data;
};

export const getNGOById = async (
  id: number
): Promise<NGOProfileResponse> => {
  const response = await api.get(
    `/api/admin/ngos/${id}`
  );

  return response.data;
};

export const verifyNGO = async (id: number): Promise<NGOProfileResponse> => {
  const response = await api.patch(`/api/admin/ngos/${id}/verification`);
  return response.data;
};
