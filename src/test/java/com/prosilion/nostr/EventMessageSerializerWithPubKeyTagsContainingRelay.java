package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.codec.IDecoder;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.FollowSetsEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.message.EventMessage;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.json.JsonComparator;
import org.springframework.test.json.JsonComparison;

import static com.prosilion.nostr.BadgeAwardReputationEventTest.PLUS_ONE_FORMULA;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ActiveProfiles("test")
public class EventMessageSerializerWithPubKeyTagsContainingRelay {
  private final static String url = "ws://localhost:5555";
  private final Relay relay = new Relay(url);
  private final static String UNIT_UPVOTE = "TEST_UNIT_UPVOTE";

  private final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  private final Identity platformIdentity = Identity.generateRandomIdentity();
  private final Identity authorIdentity = Identity.generateRandomIdentity();

  private final BadgeDefinitionGenericEvent badgeDefnUpvoteEvent;
  private final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardGenericEventWithAddressTag;

  private final PublicKey badgeReceiverPublicKey = Identity.generateRandomIdentity().getPublicKey();

  private final static String FOLLOW_SETS_EVENT = "FOLLOW_SETS_EVENT";
  private final IdentifierTag followSetsIdentifierTag = new IdentifierTag(FOLLOW_SETS_EVENT);

  private final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent;
  private final FollowSetsEvent followSetsEvent;

  private final GenericEventRecord followSetsAsGenericEventEventWithEventTag;

  public EventMessageSerializerWithPubKeyTagsContainingRelay() throws ParseException {
    this.badgeDefnUpvoteEvent = new BadgeDefinitionGenericEvent(platformIdentity, upvoteIdentifierTag, relay);
    this.badgeAwardGenericEventWithAddressTag = new BadgeAwardGenericEvent<>(
        platformIdentity,
        badgeReceiverPublicKey,
        relay,
        badgeDefnUpvoteEvent);

    this.badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
        platformIdentity,
        badgeReceiverPublicKey,
        relay,
        new BadgeDefinitionGenericEvent(authorIdentity, upvoteIdentifierTag, relay));

    FormulaEvent plusOneFormulaEvent = new FormulaEvent(
        authorIdentity,
        upvoteIdentifierTag,
        relay,
        badgeDefnUpvoteEvent,
        PLUS_ONE_FORMULA);

    BadgeDefinitionReputationEvent badgeDefinitionReputationEventPlusOneFormula = new BadgeDefinitionReputationEvent(
        platformIdentity,
        authorIdentity.getPublicKey(),
        FollowSetsEvent.defaultIdentifierTag,
        relay,
        new ExternalIdentityTag("afterimage", "badge_definition_reputation", String.valueOf(BadgeDefinitionReputationEvent.class.hashCode())),
        plusOneFormulaEvent);

    this.followSetsAsGenericEventEventWithEventTag = new GenericEventRecord(
        "09848ce3194d4db99443a1032463092c33454e62b57839ab0e51676ace290c50",
        new PublicKey("703c164e01d6ba4632d440af596f21ff51e8e01f04283e1e2797de04127f91cc"),
        1769322511594L,
        Kind.FOLLOW_SETS,
        List.of(
            new IdentifierTag(FOLLOW_SETS_EVENT),
            new RelayTag(relay),
            new EventTag("2e0864780d99e270cf9c1d9f124d8efd18e9e8be7e2b8c6537c79f34ef2ed445", relay.getUrl()),
            new PubKeyTag(
                new PublicKey("fd320dfb0433681cf5a4244cbc18f82b19407beec2867fc03d8109902ecc6d0c"),
                url)),
        "AfterImage generated FollowSetsEvent",
        new Signature("27683ca56acf67502769eb2900f53803086e56e5ae6aaa8a19f12441f9b29c58f5950ee4ac05ce8559a61295e036bae3609c022522e85588b5a21de5c1518843"));

    this.followSetsEvent = new FollowSetsEvent(
        platformIdentity,
        badgeDefinitionReputationEventPlusOneFormula,
        relay,
        List.of(badgeAwardUpvoteEvent));
  }

  @Test
  void testStringEventMessageFollowSetsEventTagGenericEventRecordEncoder() throws IOException, NostrException {
    getStringEquals(
        new EventMessage(
            followSetsAsGenericEventEventWithEventTag),
        expectedStringFollowSetsEventMessageAddressTagGenericEventRecordWithPubKeyTagContainingRelay());
  }

  @Test
  void testStringEventMessageFollowSetsEventTagGenericEventRecordEncoderWithPubKeyTagContaingRelay() throws IOException, NostrException {
    getStringEquals(
        new EventMessage(
            followSetsAsGenericEventEventWithEventTag),
        expectedStringFollowSetsEventMessageAddressTagGenericEventRecordWithPubKeyTagContainingRelay());
  }

  @Test
  void testJsonEventMessageAddressTagGenericEventKindEncoder() throws IOException, NostrException {
    getJsonEquals(
        new EventMessage(
            followSetsAsGenericEventEventWithEventTag),
        expectedStringFollowSetsEventMessageAddressTagGenericEventRecordWithPubKeyTagContainingRelay());
  }

  @Test
  void testJsonEventMessageAddressTagGenericEventKindEncoderGenericEventRecord() throws IOException, NostrException {
    getJsonEquals(
        new EventMessage(
            followSetsAsGenericEventEventWithEventTag),
        expectedStringFollowSetsEventMessageAddressTagGenericEventRecordWithPubKeyTagContainingRelay());
  }

  private void getStringEquals(EventMessage eventMessage, String expected) throws IOException, NostrException {
    String actual = IDecoder.I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(eventMessage);

    assertEquals(expected, actual);
  }

  private void getJsonEquals(EventMessage eventMessage, String expected) throws IOException, NostrException {
    String actual = IDecoder.I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(eventMessage);

    assertEquals(expected, actual);

//    String actual = eventMessage.encode();
    JsonComparator comparator = (expectedJson, actualJson) -> JsonComparison.match();
    System.out.println("");
    System.out.println("");
    System.out.println(actual);
    System.out.println("");
    System.out.println("-------------");
    System.out.println("");
    System.out.println(actual);
    System.out.println("");
    System.out.println("");

    assertEquals(JsonComparison.Result.MATCH, comparator.compare(actual, actual).getResult());
  }

  private String expectedStringFollowSetsEventMessageAddressTagGenericEventRecordWithPubKeyTagContainingRelay() {
    return """
        ["EVENT",{"id":"09848ce3194d4db99443a1032463092c33454e62b57839ab0e51676ace290c50","pubkey":"703c164e01d6ba4632d440af596f21ff51e8e01f04283e1e2797de04127f91cc","created_at":1769322511594,"kind":30000,"tags":[["d","FOLLOW_SETS_EVENT"],["relay","ws://localhost:5555"],["e","2e0864780d99e270cf9c1d9f124d8efd18e9e8be7e2b8c6537c79f34ef2ed445","ws://localhost:5555"],["p","fd320dfb0433681cf5a4244cbc18f82b19407beec2867fc03d8109902ecc6d0c","ws://localhost:5555"]],"content":"AfterImage generated FollowSetsEvent","sig":"27683ca56acf67502769eb2900f53803086e56e5ae6aaa8a19f12441f9b29c58f5950ee4ac05ce8559a61295e036bae3609c022522e85588b5a21de5c1518843"}]""";
  }
}
