package com.h2owo.waterdex.controller;

import com.h2owo.waterdex.model.response.GeneralResponse;
import com.h2owo.waterdex.model.response.SpeciesResponse;
import com.h2owo.waterdex.model.response.SpeciesResponseDTO;
import com.h2owo.waterdex.service.ISpeciesService;
import com.h2owo.waterdex.util.Constants;
import com.h2owo.waterdex.util.Urls;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * Controller for Species
 */
@RestController
@RequestMapping("/species")
public class SpeciesController {

  @Autowired
  private ISpeciesService speciesService;

  @CrossOrigin(Urls.ORIGEN)
  @GetMapping("")
  public ResponseEntity<SpeciesResponse> getSpecies(
          @RequestParam(name = "lat", defaultValue = "1") double lat,
          @RequestParam(name = "long", defaultValue = "10") double lon
  ) {

    SpeciesResponseDTO speciesResponse = speciesService.getSpecies(lat, lon);

    SpeciesResponse response = SpeciesResponse.builder()
            .msgResponse(Constants.SUCCESS)
            .species(speciesResponse.getSpecies())
            .endangeredSpecies(speciesResponse.getEndangeredSpecies())
            .build();

    return new ResponseEntity<>(
            response, HttpStatus.OK
    );
  }

  @CrossOrigin(Urls.ORIGEN)
  @GetMapping("/{name}")
  public ResponseEntity<GeneralResponse> getSpecie(
          @PathVariable String name
  ) {

    String info = speciesService.getSpecie(name);

    GeneralResponse response = GeneralResponse.builder()
            .msgResponse(Constants.SUCCESS)
            .info(info)
            .build();

    return new ResponseEntity<>(
            response, HttpStatus.OK
    );
  }
}
