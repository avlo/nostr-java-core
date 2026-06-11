package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.NonNull;

/**
 * AddressableEvent def'n: an event containing, minimally:
 * 1 (of 2): a single IdentifierTag (UUID)
 * 2 (of 2): a single RelayTag (URL)
 * <p>
 * such that it may be referred to by other events via:
 * ["a", "KIND:EVENT_CREATOR_PUBKEY:UUID", "URL"]
 */
public class AddressableEvent extends BaseEvent {
  public AddressableEvent(
     @NonNull Identity identity,
     @NonNull Kind kind,
     @NonNull IdentifierTag identifierTag,
     @NonNull Relay relay,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content) throws NostrException {
    this(identity, kind, identifierTag, relay, baseTags.stream(), content);
  }

  public AddressableEvent(
     @NonNull Identity identity,
     @NonNull Kind kind,
     @NonNull IdentifierTag identifierTag,
     @NonNull Relay relay,
     @NonNull Stream<BaseTag> baseTags,
     @NonNull String content) throws NostrException {
    super(
       identity,
       validateKind(kind, intPredicate, errorMessage),
       Stream.concat(
          Stream.concat(
             Stream.of(identifierTag),
             Stream.of(new RelayTag(relay))),
          baseTags
             .filter(Predicate.not(RelayTag.class::isInstance))
             .filter(Predicate.not(IdentifierTag.class::isInstance))),
       content);
  }

  public AddressableEvent(@NonNull GenericEventRecord genericEventRecord) throws NostrException {
    super(validateIdentifierTagRelayTag(genericEventRecord));
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
       getRelay());
  }

  @JsonIgnore
  public final Relay getRelay() {
    return requireRelayTag().requireRelay();
  }

  private static final IntPredicate intPredicate = kindValue -> !(30_000 > kindValue || kindValue > 40_000);
  private static final Function<Kind, String> errorMessage = kind -> String.format("Intended AddressableEvent invalid kind [%s] value [%s] is not between 30000 and 40000", kind, kind.getValue());

  private static final String MISSING_TAG = "ctor() genericEventRecord parameter:\n%s\nis missing required [%s]";
  private static final String MULTIPLE_TAG = "ctor() genericEventRecord parameter:\n%s\nhas multiple [%s]";

  public static GenericEventRecord validateIdentifierTagRelayTag(@NonNull GenericEventRecord genericEventRecord) {
    List<IdentifierTag> identifierTags = genericEventRecord.getTypeSpecificTags(IdentifierTag.class);
    if (identifierTags.isEmpty()) throw exceptionMessage(MISSING_TAG, genericEventRecord, "IdentifierTag");
    if (identifierTags.size() > 1) throw exceptionMessage(MULTIPLE_TAG, genericEventRecord, "IdentifierTag");

    List<RelayTag> relayTags = genericEventRecord.getTypeSpecificTags(RelayTag.class);
    if (relayTags.isEmpty()) throw exceptionMessage(MISSING_TAG, genericEventRecord, "RelayTag");
    if (relayTags.size() > 1) throw exceptionMessage(MULTIPLE_TAG, genericEventRecord, "RelayTag");

    return genericEventRecord;
  }

  private static NostrException exceptionMessage(String s, GenericEventRecord ger, String tag) {
    return new NostrException(String.format(s, ger.createPrettyPrintJson(), tag));
  }
}
