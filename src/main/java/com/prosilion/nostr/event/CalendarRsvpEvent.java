package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonValue;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.GenericTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class CalendarRsvpEvent extends AddressableEvent {
  public enum Status {
    ACCEPTED("accepted"),
    TENTATIVE("tentative"),
    DECLINED("declined");

    private final String status;

    Status(String status) {
      this.status = status;
    }

    @JsonValue
    public String getStatus() {
      return status;
    }
  }

  public enum FreeOrBusy {
    FREE("free"),
    BUSY("busy");

    private final String value;

    FreeOrBusy(String freeOrBusy) {
      this.value = freeOrBusy;
    }

    @JsonValue
    public String getValue() {
      return value;
    }
  }

  public CalendarRsvpEvent(@NonNull Identity identity, @NonNull IdentifierTag identifierTag, @NonNull AddressTag addressTag, @NonNull Status status, @NonNull String title) {
    this(identity, identifierTag, addressTag, status, title, List.of(), "");
  }

  public CalendarRsvpEvent(@NonNull Identity identity, @NonNull IdentifierTag identifierTag, @NonNull AddressTag addressTag, @NonNull Status status, @NonNull String title, @NonNull List<BaseTag> baseTags, @NonNull String content) {
    super(
        identity,
        Kind.CALENDAR_RSVP_EVENT,
        identifierTag,
        Stream.concat(
                Stream.of(validateAddressTagKind(addressTag)),
                Stream.concat(
                    Stream.concat(
                        Stream.of(GenericTag.create("title", title)),
                        Stream.of(GenericTag.create("status", status.getStatus()))),
                    baseTags.stream()))
            .toList(),
        content);
  }

  private static AddressTag validateAddressTagKind(AddressTag addressTag) {
    Optional.of(Objects.equals(addressTag.getKind(), Kind.CALENDAR_DATE_BASED_EVENT) ||
        Objects.equals(addressTag.getKind(), Kind.CALENDAR_TIME_BASED_EVENT)).orElseThrow(() ->
        new NostrException(String.format("CalendarRsvpEvent %s must be of either type %s or %s",
            addressTag.getClass().getSimpleName(),
            Kind.CALENDAR_DATE_BASED_EVENT.getName(),
            Kind.CALENDAR_TIME_BASED_EVENT.getName())));
    return addressTag;
  }

  public CalendarRsvpEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
