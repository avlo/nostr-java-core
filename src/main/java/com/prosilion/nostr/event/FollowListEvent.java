package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import lombok.NonNull;

public class FollowListEvent extends BaseEvent {
  public FollowListEvent(@NonNull Identity identity, @NonNull List<BaseTag> tags) throws NostrException {
    super(identity, Kind.FOLLOW_LIST, tags);
  }

  public FollowListEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
