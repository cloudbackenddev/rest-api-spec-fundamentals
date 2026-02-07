package com.example.rest;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.HttpRequest.BodyPublishers;
import java.net.http.HttpResponse.BodyHandlers;

public class HttpBinClient {

    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        try {
            System.out.println("=== GET Request ===");
            sendGetRequest();

            System.out.println("\n=== POST Request ===");
            sendPostRequest();

            System.out.println("\n=== PUT Request ===");
            sendPutRequest();

            System.out.println("\n=== DELETE Request ===");
            sendDeleteRequest();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void sendGetRequest() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/get?msg=HelloFromJava"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: \n" + response.body());
    }

    private static void sendPostRequest() throws Exception {
        String jsonInputString = "{\"username\": \"john_doe\", \"email\": \"john@example.com\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/post"))
                .header("Content-Type", "application/json")
                .POST(BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: \n" + response.body());
    }

    private static void sendPutRequest() throws Exception {
        String jsonInputString = "{\"username\": \"john_doe_updated\", \"email\": \"john_new@example.com\"}";

        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/put"))
                .header("Content-Type", "application/json")
                .PUT(BodyPublishers.ofString(jsonInputString))
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: \n" + response.body());
    }

    private static void sendDeleteRequest() throws Exception {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("https://httpbin.org/delete"))
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, BodyHandlers.ofString());

        System.out.println("Status Code: " + response.statusCode());
        System.out.println("Response Body: \n" + response.body());
    }
}
