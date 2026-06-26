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
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BadgeAwardUpvoteEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");
  public static final RelayTag relayTag = new RelayTag(relay);

  public static final String UNIT_UPVOTE = "UNIT_UPVOTE";
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  protected final Identity submitter =
//     Identity.generateRandomIdentity();
     Identity.create("aaa4585483196998204846989544737603523651520600328805626488477202");

  protected final Identity upvoteDefnCreator =
//     Identity.generateRandomIdentity();
     Identity.create("bbb4585483196998204846989544737603523651520600328805626488477202");

  protected final Identity recipient =
//     Identity.generateRandomIdentity();
     Identity.create("ccc4585483196998204846989544737603523651520600328805626488477202");

  private final BadgeDefinitionGenericEvent badgeDefnUpvoteEvent = new BadgeDefinitionGenericEvent(
     upvoteDefnCreator, upvoteIdentifierTag, relay);

  BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent;

  public BadgeAwardUpvoteEventTest() {
    this.badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       badgeDefnUpvoteEvent, relay
    );
  }

  @Test
  final void A_NoNo__testEventNullRelayNoRelayTag() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       badgeDefnUpvoteEvent);
    assertEquals(Optional.empty(), badgeAwardGenericEvent.getRelayTag());
    assertEquals(Optional.empty(), badgeAwardGenericEvent.getRelayTag().map(RelayTag::getRelay));
    assertEquals(Optional.empty(), badgeAwardGenericEvent.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl));
    assertThrows(Exception.class, () -> badgeAwardGenericEvent.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl).orElseThrow());
    assertThrows(NostrException.class, () -> badgeAwardGenericEvent.requireFirstTag(RelayTag.class));

    assertTrue(badgeAwardGenericEvent.getRelayTag().isEmpty());
    assertTrue(badgeAwardGenericEvent.findFirstTag(RelayTag.class).isEmpty());
    assertTrue(badgeAwardGenericEvent.getTypeSpecificTags(RelayTag.class).isEmpty());
    assertThrows(NostrException.class, () -> badgeAwardGenericEvent.requireFirstTag(RelayTag.class));

//    relayTag.getRelay related    
    assertTrue(badgeAwardGenericEvent.getRelay().isEmpty());
    assertTrue(badgeAwardGenericEvent.getRelayTag().map(RelayTag::getRelay).isEmpty());

    assertEquals(Optional.empty(), badgeAwardGenericEvent.getRelay());
    assertEquals(Optional.empty(), badgeAwardGenericEvent.getRelay().map(Relay::getUrl));
    assertEquals(Optional.empty(), badgeAwardGenericEvent.getRelay().map(Relay::getUrl).map(String::toString));
  }

  @Test
  final void B_NoYes__testEventNullRelayHasRelayTag() {
    testTags(relayTag,
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnUpvoteEvent, List.of(relayTag)));
  }

  @Test
  final void C_YesNo__testEventHasRelayNoRelayTag() {
    testTags(relayTag,
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnUpvoteEvent, relay));
  }

  @Test
  final void D_YesYes__testEventHasRelayHasRelayTag() {
    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-from-another-relay-tag:5555"));
    testTags(relayTag,
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnUpvoteEvent, List.of(anotherRelayTag), relay));
  }

  @Test
  final void Z_testEventNullRelayMultipleRelayTags() {
    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-from-another-relay-tag:5555"));
    RelayTag yetAnotherRelayTag = new RelayTag(new Relay("ws://localhost-from-yet-another-relay-tag:5555"));
    testTags(anotherRelayTag,
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnUpvoteEvent,
          List.of(anotherRelayTag, yetAnotherRelayTag)));

    testTags(yetAnotherRelayTag,
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnUpvoteEvent,
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
       badgeAwardGenericEvent.getGenericEventRecord(),
       addressTag -> badgeDefnUpvoteEvent);

    assertEquals(badgeAwardGenericEvent, badgeAwardUpvoteEvent);
    assertEquals(badgeAwardGenericEvent.getBadgeDefinitionEvent(), badgeAwardUpvoteEvent.getBadgeDefinitionEvent());
    assertEquals(List.of(badgeAwardGenericEvent.getAddressableEvent().asAddressableEventAddressTag()), List.of(badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()));
    assertEquals(badgeAwardGenericEvent.getRelayTag(), badgeAwardUpvoteEvent.getRelayTag());
    assertEquals(List.of(badgeAwardGenericEvent.getAddressableEvent().asAddressableEventAddressTag()), List.of(badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()));
  }

  @Test
  final void testSingularAddressTag() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       badgeDefnUpvoteEvent, List.of(badgeAwardGenericEvent.getAddressableEvent().asAddressableEventAddressTag()), relay
    );

    assertEquals(1, List.of(badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(AddressTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getBadgeDefinitionEvent().getTypeSpecificTags(IdentifierTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(PubKeyTag.class).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(RelayTag.class).size());
  }

  @Test
  final void testEventIFAsGenericEventRecord() {
    EventIF badgeAwardGenericEventAsEventIF = badgeAwardGenericEvent;
    assertEquals(
       badgeAwardGenericEventAsEventIF.asGenericEventRecord(),
       badgeAwardGenericEvent.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       badgeAwardGenericEventAsEventIF.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       badgeAwardGenericEvent.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       EventIF.asGenericEventRecord.apply(badgeAwardGenericEvent));

    Function<EventIF, GenericEventRecord> methodInstance_AsGenericEventRecord = EventIF::asGenericEventRecord;
    assertEquals_VariantDemonstration(
       methodInstance_AsGenericEventRecord.apply(badgeAwardGenericEvent));
  }

  private void assertEquals_VariantDemonstration(GenericEventRecord genericEventRecordVariant) {
    assertEquals(badgeAwardGenericEvent.asGenericEventRecord(), genericEventRecordVariant);
  }
}
