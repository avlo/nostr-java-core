//package com.prosilion.nostr;
//
//import com.ezylang.evalex.parser.ParseException;
//import com.prosilion.nostr.codec.IDecoder;
//import com.prosilion.nostr.enums.Kind;
//import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
//import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
//import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
//import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
//import com.prosilion.nostr.event.FollowSetsEvent;
//import com.prosilion.nostr.event.FormulaEvent;
//import com.prosilion.nostr.event.GenericEventRecord;
//import com.prosilion.nostr.event.internal.Relay;
//import com.prosilion.nostr.message.EventMessage;
//import com.prosilion.nostr.tag.AddressTag;
//import com.prosilion.nostr.tag.EventTag;
//import com.prosilion.nostr.tag.ExternalIdentityTag;
//import com.prosilion.nostr.tag.IdentifierTag;
//import com.prosilion.nostr.tag.PubKeyTag;
//import com.prosilion.nostr.tag.RelayTag;
//import com.prosilion.nostr.user.Identity;
//import com.prosilion.nostr.user.PublicKey;
//import com.prosilion.nostr.user.Signature;
//import java.io.IOException;
//import java.util.List;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.test.context.ActiveProfiles;
//import org.springframework.test.json.JsonComparator;
//import org.springframework.test.json.JsonComparison;
//
//import static com.prosilion.nostr.BadgeAwardReputationEventTest.PLUS_ONE_FORMULA;
//import static org.junit.jupiter.api.Assertions.assertEquals;
//
//@Slf4j
//@ActiveProfiles("test")
//public class EventMessageSerializerWithContainedAddressableEventsRxRTest {
//  private final static String url = "ws://localhost:5555";
//  private final static String FORMULA_UNIT_UPVOTE = "FORMULA_UNIT_UPVOTE";
//  private final Relay relay = new Relay(url);
//  private final static String UNIT_UPVOTE = "UNIT_UPVOTE";
//
//  private final GenericEventRecord genericEventRecordWithAddressTag;
//  private final GenericEventRecord genericEventRecordWithEventTag;
//
//  private final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
//
//  private final Identity platformIdentity = Identity.generateRandomIdentity();
//  private final Identity authorIdentity = Identity.generateRandomIdentity();
//
//  private final BadgeDefinitionGenericEventAux badgeDefnUpvoteEventAux;
//  private final BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> badgeAwardGenericEventWithAddressTag;
//
//  private final PublicKey badgeReceiverPublicKey = Identity.generateRandomIdentity().getPublicKey();
//
//  private final static String FOLLOW_SETS_EVENT_UUID = "PROSILION_FOLLOW_SETS_EVENT";
//  private final IdentifierTag followSetsIdentifierTag = new IdentifierTag(FOLLOW_SETS_EVENT_UUID);
//
//  private final BadgeAwardGenericEventAux<BadgeDefinitionGenericEventAux> badgeAwardUpvoteEvent;
//  private final FollowSetsEvent followSetsEvent;
//
//  private final String badgeAwardGenericEventWithAddressTagEventId;
//  private final String authorPubKey;
//  private final String badgeAwardGenericEventWithAddressTagCreatedAt;
//  private final String upvotedUserPubkey;
//  private final String badgeAwardGenericEventWithAddressTagSignature;
//
//  private final GenericEventRecord followSetsAsGenericEventEventWithEventTag;
//  private final String followSetsEventWithEventTagEventId;
//  private final String followSetsEventWithEventTagCreatedAt;
//  private final String followSetsEventWithEventTagSignature;
//  private final String followSetsEventReferencedEventId;
//
//  public EventMessageSerializerWithContainedAddressableEventsRxRTest() throws ParseException {
//    this.genericEventRecordWithAddressTag = new GenericEventRecord(
//       "5f66a36101d3d152c6270e18f5622d1f8bce4ac5da9ab62d7c3cc0006e590001",
//       new PublicKey("bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"),
//       1111111111111L,
//       Kind.BADGE_AWARD_EVENT,
//       List.of(
//          new AddressTag(Kind.BADGE_DEFINITION_EVENT,
//             new PublicKey("bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"),
//             new IdentifierTag(UNIT_UPVOTE),
//             relay)),
//       "matching kind, author, identity-tag filter test",
//       new Signature("86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"));
//
//    this.genericEventRecordWithEventTag = new GenericEventRecord(
//       "5f66a36101d3d152c6270e18f5622d1f8bce4ac5da9ab62d7c3cc0006e590001",
//       new PublicKey("bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"),
//       1111111111111L,
//       Kind.BADGE_AWARD_EVENT,
//       List.of(
//          new EventTag("bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984", url)),
//       "matching kind, author, identity-tag filter test",
//       new Signature("86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"));
//
//    this.badgeDefnUpvoteEventAux = new BadgeDefinitionGenericEventAux(
//       new BadgeDefinitionGenericEvent(platformIdentity, upvoteIdentifierTag, relay),
//       new RelayTag(relay));
//
//    this.badgeAwardGenericEventWithAddressTag = new BadgeAwardGenericEventAux<>(
//       platformIdentity,
//       badgeReceiverPublicKey,
//       relay,
//       badgeDefnUpvoteEventAux);
//
//    this.badgeAwardGenericEventWithAddressTagEventId = badgeAwardGenericEventWithAddressTag.getId();
//    this.authorPubKey = platformIdentity.getPublicKey().toHexString();
//    this.badgeAwardGenericEventWithAddressTagCreatedAt = badgeAwardGenericEventWithAddressTag.getCreatedAt().toString();
//    this.upvotedUserPubkey = badgeReceiverPublicKey.toHexString();
//    this.badgeAwardGenericEventWithAddressTagSignature = badgeAwardGenericEventWithAddressTag.getSignature().toString();
//
//    this.badgeAwardUpvoteEvent = new BadgeAwardGenericEventAux<>(
//       platformIdentity,
//       badgeReceiverPublicKey,
//       relay,
//       new BadgeDefinitionGenericEventAux(
//          new BadgeDefinitionGenericEvent(authorIdentity, upvoteIdentifierTag, relay),
//          new RelayTag(relay)));
//
//    this.followSetsAsGenericEventEventWithEventTag = new GenericEventRecord(
//       "09848ce3194d4db99443a1032463092c33454e62b57839ab0e51676ace290c50",
//       new PublicKey("703c164e01d6ba4632d440af596f21ff51e8e01f04283e1e2797de04127f91cc"),
//       1769322511594L,
//       Kind.FOLLOW_SETS,
//       List.of(
//          FollowSetsEvent.defaultIdentifierTag,
//          new RelayTag(relay),
//          new EventTag("2e0864780d99e270cf9c1d9f124d8efd18e9e8be7e2b8c6537c79f34ef2ed445", relay.getUrl()),
//          new PubKeyTag(new PublicKey("fd320dfb0433681cf5a4244cbc18f82b19407beec2867fc03d8109902ecc6d0c"))),
//       "AfterImage generated FollowSetsEvent",
//       new Signature("27683ca56acf67502769eb2900f53803086e56e5ae6aaa8a19f12441f9b29c58f5950ee4ac05ce8559a61295e036bae3609c022522e85588b5a21de5c1518843"));
//
//    FormulaEvent plusOneFormulaEvent = new FormulaEvent(
//       authorIdentity,
//       new IdentifierTag(FORMULA_UNIT_UPVOTE),
//       relay,
//       badgeDefnUpvoteEventAux,
//       PLUS_ONE_FORMULA);
//
//    BadgeDefinitionReputationEvent badgeDefinitionReputationEventPlusOneFormula = new BadgeDefinitionReputationEvent(
//       platformIdentity,
//       authorIdentity.getPublicKey(),
//       FollowSetsEvent.defaultIdentifierTag,
//       relay,
//       new ExternalIdentityTag("afterimage", "badge_definition_reputation", String.valueOf(BadgeDefinitionReputationEvent.class.hashCode())),
//       plusOneFormulaEvent);
//
//    this.followSetsEvent = new FollowSetsEvent(
//       platformIdentity,
//       badgeDefinitionReputationEventPlusOneFormula,
//       relay,
//       List.of(badgeAwardUpvoteEvent));
//
//    this.followSetsEventWithEventTagEventId = followSetsEvent.getId();
//    this.followSetsEventWithEventTagCreatedAt = followSetsEvent.getCreatedAt().toString();
//    this.followSetsEventWithEventTagSignature = followSetsEvent.getSignature().toString();
//    this.followSetsEventReferencedEventId = this.badgeAwardUpvoteEvent.getId();
//  }
//
//  @Test
//  void testStringEventMessageAddressTagGenericEventRecordEncoder() throws IOException, NostrException {
//    getStringEquals(
//       new EventMessage(
//          genericEventRecordWithAddressTag),
//       expectedStringEventMessageAddressTagGenericEventRecord());
//  }
//
//  @Test
//  void testStringEventMessageAddressTagBadgeAwardGenericEventEncoder() throws IOException, NostrException {
//    getStringEquals(
//       new EventMessage(
//          badgeAwardGenericEventWithAddressTag.asGenericEventRecord()),
//       expectedStringEventMessageAddressTagBadgeAwardGenericEvent());
//  }
//
//  @Test
//  void testStringEventMessageFollowSetsEventTagGenericEventRecordEncoder() throws IOException, NostrException {
//    getStringEquals(
//       new EventMessage(
//          followSetsAsGenericEventEventWithEventTag),
//       expectedStringFollowSetsEventMessageAddressTagGenericEventRecord());
//  }
//
//  @Test
//  void testStringEventMessageFollowSetsEventAwardGenericEventEncoder() throws IOException, NostrException {
//    getStringEquals(
//       new EventMessage(
//          followSetsEvent.asGenericEventRecord()),
//       expectedStringEventMessageAddressTagFollowSetsEvent());
//  }
//
//  @Test
//  void testJsonEventMessageAddressTagGenericEventKindEncoder() throws IOException, NostrException {
//    getJsonEquals(
//       new EventMessage(
//          genericEventRecordWithAddressTag),
//       expectedStringEventMessageAddressTagGenericEventRecord());
//  }
//
//  @Test
//  void testStringEventMessageEventTagGenericEventKindEncoder() throws IOException, NostrException {
//    getStringEquals(
//       new EventMessage(
//          genericEventRecordWithEventTag),
//       expectedStringWithEventTagShouldMatch());
//  }
//
//  @Test
//  void testJsonEventMessageEventTagGenericEventKindEncoder() throws IOException, NostrException {
//    getJsonEquals(
//       new EventMessage(
//          genericEventRecordWithEventTag),
//       expectedStringWithEventTagShouldMatch());
//  }
//
//  private void getStringEquals(EventMessage eventMessage, String expected) throws IOException, NostrException {
//    String actual = IDecoder.I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(eventMessage);
//
//    assertEquals(expected, actual);
//  }
//
//  private void getJsonEquals(EventMessage eventMessage, String expected) throws IOException, NostrException {
//    String actual = IDecoder.I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(eventMessage);
//
//    assertEquals(expected, actual);
//
////    String actual = eventMessage.encode();
//    JsonComparator comparator = (expectedJson, actualJson) -> JsonComparison.match();
//    System.out.println("");
//    System.out.println("");
//    System.out.println(actual);
//    System.out.println("");
//    System.out.println("-------------");
//    System.out.println("");
//    System.out.println(actual);
//    System.out.println("");
//    System.out.println("");
//
//    assertEquals(JsonComparison.Result.MATCH, comparator.compare(actual, actual).getResult());
//    assertEquals(JsonComparison.Result.MATCH, comparator.compare(expectedStringEventMessageAddressTagGenericEventRecord(), actual).getResult());
//  }
//
//  private String expectedStringEventMessageAddressTagGenericEventRecord() {
//    return """
//       ["EVENT",{"id":"5f66a36101d3d152c6270e18f5622d1f8bce4ac5da9ab62d7c3cc0006e590001","pubkey":"bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984","created_at":1111111111111,"kind":8,"tags":[["a","30009:bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984:UNIT_UPVOTE","ws://localhost:5555"]],"content":"matching kind, author, identity-tag filter test","sig":"86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"}]""";
//  }
//
//  private String expectedStringWithEventTagShouldMatch() {
//    return """
//       ["EVENT",{"id":"5f66a36101d3d152c6270e18f5622d1f8bce4ac5da9ab62d7c3cc0006e590001","pubkey":"bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984","created_at":1111111111111,"kind":8,"tags":[["e","bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984","ws://localhost:5555"]],"content":"matching kind, author, identity-tag filter test","sig":"86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"}]""";
//  }
//
//  private String expectedStringEventMessageAddressTagBadgeAwardGenericEvent() {
//    return "[\"EVENT\",{\"id\":\"" + badgeAwardGenericEventWithAddressTagEventId + "\",\"pubkey\":\"" + authorPubKey + "\",\"created_at\":" + badgeAwardGenericEventWithAddressTagCreatedAt + ",\"kind\":8,\"tags\":[[\"a\",\"30009:" + authorPubKey + ":UNIT_UPVOTE\",\"ws://localhost:5555\"],[\"p\",\"" + upvotedUserPubkey + "\"],[\"relay\",\"ws://localhost:5555\"]],\"content\":\"\",\"sig\":\"" + badgeAwardGenericEventWithAddressTagSignature + "\"}]";
//  }
//
//  private String expectedStringFollowSetsEventMessageAddressTagGenericEventRecord() {
//    return """
//       ["EVENT",{"id":"09848ce3194d4db99443a1032463092c33454e62b57839ab0e51676ace290c50","pubkey":"703c164e01d6ba4632d440af596f21ff51e8e01f04283e1e2797de04127f91cc","created_at":1769322511594,"kind":30000,"tags":[["d","PROSILION_FOLLOW_SETS_EVENT"],["relay","ws://localhost:5555"],["e","2e0864780d99e270cf9c1d9f124d8efd18e9e8be7e2b8c6537c79f34ef2ed445","ws://localhost:5555"],["p","fd320dfb0433681cf5a4244cbc18f82b19407beec2867fc03d8109902ecc6d0c"]],"content":"AfterImage generated FollowSetsEvent","sig":"27683ca56acf67502769eb2900f53803086e56e5ae6aaa8a19f12441f9b29c58f5950ee4ac05ce8559a61295e036bae3609c022522e85588b5a21de5c1518843"}]""";
//  }
//
//  private String expectedStringEventMessageAddressTagFollowSetsEvent() {
//    String somethin = followSetsEvent.getAddressTag().getPublicKey().toHexString();
//
//    return "[\"EVENT\",{\"id\":\"" + followSetsEventWithEventTagEventId + "\",\"pubkey\":\"" + platformIdentity.getPublicKey().toHexString() + "\",\"created_at\":" + followSetsEventWithEventTagCreatedAt + ",\"kind\":30000,\"tags\":[[\"d\",\"PROSILION_FOLLOW_SETS_EVENT\"],[\"relay\",\"ws://localhost:5555\"],[\"e\",\"" + followSetsEventReferencedEventId + "\",\"ws://localhost:5555\"],[\"p\",\"" + badgeReceiverPublicKey + "\"],[\"a\",\"30009:" + somethin + ":PROSILION_FOLLOW_SETS_EVENT\",\"ws://localhost:5555\"]],\"content\":\"AfterImage generated FollowSetsEvent\",\"sig\":\"" + followSetsEventWithEventTagSignature + "\"}]";
//  }
//}
