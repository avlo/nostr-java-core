package com.prosilion.nostr;

import com.prosilion.nostr.message.BaseMessage;
import com.prosilion.nostr.codec.BaseMessageDecoder;
import com.prosilion.nostr.message.EventMessage;
import java.io.IOException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@ActiveProfiles("test")
public class EventMessageDeserializerTest {
  @Autowired
  JacksonTester<EventMessage> tester;

  @Test
  void testEventMessageNoSubscriberIdDecoder() throws IOException {
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

    assertEquals(
        tester.parseObject(json),
        BaseMessageDecoder.decode(json));
  }

  @Test
  void testEventMessageWithSubscriberIdDecoder() throws IOException {
    final String eventJsonIncludingSubscriberId = "["
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

    BaseMessage actual = BaseMessageDecoder.decode(eventJsonIncludingSubscriberId);
    assertEquals(
        tester.parseObject(eventJsonIncludingSubscriberId),
        actual);
  }
}
