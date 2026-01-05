package com.prosilion.nostr.event;

import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import org.springframework.lang.NonNull;

public class BadgeAwardGenericEvent extends BadgeAwardAbstractEvent<BadgeDefinitionAwardEvent> {
  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionAwardEvent) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionAwardEvent, "");
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionAwardEvent,
      @NonNull List<BaseTag> tags) {
    super(
        authorIdentity,
        awardRecipientPublicKey,
        badgeDefinitionAwardEvent,
        tags,
        "");
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionAwardEvent,
      @NonNull String content) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionAwardEvent, List.of(), content);
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionAwardEvent,
      @NonNull List<BaseTag> tags,
      @NonNull String content) {
    super(
        authorIdentity,
        awardRecipientPublicKey,
        badgeDefinitionAwardEvent,
        tags,
        content);
  }

  public BadgeAwardGenericEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, BadgeDefinitionAwardEvent> fxn) {
    super(genericEventRecord, fxn);
  }
  
  public BadgeDefinitionAwardEvent getBadgeDefinitionAwardEvent() {
    return super.getAddressableEvent();
  }
}
