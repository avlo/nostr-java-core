package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.FollowSetsEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.event.FollowSetsEvent.MESSAGE;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class FollowSetsEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");
  public static final Identity authorIdentity = Identity.generateRandomIdentity();
  public static final Identity upvotedUserIdentity = Identity.generateRandomIdentity();
  public static final PublicKey upvotedUserPublicKey = upvotedUserIdentity.getPublicKey();
  public static final String UNIT_UPVOTE = "UNIT_UPVOTE";
  public static final String UNIT_DOWNVOTE = "UNIT_DOWNVOTE";
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_DOWNVOTE);

  public static final String FOLLOW_SETS_EVENT = "FOLLOW_SETS_EVENT";
  public final IdentifierTag followSetsIdentifierTag = new IdentifierTag(FOLLOW_SETS_EVENT);
  public final Identity aImgIdentity = Identity.generateRandomIdentity();

  BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent;
  BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardDownvoteEvent;

  public FollowSetsEventTest() {
    this.badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
        authorIdentity,
        upvotedUserPublicKey,
        relay,
        new BadgeDefinitionGenericEvent(authorIdentity, upvoteIdentifierTag, relay));

    this.badgeAwardDownvoteEvent = new BadgeAwardGenericEvent<>(
        authorIdentity,
        upvotedUserPublicKey,
        relay,
        new BadgeDefinitionGenericEvent(authorIdentity, downvoteIdentifierTag, relay));
  }

  @Test
  void testValidFollowSetsEvent() {
    new FollowSetsEvent(
        aImgIdentity,
        upvotedUserPublicKey,
        followSetsIdentifierTag,
        relay,
        List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent));
  }

  @Test
  void testFollowSetsEventEquality() {
    List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardAbstractEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
    FollowSetsEvent expected = new FollowSetsEvent(
        aImgIdentity,
        upvotedUserPublicKey,
        followSetsIdentifierTag,
        relay,
        badgeAwardAbstractEvents);

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
        expected.getGenericEventRecord(),
        eventTag ->
            badgeAwardAbstractEvents.stream().filter(badgeAwardAbstractEvent ->
                FollowSetsEvent.badgeAwardGenericEventAsEventTag(badgeAwardAbstractEvent).equals(eventTag)).findFirst().orElseThrow());

    assertEquals(expected.getContainedAddressableEvents(), followSetsEvent.getContainedAddressableEvents());
    assertEquals(expected, followSetsEvent);
  }

  @Test
  void testFollowSetsEventEqualityViaGetContainedAddressableEvents() {
    List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
    FollowSetsEvent actual = new FollowSetsEvent(
        aImgIdentity,
        upvotedUserPublicKey,
        followSetsIdentifierTag,
        relay,
        badgeAwardGenericEvents);

    assertEquals(
        badgeAwardGenericEvents.stream()
            .map(
                FollowSetsEvent::badgeAwardGenericEventAsEventTag).toList(),
        actual.getContainedAddressableEvents());

    assertEquals(
        badgeAwardGenericEvents.stream().map(badgeAwardAbstractEvent ->
            new EventTag(
                badgeAwardAbstractEvent.getId())).toList(),
        actual.getContainedAddressableEvents());
  }

  @Test
  void tagCountTest() {
    Identity authorIdentity = Identity.generateRandomIdentity();

    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
        authorIdentity,
        upvoteIdentifierTag,
        relay);

    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
        authorIdentity,
        upvotedUserPublicKey,
        relay,
        badgeDefinitionGenericEvent);

    FollowSetsEvent followSetsEvent = new
        FollowSetsEvent(
        aImgIdentity,
        upvotedUserPublicKey,
        followSetsIdentifierTag,
        relay,
        badgeAwardGenericEvent);

    assertEquals(1, followSetsEvent.getContainedAddressableEvents().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
    assertEquals(1, Filterable.getTypeSpecificTags(EventTag.class, followSetsEvent).size());

    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
    assertEquals(1, Filterable.getTypeSpecificTags(RelayTag.class, followSetsEvent).size());
    assertEquals(relay, followSetsEvent.getRelayTagRelay());
  }

  @Test
  void eventTagCountAsListTest() {
    Identity authorIdentity = Identity.generateRandomIdentity();

    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
        authorIdentity,
        upvoteIdentifierTag,
        relay);

    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
        authorIdentity,
        upvotedUserPublicKey,
        relay,
        badgeDefinitionGenericEvent);

    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
        aImgIdentity,
        upvotedUserPublicKey,
        followSetsIdentifierTag,
        relay,
        List.of(badgeAwardGenericEvent),
        FollowSetsEvent.class.getSimpleName());

    assertEquals(1, followSetsEvent.getContainedAddressableEvents().size());
    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
    assertEquals(1, Filterable.getTypeSpecificTags(EventTag.class, followSetsEvent).size());
  }

  @Test
  void relayTagCountTest() {
    Relay followSetsEventRelay = new Relay("ws://localhost:5555");
    Relay badgeAwardGenericEventRelay = new Relay("ws://localhost:5554");
    Relay badgeDefinitionGenericEventRelay = new Relay("ws://localhost:5553");
    
    FollowSetsEvent followSetsEventWithBaseTags = new FollowSetsEvent(
        aImgIdentity,
        upvotedUserPublicKey,
        followSetsIdentifierTag,
        followSetsEventRelay,
        List.of(new BadgeAwardGenericEvent<>(
            authorIdentity,
            upvotedUserPublicKey,
            badgeAwardGenericEventRelay,
            new BadgeDefinitionGenericEvent(
                authorIdentity,
                upvoteIdentifierTag,
                badgeDefinitionGenericEventRelay))),
        List.of(new RelayTag(relay)),
        FollowSetsEvent.class.getSimpleName());

    assertEquals(1, followSetsEventWithBaseTags.getTypeSpecificTags(RelayTag.class).size());
    assertEquals(1, followSetsEventWithBaseTags.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
    assertEquals(1, Filterable.getTypeSpecificTags(RelayTag.class, followSetsEventWithBaseTags).size());
    assertEquals(followSetsEventRelay, followSetsEventWithBaseTags.getRelayTagRelay());
  }

  @Test
  void testInvalidFollowSetsEventMultipleIdentifierTags() {
    List<BaseTag> baseTags = new ArrayList<>();
    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
        aImgIdentity,
        upvotedUserPublicKey,
        followSetsIdentifierTag,
        relay,
        List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent),
        baseTags);
    assertEquals(1, Filterable.getTypeSpecificTags(IdentifierTag.class, followSetsEvent).size());
  }

  @Test
  void testInvalidEmptyBadgeAwardGenericEventsList() {
    assertTrue(
        assertThrows(
            NostrException.class, () -> new FollowSetsEvent(
                aImgIdentity,
                upvotedUserPublicKey,
                followSetsIdentifierTag,
                relay,
                List.of())
        ).getMessage().contains(MESSAGE));
  }

  @Test
  void testEventTagCount() {
    assertEquals(2, new FollowSetsEvent(
        aImgIdentity,
        upvotedUserPublicKey,
        followSetsIdentifierTag,
        relay,
        List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent),
        List.of(new EventTag(generateRandomHex64String()))).getTypeSpecificTags(EventTag.class).size());
  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
