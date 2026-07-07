package com.prosilion.nostr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosilion.nostr.codec.BaseMessageDecoder;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.message.BaseMessage;
import com.prosilion.nostr.message.EventMessage;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.util.Util;
import java.io.IOException;
import java.util.HashSet;
import java.util.Objects;
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
    expected.debug();
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
    log.debug("testing testEventMessageGenericEventKindTypeEncoder\n");
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

    String encodedEventMessage = eventMessage.encode();
    assertEquals(encodedEventMessage, encodedBaseMessage);

    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, encodedBaseMessage).getResult());
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(json, encodedEventMessage).getResult());
  }

  @Test
  void testTaglessDecodeReEncode() throws IOException {
    EventMessage eventMessageHeaderString = tester.parseObject(headerString);
    EventMessage reEncodedEventMessage = new EventMessage(eventMessageHeaderString.getEvent().asGenericEventRecord());
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(reEncodedEventMessage.encode(), headerString).getResult());
  }

  private final static String headerString = """
["EVENT",{"id":"94bdf419d9c250ff4b0c8f66892c949174f79be713b6589d5475c27f2fe8adee","pubkey":"e04e1c1c30df6058433f61681644fd24914f2e02e420496086c61f53eb504c04","created_at":1783384757054,"kind":30008,"tags":[],"content":"AfterImage generated BadgeSetsEvent","sig":"e8895b652f1e0688b2f8afee19ed1c0cf43cbb8a085651e74cfa6a767943e230aa48c325209955e7ec83046206248c71355b67e935dcef68647401413eb432ac"}]""";

  @Test
  void testHashCode() throws IOException {
    assertEquals(JsonComparison.Result.MATCH, jsonComparator.compare(order_1, order_2).getResult());

    EventMessage eventMessage_order_1 = tester.parseObject(order_1);
    EventMessage eventMessage_order_2 = tester.parseObject(order_2);

    int event1_hashCode = eventMessage_order_1.getEvent().hashCode();
    int event2_hashCode = eventMessage_order_2.getEvent().hashCode();
    Util.debug(log, "event_hashCode:\n {}", String.valueOf(event1_hashCode), true, 'Y');
    Util.debug(log, "event_getHash:\n {}", String.valueOf(event2_hashCode), true, 'Z');
    assertEquals(event1_hashCode, event2_hashCode);
  }
  
  private final static String order_1 = """
["EVENT",{"id":"94bdf419d9c250ff4b0c8f66892c949174f79be713b6589d5475c27f2fe8adee","pubkey":"e04e1c1c30df6058433f61681644fd24914f2e02e420496086c61f53eb504c04","created_at":1783384757054,"kind":30008,"tags":[["d","a604b698a84a4117d750f69a243a1fb895c70871ce4dee10249a67a6ca70684d"],["p","985a5b9ea911bb8f9d9dca82c03f776d68fdc452b774295a874423a0fa5e8879"],["e","b419a6020b498e48f263ea71b0c189c7a7d33b88e51e4de02107d7a2ab14eb3c","ws://localhost:5555"],["a","30009:e04e1c1c30df6058433f61681644fd24914f2e02e420496086c61f53eb504c04:TEST_REPUTATION","ws://localhost:5555"],["relay","ws://localhost:5555"]],"content":"AfterImagegeneratedBadgeSetsEvent","sig":"e8895b652f1e0688b2f8afee19ed1c0cf43cbb8a085651e74cfa6a767943e230aa48c325209955e7ec83046206248c71355b67e935dcef68647401413eb432ac"}]""";

  private final static String order_2 = """
["EVENT",{"id":"94bdf419d9c250ff4b0c8f66892c949174f79be713b6589d5475c27f2fe8adee","pubkey":"e04e1c1c30df6058433f61681644fd24914f2e02e420496086c61f53eb504c04","created_at":1783384757054,"kind":30008,"tags":[["p","985a5b9ea911bb8f9d9dca82c03f776d68fdc452b774295a874423a0fa5e8879"],["a","30009:e04e1c1c30df6058433f61681644fd24914f2e02e420496086c61f53eb504c04:TEST_REPUTATION","ws://localhost:5555"],["d","a604b698a84a4117d750f69a243a1fb895c70871ce4dee10249a67a6ca70684d"],["e","b419a6020b498e48f263ea71b0c189c7a7d33b88e51e4de02107d7a2ab14eb3c","ws://localhost:5555"],["relay","ws://localhost:5555"]],"content":"AfterImagegeneratedBadgeSetsEvent","sig":"e8895b652f1e0688b2f8afee19ed1c0cf43cbb8a085651e74cfa6a767943e230aa48c325209955e7ec83046206248c71355b67e935dcef68647401413eb432ac"}]""";
}
