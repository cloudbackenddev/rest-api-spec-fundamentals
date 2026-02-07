package com.example.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class SimpleRestClient {

    public static void main(String[] args) {
        HttpClient client = HttpClient.newHttpClient();
        String url = "http://localhost:8001/api/hello";

        try {
            System.out.println("--- Sending GET Request ---");
            sendGetRequest(client, url);

            System.out.println("\n--- Sending POST Request ---");
            sendPostRequest(client, url);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendGetRequest(HttpClient client, String url) throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());
        
        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
    }

    private static void sendPostRequest(HttpClient client, String url) throws Exception {
        String jsonInputString = "{\"name\": \"Java Developer\", \"tool\": \"HttpClient\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: " + response.body());
    }
}
