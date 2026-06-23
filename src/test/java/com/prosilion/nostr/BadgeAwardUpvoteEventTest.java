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
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BadgeAwardUpvoteEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");

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
       relay,
       badgeDefnUpvoteEvent);
  }

  @Test
  void testValidBadgeAwardReputationEvent() {
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
  void testSingularAddressTag() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       relay,
       badgeDefnUpvoteEvent,
       List.of(badgeAwardGenericEvent.getAddressableEvent().asAddressableEventAddressTag()));

    assertEquals(1, List.of(badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(AddressTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getBadgeDefinitionEvent().getTypeSpecificTags(IdentifierTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(PubKeyTag.class).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(RelayTag.class).size());
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
