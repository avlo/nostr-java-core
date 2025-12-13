package com.prosilion.nostr.event;

import com.prosilion.nostr.event.internal.Vote;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeAwardUpvoteEvent extends BadgeAwardAbstractEvent {
  @Getter
  private final BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent;

  public BadgeAwardUpvoteEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey upvotedUser,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent) {
    this(authorIdentity, upvotedUser, badgeDefinitionUpvoteEvent, List.of());
  }

  public BadgeAwardUpvoteEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey upvotedUser,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionUpvoteEvent,
      @NonNull List<BaseTag> tags) {
    super(
        authorIdentity,
        new Vote(
            upvotedUser,
            badgeDefinitionUpvoteEvent).getAwardEvent(),
        tags,
        badgeDefinitionUpvoteEvent.getContent());
    this.badgeDefinitionUpvoteEvent = badgeDefinitionUpvoteEvent;
  }

  public BadgeAwardUpvoteEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, BadgeDefinitionAwardEvent> fxn) {
    super(genericEventRecord);
    this.badgeDefinitionUpvoteEvent = mapTagsToEvents(this, fxn, AddressTag.class).getFirst();
  }

  @Override
  public List<AddressTag> getContainedEventsAsAddressTags() {
    return List.of(badgeDefinitionUpvoteEvent.asAddressTag());
  }
}
