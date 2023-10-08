package com.h2owo.waterdex.service.impl;

import com.h2owo.waterdex.model.entity.WaterQuality;
import com.h2owo.waterdex.model.response.SpeciesResponse;
import com.h2owo.waterdex.service.IInfoService;
import com.h2owo.waterdex.util.Constants;
import com.h2owo.waterdex.util.component.CompletableFutures;
import com.h2owo.waterdex.util.component.Geo;
import com.h2owo.waterdex.util.component.OpenAI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class InfoService implements IInfoService {

  @Autowired
  private OpenAI openAI;

  @Autowired
  private Geo geo;

  @Autowired
  private CompletableFutures completableFutures;

  @Override
  public WaterQuality getWaterQuality(double lat, double lon) {
    StringBuilder info = completableFutures.getWaterQualityInfo(lat, lon);

    if (info.isEmpty()) {
      return WaterQuality.builder()
              .info(Constants.NOT_FOUND)
              .xml(Constants.NOT_FOUND)
              .color(Constants.NA)
              .build();
    }

    String prompt = "Can you give me a final result if the quality of water is acceptable or not and why, based on these results, just give me one final concise paragraph, and in another paragraph based on your result  give me just a color  from a scale of green to red, green been acceptable, in the form of 'Color: just color name' "
            + info;

    StringBuilder response = openAI.getResponse(Constants.GPT3, prompt);

    String contentFromResponse = openAI.getContentFromResponse(String.valueOf(response));
    String[] split = contentFromResponse.split("\n\n");
    String[] color = split[1].split(": ");

    return WaterQuality.builder()
            .info(split[0])
            .xml(String.valueOf(info))
            .color(color[1])
            .build();
  }

  @Override
  public SpeciesResponse getSpecies(double lat, double lon) {
    return null;
  }

  @Override
  public String getWaterFlowInfo(double lat, double lon) {

    String prompt = "im at Latitude: " + lat +
            " and Longitude: " + lon + ", where does the water from here originated? where does it come from?";

    StringBuilder response = openAI.getResponse(Constants.GPT3, prompt);
//    StringBuilder response = new StringBuilder("{  \"id\": \"chatcmpl-87Dfrg7B73KD8WiqxmyarzgoVNAiC\",  \"object\": \"chat.completion\",  \"created\": 1696731551,  \"model\": \"gpt-3.5-turbo-0613\",  \"choices\": [    {      \"index\": 0,      \"message\": {        \"role\": \"assistant\",        \"content\": \"The latitude and longitude coordinates provided (17.9386° N, -102.238269° W) correspond to a location in the Pacific Ocean, off the coast of Mexico. The water in this area can be traced back to various sources, such as rainfall, rivers, and ocean currents. It is difficult to pinpoint a specific origin without additional information.\"      },      \"finish_reason\": \"stop\"    }  ],  \"usage\": {    \"prompt_tokens\": 32,    \"completion_tokens\": 72,    \"total_tokens\": 104  }}");

    return openAI.getContentFromResponse(String.valueOf(response));
  }
}
