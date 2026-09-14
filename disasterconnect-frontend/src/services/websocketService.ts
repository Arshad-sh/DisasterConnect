import { Client, type IMessage } from "@stomp/stompjs";

const WS_URL = "ws://localhost:8080/ws";

let stompClient: Client | null = null;

export const connectWebSocket = (
  onMessage: (message: string) => void
) => {
  stompClient = new Client({
    brokerURL: WS_URL,

    reconnectDelay: 5000,

    onConnect: () => {
      console.log("WebSocket connected successfully.");

      stompClient?.subscribe("/topic/test", (message: IMessage) => {
        onMessage(message.body);
      });
    },

    onDisconnect: () => {
      console.log("WebSocket disconnected.");
    },

    onStompError: (frame) => {
      console.error(
        "WebSocket STOMP error:",
        frame.headers["message"]
      );
    },

    onWebSocketError: (error) => {
      console.error("WebSocket connection error:", error);
    },
  });

  stompClient.activate();
};

export const sendWebSocketMessage = (message: string) => {
  if (!stompClient || !stompClient.connected) {
    console.error("WebSocket is not connected.");
    return;
  }

  stompClient.publish({
    destination: "/app/test",
    body: message,
  });

  console.log("WebSocket message sent:", message);
};

export const disconnectWebSocket = () => {
  if (stompClient) {
    stompClient.deactivate();
    stompClient = null;
  }
};