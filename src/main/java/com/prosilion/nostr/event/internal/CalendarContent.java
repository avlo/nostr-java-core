package com.prosilion.nostr.event.internal;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.prosilion.nostr.tag.IdentifierTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CalendarContent {
  @Getter
  @JsonProperty
  private final IdentifierTag identifierTag;

  @Getter
  @JsonProperty
  private final String title;

  @Getter
  @JsonProperty
  private final Long start;

  // below fields optional

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private Long end;

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String startTzid;

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String endTzid;

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String summary;

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String image;

  @JsonProperty
  @JsonInclude(JsonInclude.Include.NON_NULL)
  private String location;

//  public CalendarContent(@NonNull IdentifierTag identifierTag, @NonNull String title, @NonNull Long start) {
//    this.identifierTag = identifierTag;
//    this.title = title;
//    this.start = start;
//  }
}
