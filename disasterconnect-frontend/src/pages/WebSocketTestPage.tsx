import { useEffect, useState } from "react";
import {
  connectWebSocket,
  disconnectWebSocket,
  sendWebSocketMessage,
} from "../services/websocketService";

function WebSocketTestPage() {
  const [message, setMessage] = useState("");
  const [input, setInput] = useState("");

  useEffect(() => {
    connectWebSocket((receivedMessage) => {
      console.log(
        "WebSocket message received:",
        receivedMessage
      );

      setMessage(receivedMessage);
    });

    return () => {
      disconnectWebSocket();
    };
  }, []);

  const handleSend = () => {
    if (!input.trim()) {
      return;
    }

    sendWebSocketMessage(input);

    setInput("");
  };

  return (
    <section>
      <h2>WebSocket Test</h2>

      <p>
        Testing real-time communication between React
        and Spring Boot.
      </p>

      <div>
        <label htmlFor="message">
          Message
        </label>

        <input
          id="message"
          type="text"
          value={input}
          onChange={(event) =>
            setInput(event.target.value)
          }
          placeholder="Enter a message"
        />

        <button
          type="button"
          onClick={handleSend}
        >
          Send Message
        </button>
      </div>

      <p>
        <strong>Received Message:</strong>{" "}
        {message || "Waiting for WebSocket message..."}
      </p>
    </section>
  );
}

export default WebSocketTestPage;