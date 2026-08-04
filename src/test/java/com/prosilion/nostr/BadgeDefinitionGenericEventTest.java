package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.RelayTag;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeDefinitionGenericEventTest extends EventTestFixtures {
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
    testTags(baseTagsRelayTag, defnEvent_NoYes_Upvote);
  }

  @Test
  final void C_YesNo__testEventHasRelayNoRelayTag() {
    testTags(relayArgRelayTag, defnEvent_YesNo_Upvote);
  }

  @Test
  final void D_YesYes__testEventHasRelayHasRelayTag() {
    testTags(relayArgRelayTag, defnEvent_YesYes_Upvote);
  }

  @Test
  final void Z_testEventNullRelayMultipleRelayTags() {
    RelayTag relayTag = new RelayTag(new Relay("ws://localhost-from-another-relay-tag:5555"));

    testTags(baseTagsRelayTag,
       new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(baseTagsRelayTag, relayTag)));

    testTags(baseTagsRelayTag,
       new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(baseTagsRelayTag, relayTag), ""));
    testTags(relayTag,
       new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(relayTag, baseTagsRelayTag), ""));
  }

  @Test
  final void testEventValidBadgeDefinitionGenericEventMultipleRelayTags() {
    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-should-not-appear:5555"));
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(anotherRelayTag), "content", relayArgRelay));
  }

  @Test
  final void testEventValidBadgeDefinitionGenericEventWithoutRelayTagWithRelayBaseTagContainingNullRelay() {
    assertThrows(
       IllegalArgumentException.class, () ->
          new BadgeDefinitionGenericEvent(
             upvoteDefnCreator,
             upvoteIdentifierTag,
             List.of(new RelayTag(null)),
             "testValidBadgeDefinitionGenericEventWithoutRelayTagWithRelayBaseTag"));
  }

  @Test
  final void testEventCreateNewFromGenericEventRecord() {
    BadgeDefinitionGenericEvent tempSetupWithoutRelayTag = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag);
    GenericEventRecord genericEventRecord = new GenericEventRecord(
       tempSetupWithoutRelayTag.getId(),
       tempSetupWithoutRelayTag.getPublicKey(),
       tempSetupWithoutRelayTag.getCreatedAt(),
       tempSetupWithoutRelayTag.getKind(),
       tempSetupWithoutRelayTag.getTags(),
       tempSetupWithoutRelayTag.getContent(),
       tempSetupWithoutRelayTag.getSignature());

    RelayTag auxTagsRelayTag = new RelayTag(auxRelay);
    BadgeDefinitionGenericEvent withAuxRelay = new BadgeDefinitionGenericEvent(
       aImgIdentity,
       genericEventRecord,
       auxTagsRelayTag.getRelay());
    testTags(auxTagsRelayTag, withAuxRelay);
  }

  @Test
  final void testEventCreateNewFromBadgeDefinitionGenericEventAsGenericEventRecordWithoutRelayTag() {
    BadgeDefinitionGenericEvent expectedWithoutRelayTag = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag);
    BadgeDefinitionGenericEvent badgeDefinitionAsGenericEventRecord = new BadgeDefinitionGenericEvent(expectedWithoutRelayTag.asGenericEventRecord());
    assertEquals(expectedWithoutRelayTag, badgeDefinitionAsGenericEventRecord);
  }

  @Test
  final void testEqualsPureGenericVariantLenientCreationTimeNoRelayTag() {
    BadgeDefinitionGenericEvent expectedWithoutRelayTag = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag);
    long epochSecond = System.currentTimeMillis();
    GenericEventRecord genericEventRecord = new GenericEventRecord(
       expectedWithoutRelayTag.getId(),
       expectedWithoutRelayTag.getPublicKey(),
       epochSecond,
       Kind.BADGE_DEFINITION_EVENT,
       expectedWithoutRelayTag.getTags(),
       expectedWithoutRelayTag.getContent(),
       expectedWithoutRelayTag.getSignature());

    BadgeDefinitionGenericEvent actual = new BadgeDefinitionGenericEvent(genericEventRecord);
    assertEquals(expectedWithoutRelayTag, actual);
    assertEquals(expectedWithoutRelayTag.getId(), actual.getId());
    assertEquals(expectedWithoutRelayTag.getSignature(), actual.getSignature());
    assertEquals(expectedWithoutRelayTag.getTags(), actual.getTags());
    assertTrue(actual.getRelay().isEmpty());
    assertTrue(actual.asAddressableEventAddressTag().findRelay().isEmpty());

    assertNotEquals(expectedWithoutRelayTag.getCreatedAt(), actual.getCreatedAt());
  }

  @Test
  final void testEqualsPureGenericVariantLenientCreationTimeWithRelayTag() {
    BadgeDefinitionGenericEvent expectedBadgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, relay);

    BadgeDefinitionGenericEvent actual =
       new BadgeDefinitionGenericEvent(
          new GenericEventRecord(
             expectedBadgeDefinitionGenericEvent.getId(),
             expectedBadgeDefinitionGenericEvent.getPublicKey(),
             System.currentTimeMillis(),
             Kind.BADGE_DEFINITION_EVENT,
             expectedBadgeDefinitionGenericEvent.getTags(),
             expectedBadgeDefinitionGenericEvent.getContent(),
             expectedBadgeDefinitionGenericEvent.getSignature()));

    assertEquals(expectedBadgeDefinitionGenericEvent, actual);
    assertEquals(expectedBadgeDefinitionGenericEvent.getId(), actual.getId());
    assertEquals(expectedBadgeDefinitionGenericEvent.getSignature(), actual.getSignature());
    assertEquals(expectedBadgeDefinitionGenericEvent.getTags(), actual.getTags());
    assertTrue(actual.getRelay().isPresent());
    assertEquals(relay, actual.getRelay().orElseThrow());
    assertEquals(relay, actual.asAddressableEventAddressTag().getRelay());
    assertNotEquals(expectedBadgeDefinitionGenericEvent.getCreatedAt(), actual.getCreatedAt());
  }

  @Test
  final void testEventCreateNewFromExisting() {
    BadgeDefinitionGenericEvent withoutRelayTag = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag);
    new GenericEventRecord(
       withoutRelayTag.getId(),
       withoutRelayTag.getPublicKey(),
       withoutRelayTag.getCreatedAt(),
       withoutRelayTag.getKind(),
       withoutRelayTag.getTags().stream().filter(RelayTag.class::isInstance).toList(),
       withoutRelayTag.getContent(),
       withoutRelayTag.getSignature());

    RelayTag auxTagsRelayTag = new RelayTag(auxRelay);
    BadgeDefinitionGenericEvent withAuxRelay = withoutRelayTag.createNewFromExisting(upvoteDefnCreator, auxTagsRelayTag.getRelay());
    testTags(auxTagsRelayTag, withAuxRelay);
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
