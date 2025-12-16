package com.prosilion.nostr.event;

import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import org.springframework.lang.NonNull;

public class BadgeAwardGenericVoteEvent extends BadgeAwardAbstractEvent<BadgeDefinitionAwardEvent> {
  public BadgeAwardGenericVoteEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionGenericVoteEvent) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericVoteEvent, "");
  }

  public BadgeAwardGenericVoteEvent(
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

  public BadgeAwardGenericVoteEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey awardRecipientPublicKey,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionGenericVoteEvent,
      @NonNull String content) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericVoteEvent, List.of(), content);
  }

  public BadgeAwardGenericVoteEvent(
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

  public BadgeAwardGenericVoteEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, BadgeDefinitionAwardEvent> fxn) {
    super(genericEventRecord, fxn);
  }
}
