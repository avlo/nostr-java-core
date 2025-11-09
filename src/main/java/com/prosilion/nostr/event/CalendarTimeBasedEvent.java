package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.CalendarContent;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import org.springframework.lang.NonNull;

public class CalendarTimeBasedEvent extends CalendarDurationBaseEvent {
  public CalendarTimeBasedEvent(@NonNull Identity identity, @NonNull CalendarContent calendarContent, @NonNull String title) {
    this(identity, calendarContent, title, "");
  }

  public CalendarTimeBasedEvent(@NonNull Identity identity, @NonNull CalendarContent calendarContent, @NonNull String title, @NonNull String content) {
    this(identity, calendarContent, title, List.of(), content);
  }

  public CalendarTimeBasedEvent(@NonNull Identity identity, @NonNull CalendarContent calendarContent, @NonNull String title, @NonNull List<BaseTag> baseTags) {
    this(identity, calendarContent, title, baseTags, "");
  }

  public CalendarTimeBasedEvent(@NonNull Identity identity, @NonNull CalendarContent calendarContent, @NonNull String title, @NonNull List<BaseTag> baseTags, @NonNull String content) {
    super(identity, Kind.CALENDAR_DATE_BASED_EVENT, calendarContent, title, baseTags, content);
  }

  public CalendarTimeBasedEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
