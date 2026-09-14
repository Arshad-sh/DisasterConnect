import api from "./api";

export interface ResourceResponse {
  id: number;
  name: string;
  description: string;
  category: string;
  quantity: number;
  unit: string;
  available: boolean;
  location: string;
  ngoId: number;
}

export interface CreateResourceData {
  name: string;
  description: string;
  category: string;
  quantity: number;
  unit: string;
  location: string;
}

// =========================
// NGO RESOURCE APIs
// =========================

export const createResource = async (
  resourceData: CreateResourceData
) => {
  const response = await api.post(
    "/api/ngo/resources",
    resourceData
  );

  return response.data;
};

export const updateResource = async (
  id: number,
  resourceData: CreateResourceData
) => {
  const response = await api.put(
    `/api/ngo/resources/${id}`,
    resourceData
  );

  return response.data;
};

export const getMyResources = async (): Promise<
  ResourceResponse[]
> => {
  const response = await api.get(
    "/api/ngo/resources"
  );

  return response.data;
};

export const getResourceById = async (
  id: number
): Promise<ResourceResponse> => {
  const response = await api.get(
    `/api/ngo/resources/${id}`
  );

  return response.data;
};

export const deleteResource = async (
  id: number
) => {
  await api.delete(
    `/api/ngo/resources/${id}`
  );
};


// =========================
// ADMIN RESOURCE APIs
// =========================

export const getAllResources = async (): Promise<
  ResourceResponse[]
> => {
  const response = await api.get(
    "/api/admin/resources"
  );

  return response.data;
};

export const getAdminResourceById = async (
  id: number
): Promise<ResourceResponse> => {
  const response = await api.get(
    `/api/admin/resources/${id}`
  );

  return response.data;
};