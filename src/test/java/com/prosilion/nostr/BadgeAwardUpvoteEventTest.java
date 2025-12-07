package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeAwardUpvoteEvent;
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

  BadgeAwardUpvoteEvent expected;

  public BadgeAwardUpvoteEventTest() throws ParseException {
    this.expected = new BadgeAwardUpvoteEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefnUpvoteEvent);
  }

  @Test
  void testValidBadgeAwardReputationEvent() {
    BadgeAwardUpvoteEvent badgeAwardUpvoteEvent = new BadgeAwardUpvoteEvent(
        expected.getGenericEventRecord(),
        addressTag -> badgeDefnUpvoteEvent);

    assertEquals(expected, badgeAwardUpvoteEvent);
    assertEquals(
        expected.getContainedEventsAsAddressTags(),
        badgeAwardUpvoteEvent.getContainedEventsAsAddressTags());
  }

  @Test
  void testSingularAddressTag() {
    BadgeAwardUpvoteEvent badgeAwardUpvoteEvent = new BadgeAwardUpvoteEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefnUpvoteEvent,
        Collections.unmodifiableList(expected.getContainedEventsAsAddressTags()));

    assertEquals(1, badgeAwardUpvoteEvent.getContainedEventsAsAddressTags().size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(AddressTag.class).size());
  }
}
