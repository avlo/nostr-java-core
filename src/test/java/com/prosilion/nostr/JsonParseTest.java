package com.prosilion.nostr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.codec.deserializer.TagDeserializer;
import com.prosilion.nostr.crypto.bech32.Bech32;
import com.prosilion.nostr.enums.Command;
import com.prosilion.nostr.event.GenericEventId;
import com.prosilion.nostr.event.internal.ElementAttribute;
import com.prosilion.nostr.event.GenericEventKindIF;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.enums.Marker;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.filter.tag.AddressTagFilter;
import com.prosilion.nostr.filter.event.AuthorFilter;
import com.prosilion.nostr.filter.event.EventFilter;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.filter.Filters;
import com.prosilion.nostr.filter.GenericTagQuery;
import com.prosilion.nostr.filter.tag.GenericTagQueryFilter;
import com.prosilion.nostr.filter.tag.GeohashTagFilter;
import com.prosilion.nostr.filter.tag.HashtagTagFilter;
import com.prosilion.nostr.filter.tag.IdentifierTagFilter;
import com.prosilion.nostr.filter.event.KindFilter;
import com.prosilion.nostr.filter.tag.ReferencedEventFilter;
import com.prosilion.nostr.filter.tag.ReferencedPublicKeyFilter;
import com.prosilion.nostr.message.BaseMessage;
import com.prosilion.nostr.codec.BaseMessageDecoder;
import com.prosilion.nostr.message.EventMessage;
import com.prosilion.nostr.message.ReqMessage;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.GenericTag;
import com.prosilion.nostr.tag.GeohashTag;
import com.prosilion.nostr.tag.HashtagTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PriceTag;
import com.prosilion.nostr.tag.PubKeyTag;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import lombok.extern.java.Log;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;
import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@JsonTest
@Log
public class JsonParseTest {

  @Test
  public void testBaseMessageDecoderEventFilter() throws JsonProcessingException {
    log.info("testBaseMessageDecoderEventFilter");

    String eventId = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    final String parseTarget =
        "[\"REQ\", " +
            "\"npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh\", " +
            "{\"kinds\": [1], " +
            "\"ids\": [\"" + eventId + "\"]," +
            "\"#p\": [\"fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712\"]}]";

    final var message = BaseMessageDecoder.decode(parseTarget);

    assertEquals(Command.REQ, message.getCommand());
    assertEquals("npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh", ((ReqMessage) message).getSubscriptionId());
    assertEquals(1, ((ReqMessage) message).getFiltersList().size());

    Filters filters = ((ReqMessage) message).getFiltersList().getFirst();

    List<Filterable> kindFilters = filters.getFilterByType(KindFilter.FILTER_KEY);
    assertEquals(1, kindFilters.size());
    assertEquals(new KindFilter<>(Kind.TEXT_NOTE), kindFilters.getFirst());

    List<Filterable> eventFilter = filters.getFilterByType(EventFilter.FILTER_KEY);
    assertEquals(1, eventFilter.size());
    assertEquals(new EventFilter<>(new GenericEventId("f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75")), eventFilter.getFirst());

    List<Filterable> referencedPublicKeyfilter = filters.getFilterByType(ReferencedPublicKeyFilter.FILTER_KEY);
    assertEquals(1, referencedPublicKeyfilter.size());
    assertEquals(new ReferencedPublicKeyFilter<>(new PubKeyTag(new PublicKey("fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712"))), referencedPublicKeyfilter.getFirst());
  }

  @Test
  public void testAbsentFilter() throws JsonProcessingException {
    final String parseTarget =
        "[\"REQ\", " +
            "\"npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh\", " +
            "{\"kinds\": [1], " +
            "\"#p\": [\"fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712\"]}]";

    final var message = BaseMessageDecoder.decode(parseTarget);

    assertEquals(Command.REQ, message.getCommand());
    assertEquals("npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh", ((ReqMessage) message).getSubscriptionId());
    assertEquals(1, ((ReqMessage) message).getFiltersList().size());

    Filters filters = ((ReqMessage) message).getFiltersList().getFirst();

    assertTrue(filters.getFilterByType(AuthorFilter.FILTER_KEY).isEmpty());
  }


  @Test
  public void testBaseMessageDecoderKindsAuthorsReferencedPublicKey() throws JsonProcessingException {
    log.info("testBaseMessageDecoderKindsAuthorsReferencedPublicKey");

    final String parseTarget =
        "[\"REQ\", " +
            "\"npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh\", " +
            "{\"kinds\": [1], " +
            "\"authors\": [\"f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75\"]," +
            "\"#p\": [\"fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712\"]}]";

    final var message = BaseMessageDecoder.decode(parseTarget);

    assertEquals(Command.REQ, message.getCommand());
    assertEquals("npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh", ((ReqMessage) message).getSubscriptionId());
    assertEquals(1, ((ReqMessage) message).getFiltersList().size());

    Filters filters = ((ReqMessage) message).getFiltersList().getFirst();

    List<Filterable> kindFilters = filters.getFilterByType(KindFilter.FILTER_KEY);
    assertEquals(1, kindFilters.size());
    assertEquals(new KindFilter<>(Kind.TEXT_NOTE), kindFilters.getFirst());

    List<Filterable> authorFilters = filters.getFilterByType(AuthorFilter.FILTER_KEY);
    assertEquals(1, authorFilters.size());
    assertEquals(new AuthorFilter<>(new PublicKey("f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75")), authorFilters.getFirst());

    List<Filterable> referencedPublicKeyfilter = filters.getFilterByType(ReferencedPublicKeyFilter.FILTER_KEY);
    assertEquals(1, referencedPublicKeyfilter.size());
    assertEquals(new ReferencedPublicKeyFilter<>(new PubKeyTag(new PublicKey("fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712"))), referencedPublicKeyfilter.getFirst());
  }

  @Test
  public void testBaseMessageDecoderKindsAuthorsReferencedEvents() throws JsonProcessingException {
    log.info("testBaseMessageDecoderKindsAuthorsReferencedEvents");

    final String parseTarget =
        "[\"REQ\", " +
            "\"npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh\", " +
            "{\"kinds\": [1], " +
            "\"authors\": [\"f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75\"]," +
            "\"#e\": [\"fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712\"]}]";

    final var message = BaseMessageDecoder.decode(parseTarget);

    assertEquals(Command.REQ, message.getCommand());
    assertEquals("npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh", ((ReqMessage) message).getSubscriptionId());
    assertEquals(1, ((ReqMessage) message).getFiltersList().size());

    Filters filters = ((ReqMessage) message).getFiltersList().getFirst();

    List<Filterable> kindFilters = filters.getFilterByType(KindFilter.FILTER_KEY);
    assertEquals(1, kindFilters.size());
    assertEquals(new KindFilter<>(Kind.TEXT_NOTE), kindFilters.getFirst());

    List<Filterable> authorFilters = filters.getFilterByType(AuthorFilter.FILTER_KEY);
    assertEquals(1, authorFilters.size());
    assertEquals(new AuthorFilter<>(new PublicKey("f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75")), authorFilters.getFirst());

    List<Filterable> referencedEventFilters = filters.getFilterByType(ReferencedEventFilter.FILTER_KEY);
    assertEquals(1, referencedEventFilters.size());
    assertEquals(new ReferencedEventFilter<>(new EventTag("fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712")), referencedEventFilters.getFirst());
  }

  @Test
  public void testBaseReqMessageDecoder() throws IOException {
    log.info("testBaseReqMessageDecoder");

    var publicKey = Identity.generateRandomIdentity().getPublicKey();

    ReqMessage expectedReqMessage = new ReqMessage(publicKey.toString(),
        new Filters(
            new KindFilter<>(Kind.SET_METADATA),
            new KindFilter<>(Kind.TEXT_NOTE),
            new KindFilter<>(Kind.CONTACT_LIST),
            new KindFilter<>(Kind.DELETION),
            new AuthorFilter<>(publicKey)));

    String jsonMessage = ENCODER_MAPPED_AFTERBURNER.writeValueAsString(expectedReqMessage);

    String jsonMsg = jsonMessage.substring(1, jsonMessage.length() - 1);

    System.out.println(jsonMessage);

    String[] parts = jsonMsg.split(",");
    assertEquals("\"REQ\"", parts[0]);
    assertEquals("\"" + publicKey.toHexString() + "\"", parts[1]);
    assertFalse(parts[2].startsWith("["));
    assertFalse(parts[parts.length - 1].endsWith("]"));

    BaseMessage actualMessage = BaseMessageDecoder.decode(jsonMessage);

    assertEquals(expectedReqMessage, actualMessage);
  }

  @Test
  public void testBaseEventMessageDecoder() throws JsonProcessingException {
    log.info("testBaseEventMessageDecoder");

    final String parseTarget
        = "[\"EVENT\","
        + "\"npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh\","
        + "{"
        + "\"content\":\"直んないわ。まあええか\","
        + "\"created_at\":1686199583,"
        + "\"id\":\"fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712\","
        + "\"kind\":1,"
        + "\"pubkey\":\"8c59239319637f97e007dad0d681e65ce35b1ace333b629e2d33f9465c132608\","
        + "\"sig\":\"9584afd231c52fcbcec6ce668a2cc4b6dc9b4d9da20510dcb9005c6844679b4844edb7a2e1e0591958b0295241567c774dbf7d39a73932877542de1a5f963f4b\","
        + "\"tags\":[]"
        + "}]";

    BaseMessage message = BaseMessageDecoder.decode(parseTarget);

    assertEquals(Command.EVENT, message.getCommand());

    final var event = (((EventMessage) message).getEvent());
//    assertEquals("npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh", ((EventMessage) message).getSubscriptionId());
    assertEquals(1, event.getKind().getValue());
    assertEquals(1686199583, event.getCreatedAt().longValue());
    assertEquals("fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712", event.getId());
  }

  @Test
  public void testBaseEventMessageAddressTagDecoder() throws JsonProcessingException {
    log.info("testBaseEventMessageAddressTagDecoder");

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

    final var event = (((EventMessage) message).getEvent());
    var tags = event.getTags();
    for (BaseTag t : tags) {
      if (t.getCode().equalsIgnoreCase("a")) {
        AddressTag et = (AddressTag) t;
        assertEquals(AddressTag.class, et.getClass());
      }
    }
  }

  @Test
  public void testBaseEventGenericEventDecoder() throws JsonProcessingException {
    log.info("testBaseEventMessageMarkerDecoder");

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
        + "[\"custom-tag\",\"2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984\"]"
        + "],"
        + "\"sig\":\"86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546\""
        + "}]";

    BaseMessage message = BaseMessageDecoder.decode(json);

    final var event = (((EventMessage) message).getEvent());
    assertTrue(event.getTags().stream().anyMatch(t -> t.getCode().equalsIgnoreCase("custom-tag")));
  }

  @Test
  public void testBaseEventMessageMarkerDecoder() throws JsonProcessingException {
    log.info("testBaseEventMessageMarkerDecoder");

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
        + "[\"e\",\"494001ac0c8af2a10f60f23538e5b35d3cdacb8e1cc956fe7a16dfa5cbfc4346\",\"\",\"root\"],"
        + "[\"p\",\"2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984\"]"
        + "],"
        + "\"sig\":\"86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546\""
        + "}]";

    BaseMessage message = BaseMessageDecoder.decode(json);

    final var event = (((EventMessage) message).getEvent());
    var tags = event.getTags();
    for (BaseTag t : tags) {
      if (t.getCode().equalsIgnoreCase("e")) {
        EventTag et = (EventTag) t;
        assertEquals(Marker.ROOT, et.getMarker());
      }
    }
  }

  @Test
  public void testGenericTagDecoder() throws JsonProcessingException {
    log.info("testGenericTagDecoder");
    final String jsonString = "[\"saturn\", \"jetpack\", false]";

    GenericTag tag = (GenericTag) TagDeserializer.decode(jsonString);

    assertEquals("saturn", tag.getCode());
    assertEquals(2, tag.getAttributes().size());
    assertEquals("jetpack", ((ElementAttribute) (tag.getAttributes().toArray())[0]).getValue());
    assertEquals(false, Boolean.valueOf(((ElementAttribute) (tag.getAttributes().toArray())[1]).getValue().toString()));
  }

  @Test
  public void testClassifiedListingTagSerializer() throws JsonProcessingException {
    log.info("testClassifiedListingSerializer");
    final String classifiedListingEventJson = "["
        + "\"EVENT\","
        + "\"temp20230627\","
        + "{"
        + "\"id\":\"28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a\","
        + "\"kind\":30402,"
        + "\"content\":\"content ipsum\","
        + "\"pubkey\":\"ec0762fe78b0f0b763d1324452d973a38bef576d1d76662722d2b8ff948af1de\","
        + "\"created_at\":1687765220,"
        + "\"tags\":["
        + "[\"p\",\"ec0762fe78b0f0b763d1324452d973a38bef576d1d76662722d2b8ff948af1de\"],"
        + "[\"title\",\"title ipsum\"],"
        + "[\"summary\",\"summary ipsum\"],"
        + "[\"published_at\",\"1687765220\"],"
        + "[\"location\",\"location ipsum\"],"
        + "[\"price\",\"11111\",\"BTC\",\"1\"]],"
        + "\"sig\":\"86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546\""
        + "}]";

    BaseMessage message = BaseMessageDecoder.decode(classifiedListingEventJson);
    GenericEventKindIF event = ((EventMessage) message).getEvent();

    assertEquals("28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a", event.getId());
    assertEquals(30402, event.getKind().getValue());
    assertEquals("content ipsum", event.getContent());
    assertEquals("ec0762fe78b0f0b763d1324452d973a38bef576d1d76662722d2b8ff948af1de", event.getPublicKey().toString());
    assertEquals(1687765220L, event.getCreatedAt());
    assertEquals("86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546", event.getSignature().toString());

    assertEquals(new BigDecimal("11111"), event.getTags().stream().filter(baseTag ->
            baseTag.getCode().equalsIgnoreCase("price"))
        .filter(PriceTag.class::isInstance)
        .map(PriceTag.class::cast)
        .map(PriceTag::number).findFirst().orElseThrow());

    assertEquals("BTC", Filterable.getTypeSpecificTags(PriceTag.class, event).stream()
        .map(PriceTag::currency).findFirst().orElseThrow());

    assertEquals("1", event.getTags().stream().filter(baseTag ->
            baseTag.getCode().equalsIgnoreCase("price"))
        .filter(PriceTag.class::isInstance)
        .map(PriceTag.class::cast)
        .map(PriceTag::frequency).findFirst().orElseThrow());

    List<GenericTag> genericTags = event.getTags().stream()
        .filter(GenericTag.class::isInstance)
        .map(GenericTag.class::cast).toList();

    assertEquals("title ipsum", genericTags.stream()
        .filter(tag -> tag.getCode().equalsIgnoreCase("title")).map(GenericTag::getAttributes).toList().getFirst().getFirst().getValue());

    assertEquals("summary ipsum", genericTags.stream()
        .filter(tag -> tag.getCode().equalsIgnoreCase("summary")).map(GenericTag::getAttributes).toList().getFirst().getFirst().getValue());

    assertEquals("1687765220", genericTags.stream()
        .filter(tag -> tag.getCode().equalsIgnoreCase("published_at")).map(GenericTag::getAttributes).toList().getFirst().getFirst().getValue());

    assertEquals("location ipsum", genericTags.stream()
        .filter(tag -> tag.getCode().equalsIgnoreCase("location")).map(GenericTag::getAttributes).toList().getFirst().getFirst().getValue());
  }

  @Test
  public void testDeserializeTag() throws Exception {
    log.info("testDeserializeTag");

    String npubHex = new PublicKey(Bech32.fromBech32("npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9")).toString();
    final String jsonString = "[\"p\", \"" + npubHex + "\", \"wss://nostr.java\", \"alice\"]";
    var tag = decode(jsonString);

    assertInstanceOf(PubKeyTag.class, tag);

    PubKeyTag pTag = (PubKeyTag) tag;
    assertEquals("wss://nostr.java", pTag.getMainRelayUrl());
    assertEquals(npubHex, pTag.getPublicKey().toString());
    assertEquals("alice", pTag.getPetName());
  }

  @Test
  public void testDeserializeGenericTag() throws Exception {
    log.info("testDeserializeGenericTag");
    String npubHex = new PublicKey(Bech32.fromBech32("npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9")).toString();
    final String jsonString = "[\"gt\", \"" + npubHex + "\", \"wss://nostr.java\", \"alice\"]";
    var tag = decode(jsonString);

    assertInstanceOf(GenericTag.class, tag);

    GenericTag gTag = (GenericTag) tag;
    assertEquals("gt", gTag.getCode());
  }

  @Test
  public void testReqMessageFilterListSerializer() {
    log.info("testReqMessageFilterListSerializer");

    String new_geohash = "2vghde";
    String second_geohash = "3abcde";

    ReqMessage reqMessage = new ReqMessage("npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9",
        new Filters(
            new GenericTagQueryFilter<>(new GenericTagQuery("#g", new_geohash)),
            new GenericTagQueryFilter<>(new GenericTagQuery("#g", second_geohash))));

    assertDoesNotThrow(() -> {
      String jsonMessage = ENCODER_MAPPED_AFTERBURNER.writeValueAsString(reqMessage);
      String expected = "[\"REQ\",\"npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9\",{\"#g\":[\"2vghde\",\"3abcde\"]}]";
      assertEquals(expected, jsonMessage);
    });
  }

  @Test
  public void testReqMessageGeohashTagDeserializer() throws JsonProcessingException {
    log.info("testReqMessageGeohashTagDeserializer");

    String subscriptionId = "npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9";
    String geohashKey = "#g";
    String geohashValue = "2vghde";
    String reqJsonWithCustomTagQueryFilterToDecode = "[\"REQ\",\"" + subscriptionId + "\",{\"" + geohashKey + "\":[\"" + geohashValue + "\"]}]";

    BaseMessage decodedReqMessage = BaseMessageDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
        new Filters(
            new GeohashTagFilter<>(new GeohashTag(geohashValue))));

    assertEquals(expectedReqMessage, decodedReqMessage);
  }

  @Test
  public void testReqMessageGeohashFilterListDecoder() {
    log.info("testReqMessageGeohashFilterListDecoder");

    String subscriptionId = "npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9";
    String geohashKey = "#g";
    String geohashValue1 = "2vghde";
    String geohashValue2 = "3abcde";
    String reqJsonWithCustomTagQueryFiltersToDecode = "[\"REQ\",\"" + subscriptionId + "\",{\"" + geohashKey + "\":[\"" + geohashValue1 + "\",\"" + geohashValue2 + "\"]}]";

    assertDoesNotThrow(() -> {
      BaseMessage decodedReqMessage = BaseMessageDecoder.decode(reqJsonWithCustomTagQueryFiltersToDecode);

      ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
          new Filters(
              new GeohashTagFilter<>(new GeohashTag(geohashValue1)),
              new GeohashTagFilter<>(new GeohashTag(geohashValue2))));

      assertEquals(reqJsonWithCustomTagQueryFiltersToDecode, ENCODER_MAPPED_AFTERBURNER.writeValueAsString(decodedReqMessage));
      assertEquals(expectedReqMessage, decodedReqMessage);
    });
  }

  @Test
  public void testReqMessageHashtagTagDeserializer() throws JsonProcessingException {
    log.info("testReqMessageHashtagTagDeserializer");

    String subscriptionId = "npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9";
    String hashtagKey = "#t";
    String hashtagValue = "2vghde";
    String reqJsonWithCustomTagQueryFilterToDecode = "[\"REQ\",\"" + subscriptionId + "\",{\"" + hashtagKey + "\":[\"" + hashtagValue + "\"]}]";

    BaseMessage decodedReqMessage = BaseMessageDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
        new Filters(
            new HashtagTagFilter<>(new HashtagTag(hashtagValue))));

    assertEquals(expectedReqMessage, decodedReqMessage);
  }

  @Test
  public void testReqMessageHashtagTagFilterListDecoder() {
    log.info("testReqMessageHashtagTagFilterListDecoder");

    String subscriptionId = "npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9";
    String hashtagKey = "#t";
    String hashtagValue1 = "2vghde";
    String hashtagValue2 = "3abcde";
    String reqJsonWithCustomTagQueryFiltersToDecode = "[\"REQ\",\"" + subscriptionId + "\",{\"" + hashtagKey + "\":[\"" + hashtagValue1 + "\",\"" + hashtagValue2 + "\"]}]";

    assertDoesNotThrow(() -> {
      BaseMessage decodedReqMessage = BaseMessageDecoder.decode(reqJsonWithCustomTagQueryFiltersToDecode);

      ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
          new Filters(
              new HashtagTagFilter<>(new HashtagTag(hashtagValue1)),
              new HashtagTagFilter<>(new HashtagTag(hashtagValue2))));

      assertEquals(reqJsonWithCustomTagQueryFiltersToDecode, ENCODER_MAPPED_AFTERBURNER.writeValueAsString((decodedReqMessage)));
      assertEquals(expectedReqMessage, decodedReqMessage);
    });
  }

  @Test
  public void testReqMessagePopulatedFilterDecoder() {
    log.info("testReqMessagePopulatedFilterDecoder");

    String subscriptionId = "npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh";
    String kind = "1";
    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String geohashKey = "#g";
    String geohashValue1 = "2vghde";
    String geohashValue2 = "3abcde";
    String referencedEventId = "fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712";
    String reqJsonWithCustomTagQueryFilterToDecode =
        "[\"REQ\", " +
            "\"" + subscriptionId + "\", " +
            "{\"kinds\": [" + kind + "], " +
            "\"authors\": [\"" + author + "\"]," +
            "\"" + geohashKey + "\": [\"" + geohashValue1 + "\",\"" + geohashValue2 + "\"]," +
            "\"#e\": [\"" + referencedEventId + "\"]," +
            "\"#p\": [\"" + author + "\"]" +
            "}]";

    assertDoesNotThrow(() -> {
      BaseMessage decodedReqMessage = BaseMessageDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

      ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
          new Filters(
              new GeohashTagFilter<>(new GeohashTag(geohashValue1)),
              new GeohashTagFilter<>(new GeohashTag(geohashValue2)),
              new ReferencedPublicKeyFilter<>(new PubKeyTag(new PublicKey(author))),
              new KindFilter<>(Kind.TEXT_NOTE),
              new AuthorFilter<>(new PublicKey(author)),
              new ReferencedEventFilter<>(new EventTag(referencedEventId))));

      assertEquals(expectedReqMessage, decodedReqMessage);
    });
  }


  @Test
  public void testReqMessagePopulatedListOfFiltersWithIdentityDecoder() throws JsonProcessingException {
    log.info("testReqMessagePopulatedListOfFiltersWithIdentityDecoder");

    String subscriptionId = "npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh";
    String kind = "1";
    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String geohashKey = "#g";
    String geohashValue1 = "2vghde";
    String geohashValue2 = "3abcde";
    String referencedEventId = "fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712";
    String uuidKey = "#d";
    String uuidValue1 = "UUID-1";
    String uuidValue2 = "UUID-2";
    String reqJsonWithCustomTagQueryFilterToDecode =
        "[\"REQ\", " +
            "\"" + subscriptionId + "\", " +
            "{\"kinds\": [" + kind + "], " +
            "\"authors\": [\"" + author + "\"]," +
            "\"" + geohashKey + "\": [\"" + geohashValue1 + "\",\"" + geohashValue2 + "\"]," +
            "\"" + uuidKey + "\": [\"" + uuidValue1 + "\",\"" + uuidValue2 + "\"]," +
            "\"#e\": [\"" + referencedEventId + "\"]}]";

    BaseMessage decodedReqMessage = BaseMessageDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
        new Filters(
            new KindFilter<>(Kind.TEXT_NOTE),
            new AuthorFilter<>(new PublicKey(author)),
            new ReferencedEventFilter<>(new EventTag(referencedEventId)),
            new GeohashTagFilter<>(new GeohashTag(geohashValue1)),
            new GeohashTagFilter<>(new GeohashTag(geohashValue2)),
            new IdentifierTagFilter<>(new IdentifierTag(uuidValue1)),
            new IdentifierTagFilter<>(new IdentifierTag(uuidValue2))));

    assertEquals(expectedReqMessage, decodedReqMessage);
  }

  @Test
  public void testReqMessagePopulatedListOfFiltersListDecoder() throws IOException {
    log.info("testReqMessagePopulatedListOfFiltersListDecoder");

    String subscriptionId = "npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh";
    Kind kind = Kind.TEXT_NOTE;
    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String referencedEventId = "fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712";
    String uuidValue1 = "UUID-1";

    String addressableTag = String.join(":", String.valueOf(kind), author, uuidValue1);

    String reqJsonWithCustomTagQueryFilterToDecode =
        "[\"REQ\", " +
            "\"" + subscriptionId + "\", " +
            "{\"kinds\": [" + kind + "], " +
            "\"authors\": [\"" + author + "\"]," +
            "\"#e\": [\"" + referencedEventId + "\"]," +
            "\"#a\": [\"" + addressableTag + "\"]," +
            "\"#p\": [\"" + author + "\"]" +
            "}]";

    BaseMessage decodedReqMessage = BaseMessageDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    AddressTag addressTag1 = new AddressTag(kind, new PublicKey(author), new IdentifierTag(uuidValue1));

    ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
        new Filters(
            new KindFilter<>(Kind.TEXT_NOTE),
            new AuthorFilter<>(new PublicKey(author)),
            new ReferencedEventFilter<>(new EventTag(referencedEventId)),
            new ReferencedPublicKeyFilter<>(new PubKeyTag(new PublicKey(author))),
            new AddressTagFilter<>(addressTag1)));

    String encoded = ENCODER_MAPPED_AFTERBURNER.writeValueAsString(expectedReqMessage);
    String decoded = ENCODER_MAPPED_AFTERBURNER.writeValueAsString(decodedReqMessage);
    assertEquals(encoded, decoded);
    assertEquals(expectedReqMessage, decodedReqMessage);
  }

  @Test
  public void testReqMessagePopulatedListOfMultipleTypeFiltersListDecoder() throws IOException {
    log.info("testReqMessagePopulatedListOfFiltersListDecoder");

    String subscriptionId = "npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh";
    String kind = "1";
    String kind2 = "2";
    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String author2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    String referencedEventId = "fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712";
    String reqJsonWithCustomTagQueryFilterToDecode =
        "[\"REQ\", " +
            "\"" + subscriptionId + "\", " +
            "{\"kinds\": [" + kind + ", " + kind2 + "], " +
            "\"authors\": [\"" + author + "\",\"" + author2 + "\"]," +
            "\"#e\": [\"" + referencedEventId + "\"]" +
            "}]";

    BaseMessage decodedReqMessage = BaseMessageDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
        new Filters(
            new KindFilter<>(Kind.TEXT_NOTE),
            new KindFilter<>(Kind.RECOMMEND_SERVER),
            new AuthorFilter<>(new PublicKey(author)),
            new AuthorFilter<>(new PublicKey(author2)),
            new ReferencedEventFilter<>(new EventTag(referencedEventId))));

    assertEquals(ENCODER_MAPPED_AFTERBURNER.writeValueAsString(expectedReqMessage), ENCODER_MAPPED_AFTERBURNER.writeValueAsString(decodedReqMessage));
    assertEquals(expectedReqMessage, decodedReqMessage);
  }

  @Test
  public void testGenericTagQueryListDecoder() throws IOException {
    log.info("testReqMessagePopulatedListOfFiltersListDecoder");

    String subscriptionId = "npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh";
    String kind = "1";
    String kind2 = "2";
    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String author2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    String geohashKey = "#g";
    String geohashValue1 = "2vghde";
    String geohashValue2 = "3abcde";
    String referencedEventId = "fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712";
    String uuidKey = "#d";
    String uuidValue1 = "UUID-1";
    String uuidValue2 = "UUID-2";
    String reqJsonWithCustomTagQueryFilterToDecode =
        "[\"REQ\", " +
            "\"" + subscriptionId + "\", " +
            "{\"kinds\": [" + kind + ", " + kind2 + "], " +
            "\"authors\": [\"" + author + "\",\"" + author2 + "\"]," +
            "\"" + geohashKey + "\": [\"" + geohashValue1 + "\",\"" + geohashValue2 + "\"]," +
            "\"" + uuidKey + "\": [\"" + uuidValue1 + "\",\"" + uuidValue2 + "\"]," +
            "\"#e\": [\"" + referencedEventId + "\"]" +
            "}]";

    BaseMessage decodedReqMessage = BaseMessageDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
        new Filters(
            new KindFilter<>(Kind.TEXT_NOTE),
            new KindFilter<>(Kind.RECOMMEND_SERVER),
            new AuthorFilter<>(new PublicKey(author)),
            new AuthorFilter<>(new PublicKey(author2)),
            new ReferencedEventFilter<>(new EventTag(referencedEventId)),
            new GeohashTagFilter<>(new GeohashTag(geohashValue1)),
            new GeohashTagFilter<>(new GeohashTag(geohashValue2)),
            new IdentifierTagFilter<>(new IdentifierTag(uuidValue1)),
            new IdentifierTagFilter<>(new IdentifierTag(uuidValue2))));

    assertTrue(JsonComparator.isEquivalentJson(
        MAPPER_AFTERBURNER.createArrayNode()
            .add(MAPPER_AFTERBURNER.readTree(
                ENCODER_MAPPED_AFTERBURNER.writeValueAsString(expectedReqMessage))),
        MAPPER_AFTERBURNER.createArrayNode()
            .add(MAPPER_AFTERBURNER.readTree(ENCODER_MAPPED_AFTERBURNER.writeValueAsString(decodedReqMessage)))));
    assertEquals(expectedReqMessage, decodedReqMessage);
  }

  @Test
  public void testReqMessageSubscriptionIdTooLong() {
    log.info("testReqMessageSubscriptionIdTooLong");

    String malformedSubscriptionId = "npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujhaa";
    final String parseTarget =
        "[\"REQ\", " +
            "\"" + malformedSubscriptionId + "\", " +
            "{\"kinds\": [1], " +
            "\"authors\": [\"f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75\"]," +
            "\"#p\": [\"fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712\"]}]";

    assertThrows(IllegalArgumentException.class, () -> BaseMessageDecoder.decode(parseTarget));
  }

  @Test
  public void testReqMessageSubscriptionIdTooShort() {
    log.info("testReqMessageSubscriptionIdTooShort");

    String malformedSubscriptionId = "";
    final String parseTarget =
        "[\"REQ\", " +
            "\"" + malformedSubscriptionId + "\", " +
            "{\"kinds\": [1], " +
            "\"authors\": [\"f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75\"]," +
            "\"#p\": [\"fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712\"]}]";

    assertThrows(IllegalArgumentException.class, () -> BaseMessageDecoder.decode(parseTarget));
  }

  @Test
  public void testBaseEventMessageDecoderMultipleFiltersJson() throws JsonProcessingException {
    log.info("testBaseEventMessageDecoderMultipleFiltersJson");

    final String eventJson
        = "[\"EVENT\","
        + "{"
        + "\"content\":\"直ん直んないわ。まあええか\","
        + "\"created_at\":1786199583,"
        + "\"id\":\"ec7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712\","
        + "\"kind\":1,"
        + "\"pubkey\":\"9c59239319637f97e007dad0d681e65ce35b1ace333b629e2d33f9465c132608\","
        + "\"sig\":\"9584afd231c52fcbcec6ce668a2cc4b6dc9b4d9da20510dcb9005c6844679b4844edb7a2e1e0591958b0295241567c774dbf7d39a73932877542de1a5f963f4b\","
        + "\"tags\":[]"
        + "}]";

    BaseMessage eventMessage = BaseMessageDecoder.decode(eventJson);

    assertEquals(Command.EVENT, eventMessage.getCommand());

    final var event = (((EventMessage) eventMessage).getEvent());
    assertEquals(Kind.TEXT_NOTE, event.getKind());
    assertEquals(1786199583, event.getCreatedAt().longValue());
    assertEquals("ec7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712", event.getId());

    String subscriptionId = "npub27x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh";
    final String requestJson =
        "[\"REQ\", " +
            "\"" + subscriptionId + "\", " +
            "{\"kinds\": [1], \"authors\": [\"9c59239319637f97e007dad0d681e65ce35b1ace333b629e2d33f9465c132608\"]}," + // first filter set
            "{\"kinds\": [1], \"#p\": [\"ec7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712\"]}" + // second filter set
            "]";

    final var message = BaseMessageDecoder.decode(requestJson);

    assertEquals(Command.REQ, message.getCommand());
    assertEquals(subscriptionId, ((ReqMessage) message).getSubscriptionId());
    assertEquals(2, ((ReqMessage) message).getFiltersList().size());
  }

  BaseTag decode(String jsonString) throws JsonProcessingException {
    return MAPPER_AFTERBURNER.readValue(jsonString, BaseTag.class);
  }

//    @Test
//    public void testReqMessageVoteTagFilterDecoder() {
//        log.info("testReqMessageVoteTagFilterDecoder");
//
//        String subscriptionId = "npub333k6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9";
//        String voteTagKey = "#v";
//        Integer voteTagValue = 1;
//        String reqJsonWithVoteTagFilterToDecode = "[\"REQ\",\"" + subscriptionId + "\",{\"" + voteTagKey + "\":[\"" + voteTagValue + "\"]}]";
//
//        assertDoesNotThrow(() -> {
//            ReqMessage decodedReqMessage = new BaseMessageDecoder<ReqMessage>().decode(reqJsonWithVoteTagFilterToDecode);
//
//            ReqMessage expectedReqMessage = new ReqMessage(subscriptionId,
//                new Filters(
//                    new VoteTagFilter<>(new VoteTag(voteTagValue))));
//
//            assertEquals(reqJsonWithVoteTagFilterToDecode, decodedReqMessage.encode());
//            assertEquals(expectedReqMessage, decodedReqMessage);
//        });
//    }
}
