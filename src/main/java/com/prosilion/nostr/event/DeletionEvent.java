package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;

public class DeletionEvent extends BaseEvent {

  public DeletionEvent(
     @NonNull Identity identity,
     @NonNull EventTag eventTag,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, List.of(eventTag), content, relay);
  }

  public DeletionEvent(
     @NonNull Identity identity,
     @NonNull List<EventTag> eventTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.DELETION,
       Stream.concat(
             eventTags.stream(),
             Stream.of(new RelayTag(relay)))
          .<List<BaseTag>>map(List::of).flatMap(Collection::stream).toList(),
       content);
  }

  public DeletionEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(validateRequiredTags(genericEventRecord, REQUIRED_TAG_TYPES));
  }

  private static final List<Class<? extends BaseTag>> REQUIRED_TAG_TYPES =
     List.of(EventTag.class, RelayTag.class);
}
