package com.prosilion.nostr.event.curated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.BadgeAwardCanonicalEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import lombok.NonNull;

public class CuratedBadgeAwardGenericEvent extends AbstractSetsEvent implements SetsPairedEventTagIF {
  public static final String DEFAULT_CONTENT =
     "AfterImage generated CuratedBadgeAwardGenericEvent- appending BadgeAwardGenericEvent content: %s";
// TODO: potentially re-add later if can resolve generic ctor variant
//  @Getter
//  @JsonIgnore
//  protected final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent;
//
//  @Getter
//  @JsonIgnore
//  protected final CuratedBadgeDefinitionGenericEvent curatedBadgeDefinitionGenericEvent;

  public CuratedBadgeAwardGenericEvent(
     @NonNull Identity identity,
     @NonNull BadgeAwardCanonicalEvent badgeAwardCanonicalEvent,
     @NonNull ReferenceTag badgeDefinitionGenericEventReferenceTag,
     @NonNull ReferenceTag badgeAwardGenericEventReferenceTag,
     @NonNull Relay relay) {
    this(
       identity,
       badgeAwardCanonicalEvent,
       new CuratedBadgeDefinitionGenericEvent(
          identity,
          badgeAwardCanonicalEvent.getBadgeDefinitionEvent(),
          badgeDefinitionGenericEventReferenceTag,
          relay),
       badgeAwardGenericEventReferenceTag,
       relay);
  }

  public CuratedBadgeAwardGenericEvent(
     @NonNull Identity identity,
     @NonNull BadgeAwardCanonicalEvent badgeAwardCanonicalEvent,
     @NonNull CuratedBadgeDefinitionGenericEvent curatedBadgeDefinitionGenericEvent,
     @NonNull ReferenceTag badgeAwardGenericEventReferenceTag,
     @NonNull Relay relay) {
    super(
       identity,
       Kind.CURATION_SETS_BADGE_AWARD_EVENT,
       new IdentifierTag(curatedBadgeDefinitionGenericEvent.getId()),
       new SetsPairedEvent(
          fillAddressTag(
             new AddressTag(
                curatedBadgeDefinitionGenericEvent.asAddressableEventAddressTag().getKind(),
                identity.getPublicKey(),
                curatedBadgeDefinitionGenericEvent.asAddressableEventAddressTag().getIdentifierTag()),
             badgeAwardGenericEventReferenceTag),
          new EventTag(
             badgeAwardCanonicalEvent.getId(),
             badgeAwardCanonicalEvent.getRelay().map(Relay::getUrl).orElse(badgeAwardGenericEventReferenceTag.getUrl()))),
       List.of(
          new PubKeyTag(badgeAwardCanonicalEvent.getAwardRecipientPublicKey())),
       String.format(DEFAULT_CONTENT, badgeAwardCanonicalEvent.getContent()),
       relay);
// TODO: potentially re-add later if can resolve below Generic variant    
//    this.badgeAwardCanonicalEvent = badgeAwardCanonicalEvent;
//    this.curatedBadgeDefinitionGenericEvent = curatedBadgeDefinitionGenericEvent;
  }

  public CuratedBadgeAwardGenericEvent(@NonNull GenericEventRecord genericEventRecord) {
    this(genericEventRecord, requireTags(genericEventRecord));
  }

  private CuratedBadgeAwardGenericEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull RequiredTags requiredTags) {
    super(
       genericEventRecord,
       new SetsPairedEvent(
          requiredTags.addressTag(),
          requiredTags.eventTag()));
// TODO: potentially re-add later    
//    this.curatedBadgeDefinitionGenericEvent =
//       new CuratedBadgeDefinitionGenericEvent(
//          new GenericEventRecord(
//             
//          ));
//
//    this.badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
//       genericEventRecord, addressTag ->
//       new BadgeDefinitionGenericEvent(
//          new GenericEventRecord(
//             requiredTags.identifierTag().getUuid(),
//             requiredTags.addressTag().getPublicKey(),
//             genericEventRecord.getCreatedAt(),
//             Kind.BADGE_DEFINITION_EVENT,
//             genericEventRecord.getTags(),
//             genericEventRecord.getContent(),
//             genericEventRecord.getSignature())));
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return requireFirstTag(PubKeyTag.class).getPublicKey();
  }

  private static final List<Class<? extends BaseTag>> REQUIRED_TAG_TYPES =
     List.of(IdentifierTag.class, PubKeyTag.class, AddressTag.class, EventTag.class, RelayTag.class);

  private static RequiredTags requireTags(@NonNull GenericEventRecord genericEventRecord) {
    validateGenericConstructorKind(genericEventRecord, Kind.CURATION_SETS_BADGE_AWARD_EVENT);
    validateRequiredTags(genericEventRecord, REQUIRED_TAG_TYPES);
    return new RequiredTags(
       genericEventRecord.requireFirstTag(IdentifierTag.class),
       genericEventRecord.requireFirstTag(PubKeyTag.class),
       genericEventRecord.requireFirstTag(AddressTag.class),
       genericEventRecord.requireFirstTag(EventTag.class),
       genericEventRecord.requireFirstTag(RelayTag.class));
  }

  private record RequiredTags(
     IdentifierTag identifierTag,
     PubKeyTag pubKeyTag,
     AddressTag addressTag,
     EventTag eventTag,
     RelayTag relayTag) {
  }
}
