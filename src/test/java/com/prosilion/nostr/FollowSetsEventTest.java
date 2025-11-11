package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.EventTagAddressTagPair;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class FollowSetsEventTest {
  public static final Kind kind = Kind.TEXT_NOTE;
  public static final Relay relay = new Relay("ws://localhost:5555");
  public static final Identity authorIdentity = Identity.generateRandomIdentity();
  public static final Identity upvotedUserIdentity = Identity.generateRandomIdentity();
  public static final PublicKey upvotedUserPublicKey = upvotedUserIdentity.getPublicKey();
  public static final IdentifierTag upvote = new IdentifierTag("UPVOTE");
  public static final IdentifierTag downvote = new IdentifierTag("DOWNVOTE");
  public static final String eventId = generateRandomHex64String();

  @Test
  void testContainsFromIdenticalTags() {
    AddressTag addressTag = new AddressTag(kind, upvotedUserPublicKey, upvote);
    EventTag eventTag = new EventTag(eventId, relay.getUrl());

    EventTagAddressTagPair eventTagAddressTagPair = new EventTagAddressTagPair(eventTag, addressTag);
    List<EventTagAddressTagPair> eventTagAddressTagList = List.of(eventTagAddressTagPair);

    EventTagAddressTagPair eventTagAddressTagPairDuplicate = new EventTagAddressTagPair(eventTag, addressTag);

    List<EventTagAddressTagPair> eventTagAddressTagPairDuplicateList = List.of(eventTagAddressTagPairDuplicate);

    List<EventTagAddressTagPair> nonMatches = eventTagAddressTagList.stream()
        .filter(eventTagAddressTagPairDuplicateList::contains).toList();

    assertFalse(nonMatches.isEmpty());

    List<EventTagAddressTagPair> nonMatchesReverse = eventTagAddressTagPairDuplicateList.stream()
        .filter(eventTagAddressTagPairDuplicateList::contains).toList();

    assertFalse(nonMatchesReverse.isEmpty());
  }

  @Test
  void testContainsFromDuplicateTags() {

    List<EventTagAddressTagPair> eventTagAddressTagList =
        List.of(
            new EventTagAddressTagPair(
                new EventTag(eventId, relay.getUrl()),
                new AddressTag(kind, upvotedUserPublicKey, upvote)));

    List<EventTagAddressTagPair> eventTagAddressTagPair2 =
        List.of(new EventTagAddressTagPair(
            new EventTag(eventId, relay.getUrl()),
            new AddressTag(kind, upvotedUserPublicKey, upvote)));

    List<EventTagAddressTagPair> nonMatches = eventTagAddressTagList.stream()
        .filter(eventTagAddressTagPair2::contains).toList();

    assertFalse(nonMatches.isEmpty());

    List<EventTagAddressTagPair> nonMatchesReverse = eventTagAddressTagPair2.stream()
        .filter(eventTagAddressTagPair2::contains).toList();

    assertFalse(nonMatchesReverse.isEmpty());
  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
