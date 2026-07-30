package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.jspecify.annotations.NonNull;

/**
 * AddressableEvent def'n: an event containing, minimally:
 * 1 (of 2): a single IdentifierTag (UUID)
 * 2 (of 2): a single RelayTag (URL)
 * <p>
 * such that it may be referred to by other events via:
 * ["a", "KIND:EVENT_CREATOR_PUBKEY:UUID", "URL"]
 */
public class AddressableEvent extends BaseEvent {
  public static final String MISSING_REFERENCE_TAG = "CuratedBadgeDefinitionGenericEvent requires a ReferenceTag (URL) associated with BadgeDefinitionGenericEvent";

  public AddressableEvent(
     @NonNull Identity identity,
     @NonNull Kind kind,
     @NonNull IdentifierTag identifierTag,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     Relay... relay) throws NostrException {
    this(identity, kind, identifierTag, baseTags.stream(), content, relay);
  }

  public AddressableEvent(
     @NonNull Identity identity,
     @NonNull Kind kind,
     @NonNull IdentifierTag identifierTag,
     @NonNull Stream<BaseTag> baseTags,
     @NonNull String content,
     Relay... relay) throws NostrException {
    super(
       identity,
       validateKind(kind, addressableKindPredicate, errorMessage),
       Stream.concat(
          Stream.of(identifierTag),
          useFirstRelayTag(
             prependVariadRelayTagStream(baseTags, relay))
             .filter(Predicate.not(IdentifierTag.class::isInstance))),
       content);
  }

  public AddressableEvent(@NonNull GenericEventRecord genericEventRecord) throws NostrException {
    super(
       validateRequiredTags(
          validateKind(
             genericEventRecord, addressableKindPredicate, errorMessage),
          List.of(IdentifierTag.class)));
  }

  @JsonIgnore
  public List<EventTag> getEventTags() {
    return getTypeSpecificTags(EventTag.class);
  }

  @JsonIgnore
  public final IdentifierTag getIdentifierTag() {
    return requireFirstTag(IdentifierTag.class);
  }

  @JsonIgnore
  public final AddressTag asAddressableEventAddressTag() {
    return new AddressTag(
       getKind(),
       getPublicKey(),
       getIdentifierTag(),
       getRelay().orElse(null));
  }

  @JsonIgnore
  public final Optional<Relay> getRelay() {
    return getRelayTag().map(RelayTag::getRelay);
  }

  private static final IntPredicate addressableKindPredicate = kindValue -> !(30_000 > kindValue || kindValue > 40_000);
  private static final Function<Kind, String> errorMessage = kind -> String.format("Intended AddressableEvent invalid kind [%s] value [%s] is not between 30000 and 40000", kind, kind.getValue());
}
