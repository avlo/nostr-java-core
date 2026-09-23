package com.prosilion.nostr.event.curated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.AddressableEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;

public class BadgeSetsEvent extends AddressableEvent {
  public static final String DEFAULT_CONTENT = "AfterImage generated BadgeSetsEvent";

  @Getter
  @JsonIgnore
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  @JsonIgnore
  private final List<CuratedBadgeAwardCanonicalEvent> curatedBadgeAwardCanonicalEventList;

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull CuratedBadgeAwardCanonicalEvent curatedBadgeAwardCanonicalEvent,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(curatedBadgeAwardCanonicalEvent), List.of(), DEFAULT_CONTENT, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<CuratedBadgeAwardCanonicalEvent> curatedBadgeAwardCanonicalEventList,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, curatedBadgeAwardCanonicalEventList, List.of(), DEFAULT_CONTENT, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull CuratedBadgeAwardCanonicalEvent curatedBadgeAwardCanonicalEvent,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(curatedBadgeAwardCanonicalEvent), List.of(), content, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull CuratedBadgeAwardCanonicalEvent curatedBadgeAwardCanonicalEvent,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(curatedBadgeAwardCanonicalEvent), baseTags, content, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<CuratedBadgeAwardCanonicalEvent> curatedBadgeAwardCanonicalEventList,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.BADGE_SETS_EVENT,
       generateIdentifierTag(
          badgeDefinitionReputationEvent,
          curatedBadgeAwardCanonicalEventList.getFirst().getAwardRecipientPublicKey()),
       mapStream(badgeDefinitionReputationEvent,
          validateNonEmptyCuratedBadgeAwardCanonicalEventList(curatedBadgeAwardCanonicalEventList), baseTags),
       content, relay);
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
    this.curatedBadgeAwardCanonicalEventList = curatedBadgeAwardCanonicalEventList;
  }

  public BadgeSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<CuratedBadgeAwardCanonicalEvent> curatedBadgeAwardCanonicalEventList) throws NostrException {
    super(validateGenericConstructorKind(genericEventRecord, Kind.BADGE_SETS_EVENT));
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
    this.curatedBadgeAwardCanonicalEventList =
       validateNonEmptyCuratedBadgeAwardCanonicalEventList(curatedBadgeAwardCanonicalEventList);
  }

  public BadgeSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull CuratedBadgeAwardCanonicalEvent curatedBadgeAwardCanonicalEvent) {
    return createNewFromExisting(identity, List.of(curatedBadgeAwardCanonicalEvent));
  }

  public BadgeSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull List<CuratedBadgeAwardCanonicalEvent> curatedBadgeAwardCanonicalEvents) {
    List<CuratedBadgeAwardCanonicalEvent> appendList = new ArrayList<>(getCuratedBadgeAwardCanonicalEventList());
    appendList.addAll(curatedBadgeAwardCanonicalEvents);
    List<CuratedBadgeAwardCanonicalEvent> distinctCuratedBadgeAwardCanonicalEventList = appendList.stream().distinct().toList();
    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       identity,
       getBadgeDefinitionReputationEvent(),
       validateNonEmptyCuratedBadgeAwardCanonicalEventList(distinctCuratedBadgeAwardCanonicalEventList),
       getTags(),
       getContent(),
       getRelay().orElseThrow(() ->
          new NostrException("createNewFromExisting BadgeSetsEvent is missing a Relay")));
    return badgeSetsEvent;
  }

  @JsonIgnore
  public List<CuratedBadgeAwardCanonicalEvent> getCuratedBadgeAwardCanonicalEventList() {
    return curatedBadgeAwardCanonicalEventList;
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return curatedBadgeAwardCanonicalEventList.getFirst().getAwardRecipientPublicKey();
  }

  private static List<BaseTag> mapStream(
     BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     List<CuratedBadgeAwardCanonicalEvent> curatedBadgeAwardCanonicalEventList,
     List<BaseTag> baseTags) {
    return Stream.concat(
       Stream.concat(
          Stream.concat(
             Stream.of(
                new PubKeyTag(curatedBadgeAwardCanonicalEventList.getFirst().getAwardRecipientPublicKey())),
             curatedBadgeAwardCanonicalEventList
                .stream()
                .map(curationSetsEvent ->
                   new EventTag(
                      curationSetsEvent.getId(),
                      curationSetsEvent.getRelay().map(Relay::getUrl).orElse(null)))),
          Stream.of(
             badgeDefinitionReputationEvent.asAddressableEventAddressTag())),
       baseTags.stream()
          .filter(Predicate.not(PubKeyTag.class::isInstance))
          .filter(Predicate.not(EventTag.class::isInstance))
          .filter(Predicate.not(AddressTag.class::isInstance))).toList();
  }

  private static List<CuratedBadgeAwardCanonicalEvent> validateNonEmptyCuratedBadgeAwardCanonicalEventList(
     List<CuratedBadgeAwardCanonicalEvent> curatedBadgeAwardCanonicalEventList) {
    if (curatedBadgeAwardCanonicalEventList.isEmpty())
      throw new NostrException("BadgeSetsEvent constructor received empty List<CuratedBadgeAwardCanonicalEvent>");

    return curatedBadgeAwardCanonicalEventList.stream().distinct().toList();
  }

  public static IdentifierTag generateIdentifierTag(BadgeDefinitionReputationEvent event, PublicKey publicKey) {
    return new IdentifierTag(
       String.valueOf(
          Objects.hash(
             AbstractSetsEvent.getAddressTagValuesHashed(event.asAddressableEventAddressTag()),
             publicKey.toHexString(),
             String.valueOf(Instant.now()))));
  }

  private final Function<List<BaseTag>, List<BaseTag>> filterFxn =
     baseTags -> baseTags.stream()
        .filter(baseTag ->
           baseTag.getClass().equals(AddressTag.class) ||
              baseTag.getClass().equals(EventTag.class) ||
              baseTag.getClass().equals(PubKeyTag.class)
        ).toList();


  public final boolean equalsSoft(BadgeSetsEvent that) {
    return equalsSoft(that.asGenericEventRecord());
  }

  public final boolean equalsSoft(GenericEventRecord genericEventRecord) {
    return
       new HashSet<>(
          filterFxn.apply(this.getTags())).containsAll(filterFxn.apply(genericEventRecord.getTags())) &&
          Objects.equals(this.getPublicKey(), genericEventRecord.getPublicKey());
  }
}
