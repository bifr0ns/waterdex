package com.h2owo.waterdex.model.response;

import com.h2owo.waterdex.model.entity.Species;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SpeciesResponseDTO {
  private List<Species> species;
  private List<Species> endangeredSpecies;
}
