package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.BadgeAwardReputationEvent;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.math.BigDecimal;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeAwardReputationEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");

  public static final String REPUTATION = "REPUTATION";
  public static final String UNIT_UPVOTE = "UNIT_UPVOTE";

  public final IdentifierTag reputationIdentifierTag = new IdentifierTag(REPUTATION);
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  public final Identity identity = Identity.generateRandomIdentity();
  private final BadgeDefinitionAwardEvent badgeDefnUpvoteEvent = new BadgeDefinitionAwardEvent(identity, upvoteIdentifierTag, relay);

  public static final String PLATFORM = BadgeAwardReputationEventTest.class.getPackageName();
  public static final String IDENTITY = BadgeAwardReputationEventTest.class.getSimpleName();
  public static final String PROOF = String.valueOf(BadgeAwardReputationEventTest.class.hashCode());

  public static final String FORMULA_PLUS_ONE = "FORMULA_PLUS_ONE";
  private final IdentifierTag formulaPlusOneIdentifierTag = new IdentifierTag(FORMULA_PLUS_ONE);

  public static final String PLUS_ONE_FORMULA = "+1";
  private final FormulaEvent plusOneFormulaEvent = new FormulaEvent(identity, formulaPlusOneIdentifierTag, badgeDefnUpvoteEvent, PLUS_ONE_FORMULA);
  private final ExternalIdentityTag externalIdentityTag = new ExternalIdentityTag(PLATFORM, IDENTITY, PROOF);

  PublicKey badgeReceiverPublicKey = Identity.generateRandomIdentity().getPublicKey();

  BadgeDefinitionReputationEvent badgeDefinitionReputationEvent;

  public BadgeAwardReputationEventTest() throws ParseException {
    this.badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
        identity,
        reputationIdentifierTag,
        relay,
        externalIdentityTag,
        plusOneFormulaEvent);
  }

  @Test
  void testValidBadgeAwardReputationEvent() {
    BadgeAwardReputationEvent expected = new BadgeAwardReputationEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefinitionReputationEvent,
        BigDecimal.ZERO);

    BadgeAwardReputationEvent badgeAwardReputationEvent = new BadgeAwardReputationEvent(
        expected.getGenericEventRecord(),
        addressTag -> badgeDefinitionReputationEvent);

    assertEquals(expected, badgeAwardReputationEvent);
    assertEquals(expected.getBadgeDefinitionReputationEvent(), badgeAwardReputationEvent.getBadgeDefinitionReputationEvent());
    assertEquals(expected.getContainedEventsAsAddressTags(), badgeAwardReputationEvent.getContainedEventsAsAddressTags());
  }

  @Test
  void testSingularAddressTag() {
    BadgeAwardReputationEvent badgeAwardReputationEvent = new BadgeAwardReputationEvent(
        identity,
        badgeReceiverPublicKey,
        badgeDefinitionReputationEvent,
        List.of(badgeDefinitionReputationEvent.asAddressTag()),
        BigDecimal.ZERO);

    assertEquals(1, badgeAwardReputationEvent.getContainedEventsAsAddressTags().size());
    assertEquals(1, badgeAwardReputationEvent.getTypeSpecificTags(AddressTag.class).size());
    assertEquals(1, badgeAwardReputationEvent.getBadgeDefinitionReputationEvent().getTypeSpecificTags(IdentifierTag.class).size());
  }
}
