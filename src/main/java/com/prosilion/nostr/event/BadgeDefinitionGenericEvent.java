package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;

public class BadgeDefinitionGenericEvent extends AddressableEvent {
  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag) throws NostrException {
    this(identity, identifierTag, "");
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull Relay relay) throws NostrException {
    this(identity, identifierTag, "", relay);
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull String content) throws NostrException {
    this(identity, identifierTag, Stream.of(), content);
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, identifierTag, List.of(), content, relay);
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, identifierTag, baseTags.stream(), content, relay);
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull Stream<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(identity, Kind.BADGE_DEFINITION_EVENT, identifierTag,
       prependExtraRelayTagStream(baseTags, relay), content);
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull Stream<BaseTag> baseTags,
     @NonNull String content) throws NostrException {
    super(identity, Kind.BADGE_DEFINITION_EVENT, identifierTag, baseTags, content);
  }

  public BadgeDefinitionGenericEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
