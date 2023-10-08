package com.h2owo.waterdex.util.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.h2owo.waterdex.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class INaturalist {

  public ArrayNode getPlacesID(double latitude, double longitude, String name) {

    StringBuilder response = new StringBuilder();

    try {
      // Define the base URL
      String baseUrl = "https://www.inaturalist.org/places.json";

      // Encode query parameters (optional but recommended)
      String lat = URLEncoder.encode(String.valueOf(latitude), StandardCharsets.UTF_8);
      String lon = URLEncoder.encode(String.valueOf(longitude), StandardCharsets.UTF_8);
      String placeType = URLEncoder.encode(name, StandardCharsets.UTF_8);

      // Build the complete URL with query parameters
      String completeUrl = baseUrl + "?latitude=" + lat + "&longitude=" + lon + "&place_type=" + placeType;

      // Create a URL object
      URL url = new URL(completeUrl);

      // Open a connection to the URL
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // Set the request method to GET
      connection.setRequestMethod("GET");

      // Read the response
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;

      while ((line = reader.readLine()) != null) {
        response.append(line);
      }

      // Close the connection
      reader.close();
      connection.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
    }

    ObjectMapper objectMapper = new ObjectMapper();

    ArrayNode jsonArray = null;
    try {
      // Parse the JSON data into an array of JSON objects
      jsonArray = (ArrayNode) objectMapper.readTree(String.valueOf(response));
    } catch (Exception e) {
      e.printStackTrace();
    }

    return jsonArray;
  }

  public ArrayNode getSpecies(String placeID, String type) {

    StringBuilder response = new StringBuilder();

    try {
      // Define the base URL
      String baseUrl = "https://www.inaturalist.org/observations.json";

      // Encode query parameters (optional but recommended)
      String placeId = URLEncoder.encode(placeID, StandardCharsets.UTF_8);
      String q = URLEncoder.encode(type, StandardCharsets.UTF_8);

      // Build the complete URL with query parameters
      String completeUrl = baseUrl + "?place_id=" + placeId + "&q=" + q;

      // Create a URL object
      URL url = new URL(completeUrl);

      // Open a connection to the URL
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // Set the request method to GET
      connection.setRequestMethod("GET");

      // Read the response
      BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
      String line;

      while ((line = reader.readLine()) != null) {
        response.append(line);
      }

      // Close the connection
      reader.close();
      connection.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
    }

    ObjectMapper objectMapper = new ObjectMapper();

    ArrayNode jsonArray = null;
    JsonNodeFactory factory = objectMapper.getNodeFactory();
    ArrayNode newArray = factory.arrayNode();
    Set<String> used = new HashSet<>();
    try {
      // Parse the JSON data into an array of JSON objects
      jsonArray = (ArrayNode) objectMapper.readTree(String.valueOf(response));
      // Iterate through the original array
      for (JsonNode node : jsonArray) {
        // Iterate through the array elements
        if (node.isEmpty()) {
          continue;
        }
        String name = node.get("taxon").get("name").asText();
        ObjectNode objectNode = factory.objectNode();
        if (!used.contains(name)) {
          objectNode.put("science_name", name);
          objectNode.put("name", node.get("taxon").get("common_name").get("name").asText());
          objectNode.put("square_url", node.get(Constants.PHOTOS).get(0).get("square_url").asText());
          objectNode.put("thumb_url", node.get(Constants.PHOTOS).get(0).get("thumb_url").asText());
          objectNode.put("small_url", node.get(Constants.PHOTOS).get(0).get("small_url").asText());
          objectNode.put("medium_url", node.get(Constants.PHOTOS).get(0).get("medium_url").asText());
          objectNode.put("large_url", node.get(Constants.PHOTOS).get(0).get("large_url").asText());
          used.add(name);

          // Add the ObjectNode to the new ArrayNode
          newArray.add(objectNode);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return newArray;
  }
}
