package com.prosilion.nostr.curated;

import com.prosilion.nostr.EventTestFixtures;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class AbstractSetsEventTest extends EventTestFixtures {
  private final IdentifierTag expectedBadgeDefinitionUpvoteHashedIdentifierTag = new IdentifierTag("000000000000000000000000000000000000000000000000000000005187e8c5");
  private final IdentifierTag expectedBadgeDefinitionDownvoteHashedIdentifierTag = new IdentifierTag("000000000000000000000000000000000000000000000000000000005c19c50c");
  
  private final AddressTag badgeDefinitionUpvoteEventAsAddressTag;
  private final AddressTag badgeDefinitionDownvoteEventAsAddressTagWithoutRelay;

  public AbstractSetsEventTest() {
    this.badgeDefinitionUpvoteEventAsAddressTag = new AddressTag(
       Kind.BADGE_DEFINITION_EVENT,
       upvoteDefnCreator.getPublicKey(),
       upvoteIdentifierTag,
       relay);

    this.badgeDefinitionDownvoteEventAsAddressTagWithoutRelay = new AddressTag(
       Kind.BADGE_DEFINITION_EVENT,
       upvoteDefnCreator.getPublicKey(),
       downvoteIdentifierTag);
  }

  @Test
  final void testBadgeDefinitionUpvoteEventAsAddressTagHashValue() {
    assertEquals(expectedBadgeDefinitionUpvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(badgeDefinitionUpvoteEventAsAddressTag));

    AddressTag actualContainsRelay = new AddressTag(
       badgeDefinitionUpvoteEventAsAddressTag.getKind(),
       badgeDefinitionUpvoteEventAsAddressTag.getPublicKey(),
       badgeDefinitionUpvoteEventAsAddressTag.getIdentifierTag(),
       badgeDefinitionUpvoteEventAsAddressTag.getRelay());
    assertEquals(badgeDefinitionUpvoteEventAsAddressTag, actualContainsRelay);
    assertEquals(expectedBadgeDefinitionUpvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(actualContainsRelay));

    AddressTag actualNoContainsRelay = new AddressTag(
       badgeDefinitionUpvoteEventAsAddressTag.getKind(),
       badgeDefinitionUpvoteEventAsAddressTag.getPublicKey(),
       badgeDefinitionUpvoteEventAsAddressTag.getIdentifierTag());
    assertEquals(badgeDefinitionUpvoteEventAsAddressTag, actualNoContainsRelay);
    assertEquals(expectedBadgeDefinitionUpvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(actualNoContainsRelay));

    AddressTag actualRelayTagIsNull = new AddressTag(
       badgeDefinitionUpvoteEventAsAddressTag.getKind(),
       badgeDefinitionUpvoteEventAsAddressTag.getPublicKey(),
       badgeDefinitionUpvoteEventAsAddressTag.getIdentifierTag(),
       null);
    assertEquals(badgeDefinitionUpvoteEventAsAddressTag, actualRelayTagIsNull);
    assertEquals(expectedBadgeDefinitionUpvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(actualRelayTagIsNull));

    AddressTag actualRelayTagDifferentValue = new AddressTag(
       badgeDefinitionUpvoteEventAsAddressTag.getKind(),
       badgeDefinitionUpvoteEventAsAddressTag.getPublicKey(),
       badgeDefinitionUpvoteEventAsAddressTag.getIdentifierTag(),
       new Relay("ws://localhost-nomatch:5555"));
    assertEquals(badgeDefinitionUpvoteEventAsAddressTag, actualRelayTagDifferentValue);
    assertEquals(expectedBadgeDefinitionUpvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(actualRelayTagDifferentValue));

    AddressTag expectedWithoutRelayTag = new AddressTag(
       Kind.BADGE_DEFINITION_EVENT,
       upvoteDefnCreator.getPublicKey(),
       upvoteIdentifierTag);

    assertEquals(expectedBadgeDefinitionUpvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(expectedWithoutRelayTag));
  }

  @Test
  final void testBadgeDefinitionDownvoteEventAsAddressTagHashValue() {
    assertEquals(expectedBadgeDefinitionDownvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(badgeDefinitionDownvoteEventAsAddressTagWithoutRelay));

    AddressTag actualContainsRelay = new AddressTag(
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getKind(),
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getPublicKey(),
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getIdentifierTag(),
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getRelay());
    assertEquals(badgeDefinitionDownvoteEventAsAddressTagWithoutRelay, actualContainsRelay);
    assertEquals(expectedBadgeDefinitionDownvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(actualContainsRelay));

    AddressTag actualNoContainsRelay = new AddressTag(
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getKind(),
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getPublicKey(),
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getIdentifierTag());
    assertEquals(badgeDefinitionDownvoteEventAsAddressTagWithoutRelay, actualNoContainsRelay);
    assertEquals(expectedBadgeDefinitionDownvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(actualNoContainsRelay));

    AddressTag actualRelayTagIsNull = new AddressTag(
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getKind(),
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getPublicKey(),
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getIdentifierTag(),
       null);
    assertEquals(badgeDefinitionDownvoteEventAsAddressTagWithoutRelay, actualRelayTagIsNull);
    assertEquals(expectedBadgeDefinitionDownvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(actualRelayTagIsNull));

    AddressTag actualRelayTagDifferentValue = new AddressTag(
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getKind(),
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getPublicKey(),
       badgeDefinitionDownvoteEventAsAddressTagWithoutRelay.getIdentifierTag(),
       new Relay("ws://localhost-nomatch:5555"));
    assertEquals(badgeDefinitionDownvoteEventAsAddressTagWithoutRelay, actualRelayTagDifferentValue);
    assertEquals(expectedBadgeDefinitionDownvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(actualRelayTagDifferentValue));

    AddressTag expectedWRelayTag = new AddressTag(
       Kind.BADGE_DEFINITION_EVENT,
       upvoteDefnCreator.getPublicKey(),
       downvoteIdentifierTag,
       new Relay("ws://localhost:5555"));

    assertEquals(expectedBadgeDefinitionDownvoteHashedIdentifierTag, AbstractSetsEvent.hashedAddressTag(expectedWRelayTag));
  }
}
