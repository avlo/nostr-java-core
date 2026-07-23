package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.user.Identity;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import lombok.NonNull;

public class CuratedBadgeDefinitionGenericEvent extends AbstractSetsEvent implements SetsPairedEventTagIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated CuratedBadgeDefinitionEvent";

//  @Getter
//  @JsonIgnore
//  protected final BadgeDefinitionGenericEvent badgeDefinitionGenericEvent;

  public CuratedBadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull ReferenceTag badgeDefinitionGenericEventReferenceTag,
     @NonNull Relay relay) {
    super(
       identity,
       Kind.REFERENCED_SET,
       new IdentifierTag(
          String.valueOf(badgeDefinitionGenericEvent.asAddressableEventAddressTag().hashCode())),
       new SetsPairedEvent(
          badgeDefinitionGenericEvent.asAddressableEventAddressTag(),
          new EventTag(
             badgeDefinitionGenericEvent.getId(),
             badgeDefinitionGenericEvent.getRelay().map(Relay::getUrl).orElse(
                badgeDefinitionGenericEventReferenceTag.getUrl()))
       ),
       List.of(badgeDefinitionGenericEventReferenceTag), DEFAULT_CONTENT, relay);
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
          genericEventRecord.requireFirstTag(AddressTag.class),
          genericEventRecord.requireFirstTag(EventTag.class)
       ));
  }

  protected static GenericEventRecord validateIdentifierTagHash(GenericEventRecord genericEventRecord) {
    if (
       !Objects.equals(genericEventRecord.requireFirstTag(IdentifierTag.class).getUuid(),
          String.valueOf(genericEventRecord.requireFirstTag(AddressTag.class).hashCode())))
      throw new NostrException("IdentifierTag UUID != hashcode(AddressTag)");
    return genericEventRecord;
  }
}

