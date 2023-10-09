package com.h2owo.waterdex.controller;

import com.h2owo.waterdex.model.entity.WaterQuality;
import com.h2owo.waterdex.model.response.GeneralResponse;
import com.h2owo.waterdex.model.response.InfoResponse;
import com.h2owo.waterdex.service.IInfoService;
import com.h2owo.waterdex.util.Constants;
import com.h2owo.waterdex.util.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Info
 */
@RestController
@RequestMapping("/info")
public class InfoController {

  @Autowired
  private IInfoService infoService;

  /**
   * Get water quality and species based on a coordinate
   *
   * @param lat Latitude
   * @param lon Longitude
   * @return Response contain both data
   */
  @CrossOrigin(Urls.ORIGEN)
  @GetMapping("")
  public ResponseEntity<InfoResponse> getInfo(
          @RequestParam(name = "lat", defaultValue = "1") double lat,
          @RequestParam(name = "long", defaultValue = "10") double lon
  ) {

    WaterQuality waterQuality = infoService.getWaterQuality(lat, lon);

    InfoResponse response = InfoResponse.builder()
            .msgResponse(Constants.SUCCESS)
            .waterQuality(waterQuality)
            .build();

    return new ResponseEntity<>(
            response, HttpStatus.OK
    );
  }

  /**
   * Get the flow of the water involved
   *
   * @param lat Latitude
   * @param lon Longitude
   * @return Information on where does this water originated
   */
  @CrossOrigin(Urls.ORIGEN)
  @GetMapping("/flow")
  public ResponseEntity<GeneralResponse> getInfoFlow(
          @RequestParam(name = "lat", defaultValue = "1") double lat,
          @RequestParam(name = "long", defaultValue = "10") double lon
  ) {

    String flow = infoService.getWaterFlowInfo(lat, lon);

    GeneralResponse response = GeneralResponse.builder()
            .msgResponse(Constants.SUCCESS)
            .info(flow)
            .build();

    return new ResponseEntity<>(
            response, HttpStatus.OK
    );
  }
}
