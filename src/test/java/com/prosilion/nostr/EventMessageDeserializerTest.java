package com.prosilion.nostr;

import com.prosilion.nostr.codec.BaseMessageDecoder;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.message.BaseMessage;
import com.prosilion.nostr.message.EventMessage;
import java.io.IOException;
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
public class EventMessageDeserializerTest {
  private final JsonComparator jsonComparator = (expected, actual) -> JsonComparison.match();
  @Autowired
  JacksonTester<EventMessage> tester;


  @Test
  void testEventMessageNoSubscriberIdDecoder() throws IOException, NostrException {
    final String json = "["
        + "\"EVENT\","
        + "{"
        + "\"id\":\"28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a\","
        + "\"kind\":1,"
        + "\"pubkey\":\"2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984\","
        + "\"created_at\":1687765220,"
        + "\"content\":\"手順書が間違ってたら作業者は無理だな\","
        + "\"tags\":["
        + "[\"a\",\"1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:UUID-1\"],"
        + "[\"p\",\"2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984\"]"
        + "],"
        + "\"sig\":\"86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546\""
        + "}]";

    EventMessage expected = tester.parseObject(json);
    BaseMessage message = BaseMessageDecoder.decode(json);
    assertEquals(expected, message);

    String encoded = message.encode();
    log.debug("");
    log.debug("testing testEventMessageNoSubscriberIdDecoder\n");
    log.debug(json);
    log.debug("------");
    log.debug(encoded);
    log.debug("");
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, encoded).getResult());
  }

  @Test
  void testEventMessageWithSubscriberIdDecoder() throws IOException, NostrException {
    final String json = "["
        + "\"EVENT\","
        + "\"temp20230627\","
        + "{"
        + "\"id\":\"28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a\","
        + "\"kind\":1,"
        + "\"pubkey\":\"2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984\","
        + "\"created_at\":1687765220,"
        + "\"content\":\"手順書が間違ってたら作業者は無理だな\","
        + "\"tags\":["
        + "[\"a\",\"1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:UUID-1\"],"
        + "[\"p\",\"2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984\"]"
        + "],"
        + "\"sig\":\"86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546\""
        + "}]";

    BaseMessage message = BaseMessageDecoder.decode(json);
    EventMessage expected = tester.parseObject(json);
    assertEquals(expected, message);

    String encoded = message.encode();
    log.debug("");
    log.debug("testing testEventMessageWithSubscriberIdDecoder\n");
    log.debug(json);
    log.debug("------");
    log.debug(encoded);
    log.debug("");
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, encoded).getResult());
  }

  @Test
  void testEventMessageGenericEventKindTypeEncoder() throws IOException, NostrException {
    final String json = "["
        + "\"EVENT\","
        + "{"
        + "\"id\":\"28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a\","
        + "\"kind\":8,"
        + "\"pubkey\":\"2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984\","
        + "\"created_at\":1687765220,"
        + "\"content\":\"手順書が間違ってたら作業者は無理だな\","
        + "\"tags\":["
        + "[\"a\",\"1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:upvote\"],"
        + "[\"p\",\"2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984\"]"
        + "],"
        + "\"sig\":\"86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546\""
        + "}]";

    BaseMessage baseMessage = BaseMessageDecoder.decode(json);
    EventMessage eventMessage = tester.parseObject(json);
    EventIF expected = eventMessage.getEvent();
    assertEquals(expected, ((EventMessage) baseMessage).getEvent());

    assertEquals(
        eventMessage,
        baseMessage);

    String encodedBaseMessage = baseMessage.encode();
    log.debug("");
    log.debug("testing testEventMessageGenericEventKindTypeEncoder\n");
    log.debug(json);
    log.debug("------");
    log.debug(encodedBaseMessage);
    log.debug("");

    String encodedEventMessage = eventMessage.encode();
    assertEquals(encodedEventMessage, encodedBaseMessage);
    
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, encodedBaseMessage).getResult());
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, encodedEventMessage).getResult());
  }
}
