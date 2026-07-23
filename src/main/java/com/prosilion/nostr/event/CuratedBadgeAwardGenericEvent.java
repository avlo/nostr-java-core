package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.enums.Kind;
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

import static com.prosilion.nostr.event.CuratedBadgeDefinitionGenericEvent.validateIdentifierTagHash;

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
     @NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent,
     @NonNull CuratedBadgeDefinitionGenericEvent curatedBadgeDefinitionGenericEvent,
     @NonNull ReferenceTag badgeAwardGenericEventReferenceTag,
     @NonNull Relay relay) {
    super(
       identity,
       Kind.CURATION_SETS,
       new IdentifierTag(
          String.valueOf(curatedBadgeDefinitionGenericEvent.getAddressTag().hashCode())),
       new SetsPairedEvent(
          curatedBadgeDefinitionGenericEvent.getAddressTag(),
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
          genericEventRecord.requireFirstTag(AddressTag.class),
          genericEventRecord.requireFirstTag(EventTag.class)
       ));
//    this.curatedBadgeDefinitionGenericEvent =
//       new CuratedBadgeDefinitionGenericEvent(
//          identity,
//          award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent(),
//          genericEventRecord.requireFirstTag(ReferenceTag.class),
//          genericEventRecord.requireFirstTag(RelayTag.class).getRelay());
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return requireFirstTag(PubKeyTag.class).getPublicKey();
  }
}

