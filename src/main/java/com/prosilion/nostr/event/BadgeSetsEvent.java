package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;

public class BadgeSetsEvent extends AddressableEvent implements TagMappedEventIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated BadgeSetsEvent";

  @Getter
  @JsonIgnore
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  @JsonIgnore
  private final List<CuratedBadgeAwardGenericEvent> curatedBadgeAwardGenericEventList;

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull CuratedBadgeAwardGenericEvent curatedBadgeAwardGenericEvent,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(curatedBadgeAwardGenericEvent), List.of(), DEFAULT_CONTENT, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<CuratedBadgeAwardGenericEvent> curatedBadgeAwardGenericEventList,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, curatedBadgeAwardGenericEventList, List.of(), DEFAULT_CONTENT, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull CuratedBadgeAwardGenericEvent curatedBadgeAwardGenericEvent,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(curatedBadgeAwardGenericEvent), List.of(), content, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull CuratedBadgeAwardGenericEvent curatedBadgeAwardGenericEvent,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(curatedBadgeAwardGenericEvent), baseTags, content, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<CuratedBadgeAwardGenericEvent> curatedBadgeAwardGenericEventList,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.BADGE_SETS_EVENT,
// BadgeSetsEvent IdentifierTag points to BadgeDefinitionReputationEvent's PubKeyTag (Reputation Definition Creator's Public Key)
       new IdentifierTag(
          badgeDefinitionReputationEvent.getReputationDefinitionCreatorPublicKey().toHexString()),
// re: IdentifierTag's value, see above note 
       mapStream(badgeDefinitionReputationEvent, curatedBadgeAwardGenericEventList, baseTags),
       content, relay);
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
    this.curatedBadgeAwardGenericEventList = curatedBadgeAwardGenericEventList;
  }

  public BadgeSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<CuratedBadgeAwardGenericEvent> curatedBadgeAwardGenericEventList) throws NostrException {
    super(genericEventRecord);
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
    this.curatedBadgeAwardGenericEventList = curatedBadgeAwardGenericEventList;
  }

  public BadgeSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull CuratedBadgeAwardGenericEvent curatedBadgeAwardGenericEvent) {
    return createNewFromExisting(identity, List.of(curatedBadgeAwardGenericEvent));
  }

  public BadgeSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull List<CuratedBadgeAwardGenericEvent> curatedBadgeAwardGenericEvents) {
    List<CuratedBadgeAwardGenericEvent> appendList = new ArrayList<>(getCuratedBadgeAwardGenericEventList());
    appendList.addAll(curatedBadgeAwardGenericEvents);
    List<CuratedBadgeAwardGenericEvent> distinctCuratedBadgeAwardGenericEventList = appendList.stream().distinct().toList();
    BadgeSetsEvent badgeSetsEvent = new BadgeSetsEvent(
       identity,
       getBadgeDefinitionReputationEvent(),
       distinctCuratedBadgeAwardGenericEventList,
       getTags(),
       getContent(),
       getRelay().orElseThrow(() ->
          new NostrException("createNewFromExisting BadgeSetsEvent is missing a Relay")));
    return badgeSetsEvent;
  }

  @JsonIgnore
  public List<CuratedBadgeAwardGenericEvent> getCuratedBadgeAwardGenericEventList() {
    return curatedBadgeAwardGenericEventList;
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return curatedBadgeAwardGenericEventList.getFirst().getAwardRecipientPublicKey();
  }

  private static List<BaseTag> mapStream(
     BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     List<CuratedBadgeAwardGenericEvent> curatedBadgeAwardGenericEventList,
     List<BaseTag> baseTags) {
    return Stream.concat(
       Stream.concat(
          Stream.concat(
             Stream.of(
                new PubKeyTag(curatedBadgeAwardGenericEventList.getFirst().getAwardRecipientPublicKey())),
             curatedBadgeAwardGenericEventList
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
}
