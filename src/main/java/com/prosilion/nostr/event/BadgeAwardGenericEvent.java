package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class BadgeAwardGenericEvent<T extends BadgeDefinitionGenericEvent> extends BadgeAwardAbstractEvent<T> {
  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull Relay relay,
      @NonNull T badgeDefinitionGenericEvent) {
    this(authorIdentity, awardRecipientPublicKey, relay, badgeDefinitionGenericEvent, "");
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull Relay relay,
      @NonNull T badgeDefinitionGenericEvent,
      @NonNull List<BaseTag> tags) {
    this(authorIdentity, awardRecipientPublicKey, relay, badgeDefinitionGenericEvent, tags, "");
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull Relay relay,
      @NonNull T badgeDefinitionGenericEvent,
      @NonNull String content) {
    this(authorIdentity, awardRecipientPublicKey, relay, badgeDefinitionGenericEvent, List.of(), content);
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull Relay relay,
      @NonNull T badgeDefinitionGenericEvent,
      @NonNull List<BaseTag> tags,
      @NonNull String content) {
    this(authorIdentity, awardRecipientPublicKey, relay, badgeDefinitionGenericEvent, tags.stream(), content);
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull Relay relay,
      @NonNull T badgeDefinitionGenericEvent,
      @NonNull Stream<BaseTag> tags,
      @NonNull String content) {
    super(authorIdentity, awardRecipientPublicKey, relay, badgeDefinitionGenericEvent, tags, content);
  }

  public BadgeAwardGenericEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, T> fxn) {
    super(genericEventRecord, fxn);
  }

  @JsonIgnore
  public T getBadgeAwardGenericEvent() {
    return super.getAddressableEvent();
  }

  @Override
  @JsonIgnore
  public List<AddressTag> getContainedAddressableEvents() {
    return List.of(getBadgeAwardGenericEvent().asAddressTag());
  }
}
