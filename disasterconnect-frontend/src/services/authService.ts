import api from "./api";

export interface LoginRequest {
  email: string;
  password: string;
}

export interface RegisterRequest {
  name: string;
  email: string;
  phone: string;
  password: string;
  role: string;
}

export const login = async (credentials: LoginRequest) => {
  const response = await api.post("/api/auth/login", credentials);

  return response.data;
};

export const register = async (userData: RegisterRequest) => {
  const response = await api.post("/api/auth/register", userData);

  return response.data;
};