package com.prosilion.nostr;

import com.google.common.base.Function;
import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.RelayTag;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeAwardUpvoteEventTest extends BaseEventTest {
  @Test
  final void A_NoNo__testEventNullRelayNoRelayTag() {
    assertEquals(Optional.empty(), award_NoNo_Defn_NoNo_Upvote.getRelayTag());
    assertEquals(Optional.empty(), award_NoNo_Defn_NoNo_Upvote.getRelayTag().map(RelayTag::getRelay));
    assertEquals(Optional.empty(), award_NoNo_Defn_NoNo_Upvote.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl));
    assertThrows(Exception.class, () -> award_NoNo_Defn_NoNo_Upvote.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl).orElseThrow());
    assertThrows(NostrException.class, () -> award_NoNo_Defn_NoNo_Upvote.requireFirstTag(RelayTag.class));

    assertTrue(award_NoNo_Defn_NoNo_Upvote.getRelayTag().isEmpty());
    assertTrue(award_NoNo_Defn_NoNo_Upvote.findFirstTag(RelayTag.class).isEmpty());
    assertTrue(award_NoNo_Defn_NoNo_Upvote.getTypeSpecificTags(RelayTag.class).isEmpty());
    assertThrows(NostrException.class, () -> award_NoNo_Defn_NoNo_Upvote.requireFirstTag(RelayTag.class));

//    relayTag.getRelay related    
    assertTrue(award_NoNo_Defn_NoNo_Upvote.getRelay().isEmpty());
    assertTrue(award_NoNo_Defn_NoNo_Upvote.getRelayTag().map(RelayTag::getRelay).isEmpty());

    assertEquals(Optional.empty(), award_NoNo_Defn_NoNo_Upvote.getRelay());
    assertEquals(Optional.empty(), award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl));
    assertEquals(Optional.empty(), award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).map(String::toString));
  }

  @Test
  final void B_NoYes__testEventNullRelayHasRelayTag() {
    testTags(baseTagsRelayTag, award_NoNo_Defn_NoYes_Upvote);
  }

  @Test
  final void C_YesNo__testEventHasRelayNoRelayTag() {
    testTags(relayArgRelayTag, award_YesNo_Defn_YesNo_Upvote);
  }

  @Test
  final void D_YesYes__testEventHasRelayHasRelayTag() {
    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-from-another-relay-tag:5555"));
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, List.of(anotherRelayTag), relayArgRelay));
  }

  @Test
  final void Z_testEventNullRelayMultipleRelayTags() {
    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-from-another-relay-tag:5555"));
    RelayTag yetAnotherRelayTag = new RelayTag(new Relay("ws://localhost-from-yet-another-relay-tag:5555"));
    testTags(anotherRelayTag,
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote,
          List.of(anotherRelayTag, yetAnotherRelayTag)));

    testTags(yetAnotherRelayTag,
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote,
          List.of(yetAnotherRelayTag, anotherRelayTag)));
  }

  private void testTags(RelayTag relayTag, BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> event) {
    assertTrue(event.getRelay().isPresent());
    assertTrue(event.findFirstTag(RelayTag.class).isPresent());
    assertEquals(relayTag, event.getRelayTag().orElseThrow());
    assertEquals(relayTag, event.requireFirstTag(RelayTag.class));
    assertEquals(relayTag.getRelay(), event.getRelayTag().map(RelayTag::getRelay).orElseThrow());
    assertEquals(relayTag.getRelay().getUrl(), event.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl).orElseThrow());
    assertEquals(1, event.getTypeSpecificTags(RelayTag.class).size());

    assertTrue(event.getRelay().isPresent());
    assertEquals(relayTag.getRelay().getUrl(), event.getRelay().map(Relay::getUrl).orElseThrow());
    assertEquals(relayTag.getRelay().getUrl(), event.getRelay().map(Relay::getUrl).map(String::toString).orElseThrow());
  }

  @Test
  final void testValidBadgeAwardReputationEvent() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
       award_NoNo_Defn_NoNo_Upvote.getGenericEventRecord(),
       addressTag -> defnEvent_NoNo_Upvote);

    assertEquals(award_NoNo_Defn_NoNo_Upvote, badgeAwardUpvoteEvent);
    assertEquals(award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent(), badgeAwardUpvoteEvent.getBadgeDefinitionEvent());
    assertEquals(List.of(award_NoNo_Defn_NoNo_Upvote.getAddressableEvent().asAddressableEventAddressTag()), List.of(badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()));
    assertEquals(award_NoNo_Defn_NoNo_Upvote.getRelayTag(), badgeAwardUpvoteEvent.getRelayTag());
    assertEquals(List.of(award_NoNo_Defn_NoNo_Upvote.getAddressableEvent().asAddressableEventAddressTag()), List.of(badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()));
  }

  @Test
  final void testSingularAddressTag() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       defnEvent_NoNo_Upvote, List.of(award_NoNo_Defn_NoNo_Upvote.getAddressableEvent().asAddressableEventAddressTag()),
       relayArgRelay);

    assertEquals(1, List.of(badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(AddressTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getBadgeDefinitionEvent().getTypeSpecificTags(IdentifierTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(PubKeyTag.class).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(RelayTag.class).size());
  }

  @Test
  final void testEventIFAsGenericEventRecord() {
    EventIF badgeAwardGenericEventAsEventIF = award_NoNo_Defn_NoNo_Upvote;
    assertEquals(
       badgeAwardGenericEventAsEventIF.asGenericEventRecord(),
       award_NoNo_Defn_NoNo_Upvote.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       badgeAwardGenericEventAsEventIF.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       award_NoNo_Defn_NoNo_Upvote.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       EventIF.asGenericEventRecord.apply(award_NoNo_Defn_NoNo_Upvote));

    Function<EventIF, GenericEventRecord> methodInstance_AsGenericEventRecord = EventIF::asGenericEventRecord;
    assertEquals_VariantDemonstration(
       methodInstance_AsGenericEventRecord.apply(award_NoNo_Defn_NoNo_Upvote));
  }

  private void assertEquals_VariantDemonstration(GenericEventRecord genericEventRecordVariant) {
    assertEquals(award_NoNo_Defn_NoNo_Upvote.asGenericEventRecord(), genericEventRecordVariant);
  }
}
