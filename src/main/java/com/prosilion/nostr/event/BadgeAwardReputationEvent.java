package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.math.BigDecimal;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class BadgeAwardReputationEvent extends BadgeAwardGenericEvent<BadgeDefinitionReputationEvent> {
  public BadgeAwardReputationEvent(
      @NonNull Identity aImgIdentity,
      @NonNull PublicKey badgeReceiverPubkey,
      @NonNull Relay relay,
      @NonNull ExternalIdentityTag externalIdentityTag,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull BigDecimal score) {
    this(aImgIdentity, badgeReceiverPubkey, relay, externalIdentityTag, badgeDefinitionReputationEvent, List.of(), score);
  }

  public BadgeAwardReputationEvent(
      @NonNull Identity aImgIdentity,
      @NonNull PublicKey badgeReceiverPubkey,
      @NonNull Relay relay,
      @NonNull ExternalIdentityTag externalIdentityTag,
      @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
      @NonNull List<BaseTag> tags,
      @NonNull BigDecimal score) {
    super(
        aImgIdentity,
        badgeReceiverPubkey,
        relay,
        badgeDefinitionReputationEvent,
        Stream.concat(
            Stream.of(
                externalIdentityTag),
            tags.stream()
                .filter(Predicate.not(ExternalIdentityTag.class::isInstance))),
        score.toString());
  }

  public BadgeAwardReputationEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, BadgeDefinitionReputationEvent> fxn) {
    super(genericEventRecord, fxn);
  }

  @JsonIgnore
  public ExternalIdentityTag getExternalIdentityTag() {
    return requireFirstTag(ExternalIdentityTag.class);
  }

  @JsonIgnore
  public String getScore() {
    return getContent();
  }
}
