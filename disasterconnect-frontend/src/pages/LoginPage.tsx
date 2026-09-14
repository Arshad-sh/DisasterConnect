import { useState } from "react";
import { useNavigate } from "react-router-dom";
import { login } from "../services/authService";

function getRoleFromToken(token: string): string | null {
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

    return decodedPayload.role ?? null;
  } catch (error) {
    console.error(
      "Failed to read role from JWT:",
      error
    );

    return null;
  }
}

function LoginPage() {
  const [email, setEmail] = useState("");
  const [password, setPassword] = useState("");

  const navigate = useNavigate();

  const handleSubmit = async (
    event: React.FormEvent<HTMLFormElement>
  ) => {
    event.preventDefault();

    try {
      const token = await login({
        email,
        password,
      });

      localStorage.setItem("token", token);

      console.log("Login successful");
      console.log("JWT stored successfully");

      const role = getRoleFromToken(token);

      console.log("User role:", role);

      if (role === "ADMIN") {
        window.location.href = "/admin";
        return;
      }

      if (role === "CITIZEN") {
        window.location.href = "/citizen";
        return;
      }

      if (role === "NGO") {
        window.location.href = "/ngo";
        return;
      }

      if (role === "VOLUNTEER") {
        window.location.href = "/volunteer";
        return;
      }

      console.error("Unknown or missing user role.");
      navigate("/");
    } catch (error) {
      console.error("Login failed:", error);
    }
  };

  return (
    <section>
      <h2>Login</h2>

      <form onSubmit={handleSubmit}>
        <div>
          <label htmlFor="email">
            Email
          </label>

          <input
            id="email"
            type="email"
            value={email}
            onChange={(event) =>
              setEmail(event.target.value)
            }
            placeholder="Enter your email"
            required
          />
        </div>

        <div>
          <label htmlFor="password">
            Password
          </label>

          <input
            id="password"
            type="password"
            value={password}
            onChange={(event) =>
              setPassword(event.target.value)
            }
            placeholder="Enter your password"
            required
          />
        </div>

        <button type="submit">
          Login
        </button>
      </form>
    </section>
  );
}

export default LoginPage;