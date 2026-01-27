package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class BadgeAwardGenericEvent<T extends BadgeDefinitionAwardEvent> extends BadgeAwardAbstractEvent<T> {
  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull T badgeDefinitionAwardEvent) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionAwardEvent, "");
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull T badgeDefinitionAwardEvent,
      @NonNull List<BaseTag> tags) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionAwardEvent, tags, "");
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull T badgeDefinitionAwardEvent,
      @NonNull String content) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionAwardEvent, List.of(), content);
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull T badgeDefinitionAwardEvent,
      @NonNull List<BaseTag> tags,
      @NonNull String content) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionAwardEvent, tags.stream(), content);
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull T badgeDefinitionAwardEvent,
      @NonNull Stream<BaseTag> tags,
      @NonNull String content) {
    super(authorIdentity, awardRecipientPublicKey, badgeDefinitionAwardEvent, tags, content);
  }

  public BadgeAwardGenericEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, T> fxn) {
    super(genericEventRecord, fxn);
  }

  @JsonIgnore
  public T getBadgeDefinitionAwardEvent() {
    return super.getAddressableEvent();
  }

  @JsonIgnore
  public List<AddressTag> getContainedAddressableEvents() {
    return List.of(getBadgeDefinitionAwardEvent().asAddressTag());
  }
}
