package com.h2owo.waterdex.util.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.h2owo.waterdex.util.Constants;
import com.h2owo.waterdex.util.Urls;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
@Slf4j
public class INaturalist {

  public ArrayNode getPlacesID(double latitude, double longitude, String name) {

    StringBuilder response = new StringBuilder();

    try {
      // Define the base URL
      String baseUrl = Urls.INATURALISTPLACES;

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
    try {
      // Define the base URL
      String baseUrl = Urls.INATURALISTOBSERVATIONS;

      // Encode query parameters (optional but recommended)
      String placeId = URLEncoder.encode(placeID, StandardCharsets.UTF_8);
      String q = URLEncoder.encode(type, StandardCharsets.UTF_8);

      // Build the complete URL with query parameters
      String completeUrl = baseUrl + "?place_id=" + placeId + "&q=" + q + "&per_page=" + 5;

      // Create a URL object
      URL url = new URL(completeUrl);

      // Open a connection to the URL
      HttpURLConnection connection = (HttpURLConnection) url.openConnection();

      // Set the request method to GET
      connection.setRequestMethod("GET");

      // Read the response
      StringBuilder response = new StringBuilder();
      try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
        String line;
        while ((line = reader.readLine()) != null) {
          response.append(line);
        }
      }

      // Close the connection
      connection.disconnect();

      ObjectMapper objectMapper = new ObjectMapper();
      ArrayNode jsonArray = (ArrayNode) objectMapper.readTree(response.toString());

      Map<String, ObjectNode> speciesMap = new LinkedHashMap<>();

      for (JsonNode node : jsonArray) {
        if (node == null || node.get(Constants.TAXON) == null) {
          continue;
        }
        JsonNode taxonNode = node.get(Constants.TAXON);
        String name = taxonNode.get("name") != null ? taxonNode.get("name").asText() : null;
        if (name != null && !speciesMap.containsKey(name) && node.get(Constants.TAXON).get("common_name").get("name") != null) {
          ObjectNode objectNode = objectMapper.createObjectNode();
          objectNode.put("science_name", name);
          objectNode.put("name", node.get(Constants.TAXON).get("common_name").get("name").asText());

          JsonNode photosNode = node.get(Constants.PHOTOS);
          objectNode.put(Constants.SMALLURL, photosNode.get(0) != null && photosNode.get(0).get(Constants.SMALLURL) != null
                  ? photosNode.get(0).get(Constants.SMALLURL).asText() : "");
          objectNode.put(Constants.THUMBURL, photosNode.get(0) != null && photosNode.get(0).get(Constants.THUMBURL) != null
                  ? photosNode.get(0).get(Constants.THUMBURL).asText() : "");
          objectNode.put(Constants.SMALLURL, photosNode.get(0) != null && photosNode.get(0).get(Constants.SMALLURL) != null
                  ? photosNode.get(0).get(Constants.SMALLURL).asText() : "");
          objectNode.put(Constants.MEDIUMURL, photosNode.get(0) != null && photosNode.get(0).get(Constants.MEDIUMURL) != null
                  ? photosNode.get(0).get(Constants.MEDIUMURL).asText() : "");
          objectNode.put(Constants.LARGEURL, photosNode.get(0) != null && photosNode.get(0).get(Constants.LARGEURL) != null
                  ? photosNode.get(0).get(Constants.LARGEURL).asText() : "");

          speciesMap.put(name, objectNode);
        }
      }

      // Create a new ArrayNode and add the unique species
      ArrayNode newArray = objectMapper.createArrayNode();
      speciesMap.values().forEach(newArray::add);

      return newArray;
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

}
