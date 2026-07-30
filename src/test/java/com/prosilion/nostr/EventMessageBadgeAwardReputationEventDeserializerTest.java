package com.prosilion.nostr;

import com.prosilion.nostr.codec.BaseMessageDecoder;
import com.prosilion.nostr.codec.IDecoder;
import com.prosilion.nostr.event.BadgeAwardReputationEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.CuratedFormulaEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.message.BaseMessage;
import com.prosilion.nostr.message.EventMessage;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.user.Identity;
import java.io.IOException;
import java.math.BigDecimal;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.json.JsonComparator;
import org.springframework.test.json.JsonComparison;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@JsonTest
@ActiveProfiles("test")
public class EventMessageBadgeAwardReputationEventDeserializerTest extends EventTestFixtures {
  public final static Identity aImgIdentity =
//     Identity.generateRandomIdentity();
     Identity.create("2684585483196998204846989544737603523651520600328805626488477202");

  public final static Identity submitter =
//     Identity.generateRandomIdentity();
     Identity.create("aaa4585483196998204846989544737603523651520600328805626488477202");

  public final static Identity upvoteDefnCreator =
//     Identity.generateRandomIdentity();
     Identity.create("bbb4585483196998204846989544737603523651520600328805626488477202");

  public final static Identity recipient =
//     Identity.generateRandomIdentity();
     Identity.create("ccc4585483196998204846989544737603523651520600328805626488477202");

  public final static Identity formulaCreator =
//     Identity.generateRandomIdentity();
     Identity.create("ddd4585483196998204846989544737603523651520600328805626488477202");

  public final static Identity repDefnCreator =
//     Identity.generateRandomIdentity();
     Identity.create("eee4585483196998204846989544737603523651520600328805626488477202");

  private final JsonComparator jsonComparator = (expected, actual) -> JsonComparison.match();
  @Autowired
  JacksonTester<EventMessage> tester;

  @Test
  void testDeserializeBadgeAwardReputationEventJson() throws IOException {
    String json = """
       ["EVENT",{"id":"5f66a36101d3d152c6270e18f5622d1f8bce4ac5da9ab62d7c3cc0006e590001","pubkey":"bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984","created_at":1111111111111,"kind":8,"tags":[["a","30009:5fdd11ce985348f48d552ac5060d855652d33d389b0f47c72b2269e59cfeba6e:TEST_UNIT_UPVOTE","ws://localhost:5555"],["p","fd2f8f36bbbc889b34494a1b630bd2a45e7ea8bfae09a02133ab1567014f206d"]],"content":"matching kind, author, identity-tag filter test","sig":"86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"}]""";

    EventMessage expected = tester.parseObject(json);
    BaseMessage message = BaseMessageDecoder.decode(json);
    assertEquals(expected, message);

    String encoded = expected.encode();
    log.debug("");
    log.debug("testing testEventMessageNoSubscriberIdDecoder\n");
    log.debug(json);
    log.debug("------");
    log.debug(encoded);
    log.debug("");
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, encoded).getResult());
  }

  @Test
  void testDeserializeBadgeAwardReputationEventObject() throws IOException {
    ExternalIdentityTag externalIdentityTag = new ExternalIdentityTag("platform", "identity", "proof");
    IdentifierTag formulaPlusOneIdentifierTag = new IdentifierTag(FORMULA_UNIT_UPVOTE);

    BadgeDefinitionGenericEvent badgeDefnUpvoteEvent = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, relay);

    String CONTENT = "+1";
    CuratedFormulaEvent plusOneFormulaEvent =
       new CuratedFormulaEvent(
          aImgIdentity,
          new FormulaEvent(
             formulaCreator,
             formulaPlusOneIdentifierTag,
             badgeDefnUpvoteEvent,
             CONTENT,
             relay),
          new ReferenceTag(relayArgUrl),
          relayArgRelay);

    BadgeDefinitionReputationEvent badgeDefinitionReputationEvent = new BadgeDefinitionReputationEvent(
       repDefnCreator,
       aImgIdentity.getPublicKey(),
       reputationIdentifierTag,
       externalIdentityTag, relay,
       plusOneFormulaEvent);

    BadgeAwardReputationEvent actualBadgeAwardReputationEvent = new BadgeAwardReputationEvent(
       aImgIdentity,
       recipient.getPublicKey(),
       externalIdentityTag, badgeDefinitionReputationEvent, new BigDecimal("+1"), relay
    );

    String eventId = actualBadgeAwardReputationEvent.getId();
    String createdAt = actualBadgeAwardReputationEvent.getCreatedAt().toString();
    String content = "1";
    String signature = actualBadgeAwardReputationEvent.getSignature().toString();

    String json = "[\"EVENT\",{\"id\":\"" + eventId + "\",\"pubkey\":\"" + aImgIdentity.getPublicKey().toHexString() + "\",\"created_at\":" + createdAt + ",\"kind\":8,\"tags\":[" +
       "[\"a\",\"30009:" + repDefnCreator.getPublicKey().toHexString() + ":" + TEST_UNIT_REPUTATION + "\",\"" + relayArgUrl + "\"]," +
       "[\"p\",\"" + recipient.getPublicKey().toHexString() + "\"]," +
       "[\"relay\",\"" + relay.getUrl() + "\"]," +
       "[\"i\",\"platform:identity\",\"proof\"]" +
       "],\"content\":\"" + content + "\",\"sig\":\"" + signature + "\"}]";

    logDebug(0);
    EventMessage expectedBadgeAwardReputationEventEventMessage = tester.parseObject(json);
//    EventMessage expectedBadgeAwardReputationEventEventMessage = (EventMessage) EventMessage.decode(json);
    logDebug(1);
    assertEquals(json, expectedBadgeAwardReputationEventEventMessage.encode());

//    EventMessage actualBadgeAwardReputationEventEventMessageContainingSubscriberId = getEventMessageWithSubscriberId(actualBadgeAwardReputationEvent, subscriberId);
    EventMessage actualBadgeAwardReputationEventEventMessageContainingSubscriberId = getEventMessageWithSubscriberNoId(actualBadgeAwardReputationEvent);

    logDebug(2);
    String encode = actualBadgeAwardReputationEventEventMessageContainingSubscriberId.encode();
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, encode).getResult());
    logDebug(3);
    String afterBurnerEncodedJson = IDecoder.I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(actualBadgeAwardReputationEventEventMessageContainingSubscriberId);
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, afterBurnerEncodedJson).getResult());

    BadgeAwardReputationEvent actualBadgeAwardReputationEventCtorGenericEventRecord = new BadgeAwardReputationEvent(actualBadgeAwardReputationEventEventMessageContainingSubscriberId.getEvent().asGenericEventRecord(), addressTag -> badgeDefinitionReputationEvent);

    BadgeAwardReputationEvent expectedBadgeAwardReputationEventCtorGenericEventRecord = new BadgeAwardReputationEvent(expectedBadgeAwardReputationEventEventMessage.getEvent().asGenericEventRecord(), addressTag -> badgeDefinitionReputationEvent);

    assertEquals(expectedBadgeAwardReputationEventCtorGenericEventRecord, actualBadgeAwardReputationEventCtorGenericEventRecord);

    BaseMessage message = BaseMessageDecoder.decode(json);
    assertEquals(expectedBadgeAwardReputationEventEventMessage, message);

    String encoded = message.encode();
    log.debug("");
    log.debug("testing testEventMessageNoSubscriberIdDecoder\n");
    log.debug(json);
    log.debug("------");
    log.debug(encoded);
    log.debug("");
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, encoded).getResult());
  }

  private @NonNull EventMessage getEventMessageWithSubscriberId(BadgeAwardReputationEvent badgeAwardReputationEvent, String subscriberId) {
    return new EventMessage(badgeAwardReputationEvent, subscriberId);
  }

  private @NonNull EventMessage getEventMessageWithSubscriberNoId(BadgeAwardReputationEvent badgeAwardReputationEvent) {
    return new EventMessage(badgeAwardReputationEvent);
  }

  private void logDebug(int s) {
    String newString = String.valueOf(s).repeat(10);
    log.debug(newString);
    log.debug(newString);
  }
}
