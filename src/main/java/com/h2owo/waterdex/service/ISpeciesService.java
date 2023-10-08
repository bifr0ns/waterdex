package com.h2owo.waterdex.service;

import com.h2owo.waterdex.model.entity.Species;
import com.h2owo.waterdex.model.response.SpeciesResponseDTO;

public interface ISpeciesService {
  SpeciesResponseDTO getSpecies(double lat, double lon);

  Species getSpecie(String name);
}
