package com.h2owo.waterdex.util.component;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

@Component
public class Geo {
  @Autowired
  private Environment environment;

  public String reverseGeocode(double latitude, double longitude) throws IOException {
    String apiKey = environment.getProperty("keys.maps");
    String apiUrl = "https://maps.googleapis.com/maps/api/geocode/json?latlng=" + latitude + "," + longitude + "&key=" + apiKey;

    URL url = new URL(apiUrl);
    HttpURLConnection connection = (HttpURLConnection) url.openConnection();
    connection.setRequestMethod("GET");

    BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
    StringBuilder response = new StringBuilder();
    String line;

    while ((line = reader.readLine()) != null) {
      response.append(line);
    }

    reader.close();
    connection.disconnect();

    return response.toString();
  }
}
