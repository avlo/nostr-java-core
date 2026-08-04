package com.prosilion.nostr.event.curated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Objects;
import lombok.Getter;
import lombok.NonNull;

public class CuratedBadgeDefinitionGenericEvent extends AbstractSetsEvent implements SetsPairedEventTagIF {
  public static final String DEFAULT_CONTENT =
     "AfterImage generated CuratedBadgeDefinitionGenericEvent- appending BadgeDefinitionGenericEvent content: %s";

  @Getter
  @JsonIgnore
  protected final BadgeDefinitionGenericEvent badgeDefinitionGenericEvent;

  public CuratedBadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull ReferenceTag badgeDefinitionGenericEventReferenceTag,
     @NonNull Relay relay) {
    this(identity, badgeDefinitionGenericEvent, badgeDefinitionGenericEventReferenceTag,
       String.format(DEFAULT_CONTENT, badgeDefinitionGenericEvent.getContent()), relay);
  }

  public CuratedBadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull ReferenceTag badgeDefinitionGenericEventReferenceTag,
     @NonNull String content,
     @NonNull Relay relay) {
    super(
       identity,
       Kind.CURATION_SETS_BADGE_DEFINITION_EVENT,
       hashedAddressTag(
          badgeDefinitionGenericEvent.asAddressableEventAddressTag()),
       new SetsPairedEvent(
          fillAddressTag(
             badgeDefinitionGenericEvent.asAddressableEventAddressTag(),
             badgeDefinitionGenericEventReferenceTag),
          new EventTag(
             badgeDefinitionGenericEvent.getId(),
             badgeDefinitionGenericEvent.getRelay().map(Relay::getUrl).orElse(
                badgeDefinitionGenericEventReferenceTag.getUrl()))
       ),
       List.of(badgeDefinitionGenericEventReferenceTag), content, relay);
    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
  }

  public CuratedBadgeDefinitionGenericEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(
       validateIdentifierTagHash(
          validateRequiredTags(
             genericEventRecord,
             List.of(
                IdentifierTag.class,
                AddressTag.class,
                EventTag.class,
                RelayTag.class,
                ReferenceTag.class))),
       new SetsPairedEvent(
          fillAddressTag(
             genericEventRecord.requireFirstTag(AddressTag.class),
             genericEventRecord.requireFirstTag(ReferenceTag.class)),
          fillEventTag(
             genericEventRecord.requireFirstTag(EventTag.class),
             genericEventRecord.requireFirstTag(ReferenceTag.class))));

    this.badgeDefinitionGenericEvent =
       new BadgeDefinitionGenericEvent(
          new GenericEventRecord(
             genericEventRecord.requireFirstTag(EventTag.class).getEventId(),
             genericEventRecord.requireFirstTag(AddressTag.class).getPublicKey(),
             genericEventRecord.getCreatedAt(),
             genericEventRecord.requireFirstTag(AddressTag.class).getKind(),
             curateBadgeDefinitionEventTags(genericEventRecord),
             genericEventRecord.getContent(),
             genericEventRecord.getSignature()).asGenericEventRecord());
  }

  protected static GenericEventRecord validateIdentifierTagHash(GenericEventRecord genericEventRecord) {
    if (
       !Objects.equals(
          genericEventRecord.requireFirstTag(IdentifierTag.class).getUuid(),
          hashedAddressTag(
             genericEventRecord.requireFirstTag(AddressTag.class)).getUuid()))
      throw new NostrException(
         String.format("IdentifierTag UUID [%s] != hashcode(AddressTag) [%s]",
            genericEventRecord.requireFirstTag(IdentifierTag.class).getUuid(),
            hashedAddressTag(
               genericEventRecord.requireFirstTag(AddressTag.class)).getUuid()));
    return genericEventRecord;
  }

  private static List<BaseTag> curateBadgeDefinitionEventTags(@NonNull GenericEventRecord genericEventRecord) {
    AddressTag addressTag = genericEventRecord.requireFirstTag(AddressTag.class);
    IdentifierTag identifierTag = addressTag.requireIdentifierTag();

    return genericEventRecord.requireFirstTag(EventTag.class).findRelay()
       .or(addressTag::findRelay)
       .<List<BaseTag>>map(relay -> List.of(identifierTag, new RelayTag(relay)))
       .orElseGet(() -> List.of(identifierTag));
  }
}

