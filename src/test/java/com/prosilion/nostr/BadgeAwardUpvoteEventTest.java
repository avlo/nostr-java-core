package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.Collections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeAwardUpvoteEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");

  public static final String UNIT_UPVOTE = "UNIT_UPVOTE";
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  public final Identity identity = Identity.generateRandomIdentity();
  private final BadgeDefinitionAwardEvent badgeDefnUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag, relay);

  PublicKey badgeReceiverPublicKey = Identity.generateRandomIdentity().getPublicKey();

  BadgeAwardGenericEvent expected;

  public BadgeAwardUpvoteEventTest() {
    this.expected = new BadgeAwardGenericEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefnUpvoteEvent);
  }

  @Test
  void testValidBadgeAwardReputationEvent() {
    BadgeAwardGenericEvent badgeAwardUpvoteEvent = new BadgeAwardGenericEvent(
        expected.getGenericEventRecord(),
        addressTag -> badgeDefnUpvoteEvent);

    assertEquals(expected, badgeAwardUpvoteEvent);
    assertEquals(
        expected.getContainedAddressableEvents(),
        badgeAwardUpvoteEvent.getContainedAddressableEvents());
  }

  @Test
  void testSingularAddressTag() {
    BadgeAwardGenericEvent badgeAwardUpvoteEvent = new BadgeAwardGenericEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefnUpvoteEvent,
        Collections.unmodifiableList(expected.getContainedAddressableEvents()));

    assertEquals(1, badgeAwardUpvoteEvent.getContainedAddressableEvents().size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(AddressTag.class).size());
  }
}
