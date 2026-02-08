package com.example.rest;

import com.example.rest.model.Session;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class ConferenceServer {

    // In-memory storage for sessions
    private static final Map<String, Session> sessions = new ConcurrentHashMap<>();

    public static void main(String[] args) throws IOException {
        // Pre-populate with some data
        Session s1 = new Session(UUID.randomUUID().toString(), "REST API Fundamentals", "Alice", "10:00", 60);
        sessions.put(s1.getId(), s1);

        int port = 8080;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);

        // TODO: Create a context for /sessions and set the handler
        // server.createContext("/sessions", ...);

        server.setExecutor(null); // default executor
        System.out.println("Conference Server started on port " + port);
        server.start();
    }

    static class SessionHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            // TODO: Implement the routing logic
            // 1. Get request method (GET, POST, DELETE, etc.)
            // 2. Get request path
            // 3. route to appropriate methods: handleListSessions, handleCreateSession,
            // handleGetSession, etc.

            String method = exchange.getRequestMethod();
            String path = exchange.getRequestURI().getPath();

            System.out.println("Received " + method + " " + path);

            // Placeholder: currently returns 404 for everything
            // TODO: Send a 404 response
            // exchange.sendResponseHeaders(404, -1);
        }

        private void handleListSessions(HttpExchange exchange) throws IOException {
            // TODO: Implement logic to list all sessions from the 'sessions' map
            // Return 200 OK with JSON array
        }

        private void handleGetSession(HttpExchange exchange, String id) throws IOException {
            // TODO: Implement logic to get a specific session by ID
            // Return 200 OK with JSON object or 404 Not Found
        }

        private void handleCreateSession(HttpExchange exchange) throws IOException {
            // TODO: Implement logic to create a new session
            // 1. Read request body
            // 2. Parse JSON (manually or use a simple helper)
            // 3. Create Session object
            // 4. Add to 'sessions' map
            // 5. Return 201 Created
        }

        private void handleDeleteSession(HttpExchange exchange, String id) throws IOException {
            // TODO: Implement logic to delete a session
            // Return 204 No Content or 404 Not Found
        }
    }
}
