package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionAwardEvent;
import com.prosilion.nostr.event.FollowSetsEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

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

  BadgeAwardGenericEvent badgeAwardUpvoteEvent;
  BadgeAwardGenericEvent badgeAwardDownvoteEvent;

  public FollowSetsEventTest() {
    this.badgeAwardUpvoteEvent = new BadgeAwardGenericEvent(
        authorIdentity,
        upvotedUserPublicKey,
        new BadgeDefinitionAwardEvent(authorIdentity, upvoteIdentifierTag, relay));

    this.badgeAwardDownvoteEvent = new BadgeAwardGenericEvent(
        authorIdentity,
        upvotedUserPublicKey,
        new BadgeDefinitionAwardEvent(authorIdentity, downvoteIdentifierTag, relay));
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
    List<BadgeAwardGenericEvent> badgeAwardAbstractEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
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
                new EventTag(badgeAwardAbstractEvent.getId()).equals(eventTag)).findFirst().orElseThrow());

    assertEquals(expected.getContainedAddressableEvents(), followSetsEvent.getContainedAddressableEvents());
    assertEquals(expected, followSetsEvent);
  }

  @Test
  void testInvalidFollowSetsEventMultipleIdentifierTags() {
    List<BaseTag> baseTags = new ArrayList<>();
    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
    new FollowSetsEvent(
        aImgIdentity,
        upvotedUserPublicKey,
        followSetsIdentifierTag,
        relay,
        List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent),
        baseTags);
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
