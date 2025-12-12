package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.CalendarContent;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.GenericTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class CalendarDurationBaseEvent extends AddressableEvent {
  public CalendarDurationBaseEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull CalendarContent calendarContent, @NonNull String title) {
    this(identity, kind, calendarContent, title, "");
  }

  public CalendarDurationBaseEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull CalendarContent calendarContent, @NonNull String title, @NonNull String content) {
    this(identity, kind, calendarContent, title, List.of(), content);
  }

  public CalendarDurationBaseEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull CalendarContent calendarContent, @NonNull String title, @NonNull List<BaseTag> baseTags) {
    this(identity, kind, calendarContent, title, baseTags, "");
  }

  public CalendarDurationBaseEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull CalendarContent calendarContent, @NonNull String title, @NonNull List<BaseTag> baseTags, @NonNull String content) {
    super(
        identity,
        kind,
        calendarContent.getIdentifierTag(),
        Stream.concat(
            Stream.of(GenericTag.create("title", title)),
            baseTags.stream()).toList(),
        content);
  }

  public CalendarDurationBaseEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
