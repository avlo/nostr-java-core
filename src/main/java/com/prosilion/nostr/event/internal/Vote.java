package com.prosilion.nostr.event.internal;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.PublicKey;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;

@Getter
@EqualsAndHashCode(callSuper = false)
public class Vote {
  private final AwardEvent awardEvent;

  public Vote(@NonNull PublicKey upvotedUser, @NonNull BadgeDefinitionGenericEvent badgeDefinitionUpvoteEvent) {
    AddressTag addressTag = new AddressTag(
        Kind.BADGE_DEFINITION_EVENT,
        badgeDefinitionUpvoteEvent.getPublicKey(),
        badgeDefinitionUpvoteEvent.getIdentifierTag());

    awardEvent = new AwardEvent(addressTag, new PubKeyTag(upvotedUser));
  }
}

