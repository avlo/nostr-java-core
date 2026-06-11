package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.NonNull;

public abstract class BadgeAwardAbstractEvent<T extends AddressableEvent> extends UniqueAddressTagEvent<T> {
  public BadgeAwardAbstractEvent(
     @NonNull Identity identity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull Relay relay,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull String content) throws NostrException {
    this(identity, awardRecipientPublicKey, relay, badgeDefinitionGenericEvent, List.of(), content);
  }

  public BadgeAwardAbstractEvent(
     @NonNull Identity identity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull Relay relay,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull List<BaseTag> tags,
     @NonNull String content) throws NostrException {
    this(identity, awardRecipientPublicKey, relay, badgeDefinitionGenericEvent, tags.stream(), content);
  }

  public BadgeAwardAbstractEvent(
     @NonNull Identity identity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull Relay relay,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull Stream<BaseTag> tags,
     @NonNull String content) throws NostrException {
    super(
       identity,
       Kind.BADGE_AWARD_EVENT,
       badgeDefinitionGenericEvent,
       Stream.concat(
             Stream.concat(
                Stream.of(new PubKeyTag(awardRecipientPublicKey)),
                Stream.of(new RelayTag(relay))),
             tags
                .filter(Predicate.not(AddressTag.class::isInstance))
                .filter(Predicate.not(PubKeyTag.class::isInstance))
                .filter(Predicate.not(RelayTag.class::isInstance)))
          .toList(),
       content);
  }

  public BadgeAwardAbstractEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull Function<AddressTag, T> fxn) {
    super(genericEventRecord, fxn);
  }

  @Override
  @JsonIgnore
  public final Relay getRelay() {
    return requireRelayTag().requireRelay();
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return requireFirstTag(PubKeyTag.class).getPublicKey();
  }

  @JsonIgnore
  public final T getBadgeDefinitionEvent() {
    return getAddressableEvent();
  }
}
