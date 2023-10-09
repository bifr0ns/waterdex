package com.h2owo.waterdex.util.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.h2owo.waterdex.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ClosestLocationFinder {

  // Radius of the Earth in kilometers
  private static final double EARTH_RADIUS_KM = 6371.0;

  public ArrayNode getCoordinates(ArrayNode jsonArray) {
    ObjectMapper objectMapper = new ObjectMapper();
    JsonNodeFactory factory = objectMapper.getNodeFactory();

    // Create a new ArrayNode to hold the extracted data
    ArrayNode newArray = factory.arrayNode();

    try {

      // Iterate through the original array
      for (JsonNode node : jsonArray) {
        // Check if the current element is an array
        if (node.isArray()) {
          // Iterate through the array elements
          for (JsonNode arrayElement : node) {
            // Check if the current element has 'id', 'latitude', and 'longitude' fields
            if (arrayElement.has("id") && arrayElement.has(Constants.LAT) && arrayElement.has(Constants.LONG)) {
              // Create a new ObjectNode with 'id', 'latitude', and 'longitude'
              ObjectNode objectNode = factory.objectNode();
              objectNode.put("id", arrayElement.get("id").asInt());
              objectNode.put(Constants.LAT, arrayElement.get(Constants.LAT).asText());
              objectNode.put(Constants.LONG, arrayElement.get(Constants.LONG).asText());

              // Add the ObjectNode to the new ArrayNode
              newArray.add(objectNode);
            }
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return newArray;
  }

  public String getClosestLocation(double lat, double lon, ArrayNode coordinates) {
    JsonNode closest = findClosestLocation(lat, lon, coordinates);

    return closest != null ? closest.get("id").asText() : "";
  }

  private static JsonNode findClosestLocation(double targetLatitude, double targetLongitude, ArrayNode coordinates) {
    if (coordinates.isEmpty()) {
      return null;
    }

    JsonNode closest = null;
    double closestDistance = Double.MAX_VALUE;

    for (JsonNode node : coordinates) {
      double distance = calculateHaversineDistance(targetLatitude, targetLongitude,
              node.get(Constants.LAT).asDouble(), node.get(Constants.LONG).asDouble());
      if (distance < closestDistance) {
        closestDistance = distance;
        closest = node;
      }
    }

    return closest;
  }

  private static double calculateHaversineDistance(double lat1, double lon1, double lat2, double lon2) {
    double dLat = Math.toRadians(lat2 - lat1);
    double dLon = Math.toRadians(lon2 - lon1);

    double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
            Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                    Math.sin(dLon / 2) * Math.sin(dLon / 2);

    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

    return EARTH_RADIUS_KM * c;
  }
}
