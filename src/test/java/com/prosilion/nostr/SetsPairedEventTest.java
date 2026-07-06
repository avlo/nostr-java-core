package com.prosilion.nostr;

import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import org.junit.jupiter.api.Test;

import static com.prosilion.nostr.tag.SetsPairedEvent.NULL_EVENT_TAG_RELAY;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class SetsPairedEventTest extends BaseEventTest {

  @Test
  void testConstructionNullRelay() {
    SetsPairedEvent setsPairedEvent = new SetsPairedEvent(
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       null,
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY))),
       award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey());
    assertNull(setsPairedEvent.getAddressTag().getRelay());
  }

  @Test
  void testConstructionWithRelay() {
    SetsPairedEvent setsPairedEvent = new SetsPairedEvent(
       award_YesNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       auxRelay,
       new EventTag(
          award_YesNo_Defn_NoNo_Upvote.getId(),
          award_YesNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY))),
       award_YesNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey());
    assertEquals(relayArgRelay, setsPairedEvent.getAddressTag().getRelay());
  }

  @Test
  void testConstructionNullAddressTagNullBackupRelay() {
    AddressTag addressTag = new AddressTag(
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().getKind(),
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().getPublicKey(),
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().getIdentifierTag(),
    null);
    SetsPairedEvent setsPairedEvent = new SetsPairedEvent(
       addressTag,
       relayArgRelay,
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY))),
       award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey());
    assertEquals(relayArgRelay, setsPairedEvent.getAddressTag().getRelay());
  }

  @Test
  void testConstructionNullAddressTagWithBackupRelay() {
    AddressTag addressTag = new AddressTag(
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().getKind(),
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().getPublicKey(),
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().getIdentifierTag(),
       auxRelay);
    SetsPairedEvent setsPairedEvent = new SetsPairedEvent(
       addressTag,
       relayArgRelay,
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY))),
       award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey());
    assertEquals(auxRelay, setsPairedEvent.getAddressTag().getRelay());
  }

  @Test
  void testConstructionNullEventTagRelay() {
    AddressTag addressTag = new AddressTag(
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().getKind(),
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().getPublicKey(),
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().getIdentifierTag(),
       auxRelay);
    assertThrows(NostrException.class, () -> new SetsPairedEvent(
       addressTag,
       relayArgRelay,
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          null),
       award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey()));
  }

  @Test
  void testEquals() {
    SetsPairedEvent expectedEquals = new SetsPairedEvent(
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       null,
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY))),
       award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey());

    SetsPairedEvent actualEquals = new SetsPairedEvent(
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       null,
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY))),
       award_NoNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey());
    
    assertEquals(expectedEquals, actualEquals);

    SetsPairedEvent actualNotEqualsHasAddressTagRelay = new SetsPairedEvent(
       award_YesNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       auxRelay,
       new EventTag(
          award_YesNo_Defn_NoNo_Upvote.getId(),
          award_YesNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY))),
       award_YesNo_Defn_NoNo_Upvote.getAwardRecipientPublicKey());
    
    assertNotEquals(expectedEquals, actualNotEqualsHasAddressTagRelay);
  }
}

