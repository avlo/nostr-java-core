package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.util.Util;
import java.util.Collections;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeAwardDownvoteEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");

  public static final String UNIT_UPVOTE = "UNIT_DOWNVOTE";
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  public final Identity identity = Identity.generateRandomIdentity();
  private final BadgeDefinitionGenericEvent badgeDefnDownvoteEvent;

  PublicKey badgeReceiverPublicKey = Identity.generateRandomIdentity().getPublicKey();

  BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> expected;

  public BadgeAwardDownvoteEventTest() {
    this.badgeDefnDownvoteEvent = new BadgeDefinitionGenericEvent(identity, downvoteIdentifierTag, relay);
    this.expected = new BadgeAwardGenericEvent<>(
        identity,
        badgeReceiverPublicKey,
        relay,
        badgeDefnDownvoteEvent);
  }

  @Test
  void testValidBadgeAwardReputationEvent() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardDownvoteEvent = new BadgeAwardGenericEvent<>(
        expected.getGenericEventRecord(),
        addressTag -> badgeDefnDownvoteEvent);

    assertEquals(expected, badgeAwardDownvoteEvent);
    assertEquals(
        expected.getAddressableEvent().asAddressTag(),
        badgeAwardDownvoteEvent.getAddressableEvent().asAddressTag());

    System.out.println(Util.prettyPrintAddressTags(expected.getAddressableEvent().asAddressTag()));
    System.out.println(Util.prettyPrintAddressTags(expected.getContainedAddressableEvents()));
    assertEquals(
        expected.getContainedAddressableEvents(),
        badgeAwardDownvoteEvent.getContainedAddressableEvents());
  }

  @Test
  void testSingularAddressTag() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardDownvoteEvent = new BadgeAwardGenericEvent<>(
        identity,
        badgeReceiverPublicKey,
        relay,
        badgeDefnDownvoteEvent,
        Collections.unmodifiableList(expected.getContainedAddressableEvents()));

    assertEquals(1, badgeAwardDownvoteEvent.getContainedAddressableEvents().size());
    assertEquals(1, badgeAwardDownvoteEvent.getTypeSpecificTags(AddressTag.class).size());
  }
}
