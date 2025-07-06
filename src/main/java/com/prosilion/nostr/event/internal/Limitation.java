package com.prosilion.nostr.event.internal;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class Limitation {
  @JsonProperty("max_message_length")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private int maxMessageLength;

  @JsonProperty("max_subscriptions")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private int maxSubscriptions;

  @JsonProperty("max_filters")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private int maxFilters;

  @JsonProperty("max_limit")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private int maxLimit;

  @JsonProperty("max_subid_length")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private int maxSubIdLength;

  @JsonProperty("min_prefix")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private int minPrefix;

  @JsonProperty("max_event_tags")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private int maxEventTags;

  @JsonProperty("max_content_length")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private int maxContentLength;

  @JsonProperty("min_pow_difficulty")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private int minPowDifficulty;

  @JsonProperty("auth_required")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private boolean authRequired;

  @JsonProperty("payment_required")
  @JsonIgnoreProperties(ignoreUnknown = true)
  private boolean paymentRequired;
}
