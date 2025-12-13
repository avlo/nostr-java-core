package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import org.springframework.lang.NonNull;

public class BadgeDefinitionAwardEvent extends AddressableEvent {
  public static final String MISSING_RELAY = "BadgeDefinitionAwardEvent tags [%s} is missing a RelayTag";

  public BadgeDefinitionAwardEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay) throws NostrException {
    this(identity, identifierTag, relay, "");
  }

  public BadgeDefinitionAwardEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull String content) throws NostrException {
    this(identity, identifierTag, relay, List.of(), content);
  }

  public BadgeDefinitionAwardEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.BADGE_DEFINITION_EVENT,
        identifierTag,
        relay,
        baseTags,
        content);
  }

  public Relay getRelayTagRelay() {
    return getTypeSpecificTags(RelayTag.class).stream().map(RelayTag::getRelay).findFirst().orElseThrow(() ->
        new NostrException(
            String.format(MISSING_RELAY, getTags())));
  }

  public BadgeDefinitionAwardEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
