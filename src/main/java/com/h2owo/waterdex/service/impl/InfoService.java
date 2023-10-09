package com.h2owo.waterdex.service.impl;

import com.h2owo.waterdex.model.entity.WaterQuality;
import com.h2owo.waterdex.service.IInfoService;
import com.h2owo.waterdex.util.Constants;
import com.h2owo.waterdex.util.component.CompletableFutures;
import com.h2owo.waterdex.util.component.OpenAI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoService implements IInfoService {

  @Autowired
  private OpenAI openAI;

  @Autowired
  private CompletableFutures completableFutures;

  @Override
  public WaterQuality getWaterQuality(double lat, double lon) {
    StringBuilder info = completableFutures.getWaterQualityInfo(lat, lon);

    if (info.isEmpty()) {
      return WaterQuality.builder()
              .color(Constants.NA)
              .xml(Constants.NOT_FOUND)
              .build();
    }

    String prompt = "Can you give me a final answer in the format of 'Color: color_in_lowercase' if the quality of water is acceptable or not, " +
            "based on these results, just give a color from a scale of green to red, green been acceptable, just answer me with that. "
            + info;

    StringBuilder response = openAI.getResponse(prompt);

    String contentFromResponse = openAI.getContentFromResponse(String.valueOf(response));
    String[] color = contentFromResponse.split(": ");

    return WaterQuality.builder()
            .color(color[1])
            .xml(String.valueOf(info))
            .build();
  }

  @Override
  public String getWaterFlowInfo(double lat, double lon) {

    String prompt = "im at Latitude: " + lat +
            " and Longitude: " + lon + ", where does the water from here originated? where does it come from?";

    StringBuilder response = openAI.getResponse(prompt);

    return openAI.getContentFromResponse(String.valueOf(response));
  }
}
