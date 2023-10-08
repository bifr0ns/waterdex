package com.h2owo.waterdex.util.component;

import com.h2owo.waterdex.util.Constants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class Waterqualitydata {

  public StringBuilder getResponse(double latitude, double longitude, String name) {

    StringBuilder response = new StringBuilder();

    try {
      // Define the base URL
      String baseUrl = "https://www.waterqualitydata.us/data/Result/search";

      // Encode query parameters (optional but recommended)
      String lat = URLEncoder.encode(String.valueOf(latitude), StandardCharsets.UTF_8);
      String lon = URLEncoder.encode(String.valueOf(longitude), StandardCharsets.UTF_8);
      String within = URLEncoder.encode(Constants.N30, StandardCharsets.UTF_8);
      String mimeType = URLEncoder.encode(Constants.XML, StandardCharsets.UTF_8);
      String startDateLo = URLEncoder.encode(Constants.MONTH_AGO, StandardCharsets.UTF_8);
      String sorted = URLEncoder.encode(Constants.YES, StandardCharsets.UTF_8);
      String characteristicName = URLEncoder.encode(name, StandardCharsets.UTF_8);

      // Build the complete URL with query parameters
      String completeUrl = baseUrl + "?lat=" + lat + "&long=" + lon + "&within=" + within + "&mimeType=" + mimeType
              + "&startDateLo=" + startDateLo + "&sorted=" + sorted + "&characteristicName=" + characteristicName;

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

      // Find the first <Result> element
      int startIndex = response.indexOf("<Result>");
      int endIndex = response.indexOf("</Result>");

      if (startIndex != -1 && endIndex != -1) {
        // Extract the content of the first <Result> element, including its tags
        String firstResultTag = response.substring(startIndex, endIndex + "</Result>".length());

        // Print or use the first <Result> tag
        log.info("First <Result> tag:");
        log.info(firstResultTag);
        response = new StringBuilder(firstResultTag);
      } else {
        log.info("No <Result> elements found in the XML.");
        response = new StringBuilder();
      }

      // Close the connection
      reader.close();
      connection.disconnect();
    } catch (Exception e) {
      e.printStackTrace();
    }

    return response;
  }
}
