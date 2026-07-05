package com.prosilion.nostr;

import com.prosilion.nostr.event.CurationSetsEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CurationSetsEventTest extends BaseEventTest {

  @Test
  final void testValidBadgeSetsEvent() {
    CurationSetsEvent curationSetsUpvoteEvent = new CurationSetsEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent(),
       eventAuxNo_award_NoNo_defn_NoNo_Upvote,
       relayArgRelay);

    CurationSetsEvent curationSetsDownvoteEvent = new CurationSetsEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Downvote.getBadgeDefinitionEvent(),
       eventAuxNo_award_NoNo_defn_NoNo_Downvote,
       relayArgRelay);

    SetsPairedEvent setsPairedUpvoteEvent = curationSetsUpvoteEvent.getSetsPairedEvent();
    SetsPairedEvent setsPairedDownvoteEvent = curationSetsDownvoteEvent.getSetsPairedEvent();

    assertEquals(eventAuxNo_award_NoNo_defn_NoNo_Upvote, setsPairedUpvoteEvent);
    assertEquals(eventAuxNo_award_NoNo_defn_NoNo_Downvote, setsPairedDownvoteEvent);
    String upvoteEventId = eventAuxNo_award_NoNo_defn_NoNo_Upvote.getAwardEventId();
    String downvoteEventId = eventAuxNo_award_NoNo_defn_NoNo_Downvote.getAwardEventId();

    assertEquals(
       upvoteEventId,
       setsPairedUpvoteEvent.getAwardEventId());
    assertEquals(
       defnAuxNo_defnEvent_NoNo_Downvote.getAddressTag(),
       setsPairedDownvoteEvent.getAddressTag());

    assertEquals(recipient.getPublicKey(), eventAuxNo_award_NoNo_defn_NoNo_Upvote.getAwardRecipientPublicKey());

    assertEquals(setsPairedUpvoteEvent.getEventTag().getEventId(), upvoteEventId);
    assertEquals(setsPairedDownvoteEvent.getEventTag().getEventId(), downvoteEventId);

    AddressTag upvoteAsAddressTag = defnAuxNo_defnEvent_NoNo_Upvote.getAddressTag();
    AddressTag downvoteAsAddressTag = defnAuxNo_defnEvent_NoNo_Downvote.getAddressTag();

    assertEquals(curationSetsUpvoteEvent.getAddressTag(), upvoteAsAddressTag);
    assertEquals(curationSetsDownvoteEvent.getAddressTag(), downvoteAsAddressTag);

    assertEquals(setsPairedUpvoteEvent.getDefinitionEventRelay(), defnAuxNo_defnEvent_NoNo_Upvote.getDefinitionEventRelay());
    assertEquals(setsPairedDownvoteEvent.getDefinitionEventRelay(), defnAuxNo_defnEvent_NoNo_Downvote.getDefinitionEventRelay());
  }

  @Test
  final void testNewFromExisting() {
    CurationSetsEvent curationSetsUpvoteEvent = new CurationSetsEvent(
       aImgIdentity,
       award_NoNo_Defn_NoNo_Upvote.getBadgeDefinitionEvent(),
       eventAuxNo_award_NoNo_defn_NoNo_Upvote,
       relayArgRelay);

    CurationSetsEvent newFromExisting = curationSetsUpvoteEvent.createNewFromExisting(
       aImgIdentity, eventAuxNo_award_NoNo_defn_NoNo_Upvote);

    assertEquals(curationSetsUpvoteEvent.getAddressTag(), newFromExisting.getAddressTag());
    assertEquals(curationSetsUpvoteEvent.getAddressTagEventTagPair(), newFromExisting.getAddressTagEventTagPair());
    assertEquals(curationSetsUpvoteEvent.getBadgeDefinitionGenericEvent(), newFromExisting.getBadgeDefinitionGenericEvent());
    assertEquals(curationSetsUpvoteEvent.getEventTag(), newFromExisting.getEventTag());
    assertEquals(curationSetsUpvoteEvent.asAddressableEventAddressTag(), newFromExisting.asAddressableEventAddressTag());
    assertEquals(curationSetsUpvoteEvent.getAwardRecipientPublicKey(), newFromExisting.getAwardRecipientPublicKey());
    assertEquals(curationSetsUpvoteEvent.getIdentifierTag(), newFromExisting.getIdentifierTag());
    assertEquals(curationSetsUpvoteEvent.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl),
       newFromExisting.getRelayTag().map(RelayTag::getRelay).map(Relay::getUrl));
  }

//  @Test
//  final void testFollowSetsEventEquality() {
//    List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardAbstractEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
//    FollowSetsEvent expected = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       badgeAwardAbstractEvents);
//
//    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
//       expected.getGenericEventRecord(),
//       eventTag ->
//          badgeAwardAbstractEvents.stream().filter(badgeAwardAbstractEvent ->
//             FollowSetsEvent.badgeAwardGenericEventAsEventTag(badgeAwardAbstractEvent).equals(eventTag)).findFirst().orElseThrow(),
//       addressTag -> badgeDefinitionReputationEventPlusOneFormula);
//
//    assertEquals(expected.getAddressTag(), followSetsEvent.getAddressTag());
//    assertEquals(expected.getBadgeDefinitionReputationEvent(), badgeDefinitionReputationEventPlusOneFormula);
//    assertEquals(expected, followSetsEvent);
//  }
//
//  @Test
//  final void testFollowSetsEventEqualityViaGetContainedAddressableEvents() {
//    List<BadgeAwardGenericEvent<BadgeDefinitionGenericEvent>> badgeAwardGenericEvents = List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent);
//    FollowSetsEvent actual = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       badgeAwardGenericEvents);
//
//    assertEquals(
//       badgeAwardGenericEvents.stream()
//          .map(
//             FollowSetsEvent::badgeAwardGenericEventAsEventTag).toList(),
//       actual.getEventTags());
//
//    assertEquals(
//       badgeAwardGenericEvents.stream().map(badgeAwardAbstractEvent ->
//          new EventTag(
//             badgeAwardAbstractEvent.getId())).toList(),
//       actual.getEventTags());
//  }
//
//  @Test
//  final void tagCountTest() {
//    Identity upvoteDefnCreator = Identity.generateRandomIdentity();
//
//    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
//       upvoteDefnCreator,
//       upvoteIdentifierTag,
//       relay);
//
//    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
//       upvoteDefnCreator,
//       recipient,
//       relay,
//       badgeDefinitionGenericEvent);
//
//    FollowSetsEvent followSetsEvent = new
//       FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       badgeAwardGenericEvent);
//
//    assertEquals(1, followSetsEvent.getEventTags().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
//    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
//
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(1, followSetsEvent.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(relay, followSetsEvent.getRelay());
//  }
//
//  @Test
//  final void eventTagCountAsListTest() {
//    Identity upvoteDefnCreator = Identity.generateRandomIdentity();
//
//    BadgeDefinitionGenericEvent badgeDefinitionGenericEvent = new BadgeDefinitionGenericEvent(
//       upvoteDefnCreator,
//       upvoteIdentifierTag,
//       relay);
//
//    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEvent = new BadgeAwardGenericEvent<>(
//       upvoteDefnCreator,
//       recipient,
//       relay,
//       badgeDefinitionGenericEvent);
//
//    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       List.of(badgeAwardGenericEvent),
//       FollowSetsEvent.class.getSimpleName());
//
//    assertEquals(1, followSetsEvent.getEventTags().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
//    assertEquals(1, followSetsEvent.getTags().stream().filter(EventTag.class::isInstance).toList().size());
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(EventTag.class).size());
//  }
//
//  @Test
//  final void relayTagCountTest() {
//    Relay followSetsEventRelay = new Relay("ws://localhost:5555");
//    Relay badgeAwardGenericEventRelay = new Relay("ws://localhost:5554");
//    Relay badgeDefinitionGenericEventRelay = new Relay("ws://localhost:5553");
//
//    FollowSetsEvent followSetsEventWithBaseTags = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       followSetsEventRelay,
//       List.of(new BadgeAwardGenericEvent<>(
//          upvoteDefnCreator,
//          recipient,
//          badgeAwardGenericEventRelay,
//          new BadgeDefinitionGenericEvent(
//             upvoteDefnCreator,
//             upvoteIdentifierTag,
//             badgeDefinitionGenericEventRelay))),
//       List.of(new RelayTag(relay)),
//       FollowSetsEvent.class.getSimpleName());
//
//    assertEquals(1, followSetsEventWithBaseTags.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(1, followSetsEventWithBaseTags.getTags().stream().filter(RelayTag.class::isInstance).toList().size());
//    assertEquals(1, followSetsEventWithBaseTags.getTypeSpecificTags(RelayTag.class).size());
//    assertEquals(followSetsEventRelay, followSetsEventWithBaseTags.getRelay());
//  }
//
//  @Test
//  final void testInvalidFollowSetsEventMultipleIdentifierTags() {
//    List<BaseTag> baseTags = new ArrayList<>();
//    baseTags.add(new IdentifierTag("DIFFERENT_REPUTATION"));
//    FollowSetsEvent followSetsEvent = new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent),
//       baseTags);
//    assertEquals(1, followSetsEvent.getTypeSpecificTags(IdentifierTag.class).size());
//  }
//
//  @Test
//  final void testInvalidEmptyBadgeAwardGenericEventsList() {
//    assertTrue(
//       assertThrows(
//          NostrException.class, () -> new FollowSetsEvent(
//             aImgIdentity,
//             badgeDefinitionReputationEventPlusOneFormula,
//             relay,
//             List.of())
//       ).getMessage().contains(MESSAGE));
//  }
//
//  @Test
//  final void testEventTagCount() {
//    assertEquals(2, new FollowSetsEvent(
//       aImgIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       List.of(badgeAwardUpvoteEvent, badgeAwardDownvoteEvent),
//       List.of(new EventTag(generateRandomHex64String()))).getTypeSpecificTags(EventTag.class).size());
//  }
//
//  @Test
//  final void testIdenticalPublicKeys() {
//    PublicKey nonMatchingPublicKey = Identity.generateRandomIdentity().getPublicKey();
//    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> notMatchingRecipientDownvoteEvent = new BadgeAwardGenericEvent<>(
//       upvoteDefnCreator,
//       nonMatchingPublicKey,
//       relay,
//       badgeAwardUpvoteEvent.getBadgeDefinitionEvent());
//
//    assertTrue(
//       assertThrows(NostrException.class, () ->
//          new FollowSetsEvent(
//             aImgIdentity,
//             badgeDefinitionReputationEventPlusOneFormula,
//             relay,
//             List.of(badgeAwardUpvoteEvent, notMatchingRecipientDownvoteEvent),
//             List.of(new EventTag(generateRandomHex64String())))
//       ).getMessage().contains(
//          String.format(
//             FollowSetsEvent.PUBKEYS_MUST_MATCH, "2",
//             badgeAwardUpvoteEvent.getAwardRecipientPublicKey())));
//  }

  public static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
