package com.example.rest;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;
import java.io.InputStream;

public class SimpleRestServer {

    public static void main(String[] args) throws IOException {
        int port = 8001;
        HttpServer server = HttpServer.create(new InetSocketAddress(port), 0);
        
        // Create a context for /api/hello
        server.createContext("/api/hello", new HelloHandler());
        
        // Create a default executor
        server.setExecutor(null);
        
        System.out.println("Server started on port " + port);
        server.start();
    }

    static class HelloHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            System.out.println("Received " + method + " request");

            if ("GET".equals(method)) {
                handleGet(exchange);
            } else if ("POST".equals(method)) {
                handlePost(exchange);
            } else {
                handleMethodNotAllowed(exchange);
            }
        }

        private void handleGet(HttpExchange exchange) throws IOException {
            String response = "{\"message\": \"Hello, REST World!\"}";
            sendResponse(exchange, 200, response);
        }

        private void handlePost(HttpExchange exchange) throws IOException {
            InputStream requestBody = exchange.getRequestBody();
            String body = new String(requestBody.readAllBytes(), StandardCharsets.UTF_8);
            
            // Simple echo back
            String response = "{\"received\": " + body + "}";
            sendResponse(exchange, 200, response); // Using 200 for simplicity, 201 if creating resource
        }

        private void handleMethodNotAllowed(HttpExchange exchange) throws IOException {
            String response = "{\"error\": \"Method Not Allowed\"}";
            sendResponse(exchange, 405, response);
        }

        private void sendResponse(HttpExchange exchange, int statusCode, String response) throws IOException {
            exchange.getResponseHeaders().set("Content-Type", "application/json");
            byte[] responseBytes = response.getBytes(StandardCharsets.UTF_8);
            exchange.sendResponseHeaders(statusCode, responseBytes.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(responseBytes);
            }
        }
    }
}
