package com.prosilion.nostr.event.curated;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
       List.of(), content, relay);
    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
  }

  public CuratedBadgeDefinitionGenericEvent(@NonNull GenericEventRecord genericEventRecord) {
    this(genericEventRecord, requireTags(genericEventRecord));
  }

  private CuratedBadgeDefinitionGenericEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull RequiredTags requiredTags) {
    super(
       genericEventRecord,
       new SetsPairedEvent(
          requiredTags.addressTag(),
          requiredTags.eventTag()));

    this.badgeDefinitionGenericEvent =
       new BadgeDefinitionGenericEvent(
          new GenericEventRecord(
             requiredTags.eventTag().getEventId(),
             requiredTags.addressTag().getPublicKey(),
             genericEventRecord.getCreatedAt(),
             requiredTags.addressTag().getKind(),
             curateBadgeDefinitionEventTags(requiredTags),
             genericEventRecord.getContent(),
             genericEventRecord.getSignature()));
  }

  private static final List<Class<? extends BaseTag>> REQUIRED_TAG_TYPES =
     List.of(IdentifierTag.class, AddressTag.class, EventTag.class, RelayTag.class);

  static RequiredTags requireTags(@NonNull GenericEventRecord genericEventRecord) {
    validateGenericConstructorKind(genericEventRecord, Kind.CURATION_SETS_BADGE_DEFINITION_EVENT);
    validateRequiredTags(genericEventRecord, REQUIRED_TAG_TYPES);
    validateIdentifierTagHash(genericEventRecord);
    return new RequiredTags(
       genericEventRecord.requireFirstTag(IdentifierTag.class),
       genericEventRecord.requireFirstTag(AddressTag.class),
       genericEventRecord.requireFirstTag(EventTag.class),
       genericEventRecord.requireFirstTag(RelayTag.class));
  }

  private static List<BaseTag> curateBadgeDefinitionEventTags(@NonNull RequiredTags requiredTags) {
    IdentifierTag identifierTag = requiredTags.addressTag().requireIdentifierTag();

    return requiredTags.eventTag().findRelay()
       .or(requiredTags.addressTag()::findRelay)
       .<List<BaseTag>>map(relay -> List.of(identifierTag, new RelayTag(relay)))
       .orElseGet(() -> List.of(identifierTag));
  }

  record RequiredTags(
     IdentifierTag identifierTag,
     AddressTag addressTag,
     EventTag eventTag,
     RelayTag relayTag) {
  }
}
