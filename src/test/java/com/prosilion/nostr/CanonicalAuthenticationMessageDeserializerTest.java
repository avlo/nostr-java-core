package com.prosilion.nostr;

import com.prosilion.nostr.codec.BaseMessageDecoder;
import com.prosilion.nostr.message.BaseMessage;
import com.prosilion.nostr.message.CanonicalAuthenticationMessage;
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
public class CanonicalAuthenticationMessageDeserializerTest {
  private final JsonComparator jsonComparator = (expected, actual) -> JsonComparison.match();
  @Autowired
  JacksonTester<CanonicalAuthenticationMessage> tester;


  @Test
  void testEventMessageNoSubscriberIdDecoder() throws IOException, NostrException {
    final String json = """
            [ "AUTH", {
              "id" : "5f66a36101d3d152c6270e18f5622d1f8bce4ac5da9ab62d7c3cc0006e590001",
              "pubkey" : "bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984",
              "created_at" : 1111111111111,
              "kind" : 22242,
              "tags" : [ [ "challenge", "challenge" ], [ "relay", "ws://localhost:5555" ] ],
              "content" : "matching kind, author, identity-tag filter test",
              "sig" : "86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"
            } ]
        """;

    CanonicalAuthenticationMessage expected = tester.parseObject(json);
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
    final String json = """
            [ "AUTH", {
              "id" : "5f66a36101d3d152c6270e18f5622d1f8bce4ac5da9ab62d7c3cc0006e590001",
              "pubkey" : "bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984",
              "created_at" : 1111111111111,
              "kind" : 22242,
              "tags" : [ [ "challenge", "challenge" ], [ "relay", "wss://localhost:5555" ] ],
              "content" : "matching kind, author, identity-tag filter test",
              "sig" : "86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"
            } ]
        """;

    BaseMessage message = BaseMessageDecoder.decode(json);
    CanonicalAuthenticationMessage expected = tester.parseObject(json);
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
}
