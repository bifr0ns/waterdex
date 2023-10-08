package com.h2owo.waterdex.service;

import com.h2owo.waterdex.model.entity.WaterQuality;
import com.h2owo.waterdex.model.response.SpeciesResponse;

public interface IInfoService {

  WaterQuality getWaterQuality(double lat, double lon);

  SpeciesResponse getSpecies(double lat, double lon);

  String getWaterFlowInfo(double lat, double lon);
}
