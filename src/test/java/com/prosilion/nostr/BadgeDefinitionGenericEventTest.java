package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.RelayTag;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeDefinitionGenericEventTest extends BaseEventTest {
  @Test
  final void A_NoNo__testEventNullRelayNoRelayTag() {
    assertEquals(Optional.empty(), defnEvent_NoNo_Upvote.getRelayTag());
    assertEquals(Optional.empty(), defnEvent_NoNo_Upvote.getRelayTag().map(RelayTag::getRelay));
    assertEquals(Optional.empty(), defnEvent_NoNo_Upvote.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl));
    assertThrows(Exception.class, () -> defnEvent_NoNo_Upvote.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl).orElseThrow());
    assertThrows(NostrException.class, () -> defnEvent_NoNo_Upvote.requireFirstTag(RelayTag.class));

    assertTrue(defnEvent_NoNo_Upvote.getRelayTag().isEmpty());
    assertTrue(defnEvent_NoNo_Upvote.findFirstTag(RelayTag.class).isEmpty());
    assertTrue(defnEvent_NoNo_Upvote.getTypeSpecificTags(RelayTag.class).isEmpty());
    assertThrows(NostrException.class, () -> defnEvent_NoNo_Upvote.requireFirstTag(RelayTag.class));

//    relayTag.getRelay related    
    assertTrue(defnEvent_NoNo_Upvote.getRelay().isEmpty());
    assertTrue(defnEvent_NoNo_Upvote.getRelayTag().map(RelayTag::getRelay).isEmpty());

    AddressTag addressableEventAddressTag = defnEvent_NoNo_Upvote.asAddressableEventAddressTag();
    assertEquals(defnEvent_NoNo_Upvote.getKind(), addressableEventAddressTag.getKind());
    assertEquals(defnEvent_NoNo_Upvote.getPublicKey(), addressableEventAddressTag.getPublicKey());
    assertEquals(defnEvent_NoNo_Upvote.getIdentifierTag(), addressableEventAddressTag.requireIdentifierTag());

    assertEquals(Optional.empty(), defnEvent_NoNo_Upvote.getRelay());
    assertEquals(Optional.empty(), defnEvent_NoNo_Upvote.getRelay().map(Relay::getUrl));
    assertEquals(Optional.empty(), defnEvent_NoNo_Upvote.getRelay().map(Relay::getUrl).map(String::toString));
    assertNull(addressableEventAddressTag.getRelay());
  }

  @Test
  final void B_NoYes__testEventNullRelayHasRelayTag() {
    testTags(baseTagsRelayTag,
       new BadgeDefinitionGenericEvent(submitter, upvoteIdentifierTag, List.of(baseTagsRelayTag), ""));
  }

  @Test
  final void C_YesNo__testEventHasRelayNoRelayTag() {
    testTags(relayArgRelayTag, defnEvent_YesNo_Upvote);
  }

  @Test
  final void D_YesYes__testEventHasRelayHasRelayTag() {
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEvent(submitter, upvoteIdentifierTag, List.of(baseTagsRelayTag), "", relayArgRelay));
  }

  @Test
  final void Z_testEventNullRelayMultipleRelayTags() {
    RelayTag relayTag = new RelayTag(new Relay("ws://localhost-from-another-relay-tag:5555"));
    testTags(baseTagsRelayTag,
       new BadgeDefinitionGenericEvent(submitter, upvoteIdentifierTag, List.of(baseTagsRelayTag, relayTag), ""));

    testTags(relayTag,
       new BadgeDefinitionGenericEvent(submitter, upvoteIdentifierTag, List.of(relayTag, baseTagsRelayTag), ""));
  }

  @Test
  final void testEventValidBadgeDefinitionGenericEventMultipleRelayTags() {
    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-should-not-appear:5555"));
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEvent(submitter, upvoteIdentifierTag, List.of(anotherRelayTag), "content", relayArgRelay));
  }

  @Test
  final void testBadgeDefinitionGenericEventCtor() {
    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-should-not-appear:5555"));
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEvent(
          new BadgeDefinitionGenericEvent(submitter, upvoteIdentifierTag, List.of(anotherRelayTag), "content", relayArgRelay).asGenericEventRecord()));
  }

  @Test
  final void testEventValidBadgeDefinitionGenericEventWithoutRelayTagWithRelayBaseTagContaingNullRelay() {
    assertThrows(IllegalArgumentException.class, () ->
       new BadgeDefinitionGenericEvent(
          submitter,
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
