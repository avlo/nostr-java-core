package com.prosilion.nostr.event;

import com.prosilion.nostr.event.internal.Vote;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import org.springframework.lang.NonNull;

public class BadgeAwardDownvoteEvent extends BadgeAwardAbstractEvent implements AddressableTagsMappedEventsIF {
  private final BadgeDefinitionAwardEvent badgeDefinitionDownvoteEvent;

  public BadgeAwardDownvoteEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey downvotedUser,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionDownvoteEvent) {
    this(authorIdentity, downvotedUser, badgeDefinitionDownvoteEvent, List.of());
  }

  public BadgeAwardDownvoteEvent(
      @NonNull Identity authorIdentity,
      @NonNull PublicKey downvotedUser,
      @NonNull BadgeDefinitionAwardEvent badgeDefinitionDownvoteEvent,
      @NonNull List<BaseTag> tags) {
    super(
        authorIdentity,
        new Vote(
            downvotedUser,
            badgeDefinitionDownvoteEvent).getAwardEvent(),
        tags,
        badgeDefinitionDownvoteEvent.getContent());
    this.badgeDefinitionDownvoteEvent = badgeDefinitionDownvoteEvent;
  }

  public BadgeAwardDownvoteEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, BadgeDefinitionAwardEvent> fxn) {
    super(genericEventRecord);
    this.badgeDefinitionDownvoteEvent = mapAddressableTagsToEvents(this, fxn).getFirst();
  }

  @Override
  public List<AddressTag> getContainedEventsAsAddressTags() {
    return List.of(badgeDefinitionDownvoteEvent.asAddressTag());
  }
}
