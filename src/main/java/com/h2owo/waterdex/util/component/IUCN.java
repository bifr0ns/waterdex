package com.h2owo.waterdex.util.component;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.h2owo.waterdex.model.entity.Species;
import com.h2owo.waterdex.model.response.SpeciesResponseDTO;
import com.h2owo.waterdex.util.Constants;
import com.h2owo.waterdex.util.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
public class IUCN {
  @Autowired
  private Environment environment;

  public int getIdByName(String name) throws IOException {
    String apiUrl = Urls.IUCN_SPECIES + name;
    StringBuilder response = getResponse(apiUrl);

    ObjectMapper objectMapper = new ObjectMapper();

    JsonNode resultNode = null;
    int taxonId = 0;
    try {
      // Parse the JSON data into an array of JSON objects
      resultNode = objectMapper.readTree(String.valueOf(response)).get(Constants.RESULT);
      taxonId = resultNode != null && resultNode.size() > 0 ? resultNode.get(0).get("taxonid").asInt() : 0;
    } catch (Exception e) {
      e.printStackTrace();
    }

    return taxonId;
  }

  public SpeciesResponseDTO clasifySpecies(List<Species> allSpecies) throws IOException {
    List<CompletableFuture<Species>> futures = allSpecies.stream()
            .map(specie -> CompletableFuture.supplyAsync(() -> processSpecies(specie))).toList();

    CompletableFuture<Void> allOf = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

    CompletableFuture<List<Species>> speciesFuture = allOf.thenApply(
            unused -> futures.stream()
                    .map(CompletableFuture::join)
                    .toList()
    );

    List<Species> species = speciesFuture.join();
    List<Species> endangeredSpecies = species.stream()
            .filter(specie -> specie.getScore() != null && specie.getScore().contains(":"))
            .filter(specie -> {
              int impactScore = Integer.parseInt(specie.getScore().split(": ")[1]);
              return impactScore >= 5;
            })
            .toList();

    return SpeciesResponseDTO.builder()
            .species(species)
            .endangeredSpecies(endangeredSpecies)
            .build();
  }

  private Species processSpecies(Species specie) {
    if (specie.getIucnId() != null) {
      String apiUrl = Urls.IUCN_SPECIES_ID + specie.getIucnId() + "/region/global";
      try {
        StringBuilder response = getResponse(apiUrl);
        ObjectMapper objectMapper = new ObjectMapper();
        JsonNode jsonArray = objectMapper.readTree(String.valueOf(response));
        if (jsonArray.get(Constants.RESULT).size() > 0) {
          String scope = jsonArray.get(Constants.RESULT).get(0).get("scope").asText();
          String severity = jsonArray.get(Constants.RESULT).get(0).get("severity").asText();
          String score = jsonArray.get(Constants.RESULT).get(0).get("score").asText();
          String invasive = jsonArray.get(Constants.RESULT).get(0).get("invasive").asText();

          specie.setScope(scope);
          specie.setSeverity(severity);
          specie.setScore(score);
          specie.setInvasive(invasive);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    return specie;
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
