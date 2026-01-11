package com.prosilion.nostr;

import com.prosilion.nostr.codec.FiltersEncoder;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.GenericEventId;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.filter.Filters;
import com.prosilion.nostr.filter.GenericTagQuery;
import com.prosilion.nostr.filter.event.AuthorFilter;
import com.prosilion.nostr.filter.event.EventFilter;
import com.prosilion.nostr.filter.event.KindFilter;
import com.prosilion.nostr.filter.event.SinceFilter;
import com.prosilion.nostr.filter.event.UntilFilter;
import com.prosilion.nostr.filter.tag.AddressTagFilter;
import com.prosilion.nostr.filter.tag.GenericTagQueryFilter;
import com.prosilion.nostr.filter.tag.GeohashTagFilter;
import com.prosilion.nostr.filter.tag.HashtagTagFilter;
import com.prosilion.nostr.filter.tag.IdentifierTagFilter;
import com.prosilion.nostr.filter.tag.ReferencedEventFilter;
import com.prosilion.nostr.filter.tag.ReferencedPublicKeyFilter;
import com.prosilion.nostr.message.ReqMessage;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.GeohashTag;
import com.prosilion.nostr.tag.HashtagTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.PublicKey;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class FiltersEncoderTest {
  public static final Relay relay = new Relay("ws://localhost:5555");

  @Test
  public void emptyFiltersTest() {
    assertThrows(IllegalArgumentException.class, Filters::new);
  }

  @Test
  public void testEventFilterEncoder() {
    log.info("testEventFilterEncoder");

    String eventId = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    assertEquals("{\"ids\":[\"" + eventId + "\"]}",
        FiltersEncoder.encode(
            new Filters(
                new EventFilter(
                    new GenericEventId(eventId)))));
  }

  @Test
  public void testMultipleEventFilterEncoder() {
    log.info("testMultipleEventFilterEncoder");

    String eventId1 = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String eventId2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    String events = String.join("\",\"", eventId1, eventId2);
    assertEquals("{\"ids\":[\"" + events + "\"]}",
        FiltersEncoder.encode(
            new Filters(
                new EventFilter(new GenericEventId(eventId1)),
                new EventFilter(new GenericEventId(eventId2)))));
  }

  @Test
  public void testSingleKindFiltersEncoder() {
    log.info("testKindFiltersEncoder");

    Kind kind = Kind.valueOf(1);
    String encodedFilters = FiltersEncoder.encode(new Filters(new KindFilter(kind)));

    String expected = "{\"kinds\":[" + kind + "]}";

    System.out.println(expected);
    System.out.println("----------");
    System.out.println(encodedFilters);
    assertEquals(expected, encodedFilters);
  }

  @Test
  public void testMixedMultipleKindFiltersMultipleAuthorFiltersEncoder() {
    log.info("testMixedMultipleKindFiltersMultipleAuthorFiltersEncoder");

    String pubKeyString1 = "111119a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e01111";
    String pubKeyString2 = "222219a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e02222";
    Kind kind1 = Kind.valueOf(1);
    Kind kind2 = Kind.valueOf(2);

    String encodedFilters = FiltersEncoder.encode(new Filters(
        List.of(
            new AuthorFilter(new PublicKey(pubKeyString1)),
            new AuthorFilter(new PublicKey(pubKeyString2)),
            new KindFilter(kind1),
            new KindFilter(kind2))));

    String expected = "{\"kinds\":[1,2],\"authors\":[\"111119a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e01111\",\"222219a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e02222\"]}";

    System.out.println(expected);
    System.out.println("----------");
    System.out.println(encodedFilters);

    assertEquals(expected, encodedFilters);
  }

  @Test
  public void testMultipleKindFiltersEncoder() {
    log.info("testMultipleKindFiltersEncoder");

    Kind kind1 = Kind.valueOf(1);
    Kind kind2 = Kind.valueOf(2);

    String encodedFilters = FiltersEncoder.encode(new Filters(
        List.of(
            new KindFilter(kind1),
            new KindFilter(kind2))));

    String kinds = String.join(",", kind1.toString(), kind2.toString());
    String expected = "{\"kinds\":[" + kinds + "]}";

    System.out.println(expected);
    System.out.println("----------");
    System.out.println(encodedFilters);

    assertEquals(expected, encodedFilters);
  }

  @Test
  public void testAuthorFilterEncoder() {
    log.info("testAuthorFilterEncoder");

    String pubKeyString = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String encodedFilters = FiltersEncoder.encode(new Filters(new AuthorFilter(new PublicKey(pubKeyString))));

    assertEquals("{\"authors\":[\"" + pubKeyString + "\"]}", encodedFilters);
  }

  @Test
  public void testMultipleAuthorFilterEncoder() {
    log.info("testMultipleAuthorFilterEncoder");

    String pubKeyString1 = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String pubKeyString2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";
    String encodedFilters = FiltersEncoder.encode(new Filters(
        List.of(
            new AuthorFilter(new PublicKey(pubKeyString1)),
            new AuthorFilter(new PublicKey(pubKeyString2)))));

    String expected = "{\"authors\":[\"f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75\",\"aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa\"]}";

    assertEquals(expected, encodedFilters);
  }

  @Test
  public void testAddressableTagFilterKindAndPublicKey() {
    log.info("testAddressableTagFilterKindAndPublicKey");

    Integer kind = 1;
    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    AddressTag addressTag = new AddressTag(Kind.TEXT_NOTE, new PublicKey(author));

    String encodedFilters = FiltersEncoder.encode(new Filters(new AddressTagFilter(addressTag)));
    String addressableTag = String.join(":", String.valueOf(kind), author) + ":";

    assertEquals("{\"#a\":[\"" + addressableTag + "\"]}", encodedFilters);
  }

  @Test
  public void testIdentifierTagFilterEncoder() {
    log.info("testIdentifierTagFilterEncoder");

    String uuidValue1 = "UUID-1";

    String encodedFilters = FiltersEncoder.encode(new Filters(new IdentifierTagFilter(new IdentifierTag(uuidValue1))));

    assertEquals("{\"#d\":[\"" + uuidValue1 + "\"]}", encodedFilters);
  }

  @Test
  public void testMultipleIdentifierTagFilterEncoder() {
    log.info("testMultipleIdentifierTagFilterEncoder");

    String uuidValue1 = "UUID-1";
    String uuidValue2 = "UUID-2";

    String encodedFilters = FiltersEncoder.encode(
        new Filters(
            List.of(
                new IdentifierTagFilter(new IdentifierTag(uuidValue1)),
                new IdentifierTagFilter(new IdentifierTag(uuidValue2)))));

    assertEquals("{\"#d\":[\"" + uuidValue1 + "\",\"" + uuidValue2 + "\"]}", encodedFilters);
  }

  @Test
  public void testReferencedEventFilterEncoder() {
    log.info("testReferencedEventFilterEncoder");

    String eventId = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    String encodedFilters = FiltersEncoder.encode(new Filters(new ReferencedEventFilter(new EventTag(eventId, relay.getUrl()))));
    String expected = "{\"#e\":[\"" + eventId + "\"]}";

    System.out.println(expected);
    System.out.println("----------");
    System.out.println(encodedFilters);

    assertEquals(expected, encodedFilters);
  }

  @Test
  public void testMultipleReferencedEventFilterEncoder() {
    log.info("testMultipleReferencedEventFilterEncoder");

    String eventId1 = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String eventId2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    String encodedFilters = FiltersEncoder.encode(new Filters(
        List.of(
            new ReferencedEventFilter(new EventTag(eventId1, relay.getUrl())),
            new ReferencedEventFilter(new EventTag(eventId2, relay.getUrl())))));

    String expected = String.join("\",\"", eventId1, eventId2);
    System.out.println(expected);
    System.out.println("----------");
    System.out.println(encodedFilters);

    assertEquals("{\"#e\":[\"" + expected + "\"]}", encodedFilters);
  }

  @Test
  public void testReferencedPublicKeyFilterEncoder() {
    log.info("testReferencedPublicKeyFilterEncoder");

    String pubKeyString = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    String encodedFilters = FiltersEncoder.encode(new Filters(new ReferencedPublicKeyFilter(new PubKeyTag(new PublicKey(pubKeyString)))));
    assertEquals("{\"#p\":[\"" + pubKeyString + "\"]}", encodedFilters);
  }

  @Test
  public void testMultipleReferencedPublicKeyFilterEncoder() {
    log.info("testMultipleReferencedPublicKeyFilterEncoder");

    String pubKeyString1 = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String pubKeyString2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    String encodedFilters = FiltersEncoder.encode(new Filters(
        new ReferencedPublicKeyFilter(new PubKeyTag(new PublicKey(pubKeyString1))),
        new ReferencedPublicKeyFilter(new PubKeyTag(new PublicKey(pubKeyString2)))));

    String pubKeyTags = String.join("\",\"", pubKeyString1, pubKeyString2);
    assertEquals("{\"#p\":[\"" + pubKeyTags + "\"]}", encodedFilters);
  }

  @Test
  public void testSingleGeohashTagFiltersEncoder() {
    log.info("testSingleGeohashTagFiltersEncoder");

    String new_geohash = "2vghde";

    String encodedFilters = FiltersEncoder.encode(
        new Filters(new GeohashTagFilter(new GeohashTag(new_geohash))));


    assertEquals("{\"#g\":[\"2vghde\"]}", encodedFilters);
  }

  @Test
  public void testMultipleGeohashTagFiltersEncoder() {
    log.info("testMultipleGenericTagFiltersEncoder");

    String geohashValue1 = "2vghde";
    String geohashValue2 = "3abcde";

    String encodedFilters = FiltersEncoder.encode(new Filters(
        new GeohashTagFilter(new GeohashTag(geohashValue1)),
        new GeohashTagFilter(new GeohashTag(geohashValue2))));

    assertEquals("{\"#g\":[\"" + geohashValue1 + "\",\"" + geohashValue2 + "\"]}", encodedFilters);
  }

  @Test
  public void testSingleHashtagTagFiltersEncoder() {
    log.info("testSingleHashtagTagFiltersEncoder");

    String hashtag_target = "2vghde";

    String encodedFilters = FiltersEncoder.encode(
        new Filters(new HashtagTagFilter(new HashtagTag(hashtag_target))));


    assertEquals("{\"#t\":[\"2vghde\"]}", encodedFilters);
  }

  @Test
  public void testMultipleHashtagTagFiltersEncoder() {
    log.info("testMultipleHashtagTagFiltersEncoder");

    String hashtagValue1 = "2vghde";
    String hashtagValue2 = "3abcde";

    String encodedFilters = FiltersEncoder.encode(new Filters(
        new HashtagTagFilter(new HashtagTag(hashtagValue1)),
        new HashtagTagFilter(new HashtagTag(hashtagValue2))));

    assertEquals("{\"#t\":[\"" + hashtagValue1 + "\",\"" + hashtagValue2 + "\"]}", encodedFilters);
  }

  @Test
  public void testSingleCustomGenericTagQueryFiltersEncoder() {
    log.info("testSingleCustomGenericTagQueryFiltersEncoder");

    String customKey = "#b";
    String customValue = "2vghde";

    String encodedFilters = FiltersEncoder.encode(
        new Filters(new GenericTagQueryFilter(new GenericTagQuery(customKey, customValue))));


    assertEquals("{\"#b\":[\"2vghde\"]}", encodedFilters);
  }

  @Test
  public void testMultipleCustomGenericTagQueryFiltersEncoder() {
    log.info("testMultipleCustomGenericTagQueryFiltersEncoder");

    String customKey = "#b";
    String customValue1 = "2vghde";
    String customValue2 = "3abcde";

    String encodedFilters = FiltersEncoder.encode(new Filters(
        new GenericTagQueryFilter(new GenericTagQuery(customKey, customValue1)),
        new GenericTagQueryFilter(new GenericTagQuery(customKey, customValue2))));

    assertEquals("{\"#b\":[\"2vghde\",\"3abcde\"]}", encodedFilters);
  }

  @Test
  public void testAddressableTagWithRelayFilterEncoder() {
    log.info("testAddressableTagWithRelayFilterEncoder");

    Integer kind = 1;
    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String uuidValue1 = "UUID-1";
    String url = "ws://localhost:5555";
    Relay relay = new Relay(url);

    String actual = FiltersEncoder.encode(
        new Filters(
            new AddressTagFilter(
                new AddressTag(
                    Kind.TEXT_NOTE,
                    new PublicKey(author),
                    new IdentifierTag(uuidValue1),
                    relay))));

    String addressableTag = String.join(":", String.valueOf(kind), author, uuidValue1);

    String expected = "{\"#a\":[\"" + addressableTag + "\"]}";
    System.out.println(expected);
    System.out.println("----------");
    System.out.println(actual);
    assertEquals(expected, actual);
  }

  @Test
  public void testMultipleAddressableTagFilterEncoder() {
    log.info("testMultipleAddressableTagFilterEncoder");

    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String uuidValue1 = "UUID-1";
    String uuidValue2 = "UUID-2";

    AddressTag addressTag1 = new AddressTag(Kind.TEXT_NOTE, new PublicKey(author), new IdentifierTag(uuidValue1));
    AddressTag addressTag2 = new AddressTag(Kind.TEXT_NOTE, new PublicKey(author), new IdentifierTag(uuidValue2));

    String encodedFilters = FiltersEncoder.encode(new Filters(
        new AddressTagFilter(addressTag1),
        new AddressTagFilter(addressTag2)));

    String expected = "{\"#a\":[\"1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:UUID-1\",\"1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:UUID-2\"]}";

    System.out.println(expected);
    System.out.println("----------");
    System.out.println(encodedFilters);

    assertEquals(expected, encodedFilters);
  }

  @Test
  public void testMultipleAddressableTagsWithRelaysFilterEncoder() {
    log.info("testMultipleAddressableTagsWithRelaysFilterEncoder");

    String author1 = "111119a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e11111";
    String author2 = "222229a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e22222";
    String uuidValue1 = "UUID-1";
    String uuidValue2 = "UUID-2";

    String url = "ws://localhost:5555";
    Relay relay = new Relay(url);

    AddressTag addressTag1 = new AddressTag(Kind.TEXT_NOTE, new PublicKey(author1), new IdentifierTag(uuidValue1), relay);
    AddressTag addressTag2 = new AddressTag(Kind.TEXT_NOTE, new PublicKey(author2), new IdentifierTag(uuidValue2), relay);

    String encodedFilters = FiltersEncoder.encode(new Filters(
        new AddressTagFilter(addressTag1),
        new AddressTagFilter(addressTag2)));

    String expected = "{\"#a\":[\"1:111119a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e11111:UUID-1\",\"1:222229a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e22222:UUID-2\"]}";

    System.out.println(expected);
    System.out.println("----------");
    System.out.println(encodedFilters);

    assertEquals(expected, encodedFilters);
  }

  @Test
  public void testSinceFiltersEncoder() {
    log.info("testSinceFiltersEncoder");

    Long since = Date.from(Instant.now()).getTime();

    String encodedFilters = FiltersEncoder.encode(new Filters(new SinceFilter(since)));

    assertEquals("{\"since\":" + since + "}", encodedFilters);
  }

  @Test
  public void testUntilFiltersEncoder() {
    log.info("testUntilFiltersEncoder");

    Long until = Date.from(Instant.now()).getTime();

    String encodedFilters = FiltersEncoder.encode(new Filters(new UntilFilter(until)));

    assertEquals("{\"until\":" + until + "}", encodedFilters);
  }

  @Test
  public void testReqMessageEmptyFilters() {
    log.info("testReqMessageEmptyFilters");
    String subscriptionId = "npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9";

    assertThrows(IllegalArgumentException.class, () -> new ReqMessage(subscriptionId, new Filters(List.of())));
  }

  @Test
  public void testReqMessageCustomGenericTagFilter() {
    log.info("testReqMessageEmptyFilterKey");
    String subscriptionId = "npub1clk6vc9xhjp8q5cws262wuf2eh4zuvwupft03hy4ttqqnm7e0jrq3upup9";

    assertDoesNotThrow(() ->
        new ReqMessage(subscriptionId, new Filters(
            new GenericTagQueryFilter(new GenericTagQuery("some-tag", "some-value")))));
  }
}
