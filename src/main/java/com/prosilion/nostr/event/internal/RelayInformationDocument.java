package com.prosilion.nostr.event.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.List;
import lombok.Data;

@Data
public class RelayInformationDocument {
  @JsonProperty
  private String name;

  @JsonProperty
  private String description;

  @JsonProperty
  private String pubkey;

  @JsonProperty
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String id;

  @JsonProperty
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String contact;

  @JsonProperty("supported_nips")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private List<Integer> supportedNips = new ArrayList<>();

  @JsonProperty("supported_nip_extensions")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private List<String> supportedNipExtensions = new ArrayList<>();

  @JsonProperty
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String software;

  @JsonProperty
  @JsonIgnoreProperties(ignoreUnknown = true)
  private String version;

  @JsonProperty
  @JsonIgnoreProperties(ignoreUnknown = true)
  private Limitation limitation;

  @JsonProperty("payments_url")
  private String paymentsUrl;

  @JsonProperty
  @JsonIgnoreProperties(ignoreUnknown = true)
  private Fees fees;
}
