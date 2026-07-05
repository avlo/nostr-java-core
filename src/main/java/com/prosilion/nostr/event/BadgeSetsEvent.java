package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
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
  private final List<CurationSetsEvent> curationSetsEventList;

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull CurationSetsEvent curationSetsEvent,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(curationSetsEvent), List.of(), DEFAULT_CONTENT, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<CurationSetsEvent> curationSetsEventList,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, curationSetsEventList, List.of(), DEFAULT_CONTENT, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull CurationSetsEvent curationSetsEvent,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(curationSetsEvent), List.of(), content, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull CurationSetsEvent curationSetsEvent,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionReputationEvent, List.of(curationSetsEvent), baseTags, content, relay);
  }

  public BadgeSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<CurationSetsEvent> curationSetsEventList,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.BADGE_SETS_EVENT,
       badgeDefinitionReputationEvent.getIdentifierTag(),
       Stream.concat(
          Stream.concat(
             Stream.concat(
                Stream.of(
                   new PubKeyTag(curationSetsEventList.getFirst().getAwardRecipientPublicKey())),
                curationSetsEventList
                   .stream()
                   .map(curationSetsEvent ->
                      new EventTag(
                         curationSetsEvent.getId(),
                         curationSetsEvent.getRelay().map(Relay::getUrl).orElse(null)))),
             Stream.of(
                new AddressTag(
                   badgeDefinitionReputationEvent.getKind(),
                   badgeDefinitionReputationEvent.getReputationDefinitionCreatorPublicKey(),
                   badgeDefinitionReputationEvent.getIdentifierTag(),
                   badgeDefinitionReputationEvent.getRelay().orElse(null)))),
          baseTags.stream()
             .filter(Predicate.not(PubKeyTag.class::isInstance))
             .filter(Predicate.not(EventTag.class::isInstance))
             .filter(Predicate.not(AddressTag.class::isInstance))).toList(),
       content, relay);
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
    this.curationSetsEventList = curationSetsEventList;
  }

  public BadgeSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<CurationSetsEvent> curationSetsEventList) throws NostrException {
    super(genericEventRecord);
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
    this.curationSetsEventList = curationSetsEventList;
  }

  @JsonIgnore
  public List<EventTag> getEventTags() {
    return getTypeSpecificTags(EventTag.class);
  }

  @JsonIgnore
  public List<CurationSetsEvent> getCurationSetsEventList() {
    return curationSetsEventList;
  }
}
