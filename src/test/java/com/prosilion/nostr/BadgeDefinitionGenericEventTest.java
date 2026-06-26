package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeDefinitionGenericEventTest {
  public static final String UNIT_UPVOTE = "UNIT_UPVOTE";
  public static final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
  public static final Identity aImgidentity = Identity.generateRandomIdentity();

  public static final String relayArgUrl = "ws://localhost:5555";
  public static final Relay relayArgRelay = new Relay(relayArgUrl);
  public static final RelayTag relayArgRelayTag = new RelayTag(relayArgRelay);

  public static final String baseTagsRelayUrl = "ws://localhost-from-relay-tag:5555";
  public static final Relay baseTagsRelay = new Relay(baseTagsRelayUrl);
  public static final RelayTag baseTagsRelayTag = new RelayTag(baseTagsRelay);

  @Test
  final void A_NoNo__testEventNullRelayNoRelayTag() {
    BadgeDefinitionGenericEvent badgeDefnUpvoteEvent = new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag);
    assertEquals(Optional.empty(), badgeDefnUpvoteEvent.getRelayTag());
    assertEquals(Optional.empty(), badgeDefnUpvoteEvent.getRelayTag().map(RelayTag::getRelay));
    assertEquals(Optional.empty(), badgeDefnUpvoteEvent.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl));
    assertThrows(Exception.class, () -> badgeDefnUpvoteEvent.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl).orElseThrow());
    assertThrows(NostrException.class, () -> badgeDefnUpvoteEvent.requireFirstTag(RelayTag.class));

    assertTrue(badgeDefnUpvoteEvent.getRelayTag().isEmpty());
    assertTrue(badgeDefnUpvoteEvent.findFirstTag(RelayTag.class).isEmpty());
    assertTrue(badgeDefnUpvoteEvent.getTypeSpecificTags(RelayTag.class).isEmpty());
    assertThrows(NostrException.class, () -> badgeDefnUpvoteEvent.requireFirstTag(RelayTag.class));

//    relayTag.getRelay related    
    assertTrue(badgeDefnUpvoteEvent.getRelay().isEmpty());
    assertTrue(badgeDefnUpvoteEvent.getRelayTag().map(RelayTag::getRelay).isEmpty());

    AddressTag addressableEventAddressTag = badgeDefnUpvoteEvent.asAddressableEventAddressTag();
    assertEquals(badgeDefnUpvoteEvent.getKind(), addressableEventAddressTag.getKind());
    assertEquals(badgeDefnUpvoteEvent.getPublicKey(), addressableEventAddressTag.getPublicKey());
    assertEquals(badgeDefnUpvoteEvent.getIdentifierTag(), addressableEventAddressTag.requireIdentifierTag());

    assertEquals(Optional.empty(), badgeDefnUpvoteEvent.getRelay());
    assertEquals(Optional.empty(), badgeDefnUpvoteEvent.getRelay().map(Relay::getUrl));
    assertEquals(Optional.empty(), badgeDefnUpvoteEvent.getRelay().map(Relay::getUrl).map(String::toString));
    assertNull(addressableEventAddressTag.getRelay());
  }

  @Test
  final void B_NoYes__testEventNullRelayHasRelayTag() {
    testTags(baseTagsRelayTag,
       new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(baseTagsRelayTag), ""));
  }

  @Test
  final void C_YesNo__testEventHasRelayNoRelayTag() {
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, relayArgRelay));
  }

  @Test
  final void D_YesYes__testEventHasRelayHasRelayTag() {
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(baseTagsRelayTag), "", relayArgRelay));
  }

  @Test
  final void Z_testEventNullRelayMultipleRelayTags() {
    RelayTag relayTag = new RelayTag(new Relay("ws://localhost-from-another-relay-tag:5555"));
    testTags(baseTagsRelayTag,
       new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(baseTagsRelayTag, relayTag), ""));

    testTags(relayTag,
       new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(relayTag, baseTagsRelayTag), ""));
  }

  @Test
  final void testEventValidBadgeDefinitionGenericEventMultipleRelayTags() {
    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-should-not-appear:5555"));
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(anotherRelayTag), "content", relayArgRelay));
  }

  @Test
  final void testBadgeDefinitionGenericEventCtor() {
    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-should-not-appear:5555"));
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEvent(
          new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(anotherRelayTag), "content", relayArgRelay).asGenericEventRecord()));
  }

  @Test
  final void testEventValidBadgeDefinitionGenericEventWithoutRelayTagWithRelayBaseTagContaingNullRelay() {
    assertThrows(IllegalArgumentException.class, () ->
       new BadgeDefinitionGenericEvent(
          aImgidentity,
          upvoteIdentifierTag,
          List.of(new RelayTag(null)),
          "testValidBadgeDefinitionGenericEventWithoutRelayTagWithRelayBaseTag"));
  }

  private void testTags(RelayTag relayTag, BadgeDefinitionGenericEvent event) {
    assertTrue(event.getRelay().isPresent());
    assertTrue(event.findFirstTag(RelayTag.class).isPresent());
    assertEquals(relayTag, event.getRelayTag().orElseThrow());
    assertEquals(relayTag, event.requireFirstTag(RelayTag.class));
    assertEquals(relayTag.getRelay(), event.getRelayTag().map(RelayTag::getRelay).orElseThrow());
    assertEquals(relayTag.getRelay().getUrl(), event.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl).orElseThrow());
    assertEquals(1, event.getTypeSpecificTags(RelayTag.class).size());

    AddressTag addressableEventAddressTag = event.asAddressableEventAddressTag();
    assertEquals(event.getKind(), addressableEventAddressTag.getKind());
    assertEquals(event.getPublicKey(), addressableEventAddressTag.getPublicKey());
    assertEquals(event.getIdentifierTag(), addressableEventAddressTag.requireIdentifierTag());

    assertTrue(event.getRelay().isPresent());
    assertEquals(relayTag.getRelay().getUrl(), event.getRelay().map(Relay::getUrl).orElseThrow());
    assertEquals(relayTag.getRelay().getUrl(), event.getRelay().map(Relay::getUrl).map(String::toString).orElseThrow());
    assertNotNull(addressableEventAddressTag.getRelay());
  }
}
