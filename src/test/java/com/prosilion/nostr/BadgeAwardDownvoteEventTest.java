package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericVoteEvent;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.Collections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeAwardDownvoteEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");

  public static final String UNIT_UPVOTE = "UNIT_DOWNVOTE";
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  public final Identity identity = Identity.generateRandomIdentity();
  private final BadgeDefinitionAwardEvent badgeDefnDownvoteEvent = new BadgeDefinitionAwardEvent(identity, downvoteIdentifierTag, relay);

  PublicKey badgeReceiverPublicKey = Identity.generateRandomIdentity().getPublicKey();

  BadgeAwardGenericVoteEvent expected;

  public BadgeAwardDownvoteEventTest() {
    this.expected = new BadgeAwardGenericVoteEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefnDownvoteEvent);
  }

  @Test
  void testValidBadgeAwardReputationEvent() {
    BadgeAwardGenericVoteEvent badgeAwardDownvoteEvent = new BadgeAwardGenericVoteEvent(
        expected.getGenericEventRecord(),
        addressTag -> badgeDefnDownvoteEvent);

    assertEquals(expected, badgeAwardDownvoteEvent);
    assertEquals(
        expected.getContainedAddressableEvents(),
        badgeAwardDownvoteEvent.getContainedAddressableEvents());
  }

  @Test
  void testSingularAddressTag() {
    BadgeAwardGenericVoteEvent badgeAwardDownvoteEvent = new BadgeAwardGenericVoteEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefnDownvoteEvent,
        Collections.unmodifiableList(expected.getContainedAddressableEvents()));

    assertEquals(1, badgeAwardDownvoteEvent.getContainedAddressableEvents().size());
    assertEquals(1, badgeAwardDownvoteEvent.getTypeSpecificTags(AddressTag.class).size());
  }
}
