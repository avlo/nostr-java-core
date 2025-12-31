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
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionGenericVoteEvent) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericVoteEvent, "");
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionGenericVoteEvent,
      @NonNull List<BaseTag> tags) {
    super(
        authorIdentity,
        awardRecipientPublicKey,
        badgeDefinitionGenericVoteEvent,
        tags,
        "");
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionGenericVoteEvent,
      @NonNull String content) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericVoteEvent, List.of(), content);
  }

  public BadgeAwardGenericEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionGenericVoteEvent,
      @NonNull List<BaseTag> tags,
      @NonNull String content) {
    super(
        authorIdentity,
        awardRecipientPublicKey,
        badgeDefinitionGenericVoteEvent,
        tags,
        content);
  }

  public BadgeAwardGenericEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, BadgeDefinitionAwardEvent> fxn) {
    super(genericEventRecord, fxn);
  }
}
