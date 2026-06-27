package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.TupleDefnEventAuxAwardEventAuxNeedsAppropriateName;
import com.prosilion.nostr.tag.SetsEventTupleNeedsAppropriateNameIF;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class BadgeSetsEvent extends AddressableEvent implements TagMappedEventIF, SetsEventTupleNeedsAppropriateNameIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated BadgeSetsEvent";
  public static final String MESSAGE = "BadgeSetsEvent ctor() is missing a BadgeAwardGenericEvent parameter";
  @JsonIgnore
  private final List<TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes; // aTag/eTag combo
  @JsonIgnore
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent; // aTag

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux> tupleDefnEventAuxAwardEventAux) {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(tupleDefnEventAuxAwardEventAux), List.of(), DEFAULT_CONTENT);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes) {
    this(identity, badgeDefinitionReputationEvent, relay, tupleDefnEventAuxAwardEventAuxes, List.of(), DEFAULT_CONTENT);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull List<BaseTag> baseTags) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, tupleDefnEventAuxAwardEventAuxes, baseTags, DEFAULT_CONTENT);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux> badgeAwardGenericEventAux,
     @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, List.of(badgeAwardGenericEventAux), List.of(), content);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux>> badgeAwardGenericEventAuxes,
     @NonNull String content) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, relay, badgeAwardGenericEventAuxes, List.of(), content);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull Relay relay,
     @NonNull List<TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content) throws NostrException {
    super(
       identity,
       Kind.BADGE_SETS_EVENT,
       badgeDefinitionReputationEvent.getIdentifierTag(),
       Stream.concat(
          Stream.concat(
             TagMappedEventIF
                .throwIfEmpty(tupleDefnEventAuxAwardEventAuxes, MESSAGE)
                .flatMap(BadgeSetsEvent::badgeAwardGenericEventAsTruple),
             Stream.of(
                validateIdenticalBadgeAwardGenericEventsPublicKeys(tupleDefnEventAuxAwardEventAuxes))),
          baseTags.stream()
             .filter(Predicate.not(EventTag.class::isInstance))
             .filter(Predicate.not(PubKeyTag.class::isInstance))
             .filter(Predicate.not(AddressTag.class::isInstance))).toList(), content, relay
    );
    this.tupleDefnEventAuxAwardEventAuxes = tupleDefnEventAuxAwardEventAuxes;
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
  }

  public BadgeSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux> tupleDefnEventAuxAwardEventAux,
     @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
    this(genericEventRecord, List.of(tupleDefnEventAuxAwardEventAux), fxnAddressTag);
  }

  public BadgeSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
    super(genericEventRecord);
    this.tupleDefnEventAuxAwardEventAuxes = tupleDefnEventAuxAwardEventAuxes;
    this.badgeDefinitionReputationEvent = mapTagsToEvents(this, fxnAddressTag, AddressTag.class).getFirst();
  }

  @JsonIgnore
  public final List<EventTag> getEventTags() {
    return tupleDefnEventAuxAwardEventAuxes.stream()
       .map(BadgeSetsEvent::badgeAwardGenericEventAsEventTag).toList();
  }

  @JsonIgnore
  public final List<AddressTag> getAddressTags() {
    return tupleDefnEventAuxAwardEventAuxes.stream()
       .map(TupleDefnEventAuxAwardEventAuxNeedsAppropriateName::getDefinitionEventAsAddressTag).toList();
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return tupleDefnEventAuxAwardEventAuxes.getFirst().getAwardRecipientPublicKey();
  }

  public static EventTag badgeAwardGenericEventAsEventTag(@NonNull TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux> tupleDefnEventAuxAwardEventAux) {
    return new EventTag(
       tupleDefnEventAuxAwardEventAux.getAwardEventId(),
       tupleDefnEventAuxAwardEventAux.getAwardEventRelay()
          .map(Relay::getUrl).orElse(null));
  }

  private static Stream<BaseTag> badgeAwardGenericEventAsTruple(@NonNull TupleDefnEventAuxAwardEventAuxNeedsAppropriateName<BadgeAwardGenericEventAux> tupleDefnEventAuxAwardEventAux) {
    return Stream.of(
       tupleDefnEventAuxAwardEventAux.getTupleATagETag().getLeft(),
       tupleDefnEventAuxAwardEventAux.getTupleATagETag().getRight());
  }

  @Override
  public String getIdEvent() {
    return super.getId();
  }

  @Override
  public Optional<Relay> findRelay() {
    return super.getRelay();
  }
}
