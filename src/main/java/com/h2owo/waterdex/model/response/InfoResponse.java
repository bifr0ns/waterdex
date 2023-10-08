package com.h2owo.waterdex.model.response;

import com.h2owo.waterdex.model.entity.Species;
import com.h2owo.waterdex.model.entity.WaterQuality;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InfoResponse {
  private String msgResponse;
  private WaterQuality waterQuality;
  private Species species;
  private Species endangeredSpecies;
}