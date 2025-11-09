package com.prosilion.nostr.event.internal;

import com.prosilion.nostr.tag.IdentifierTag;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
public class CalendarContent {
  @Getter
  private final IdentifierTag identifierTag;
  @Getter
  private final String title;
  @Getter
  private final Long start;

  // below fields optional
  private Long end;
  private String startTzid;
  private String endTzid;
  private String summary;
  private String image;
  private String location;

//  public CalendarContent(@NonNull IdentifierTag identifierTag, @NonNull String title, @NonNull Long start) {
//    this.identifierTag = identifierTag;
//    this.title = title;
//    this.start = start;
//  }
}
