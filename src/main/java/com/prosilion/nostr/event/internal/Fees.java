package com.prosilion.nostr.event.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.Data;

public class Fees {
  @JsonProperty
  @JsonIgnoreProperties(ignoreUnknown = true)
  private List<AdmissionFee> admission;

  @JsonProperty
  @JsonIgnoreProperties(ignoreUnknown = true)
  private List<PublicationFee> publication;

  @Data
  public static class AdmissionFee {

    @JsonProperty
    @JsonIgnoreProperties(ignoreUnknown = true)
    private int amount;

    @JsonProperty
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String unit;
  }

  @Data
  public static class PublicationFee {

    @JsonProperty
    @JsonIgnoreProperties(ignoreUnknown = true)
    private int amount;

    @JsonProperty
    @JsonIgnoreProperties(ignoreUnknown = true)
    private String unit;
  }
}
