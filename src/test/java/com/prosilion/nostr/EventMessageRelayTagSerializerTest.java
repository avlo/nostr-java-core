package com.prosilion.nostr;

import com.prosilion.nostr.codec.IDecoder;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.TextNoteEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.message.EventMessage;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.io.IOException;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.boot.test.json.JsonContent;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.json.JsonComparator;
import org.springframework.test.json.JsonComparison;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@JsonTest
@ActiveProfiles("test")
public class EventMessageRelayTagSerializerTest {
  private final JacksonTester<EventMessage> tester;
  private final EventMessage eventMessage;

  @Autowired
  public EventMessageRelayTagSerializerTest(JacksonTester<EventMessage> tester) {
    this.tester = tester;
    eventMessage = new EventMessage(
        new GenericEventRecord(
            "5f66a36101d3d152c6270e18f5622d1f8bce4ac5da9ab62d7c3cc0006e590001",
            new PublicKey("bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"),
            1111111111111L,
            Kind.TEXT_NOTE,
            List.of(new RelayTag(new Relay("ws://localhost:5555"))),
            "relay tag test",
            new Signature("86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546")));
  }

  @Test
  void testEventMessageNoSubscriberIdEncoder() throws IOException, NostrException {
    checkWithoutExplicitJson(eventMessage);
    checkWithExplicitJson(eventMessage, explicitJsonEventEventTagNoUrlEncoder());
  }

  @Test
  void testEventMessageWithTextNoteEvent() throws IOException, NostrException {
    Identity identity = Identity.generateRandomIdentity();
    TextNoteEvent content = new TextNoteEvent(identity, "content");
    EventMessage eventMessageContainingSubscriberId = new EventMessage(
        content,
        "subscriberId");

    checkWithoutExplicitJson(eventMessageContainingSubscriberId);
  }

  private void checkWithoutExplicitJson(EventMessage eventMessage) throws IOException, NostrException {
    JsonContent<EventMessage> testWriterEventMessage = tester.write(eventMessage);
    JsonComparator jsonComparator = (expectedJson, actualJson) -> JsonComparison.match();

    String afterBurnerEncodedJson = IDecoder.I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(eventMessage);
    String eventMessageEncodedJson = eventMessage.encode();
    String testWriterEventMessageJson = testWriterEventMessage.getJson();

    log.debug("\nafterBurnerEncodedJson:");
    log.debug(afterBurnerEncodedJson);
    log.debug("--- testWriterEventMessage.toString() ----");
    log.debug(testWriterEventMessageJson);
    log.debug("--- eventMessage.encode() ----");
    log.debug(eventMessageEncodedJson);
    log.debug("");

    log.debug("afterBurnerEncodedJson, eventMessageEncodedJson:\n  {}",
        jsonComparator.compare(afterBurnerEncodedJson, eventMessageEncodedJson).getResult());

    log.debug("afterBurnerEncodedJson, testWriterEventMessageJson:\n  {}",
        jsonComparator.compare(afterBurnerEncodedJson, testWriterEventMessageJson).getResult());

    log.debug("eventMessageEncodedJson, testWriterEventMessageJson:\n  {}",
        jsonComparator.compare(eventMessageEncodedJson, testWriterEventMessageJson).getResult());

    assertThat(testWriterEventMessage).isEqualToJson(afterBurnerEncodedJson);
    assertThat(testWriterEventMessage).isEqualToJson(eventMessageEncodedJson);
//
    assertEquals(afterBurnerEncodedJson, eventMessageEncodedJson);
  }

  private void checkWithExplicitJson(EventMessage eventMessage, String explicitJson) throws IOException, NostrException {
    JsonContent<EventMessage> testWriterEventMessage = tester.write(eventMessage);
    JsonComparator jsonComparator = (expectedJson, actualJson) -> JsonComparison.match();

    log.debug("reference explicitJson:\n{}", explicitJson);

    String afterBurnerEncodedJson = IDecoder.I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(eventMessage);
    String eventMessageEncodedJson = eventMessage.encode();
    String testWriterEventMessageJson = testWriterEventMessage.getJson();

    log.debug("\nafterBurnerEncodedJson:");
    log.debug(afterBurnerEncodedJson);
    log.debug("--- testWriterEventMessage.toString() ----");
    log.debug(testWriterEventMessageJson);
    log.debug("--- eventMessage.encode() ----");
    log.debug(eventMessageEncodedJson);
    log.debug("");

    log.debug("explicitJson, afterBurnerEncodedJson:\n  {}",
        jsonComparator.compare(explicitJson, afterBurnerEncodedJson).getResult());

    log.debug("explicitJson, eventMessageEncodedJson:\n  {}",
        jsonComparator.compare(explicitJson, eventMessageEncodedJson).getResult());

    log.debug("explicitJson, testWriterEventMessageJson:\n  {}",
        jsonComparator.compare(explicitJson, testWriterEventMessageJson).getResult());

    assertThat(testWriterEventMessage).isEqualToJson(explicitJson);
  }

  private String explicitJsonEventEventTagNoUrlEncoder() {
    return """
            [
              "EVENT",
              {
                "id": "5f66a36101d3d152c6270e18f5622d1f8bce4ac5da9ab62d7c3cc0006e590001",
                "pubkey": "bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984",
                "created_at": 1111111111111,
                "kind": 1,
                "tags": [
                  [
                    "relay",
                    "ws://localhost:5555"
                  ]
                ],
                "content": "relay tag test",
                "sig": "86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"
              }
            ]
        """;
  }
}
