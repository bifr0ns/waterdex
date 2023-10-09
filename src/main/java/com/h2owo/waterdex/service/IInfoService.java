package com.h2owo.waterdex.service;

import com.h2owo.waterdex.model.entity.WaterQuality;

public interface IInfoService {

  WaterQuality getWaterQuality(double lat, double lon);

  String getWaterFlowInfo(double lat, double lon);
}
