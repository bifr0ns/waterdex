package com.h2owo.waterdex.service;

import com.h2owo.waterdex.model.response.SpeciesResponseDTO;

public interface ISpeciesService {

  SpeciesResponseDTO getSpecie(String name);
}
