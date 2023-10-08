package com.h2owo.waterdex.util.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2owo.waterdex.model.entity.Species;
import com.h2owo.waterdex.model.response.SpeciesResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

@Component
public class IUCN {
  @Autowired
  private Environment environment;

  public int getIdByName(String name) throws IOException {
    String apiUrl = "http://apiv3.iucnredlist.org/api/v3/species/" + name;
    StringBuilder response = getResponse(apiUrl);

    ObjectMapper objectMapper = new ObjectMapper();

    JsonNode jsonArray = null;
    int taxonId = 0;
    try {
      // Parse the JSON data into an array of JSON objects
      jsonArray = objectMapper.readTree(String.valueOf(response));
      if (!jsonArray.isEmpty()) {
        taxonId = jsonArray.get("result").size() > 0 ? jsonArray.get("result").get(0).get("taxonid").asInt() : 0;
      }
    } catch (Exception e) {
      e.printStackTrace();
    }

    return taxonId;
  }

  public SpeciesResponseDTO clasifySpecies(List<Species> allSpecies) throws IOException {
    List<Species> species = new ArrayList<>();
    List<Species> endangeredSpecies = new ArrayList<>();

    for (Species specie : allSpecies) {
      if (specie.getIucnId() != null) {
        String apiUrl = "http://apiv3.iucnredlist.org/api/v3/threats/species/id/" + specie.getIucnId() + "/region/global";
        StringBuilder response = getResponse(apiUrl);
        //22694938 190279
        ObjectMapper objectMapper = new ObjectMapper();

        JsonNode jsonArray = null;
        try {
          // Parse the JSON data into an array of JSON objects
          jsonArray = objectMapper.readTree(String.valueOf(response));
          if (jsonArray.get("result").size() > 0) {
            String scope = jsonArray.get("result").get(0).get("scope").asText();
            String severity = jsonArray.get("result").get(0).get("severity").asText();
            String score = jsonArray.get("result").get(0).get("score").asText();
            String invasive = jsonArray.get("result").get(0).get("invasive").asText();

            specie.setScope(scope);
            specie.setSeverity(severity);
            specie.setScore(score);
            specie.setInvasive(invasive);

            if (score.contains(":")) {
              int impactScore = Integer.parseInt(score.split(": ")[1]);
              if (impactScore >= 5) {
                endangeredSpecies.add(specie);
                continue;
              }
            }
          }
        } catch (Exception e) {
          e.printStackTrace();
        }
      }
      species.add(specie);
    }


    return SpeciesResponseDTO.builder()
            .species(species)
            .endangeredSpecies(endangeredSpecies)
            .build();
  }

  private StringBuilder getResponse(String apiUrl) throws IOException {
    String apiKey = environment.getProperty("keys.iucnredlist");

    URL url = new URL(apiUrl + "?token=" + apiKey);
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

    return response;
  }
}
