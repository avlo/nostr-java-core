//package com.prosilion.nostr.event;
//
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.prosilion.nostr.NostrException;
//import com.prosilion.nostr.enums.Kind;
//import com.prosilion.nostr.event.internal.BadgeAwardGenericEventTruple;
//import com.prosilion.nostr.event.internal.Relay;
//import com.prosilion.nostr.tag.AddressTag;
//import com.prosilion.nostr.tag.BaseTag;
//import com.prosilion.nostr.tag.EventTag;
//import com.prosilion.nostr.tag.IdentifierTag;
//import com.prosilion.nostr.tag.PubKeyTag;
//import com.prosilion.nostr.tag.RelayTag;
//import com.prosilion.nostr.user.Identity;
//import com.prosilion.nostr.user.PublicKey;
//import java.util.List;
//import java.util.Optional;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import java.util.stream.Collectors;
//import java.util.stream.Stream;
//import lombok.Getter;
//import lombok.NonNull;
//
//@Getter
//public class BadgeSetsEvent extends AddressableEvent implements TagMappedEventIF {
//  public static final String DEFAULT_IDENTIFIER = "PROSILION_FOLLOW_SETS_EVENT";
//  public static final IdentifierTag defaultIdentifierTag = new IdentifierTag(DEFAULT_IDENTIFIER);
//
//  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";
//  public static final String MESSAGE = "FollowSetsEvent ctor() is missing a BadgeAwardGenericEvent parameter";
//  public static final String PUBKEYS_MUST_MATCH =
//     "FollowSetsEvent BadgeAwardGenericEvents PublicKeys must all match, but instead contained [%s] different keys:\n  [%s]";
//  @JsonIgnore
//  private final List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents; // eTags
//  @JsonIgnore
//  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent; // aTag
//
//  public BadgeSetsEvent(
//     @NonNull Identity identity,
//     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
//     @NonNull Relay relay,
//     @NonNull BadgeAwardGenericEventTruple badgeAwardGenericEventTruple) {
//    this(identity, badgeDefinitionReputationEvent, relay, List.of(badgeAwardGenericEventTruple), List.of(), DEFAULT_CONTENT);
//  }
//
//  public BadgeSetsEvent(
//     @NonNull Identity identity,
//     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
//     @NonNull Relay relay,
//     @NonNull List<BadgeAwardGenericEventTruple> badgeAwardGenericEventTruple) {
//    this(identity, badgeDefinitionReputationEvent, relay, badgeAwardGenericEventTruple, List.of(), DEFAULT_CONTENT);
//  }
//
//  public BadgeSetsEvent(
//     @NonNull Identity identity,
//     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
//     @NonNull Relay relay,
//     @NonNull List<BadgeAwardGenericEventTruple> badgeAwardGenericEventTruple,
//     @NonNull List<BaseTag> baseTags) throws NostrException {
//    this(identity, badgeDefinitionReputationEvent, relay, badgeAwardGenericEventTruple, baseTags, DEFAULT_CONTENT);
//  }
//
//  public BadgeSetsEvent(
//     @NonNull Identity identity,
//     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
//     @NonNull Relay relay,
//     @NonNull BadgeAwardGenericEventTruple badgeAwardGenericEventTruple,
//     @NonNull String content) throws NostrException {
//    this(identity, badgeDefinitionReputationEvent, relay, List.of(badgeAwardGenericEventTruple), List.of(), content);
//  }
//
//  public BadgeSetsEvent(
//     @NonNull Identity identity,
//     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
//     @NonNull Relay relay,
//     @NonNull List<BadgeAwardGenericEventTruple> badgeAwardGenericEventTruples,
//     @NonNull String content) throws NostrException {
//    this(identity, badgeDefinitionReputationEvent, relay, badgeAwardGenericEventTruples, List.of(), content);
//  }
//
//  public BadgeSetsEvent(
//     @NonNull Identity identity,
//     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
//     @NonNull Relay relay,
//     @NonNull List<BadgeAwardGenericEventTruple> badgeAwardGenericEventTruples,
//     @NonNull List<BaseTag> baseTags,
//     @NonNull String content) throws NostrException {
//    super(
//       identity,
//       Kind.BADGE_SETS_EVENT,
//       defaultIdentifierTag,
//       relay,
//       Stream.concat(
//          Stream.concat(
//             TagMappedEventIF
//                .throwIfEmpty(badgeAwardGenericEventTruples, MESSAGE)
//                .flatMap(BadgeSetsEvent::badgeAwardGenericEventAsTruple),
//             Stream.of(
//                validateIdenticalBadgeAwardGenericEventsPublicKeys(badgeAwardGenericEventTruples))),
//          baseTags.stream()
//             .filter(Predicate.not(EventTag.class::isInstance))
//             .filter(Predicate.not(PubKeyTag.class::isInstance))
//             .filter(Predicate.not(AddressTag.class::isInstance))),
//       content);
//    this.badgeAwardGenericEvents = badgeAwardGenericEventTruples.stream().map(BadgeAwardGenericEventTruple::getBadgeAwardGenericEvent).toList();
//    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
//  }
//
//  public BadgeSetsEvent(
//     @NonNull GenericEventRecord genericEventRecord,
//     @NonNull Function<EventTag, BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> fxnEventTag,
//     @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
//    super(genericEventRecord);
//    this.badgeAwardGenericEvents = mapTagsToEvents(this, fxnEventTag, EventTag.class);
//    this.badgeDefinitionReputationEvent = mapTagsToEvents(this, fxnAddressTag, AddressTag.class).getFirst();
//  }
//
//  @JsonIgnore
//  public final List<EventTag> getEventTags() {
//    return badgeAwardGenericEvents.stream()
//       .map(BadgeSetsEvent::badgeAwardGenericEventAsEventTag)
//       .toList();
//  }
//
//  @JsonIgnore
//  public final AddressTag getAddressTag() {
//    return requireFirstTag(AddressTag.class);
//  }
//
//  @JsonIgnore
//  public final PublicKey getAwardRecipientPulicKey() {
//    return requireFirstTag(PubKeyTag.class).publicKey();
//  }
//
//  public static EventTag badgeAwardGenericEventAsEventTag(@NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent) {
//    return new EventTag(
//       badgeAwardGenericEvent.getId(),
//       Optional.ofNullable(
//             badgeAwardGenericEvent.getAddressTag().getRelay())
//          .orElseThrow(() ->
//             new NostrException(
//                String.format(
//                   "FollowSetsEvent BadgeAwardGenericEvent / AddressTag [%s] missing relay url", badgeAwardGenericEvent)))
//          .getUrl());
//  }
//
//  private static Stream<BaseTag> badgeAwardGenericEventAsTruple(@NonNull BadgeAwardGenericEventTruple badgeAwardGenericEventTruple) {
//    return Stream.of(
//       new AddressTag(
//          badgeAwardGenericEventTruple.getBadgeAwardGenericEvent().getAddressTag().getKind(),
//          badgeAwardGenericEventTruple.getBadgeAwardGenericEvent().getAddressTag().getPublicKey(),
//          badgeAwardGenericEventTruple.getBadgeAwardGenericEvent().getAddressTag().getIdentifierTag(),
//          badgeAwardGenericEventTruple.getBadgeAwardGenericEvent().getAddressTag().findRelay()
//             .or(badgeAwardGenericEventTruple::findAddressTagRelay)
//             .orElse(null)),
//       new EventTag(
//          badgeAwardGenericEventTruple.getBadgeAwardGenericEvent().getId(),
//          badgeAwardGenericEventTruple.getBadgeAwardGenericEvent()
//             .findRelayTag()
//             .map(RelayTag::getRelay)
//             .map(Relay::getUrl)
//             .or(() ->
//                badgeAwardGenericEventTruple.findEventTagRelay()
//                   .map(Relay::getUrl))
//             .orElse(null)));
//  }
//
//  public static PubKeyTag validateIdenticalBadgeAwardGenericEventsPublicKeys(@NonNull List<BadgeAwardGenericEventTruple> badgeAwardGenericEventTruples) {
//    List<PublicKey> distinctPublicKeys = badgeAwardGenericEventTruples.stream().map(BadgeAwardGenericEventTruple::getBadgeAwardGenericEvent)
//       .map(BaseEvent::getPublicKey).distinct().toList();
//    if (distinctPublicKeys.size() != 1)
//      throw new NostrException(
//         String.format(
//            PUBKEYS_MUST_MATCH,
//            distinctPublicKeys.size(),
//            distinctPublicKeys.stream().map(PublicKey::toHexString).collect(Collectors.joining("],\n  ["))));
//    return new PubKeyTag(distinctPublicKeys.getFirst());
//  }
//}
