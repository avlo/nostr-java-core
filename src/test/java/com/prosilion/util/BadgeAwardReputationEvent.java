package com.prosilion.util;

import com.prosilion.nostr.event.AddressableTagsMappedEventsIF;
import com.prosilion.nostr.event.BadgeAwardAbstractEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeAwardReputationEvent extends BadgeAwardAbstractEvent implements AddressableTagsMappedEventsIF {
  @Getter
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  public BadgeAwardReputationEvent(
      @NonNull Identity aImgIdentity,
      @NonNull PublicKey badgeReceiverPubkey,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull BigDecimal score) {
    this(aImgIdentity, badgeReceiverPubkey, badgeDefinitionReputationEvent, List.of(), score);
  }

  public BadgeAwardReputationEvent(
      @NonNull Identity aImgIdentity,
      @NonNull PublicKey badgeReceiverPubkey,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull List<BaseTag> tags,
      @NonNull BigDecimal score) {
    super(
        aImgIdentity,
        new Reputation(
            badgeReceiverPubkey,
            badgeDefinitionReputationEvent).getAwardEvent(),
        tags,
        score.toString());
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
  }

  public BadgeAwardReputationEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxn) {
    super(genericEventRecord);
    this.badgeDefinitionReputationEvent = mapAddressableTagsToEvents(this, fxn).getFirst();
  }

  @Override
  public List<AddressTag> getBadgeDefinitionAwardEventsAsAddressTags() {
    return List.of(badgeDefinitionReputationEvent.asAddressTag());
  }
}
