package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
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

//  @Getter
//  @JsonIgnore
//  protected final BadgeDefinitionGenericEvent badgeDefinitionGenericEvent;

  public CuratedBadgeAwardGenericEvent(
     @NonNull Identity identity,
     @NonNull BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent,
     @NonNull Relay relay) {
    super(
       identity,
       Kind.CURATION_SETS,
       new IdentifierTag(
          String.valueOf(badgeAwardGenericEvent.getAddressTag().hashCode())),
       new SetsPairedEvent(
          badgeAwardGenericEvent.getAddressTag(),
          new EventTag(
             badgeAwardGenericEvent.getId(),
             badgeAwardGenericEvent.getRelay().map(Relay::getUrl).orElseThrow())),
       List.of(new PubKeyTag(badgeAwardGenericEvent.getAwardRecipientPublicKey())),
       DEFAULT_CONTENT,
       relay);
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
                RelayTag.class))),
       new SetsPairedEvent(
          genericEventRecord.requireFirstTag(AddressTag.class),
          genericEventRecord.requireFirstTag(EventTag.class)
       ));
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return requireFirstTag(PubKeyTag.class).getPublicKey();
  }
}

