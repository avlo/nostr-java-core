package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.EventTagAddressTagPair;
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
  Kind kind = Kind.TEXT_NOTE;
  Identity authorIdentity = Identity.generateRandomIdentity();
  Identity upvotedUserIdentity = Identity.generateRandomIdentity();
  PublicKey upvotedUserPublicKey = upvotedUserIdentity.getPublicKey();
  IdentifierTag upvote = new IdentifierTag("UPVOTE");
  IdentifierTag downvote = new IdentifierTag("DOWNVOTE");
  String eventId = generateRandomHex64String();

  @Test
  void testContainsFromIdenticalTags() {
    AddressTag addressTag = new AddressTag(kind, upvotedUserPublicKey, upvote);
    EventTag eventTag = new EventTag(eventId);

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
                new EventTag(eventId),
                new AddressTag(kind, upvotedUserPublicKey, upvote)));

    List<EventTagAddressTagPair> eventTagAddressTagPair2 =
        List.of(new EventTagAddressTagPair(
            new EventTag(eventId),
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
