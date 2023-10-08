package com.h2owo.waterdex.model.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Species {
  private String name;
  private String scientificName;
  private Integer iucnId;
  private String scope;
  private String severity;
  private String score;
  private String invasive;
  private String squareUrl;
  private String thumbUr;
  private String smallUrl;
  private String mediumUrl;
  private String largeUrl;
}
