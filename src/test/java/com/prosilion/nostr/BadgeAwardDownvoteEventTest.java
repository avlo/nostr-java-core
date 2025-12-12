package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardDownvoteEvent;
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

  BadgeAwardDownvoteEvent expected;

  public BadgeAwardDownvoteEventTest() {
    this.expected = new BadgeAwardDownvoteEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefnDownvoteEvent);
  }

  @Test
  void testValidBadgeAwardReputationEvent() {
    BadgeAwardDownvoteEvent badgeAwardDownvoteEvent = new BadgeAwardDownvoteEvent(
        expected.getGenericEventRecord(),
        addressTag -> badgeDefnDownvoteEvent);

    assertEquals(expected, badgeAwardDownvoteEvent);
    assertEquals(
        expected.getContainedEventsAsTags(),
        badgeAwardDownvoteEvent.getContainedEventsAsTags());
  }

  @Test
  void testSingularAddressTag() {
    BadgeAwardDownvoteEvent badgeAwardDownvoteEvent = new BadgeAwardDownvoteEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefnDownvoteEvent,
        Collections.unmodifiableList(expected.getContainedEventsAsTags()));

    assertEquals(1, badgeAwardDownvoteEvent.getContainedEventsAsTags().size());
    assertEquals(1, badgeAwardDownvoteEvent.getTypeSpecificTags(AddressTag.class).size());
  }
}
