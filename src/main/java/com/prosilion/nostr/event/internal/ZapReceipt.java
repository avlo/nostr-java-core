package com.prosilion.nostr.event.internal;


import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public class ZapReceipt {
  @JsonProperty
  private final String bolt11;

  @JsonProperty
  private final String descriptionSha256;

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String preimage;

  public ZapReceipt(@NonNull String bolt11, @NonNull String descriptionSha256, String preimage) {
    this.descriptionSha256 = descriptionSha256;
    this.bolt11 = bolt11;
    this.preimage = preimage;
  }

  public ZapReceipt(@NonNull String bolt11, @NonNull String descriptionSha256) {
    this(bolt11, descriptionSha256, null);
  }
}
