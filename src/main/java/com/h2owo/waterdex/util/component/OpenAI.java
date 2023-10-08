package com.h2owo.waterdex.util.component;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
@Slf4j
public class OpenAI {
  @Autowired
  private Environment environment;

  public StringBuilder getResponse(String model, String prompt) {

    StringBuilder response = new StringBuilder();
    try {
      // Define the API endpoint URL
      String apiUrl = "https://api.openai.com/v1/chat/completions";

      // Define your API key
      String apiKey = environment.getProperty("keys.openai");

      // Create the JSON request body
      String requestBody = "{" +
              "\"model\": \"" + model + "\"," +
              "\"messages\": [{" +
              "  \"role\": \"user\"," +
              "  \"content\": \"" + prompt + "\"" +
              "}]," +
              "\"temperature\": 0.7" +
              "}";

      // Create the URL object
      URL url = new URL(apiUrl);

      // Open a connection to the URL
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // Set the request method to POST
      connection.setRequestMethod("POST");

      // Set the request headers
      connection.setRequestProperty("Content-Type", "application/json");
      connection.setRequestProperty("Authorization", apiKey);

      // Enable input/output streams for the connection
      connection.setDoOutput(true);

      // Write the JSON request body to the connection's output stream
      try (DataOutputStream outputStream = new DataOutputStream(connection.getOutputStream())) {
        outputStream.writeBytes(requestBody);
        outputStream.flush();
      }

      // Get the response code
      int responseCode = connection.getResponseCode();
      log.info("Response Code: " + responseCode);

      // Read the response from the input stream
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }
        log.info("Response Body: " + response);
      }

      // Close the connection
      connection.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
    }
    return response;
  }

  public String getContentFromResponse(String jsonResponse) {
    JsonObject jsonObject = JsonParser.parseString(jsonResponse).getAsJsonObject();

    return jsonObject
            .getAsJsonArray("choices")
            .get(0)
            .getAsJsonObject()
            .getAsJsonObject("message")
            .get("content")
            .getAsString();
  }
}
