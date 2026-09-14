import api from "./api";

export interface VolunteerProfileResponse {
  id: number;
  skills: string;
  location: string;
  availability: boolean;
  verificationStatus: string;
  userId: number;
}

export interface CreateVolunteerProfileData {
  skills: string;
  location: string;
}

export const createVolunteerProfile = async (
  profileData: CreateVolunteerProfileData
) => {
  const response = await api.post("/api/volunteer/profile", profileData);
  return response.data;
};

export const getMyVolunteerProfile =
  async (): Promise<VolunteerProfileResponse> => {
    const response = await api.get("/api/volunteer/profile");
    return response.data;
  };

export const updateVolunteerAvailability = async (
  availability: boolean
): Promise<VolunteerProfileResponse> => {
  const response = await api.patch("/api/volunteer/profile/availability", null, {
    params: { availability },
  });
  return response.data;
};

export const getAllVolunteers = async (): Promise<VolunteerProfileResponse[]> => {
  const response = await api.get("/api/admin/volunteers");
  return response.data;
};

export const getVolunteerById = async (
  id: number
): Promise<VolunteerProfileResponse> => {
  const response = await api.get(`/api/admin/volunteers/${id}`);
  return response.data;
};


export const verifyVolunteer = async (id: number): Promise<VolunteerProfileResponse> => {
  const response = await api.patch(`/api/admin/volunteers/${id}/verification`);
  return response.data;
};
