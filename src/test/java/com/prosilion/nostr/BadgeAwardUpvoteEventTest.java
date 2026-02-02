package com.prosilion.nostr;

import com.google.common.base.Function;
import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.GenericEventRecord;
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
  private final BadgeDefinitionGenericEvent badgeDefnUpvoteEvent = new BadgeDefinitionGenericEvent(identity, upvoteIdentifierTag, relay);

  PublicKey badgeReceiverPublicKey = Identity.generateRandomIdentity().getPublicKey();

  BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent;

  public BadgeAwardUpvoteEventTest() {
    this.badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
        identity,
        badgeReceiverPublicKey,
        relay,
        badgeDefnUpvoteEvent);
  }

  @Test
  void testValidBadgeAwardReputationEvent() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
        badgeAwardGenericEvent.getGenericEventRecord(),
        addressTag -> badgeDefnUpvoteEvent);

    assertEquals(badgeAwardGenericEvent, badgeAwardUpvoteEvent);
    assertEquals(
        badgeAwardGenericEvent.getContainedAddressableEvents(),
        badgeAwardUpvoteEvent.getContainedAddressableEvents());
  }

  @Test
  void testSingularAddressTag() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
        identity,
        badgeReceiverPublicKey,
        relay,
        badgeDefnUpvoteEvent,
        Collections.unmodifiableList(badgeAwardGenericEvent.getContainedAddressableEvents()));

    assertEquals(1, badgeAwardUpvoteEvent.getContainedAddressableEvents().size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(AddressTag.class).size());
  }

  @Test
  void testEventIFAsGenericEventRecord() {
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
