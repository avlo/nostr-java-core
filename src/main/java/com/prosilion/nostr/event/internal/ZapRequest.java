package com.prosilion.nostr.event.internal;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.prosilion.nostr.tag.RelaysTag;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class ZapRequest {
  @JsonProperty("relays")
  private RelaysTag relaysTag;

  @JsonProperty
  private Long amount;

  @JsonProperty("lnurl")
  private String lnUrl;

  public ZapRequest(@NonNull RelaysTag relaysTag, @NonNull Long amount, @NonNull String lnUrl) {
    this.relaysTag = relaysTag;
    this.amount = amount;
    this.lnUrl = lnUrl;
  }
}
