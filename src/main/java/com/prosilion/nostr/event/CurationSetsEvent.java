package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;

public class CurationSetsEvent extends AbstractSetsEvent implements SetsPairedEventTagIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated CurationSetsEvent";

  @Getter
  @JsonIgnore
  protected final BadgeDefinitionGenericEvent badgeDefinitionGenericEvent;

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull Relay relay) {
    this(identity, badgeDefinitionGenericEvent, setsPairedEvent, List.of(), DEFAULT_CONTENT, relay);
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull List<BaseTag> baseTags,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionGenericEvent, setsPairedEvent, baseTags, DEFAULT_CONTENT, relay);
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionGenericEvent, setsPairedEvent, List.of(), content, relay);
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.CURATION_SETS,
       badgeDefinitionGenericEvent.getIdentifierTag(),
       setsPairedEvent,
       Stream.concat(
          Stream.concat(
             Stream.of(
                new PubKeyTag(setsPairedEvent.getAwardRecipientPublicKey())),
             Stream.of(badgeDefinitionGenericEvent.asAddressableEventAddressTag())),
          baseTags.stream()
             .filter(Predicate.not(PubKeyTag.class::isInstance))).toList(),
       content,
       relay);
    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
  }

  public CurationSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent) {
    super(genericEventRecord, setsPairedEvent);
    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
  }

  public CurationSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull SetsPairedEvent setsPairedEvent) {
    return new CurationSetsEvent(
       identity,
       badgeDefinitionGenericEvent,
       setsPairedEvent,
       getTags(),
       getContent(),
       getRelay().orElseThrow());
  }

  public List<BaseTag> getAddressTagEventTagPair() {
    return List.of(getEventTag(), getAddressTag());
  }

  public List<EventTag> getEventTags() {
    return List.of(getEventTag());
  }
}

