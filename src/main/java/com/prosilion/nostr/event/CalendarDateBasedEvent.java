package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.CalendarContent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import org.springframework.lang.NonNull;

public class CalendarDateBasedEvent extends CalendarDurationBaseEvent {
  public CalendarDateBasedEvent(@NonNull Identity identity, @NonNull Relay relay, @NonNull CalendarContent calendarContent, @NonNull String title) {
    this(identity, relay, calendarContent, title, List.of(), "");
  }

  public CalendarDateBasedEvent(@NonNull Identity identity, @NonNull Relay relay, @NonNull CalendarContent calendarContent, @NonNull String title, @NonNull String content) {
    this(identity, relay, calendarContent, title, List.of(), content);
  }

  public CalendarDateBasedEvent(@NonNull Identity identity, @NonNull Relay relay, @NonNull CalendarContent calendarContent, @NonNull String title, @NonNull List<BaseTag> baseTags) {
    this(identity, relay, calendarContent, title, baseTags, "");
  }

  public CalendarDateBasedEvent(@NonNull Identity identity, @NonNull Relay relay, @NonNull CalendarContent calendarContent, @NonNull String title, @NonNull List<BaseTag> baseTags, @NonNull String content) {
    super(identity, Kind.CALENDAR_DATE_BASED_EVENT, relay, calendarContent, title, baseTags, content);
  }

  public CalendarDateBasedEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
