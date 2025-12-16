package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public abstract class BadgeAwardAbstractEvent<T extends AddressableEvent> extends UniqueAddressTagEvent implements TagMappedEventIF {
  @Getter
  private final T addressableEvent;

  public BadgeAwardAbstractEvent(
      @NonNull Identity identity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull T badgeDefinitionAwardEvent,
      @NonNull String content) throws NostrException {
    this(identity, awardRecipientPublicKey, badgeDefinitionAwardEvent, List.of(), content);
  }

  public BadgeAwardAbstractEvent(
      @NonNull Identity identity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull T badgeDefinitionAwardEvent,
      @NonNull List<BaseTag> tags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.BADGE_AWARD_EVENT,
        badgeDefinitionAwardEvent.asAddressTag(),
        Stream.concat(
            Stream.of(new PubKeyTag(awardRecipientPublicKey)),
            tags.stream()
                .filter(Predicate.not(AddressTag.class::isInstance))
                .filter(Predicate.not(PubKeyTag.class::isInstance))).toList(),
        content);
    this.addressableEvent = badgeDefinitionAwardEvent;
  }

  public BadgeAwardAbstractEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, T> fxn) {
    super(genericEventRecord);
    this.addressableEvent = mapTagsToEvents(this, fxn, AddressTag.class).getFirst();
  }

  @Override
  public List<AddressTag> getContainedAddressableEvents() {
    return List.of(addressableEvent.asAddressTag());
  }
}
