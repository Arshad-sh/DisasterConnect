import {
  createContext,
  useContext,
  useState,
  type ReactNode,
} from "react";

export type UserRole =
  | "ADMIN"
  | "CITIZEN"
  | "NGO"
  | "VOLUNTEER";

interface AuthContextType {
  token: string | null;
  role: UserRole | null;
  isAuthenticated: boolean;
  logout: () => void;
}

const AuthContext = createContext<AuthContextType | undefined>(undefined);

interface AuthProviderProps {
  children: ReactNode;
}

function getRoleFromToken(token: string | null): UserRole | null {
  if (!token) {
    return null;
  }

  try {
    const payload = token.split(".")[1];

    if (!payload) {
      return null;
    }

    const decodedPayload = JSON.parse(
      atob(
        payload
          .replace(/-/g, "+")
          .replace(/_/g, "/")
      )
    );

    const role = decodedPayload.role;

    if (
      role === "ADMIN" ||
      role === "CITIZEN" ||
      role === "NGO" ||
      role === "VOLUNTEER"
    ) {
      return role;
    }

    return null;
  } catch (error) {
    console.error("Failed to read JWT role:", error);

    return null;
  }
}

export function AuthProvider({ children }: AuthProviderProps) {
  const [token, setToken] = useState<string | null>(
    localStorage.getItem("token")
  );

  const role = getRoleFromToken(token);

  const logout = () => {
    localStorage.removeItem("token");
    setToken(null);
  };

  const value: AuthContextType = {
    token,
    role,
    isAuthenticated: Boolean(token),
    logout,
  };

  return (
    <AuthContext.Provider value={value}>
      {children}
    </AuthContext.Provider>
  );
}

export function useAuth() {
  const context = useContext(AuthContext);

  if (!context) {
    throw new Error("useAuth must be used inside AuthProvider");
  }

  return context;
}