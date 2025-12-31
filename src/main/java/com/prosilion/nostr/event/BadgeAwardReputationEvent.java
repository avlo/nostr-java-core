package com.prosilion.nostr.event;

import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeAwardReputationEvent extends BadgeAwardAbstractEvent<BadgeDefinitionReputationEvent> {
  @Getter
  private final BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  public BadgeAwardReputationEvent(
      @NonNull Identity aImgIdentity,
      @NonNull PublicKey badgeReceiverPubkey,
      @NonNull ExternalIdentityTag externalIdentityTag,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull BigDecimal score) {
    this(aImgIdentity, badgeReceiverPubkey, externalIdentityTag, badgeDefinitionReputationEvent, List.of(), score);
  }

  public BadgeAwardReputationEvent(
      @NonNull Identity aImgIdentity,
      @NonNull PublicKey badgeReceiverPubkey,
      @NonNull ExternalIdentityTag externalIdentityTag,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull List<BaseTag> tags,
      @NonNull BigDecimal score) {
    super(
        aImgIdentity,
        badgeReceiverPubkey,
        badgeDefinitionReputationEvent,
        Stream.concat(
            Stream.of(externalIdentityTag),
            tags.stream()).toList(),
        score.toString());
    this.badgeDefinitionReputationEvent = badgeDefinitionReputationEvent;
  }

  public BadgeAwardReputationEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxn) {
    super(genericEventRecord, fxn);
    this.badgeDefinitionReputationEvent = mapTagsToEvents(this, fxn, AddressTag.class).getFirst();
  }

  @Override
  public List<AddressTag> getContainedAddressableEvents() {
    return badgeDefinitionReputationEvent.getContainedAddressableEvents();
  }

  public ExternalIdentityTag getExternalIdentityTag() {
    return getTypeSpecificTags(ExternalIdentityTag.class).getFirst();
  }
}
