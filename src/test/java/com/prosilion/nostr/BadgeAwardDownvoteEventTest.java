package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.curated.BadgeAwardCanonicalEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeAwardDownvoteEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");

  public static final String UNIT_UPVOTE = "UNIT_DOWNVOTE";
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  public final Identity identity = Identity.generateRandomIdentity();
  private final BadgeDefinitionGenericEvent badgeDefnDownvoteEvent;

  PublicKey badgeReceiverPublicKey = Identity.generateRandomIdentity().getPublicKey();

  BadgeAwardCanonicalEvent expected;

  public BadgeAwardDownvoteEventTest() {
    this.badgeDefnDownvoteEvent = new BadgeDefinitionGenericEvent(identity, downvoteIdentifierTag, relay);
    this.expected = new BadgeAwardCanonicalEvent(
       identity,
       badgeReceiverPublicKey,
       badgeDefnDownvoteEvent, relay
    );
  }

  @Test
  void testValidBadgeAwardReputationEvent() {
    BadgeAwardCanonicalEvent badgeAwardDownvoteEvent = new BadgeAwardCanonicalEvent(
       expected.getGenericEventRecord(),
       addressTag -> badgeDefnDownvoteEvent);

    assertEquals(expected, badgeAwardDownvoteEvent);
    assertEquals(
       expected.getAddressableEvent().asAddressableEventAddressTag(),
       badgeAwardDownvoteEvent.getAddressableEvent().asAddressableEventAddressTag());
  }

  @Test
  void testSingularAddressTag() {
    BadgeAwardCanonicalEvent badgeAwardDownvoteEvent = new BadgeAwardCanonicalEvent(
       identity,
       badgeReceiverPublicKey,
       badgeDefnDownvoteEvent, List.of(expected.getAddressableEvent().asAddressableEventAddressTag()), relay
    );

    assertEquals(1, List.of(badgeAwardDownvoteEvent.getAddressableEvent().asAddressableEventAddressTag()).size());
    assertEquals(1, badgeAwardDownvoteEvent.getTypeSpecificTags(AddressTag.class).size());
  }
}
