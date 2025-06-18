package com.prosilion.nostr.event.internal;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.prosilion.nostr.tag.PriceTag;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class ClassifiedListing {
  private final String title;
  private final String summary;

  @EqualsAndHashCode.Exclude
  private final Long publishedAt;
  private String location;

  @JsonProperty("price")
  private final PriceTag priceTag;

  public ClassifiedListing(@NonNull String title, @NonNull String summary, @NonNull PriceTag priceTag) {
    this.title = title;
    this.summary = summary;
    this.priceTag = priceTag;
    this.publishedAt = System.currentTimeMillis();
  }

  public ClassifiedListing(@NonNull String title, @NonNull String summary, @NonNull PriceTag priceTag, @NonNull String location) {
    this(title, summary, priceTag);
    this.location = location;
  }
}
