package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.NostrException;
import com.prosilion.nostr.enums.Type;
import com.prosilion.nostr.event.internal.Vote;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Stream;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;

public class BadgeAwardUpvoteEvent extends AbstractBadgeAwardEvent<Type> {
  private static final Log log = LogFactory.getLog(BadgeAwardUpvoteEvent.class);

  public BadgeAwardUpvoteEvent(
      @NonNull Identity identity,
      @NonNull Identity upvotedUser,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(Type.UPVOTE, identity,
        new Vote(
            identity.getPublicKey(),
            upvotedUser.getPublicKey(),
            Type.UPVOTE).getVoteTags(),
        content);
  }

  public BadgeAwardUpvoteEvent(
      @NonNull Identity identity,
      @NonNull Identity upvotedUser,
      @NonNull List<BaseTag> tags,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(Type.UPVOTE, identity,
        Stream.concat(new Vote(
            identity.getPublicKey(),
            upvotedUser.getPublicKey(),
            Type.UPVOTE).getVoteTags().stream(), tags.stream()).toList()
        , content);
  }
}
