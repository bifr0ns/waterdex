package com.h2owo.waterdex.model.response;

import com.h2owo.waterdex.model.entity.Species;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpeciesResponse {
  private Species species;
  private Species endangeredSpecies;
}
