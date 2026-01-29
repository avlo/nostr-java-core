package com.prosilion.nostr;

import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.message.EventMessage;
import java.io.IOException;
import java.util.function.Supplier;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.boot.test.json.JacksonTester;
import org.springframework.test.context.ActiveProfiles;

@Slf4j
@JsonTest
@ActiveProfiles("test")
public class EventMessageEventMessageDebugStringsTest {
  @Autowired
  JacksonTester<EventMessage> tester;

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

  @SneakyThrows
  @Test
  void testEventMessageEventIFDebugString() throws IOException, NostrException {
    EventMessage expected = tester.parseObject(json);
    System.out.println("11111111111111");
    System.out.println("11111111111111");

    System.out.println("EventMessage debug()");
    expected.debug();
    System.out.println("--------------");

    System.out.println("expected.encode()");
    expected.encode();
    System.out.println("--------------");

    System.out.println("event::createPrettyPrintJson");
    log.debug(expected.getEvent().createPrettyPrintJson());

    System.out.println("11111111111111");
    System.out.println("11111111111111");
  }

  @Test
  void testEventMessageDebugString() throws IOException, NostrException {
    EventMessage expected = tester.parseObject(json);
    System.out.println("00000000000000");
    System.out.println("00000000000000");

    System.out.println("EventMessage debug()");
    expected.debug();
    System.out.println("--------------");

    System.out.println("expected.getEvent().createPrettyPrintJson()");
    EventIF event = expected.getEvent();
    log.debug(event.createPrettyPrintJson());
    System.out.println("--------------");

    System.out.println("event::createPrettyPrintJson");
    Supplier<String> stringSupplier = event::createPrettyPrintJson;
    log.debug(stringSupplier.get());

    System.out.println("00000000000000");
    System.out.println("00000000000000");

  }
}
