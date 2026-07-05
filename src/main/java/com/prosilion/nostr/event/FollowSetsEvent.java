package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
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

public class FollowSetsEvent extends AddressableEvent implements TagMappedEventIF {
  public static final String DEFAULT_IDENTIFIER = "PROSILION_FOLLOW_SETS_EVENT";
  public static final IdentifierTag defaultIdentifierTag = new IdentifierTag(DEFAULT_IDENTIFIER);
  public static final String DEFAULT_CONTENT = "AfterImage generated FollowSetsEvent";

  @Getter
  @JsonIgnore
  private final List<BadgeSetsEvent> badgeSetsEventList;

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeSetsEvent badgeSetsEvent,
     @NonNull Relay relay) {
    this(identity, List.of(badgeSetsEvent), List.of(), DEFAULT_CONTENT, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<BadgeSetsEvent> badgeSetsEventList,
     @NonNull Relay relay) {
    this(identity, badgeSetsEventList, List.of(), DEFAULT_CONTENT, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<BadgeSetsEvent> badgeSetsEventList,
     @NonNull List<BaseTag> baseTags,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeSetsEventList, baseTags, DEFAULT_CONTENT, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeSetsEvent badgeSetsEvent,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, List.of(badgeSetsEvent), List.of(), content, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<BadgeSetsEvent> badgeSetsEventList,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeSetsEventList, List.of(), content, relay);
  }

  public FollowSetsEvent(
     @NonNull Identity identity,
     @NonNull List<BadgeSetsEvent> badgeSetsEventList,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.FOLLOW_SETS,
       defaultIdentifierTag,
       mapStream(badgeSetsEventList, baseTags),
       content,
       relay);
    this.badgeSetsEventList = badgeSetsEventList.stream().distinct().toList();
  }

  public FollowSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull BadgeSetsEvent badgeSetsEvent) {
    this(genericEventRecord, List.of(badgeSetsEvent));
  }

  public FollowSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<BadgeSetsEvent> badgeSetsEventList) {
    super(genericEventRecord);
    this.badgeSetsEventList = badgeSetsEventList.stream().distinct().toList();
  }

  public FollowSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull BadgeSetsEvent newBadgeSetsEvent) {
    return createNewFromExisting(identity, List.of(newBadgeSetsEvent));
  }

  public FollowSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull List<BadgeSetsEvent> newBadgeSetsEvents) {
    List<BadgeSetsEvent> appendList = new ArrayList<>(getBadgeSetsEventList());
    appendList.addAll(newBadgeSetsEvents);
    List<BadgeSetsEvent> distinctList = appendList.stream().distinct().toList();
    if (getBadgeSetsEventList().equals(distinctList))
      return this;
    return new FollowSetsEvent(
       identity,
       distinctList,
       getTags(),
       getContent(),
       getRelay().orElseThrow());
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return badgeSetsEventList.getFirst()
       .getCurationSetsEventList()
       .getFirst().getAwardRecipientPublicKey();
  }

  @JsonIgnore
  public List<EventTag> getEventTags() {
    return getTypeSpecificTags(EventTag.class);
  }

  private static List<BaseTag> mapStream(@NonNull List<BadgeSetsEvent> badgeSetsEventList, @NonNull List<BaseTag> baseTags) {
    List<BaseTag> baseTags1 = Stream.concat(
       Stream.concat(
          Stream.of(
             new PubKeyTag(badgeSetsEventList.getFirst()
                .getCurationSetsEventList().getFirst()
                .getAwardRecipientPublicKey())),
          badgeSetsEventList.stream().distinct()
             .map(badgeSetsEvent ->
                new EventTag(
                   badgeSetsEvent.getId(),
                   badgeSetsEvent.getRelay().map(Relay::getUrl).orElse(null)))),
       baseTags.stream()
          .filter(Predicate.not(PubKeyTag.class::isInstance))
          .filter(Predicate.not(EventTag.class::isInstance))).toList();
    return baseTags1;
  }
}
