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
public class SpeciesResponse {
  private String msgResponse;
  private List<Species> species;
  private List<Species> endangeredSpecies;
}
