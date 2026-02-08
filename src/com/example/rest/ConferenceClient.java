package com.example.rest;

import java.net.http.HttpClient;

public class ConferenceClient {

    private static final String BASE_URL = "http://localhost:8080/sessions";
    private static final HttpClient client = HttpClient.newHttpClient();

    public static void main(String[] args) {
        try {
            System.out.println("--- Starting Conference Client ---");

            // TODO: Implement the following steps:

            // 1. List all sessions (Initial state)
            // listSessions();

            // 2. Create a new session
            // String newSessionJson = "{\"title\": \"Microservices\", \"speaker\":
            // \"Charlie\", \"time\": \"14:00\", \"duration\": 90}";
            // createSession(newSessionJson);

            // 3. List sessions again to verify creation
            // listSessions();

            // 4. Get a specific session by ID (You might need to hardcode an ID or extract
            // it)
            // getSession("some-uuid");

            // 5. Delete a session
            // deleteSession("some-uuid");

            // 6. Verify deletion
            // getSession("some-uuid");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void listSessions() throws Exception {
        // TODO: Send GET request to BASE_URL and print response code & body
    }

    private static void createSession(String json) throws Exception {
        // TODO: Send POST request to BASE_URL with json body and print response code &
        // body
    }

    private static void getSession(String id) throws Exception {
        // TODO: Send GET request to BASE_URL + "/" + id and print response code & body
    }

    private static void deleteSession(String id) throws Exception {
        // TODO: Send DELETE request to BASE_URL + "/" + id and print response code
    }
}
