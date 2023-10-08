package com.h2owo.waterdex.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.h2owo.waterdex.model.entity.Species;
import com.h2owo.waterdex.model.response.SpeciesResponseDTO;
import com.h2owo.waterdex.service.ISpeciesService;
import com.h2owo.waterdex.util.Constants;
import com.h2owo.waterdex.util.component.ClosestLocationFinder;
import com.h2owo.waterdex.util.component.CompletableFutures;
import com.h2owo.waterdex.util.component.IUCN;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
@Slf4j
public class SpeciesService implements ISpeciesService {
  @Autowired
  private CompletableFutures completableFutures;

  @Autowired
  private ClosestLocationFinder closestLocationFinder;

  @Autowired
  private IUCN iucn;

  @Override
  public SpeciesResponseDTO getSpecies(double lat, double lon) {
    ArrayNode placesIDFromCoordinates = completableFutures.getPlacesIDFromCoordinates(lat, lon);

    ArrayNode coordinates = closestLocationFinder.getCoordinates(placesIDFromCoordinates);

    String closestLocationID = closestLocationFinder.getClosestLocation(lat, lon, coordinates);

    ArrayNode speciesJson = completableFutures.getSpeciesFromID(closestLocationID);

    List<Species> allSpecies = getAllSpecies(speciesJson);

    try {
      return iucn.clasifySpecies(allSpecies);
    } catch (IOException e) {
      throw new RuntimeException(e);
    }
  }

  private List<Species> getAllSpecies(ArrayNode speciesJson) {
    List<Species> allSpecies = new ArrayList<>();

    // Iterate through the original array
    for (JsonNode node : speciesJson) {
      // Iterate through the array elements
      for (JsonNode arrayElement : node) {
        if (arrayElement.has(Constants.SCIENCE_NAME)) {

          try {
            int iucnId = iucn.getIdByName(arrayElement.get(Constants.SCIENCE_NAME).asText());
            Species specie = Species.builder()
                    .name(arrayElement.get("name").asText())
                    .scientificName(arrayElement.get(Constants.SCIENCE_NAME).asText())
                    .iucnId(iucnId != 0 ? iucnId : null)
                    .squareUrl(arrayElement.get("square_url").asText())
                    .thumbUr(arrayElement.get("thumb_url").asText())
                    .smallUrl(arrayElement.get("small_url").asText())
                    .mediumUrl(arrayElement.get("medium_url").asText())
                    .largeUrl(arrayElement.get("large_url").asText())
                    .build();
            allSpecies.add(specie);
          } catch (IOException e) {
            throw new RuntimeException(e);
          }
        }
      }
    }

    return allSpecies;
  }

  @Override
  public Species getSpecie(String name) {
    return null;
  }
}
