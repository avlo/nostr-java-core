package com.prosilion.nostr.event;

import com.prosilion.nostr.event.internal.Reputation;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeAwardReputationEvent extends BadgeAwardAbstractEvent {
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
    this.badgeDefinitionReputationEvent = mapTagsToEvents(this, fxn, AddressTag.class).getFirst();
  }

  @Override
  public List<AddressTag> getContainedEventsAsAddressTags() {
    return badgeDefinitionReputationEvent.getContainedEventsAsAddressTags();
  }
}
