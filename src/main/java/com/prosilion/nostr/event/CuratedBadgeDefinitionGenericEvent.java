package com.prosilion.nostr.event;

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
       Kind.CURATION_SETS_BADGE_DEFINITION_EVENT,
       new IdentifierTag(
          String.valueOf(
             fillAddressTag(
                badgeDefinitionGenericEvent.asAddressableEventAddressTag(),
                badgeDefinitionGenericEventReferenceTag).hashCode())),
       new SetsPairedEvent(
          fillAddressTag(
             badgeDefinitionGenericEvent.asAddressableEventAddressTag(),
             badgeDefinitionGenericEventReferenceTag),
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
          fillAddressTag(
             genericEventRecord.requireFirstTag(AddressTag.class),
             genericEventRecord.requireFirstTag(ReferenceTag.class)),
          fillEventTag(
             genericEventRecord.requireFirstTag(EventTag.class),
             genericEventRecord.requireFirstTag(ReferenceTag.class))));
  }

  private static AddressTag fillAddressTag(AddressTag addressTag, ReferenceTag referenceTag) {
    return new AddressTag(
       addressTag.getKind(),
       addressTag.getPublicKey(),
       addressTag.getIdentifierTag(),
       new Relay(
          addressTag.findRelay().map(Relay::getUrl).orElse(referenceTag.getUrl())));
  }

  private static EventTag fillEventTag(EventTag eventTag, ReferenceTag referenceTag) {
    return new EventTag(
       eventTag.getEventId(),
       eventTag.findRelay().map(Relay::getUrl).orElse(referenceTag.getUrl()));
  }

  protected static GenericEventRecord validateIdentifierTagHash(GenericEventRecord genericEventRecord) {
    if (
       !Objects.equals(genericEventRecord.requireFirstTag(IdentifierTag.class).getUuid(),
          String.valueOf(genericEventRecord.requireFirstTag(AddressTag.class).hashCode())))
      throw new NostrException("IdentifierTag UUID != hashcode(AddressTag)");
    return genericEventRecord;
  }
}

