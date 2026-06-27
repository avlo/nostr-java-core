package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.tag.SetsPairedEvents;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.NonNull;

@Getter
public abstract class AbstractSetsEvent<T extends SetsPairedEventTagIF> extends AddressableEvent implements TagMappedEventIF {
  @JsonIgnore
  protected final List<SetsPairedEvents<T>> tupleDefnEventAuxAwardEventAuxes;
  @JsonIgnore
  protected final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  protected AbstractSetsEvent(
     @NonNull Identity identity,
     @NonNull Kind kind,
     @NonNull IdentifierTag identifierTag,
     @NonNull List<SetsPairedEvents<T>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
     @NonNull List<BaseTag> tags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(identity, kind, identifierTag, tags, content, relay);
    this.tupleDefnEventAuxAwardEventAuxes = tupleDefnEventAuxAwardEventAuxes;
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
  }

  protected AbstractSetsEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull List<SetsPairedEvents<T>> tupleDefnEventAuxAwardEventAuxes,
     @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxnAddressTag) {
    super(genericEventRecord);
    this.tupleDefnEventAuxAwardEventAuxes = tupleDefnEventAuxAwardEventAuxes;
    this.badgeDefinitionReputationEvent = mapTagsToEvents(this, fxnAddressTag, AddressTag.class).getFirst();
  }

  @JsonIgnore
  public final List<EventTag> getEventTags() {
    return tupleDefnEventAuxAwardEventAuxes.stream()
       .map(AbstractSetsEvent::badgeAwardGenericEventAsEventTag)
       .toList();
  }

  @JsonIgnore
  public final PublicKey getAwardRecipientPublicKey() {
    return tupleDefnEventAuxAwardEventAuxes.getFirst().getAwardRecipientPublicKey();
  }

  public static <T extends SetsPairedEventTagIF> EventTag badgeAwardGenericEventAsEventTag(@NonNull SetsPairedEvents<T> tupleDefnEventAuxAwardEventAux) {
    return new EventTag(
       tupleDefnEventAuxAwardEventAux.getAwardEventId(),
       tupleDefnEventAuxAwardEventAux.getAwardEventRelay().map(Relay::getUrl).orElse(null));
  }

  protected static Stream<BaseTag> filterBaseTags(@NonNull List<BaseTag> baseTags) {
    return baseTags.stream()
       .filter(Predicate.not(EventTag.class::isInstance))
       .filter(Predicate.not(PubKeyTag.class::isInstance))
       .filter(Predicate.not(AddressTag.class::isInstance));
  }
}
