package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.util.Factory;
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
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY)))
    );
    assertNull(setsPairedEvent.getAddressTag().getRelay());
  }

  @Test
  void testConstructionWithRelay() {
    SetsPairedEvent setsPairedEvent = new SetsPairedEvent(
       award_YesNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       new EventTag(
          award_YesNo_Defn_NoNo_Upvote.getId(),
          award_YesNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY)))
    );
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
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY)))
    );
    assertEquals(null, setsPairedEvent.getAddressTag().getRelay());
    assertEquals(relayArgRelay, setsPairedEvent.getDefinitionEventRelay());
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
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY)))
    );
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
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          null)
    ));
  }

  @Test
  void testEquals() {
    SetsPairedEvent expectedEquals = new SetsPairedEvent(
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY)))
    );

    SetsPairedEvent actualEquals = new SetsPairedEvent(
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       new EventTag(
          award_NoNo_Defn_NoNo_Upvote.getId(),
          award_NoNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY)))
    );

    assertEquals(expectedEquals, actualEquals);

    SetsPairedEvent actualNotEqualsHasAddressTagRelay = new SetsPairedEvent(
       award_YesNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       new EventTag(
          award_YesNo_Defn_NoNo_Upvote.getId(),
          award_YesNo_Defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY)))
    );

    assertNotEquals(expectedEquals, actualNotEqualsHasAddressTagRelay);
  }

  @Test
  void testRelayVariants() {
    PublicKey publicKey = Identity.generateRandomIdentity().getPublicKey();
    AddressTag addressTagNullRelay = new AddressTag(
       Kind.CURATION_SETS,
       publicKey,
       new IdentifierTag("random"),
       null);

    Relay eventRelay = new Relay("ws://localhost-event-tag-relay:5555");
    EventTag eventTag = new EventTag(
       Factory.generateRandomHex64String(),
       eventRelay.getUrl());

    SetsPairedEvent setsPairedEventWithNullBackupRelay = new SetsPairedEvent(
       addressTagNullRelay,
       eventTag);
    assertEquals(eventRelay, setsPairedEventWithNullBackupRelay.getDefinitionEventRelay());

    Relay addressTagRelay = new Relay("ws://localhost-address-tag-relay:5555");
    AddressTag addressTagNonNullRelay = new AddressTag(
       Kind.CURATION_SETS,
       publicKey,
       new IdentifierTag("random"),
       addressTagRelay);

    SetsPairedEvent setsPairedEventWithNonNullAddressTagRelayAndNonNullBackupRelay = new SetsPairedEvent(
       addressTagNonNullRelay,
       eventTag
    );
    assertEquals(addressTagRelay, setsPairedEventWithNonNullAddressTagRelayAndNonNullBackupRelay.getDefinitionEventRelay());
  }
}

