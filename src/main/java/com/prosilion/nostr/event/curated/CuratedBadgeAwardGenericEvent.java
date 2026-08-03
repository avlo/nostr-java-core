package com.prosilion.nostr.event.curated;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
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

import static com.prosilion.nostr.event.curated.CuratedBadgeDefinitionGenericEvent.validateIdentifierTagHash;

public class CuratedBadgeAwardGenericEvent extends AbstractSetsEvent implements SetsPairedEventTagIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated CuratedBadgeAwardEvent";

//  TODO: investigate readd below
//  @Getter
//  @JsonIgnore
//  protected final CuratedBadgeDefinitionGenericEvent curatedBadgeDefinitionGenericEvent;

  public CuratedBadgeAwardGenericEvent(
     @NonNull Identity identity,
     @NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent,
     @NonNull ReferenceTag badgeDefinitionGenericEventReferenceTag,
     @NonNull ReferenceTag badgeAwardGenericEventReferenceTag,
     @NonNull Relay relay) {
    this(
       identity,
       badgeAwardGenericEvent,
       new CuratedBadgeDefinitionGenericEvent(
          identity,
          badgeAwardGenericEvent.getBadgeDefinitionEvent(),
          badgeDefinitionGenericEventReferenceTag,
          relay),
       badgeAwardGenericEventReferenceTag,
       relay);
  }

  public CuratedBadgeAwardGenericEvent(
     @NonNull Identity identity,
     @NonNull GenericEventRecord badgeAwardGenericEvent,
     @NonNull ReferenceTag badgeAwardGenericEventReferenceTag,
     @NonNull Relay relay) {
    super(
       identity,
       Kind.CURATION_SETS_BADGE_AWARD_EVENT,
       hashedAddressTag(
          badgeAwardGenericEvent.requireFirstTag(AddressTag.class)),
       new SetsPairedEvent(
          fillAddressTag(
             badgeAwardGenericEvent.requireFirstTag(AddressTag.class),
             badgeAwardGenericEventReferenceTag),
          new EventTag(
             badgeAwardGenericEvent.getId(),
             badgeAwardGenericEvent.getRelayTag().map(RelayTag::relay).map(Relay::getUrl).orElse(badgeAwardGenericEventReferenceTag.getUrl()))),
       List.of(
          badgeAwardGenericEvent.requireFirstTag(PubKeyTag.class),
          badgeAwardGenericEventReferenceTag),
       DEFAULT_CONTENT,
       relay);
//    this.curatedBadgeDefinitionGenericEvent = curatedBadgeDefinitionGenericEvent;
  }

  public CuratedBadgeAwardGenericEvent(
     @NonNull Identity identity,
     @NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent,
     @NonNull CuratedBadgeDefinitionGenericEvent curatedBadgeDefinitionGenericEvent,
     @NonNull ReferenceTag badgeAwardGenericEventReferenceTag,
     @NonNull Relay relay) {
    super(
       identity,
       Kind.CURATION_SETS_BADGE_AWARD_EVENT,
       hashedAddressTag(
          curatedBadgeDefinitionGenericEvent.asAddressableEventAddressTag()),
       new SetsPairedEvent(
          fillAddressTag(
             curatedBadgeDefinitionGenericEvent.asAddressableEventAddressTag(),
             badgeAwardGenericEventReferenceTag),
          new EventTag(
             badgeAwardGenericEvent.getId(),
             badgeAwardGenericEvent.getRelay().map(Relay::getUrl).orElse(badgeAwardGenericEventReferenceTag.getUrl()))),
       List.of(
          new PubKeyTag(badgeAwardGenericEvent.getAwardRecipientPublicKey()),
          badgeAwardGenericEventReferenceTag),
       DEFAULT_CONTENT,
       relay);
//    this.curatedBadgeDefinitionGenericEvent = curatedBadgeDefinitionGenericEvent;
  }

  public CuratedBadgeAwardGenericEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(
       validateIdentifierTagHash(
          validateRequiredTags(
             genericEventRecord,
             List.of(
                IdentifierTag.class,
                PubKeyTag.class,
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
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return requireFirstTag(PubKeyTag.class).getPublicKey();
  }
}

