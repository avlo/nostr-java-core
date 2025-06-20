package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.NostrException;
import com.prosilion.nostr.enums.KindType;
import com.prosilion.nostr.event.internal.Vote;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;

public class BadgeAwardDownvoteEvent extends AbstractBadgeAwardEvent<KindType> {
  private static final Log log = LogFactory.getLog(BadgeAwardDownvoteEvent.class);

  public BadgeAwardDownvoteEvent(
      @NonNull Identity identity,
      @NonNull Identity upvotedUser,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(KindType.DOWNVOTE, identity,
        new Vote(
            identity.getPublicKey(),
            upvotedUser.getPublicKey(),
            KindType.DOWNVOTE).getVoteTags(),
        content);
  }

  public BadgeAwardDownvoteEvent(
      @NonNull Identity identity,
      @NonNull Identity upvotedUser,
      @NonNull List<BaseTag> tags,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(KindType.DOWNVOTE, identity,
        Stream.concat(new Vote(
            identity.getPublicKey(),
            upvotedUser.getPublicKey(),
            KindType.DOWNVOTE).getVoteTags().stream(), tags.stream()).toList()
        , content);
  }
}
