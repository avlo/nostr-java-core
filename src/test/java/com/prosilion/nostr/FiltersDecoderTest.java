package com.prosilion.nostr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosilion.nostr.codec.FiltersDecoder;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.GenericEventId;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.filter.Filters;
import com.prosilion.nostr.filter.GenericTagQuery;
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
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.GeohashTag;
import com.prosilion.nostr.tag.HashtagTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.PublicKey;
import java.time.Instant;
import java.util.Date;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class FiltersDecoderTest {

  @Test
  public void testEventFiltersDecoder() throws JsonProcessingException {
    log.info("testEventFiltersDecoder");

    String filterKey = "ids";
    String eventId = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    String expected = "{\"" + filterKey + "\":[\"" + eventId + "\"]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(
        new Filters(
            new EventFilter<>(new GenericEventId(eventId))),
        decodedFilters);
  }

  @Test
  public void testMultipleEventFiltersDecoder() throws JsonProcessingException {
    log.info("testMultipleEventFiltersDecoder");

    String filterKey = "ids";
    String eventId1 = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String eventId2 = "aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa";

    String joined = String.join("\",\"", eventId1, eventId2);

    String expected = "{\"" + filterKey + "\":[\"" + joined + "\"]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(
        new Filters(
            new EventFilter<>(new GenericEventId(eventId1)),
            new EventFilter<>(new GenericEventId(eventId2))),
        decodedFilters);
  }

  @Test
  public void testAddressTagFiltersKindPublicKey() throws JsonProcessingException {
    log.info("testAddressTagFiltersKindPublicKey");

    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    String manualJoined = "1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:";

    AddressTag addressTag = new AddressTag(Kind.TEXT_NOTE, new PublicKey(author));

    String expected = "{\"#a\":[\"" + manualJoined + "\"]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    Filters expectedFilters = new Filters(
        new AddressTagFilter(addressTag));
    assertEquals(
        expectedFilters,
        decodedFilters);
  }

  @Test
  public void testAddressTagFiltersKindPublicKeyIdentifierTag() throws JsonProcessingException {
    log.info("testAddressTagFiltersKindPublicKeyIdentifierTag");

    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String uuidValue1 = "UUID-1";

    String manualJoined = "1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:UUID-1";

    AddressTag addressTag = new AddressTag(Kind.TEXT_NOTE, new PublicKey(author), new IdentifierTag(uuidValue1));

    String expected = "{\"#a\":[\"" + manualJoined + "\"]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(
        new Filters(
            new AddressTagFilter(addressTag)),
        decodedFilters);
  }

  @Test
  public void testAddressableTagFiltersWithRelayDecoder() throws JsonProcessingException {
    log.info("testAddressableTagFiltersWithRelayDecoder");

    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String uuidValue1 = "UUID-1";
    Relay relay = new Relay("ws://localhost:5555");

    String manualJoined = "1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:UUID-1";

    AddressTag addressTag = new AddressTag(Kind.TEXT_NOTE, new PublicKey(author), new IdentifierTag(uuidValue1), relay);

    String manualExpected = String.join("\",\"", manualJoined, relay.getUri());
    String addressableTag = "{\"#a\":[\"" + manualExpected + "\"]}";
    Filters decodedFilters = FiltersDecoder.decode(addressableTag);

    assertEquals(
        new Filters(
            new AddressTagFilter(addressTag)),
        decodedFilters);
  }

  @Test
  public void testKindFiltersDecoder() throws JsonProcessingException {
    log.info("testKindFiltersDecoder");

    String filterKey = KindFilter.FILTER_KEY;
    Kind kind = Kind.valueOf(1);

    String expected = "{\"" + filterKey + "\":[" + kind.toString() + "]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(new Filters(new KindFilter<>(kind)), decodedFilters);
  }

  @Test
  public void testMultipleKindFiltersDecoder() throws JsonProcessingException {
    log.info("testMultipleKindFiltersDecoder");

    String filterKey = KindFilter.FILTER_KEY;
    Kind kind1 = Kind.valueOf(1);
    Kind kind2 = Kind.valueOf(2);

    String join = String.join(",", kind1.toString(), kind2.toString());

    String expected = "{\"" + filterKey + "\":[" + join + "]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(
        new Filters(
            new KindFilter<>(kind1),
            new KindFilter<>(kind2)),
        decodedFilters);
  }

  @Test
  public void testIdentifierTagFilterDecoder() throws JsonProcessingException {
    log.info("testIdentifierTagFilterDecoder");

    String uuidValue1 = "UUID-1";

    String expected = "{\"#d\":[\"" + uuidValue1 + "\"]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);


    assertEquals(new Filters(new IdentifierTagFilter(new IdentifierTag(uuidValue1))), decodedFilters);
  }

  @Test
  public void testMultipleIdentifierTagFilterDecoder() throws JsonProcessingException {
    log.info("testMultipleIdentifierTagFilterDecoder");

    String uuidValue1 = "UUID-1";
    String uuidValue2 = "UUID-2";

    String joined = String.join("\",\"", uuidValue1, uuidValue2);
    String expected = "{\"#d\":[\"" + joined + "\"]}";

    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(
        new Filters(
            new IdentifierTagFilter(new IdentifierTag(uuidValue1)),
            new IdentifierTagFilter(new IdentifierTag(uuidValue2))),
        decodedFilters);
  }

  @Test
  public void testReferencedEventFilterDecoder() throws JsonProcessingException {
    log.info("testReferencedEventFilterDecoder");

    String eventId = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    String expected = "{\"#e\":[\"" + eventId + "\"]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(new Filters(new ReferencedEventFilter(new EventTag(eventId))), decodedFilters);
  }

  @Test
  public void testMultipleReferencedEventFilterDecoder() throws JsonProcessingException {
    log.info("testMultipleReferencedEventFilterDecoder");

    String eventId1 = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String eventId2 = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    String joined = String.join("\",\"", eventId1, eventId2);
    String expected = "{\"#e\":[\"" + joined + "\"]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(
        new Filters(
            new ReferencedEventFilter(new EventTag(eventId1)),
            new ReferencedEventFilter(new EventTag(eventId2))),
        decodedFilters);
  }

  @Test
  public void testReferencedPublicKeyFilterDecofder() throws JsonProcessingException {
    log.info("testReferencedPublicKeyFilterDecoder");

    String pubkeyString = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    String expected = "{\"#p\":[\"" + pubkeyString + "\"]}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(new Filters(new ReferencedPublicKeyFilter(new PubKeyTag(new PublicKey(pubkeyString)))), decodedFilters);
  }

  @Test
  public void testMultipleReferencedPublicKeyFilterDecoder() throws JsonProcessingException {
    log.info("testMultipleReferencedPublicKeyFilterDecoder");

    String pubkeyString1 = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String pubkeyString2 = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";

    String joined = String.join("\",\"", pubkeyString1, pubkeyString2);
    String expected = "{\"#p\":[\"" + joined + "\"]}";

    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(
        new Filters(
            new ReferencedPublicKeyFilter(new PubKeyTag(new PublicKey(pubkeyString1))),
            new ReferencedPublicKeyFilter(new PubKeyTag(new PublicKey(pubkeyString2)))),
        decodedFilters);
  }

  @Test
  public void testGeohashTagFiltersDecoder() throws JsonProcessingException {
    log.info("testGeohashTagFiltersDecoder");

    String geohashKey = "#g";
    String geohashValue = "2vghde";
    String reqJsonWithCustomTagQueryFilterToDecode = "{\"" + geohashKey + "\":[\"" + geohashValue + "\"]}";

    Filters decodedFilters = FiltersDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    assertEquals(new Filters(new GeohashTagFilter(new GeohashTag(geohashValue))), decodedFilters);
  }

  @Test
  public void testMultipleGeohashTagFiltersDecoder() throws JsonProcessingException {
    log.info("testMultipleGeohashTagFiltersDecoder");

    String geohashKey = "#g";
    String geohashValue1 = "2vghde";
    String geohashValue2 = "3abcde";
    String reqJsonWithCustomTagQueryFilterToDecode = "{\"" + geohashKey + "\":[\"" + geohashValue1 + "\",\"" + geohashValue2 + "\"]}";

    Filters decodedFilters = FiltersDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    assertEquals(new Filters(
            new GeohashTagFilter(new GeohashTag(geohashValue1)),
            new GeohashTagFilter(new GeohashTag(geohashValue2))),
        decodedFilters);
  }

  @Test
  public void testHashtagTagFiltersDecoder() throws JsonProcessingException {
    log.info("testHashtagTagFiltersDecoder");

    String hashtagKey = "#t";
    String hashtagValue = "2vghde";
    String reqJsonWithCustomTagQueryFilterToDecode = "{\"" + hashtagKey + "\":[\"" + hashtagValue + "\"]}";

    Filters decodedFilters = FiltersDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    assertEquals(new Filters(new HashtagTagFilter(new HashtagTag(hashtagValue))), decodedFilters);
  }

  @Test
  public void testMultipleHashtagTagFiltersDecoder() throws JsonProcessingException {
    log.info("testMultipleHashtagTagFiltersDecoder");

    String hashtagKey = "#t";
    String hashtagValue1 = "2vghde";
    String hashtagValue2 = "3abcde";
    String reqJsonWithCustomTagQueryFilterToDecode = "{\"" + hashtagKey + "\":[\"" + hashtagValue1 + "\",\"" + hashtagValue2 + "\"]}";

    Filters decodedFilters = FiltersDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    assertEquals(new Filters(
            new HashtagTagFilter(new HashtagTag(hashtagValue1)),
            new HashtagTagFilter(new HashtagTag(hashtagValue2))),
        decodedFilters);
  }

  @Test
  public void testGenericTagFiltersDecoder() throws JsonProcessingException {
    log.info("testGenericTagFiltersDecoder");

    String customTagKey = "#b";
    String customTagValue = "2vghde";
    String reqJsonWithCustomTagQueryFilterToDecode = "{\"" + customTagKey + "\":[\"" + customTagValue + "\"]}";

    Filters decodedFilters = FiltersDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    assertEquals(new Filters(new GenericTagQueryFilter(new GenericTagQuery(customTagKey, customTagValue))), decodedFilters);
  }

  @Test
  public void testMultipleGenericTagFiltersDecoder() throws JsonProcessingException {
    log.info("testMultipleGenericTagFiltersDecoder");

    String customTagKey = "#b";
    String customTagValue1 = "2vghde";
    String customTagValue2 = "3abcde";

    String reqJsonWithCustomTagQueryFilterToDecode = "{\"" + customTagKey + "\":[\"" + customTagValue1 + "\",\"" + customTagValue2 + "\"]}";

    Filters decodedFilters = FiltersDecoder.decode(reqJsonWithCustomTagQueryFilterToDecode);

    assertEquals(
        new Filters(
            new GenericTagQueryFilter(new GenericTagQuery(customTagKey, customTagValue1)),
            new GenericTagQueryFilter(new GenericTagQuery(customTagKey, customTagValue2))),
        decodedFilters);
  }

  @Test
  public void testSinceFiltersDecoder() throws JsonProcessingException {
    log.info("testSinceFiltersDecoder");

    Long since = Date.from(Instant.now()).getTime();

    String expected = "{\"since\":" + since + "}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(new Filters(new SinceFilter(since)), decodedFilters);
  }

  @Test
  public void testUntilFiltersDecoder() throws JsonProcessingException {
    log.info("testUntilFiltersDecoder");

    Long until = Date.from(Instant.now()).getTime();

    String expected = "{\"until\":" + until + "}";
    Filters decodedFilters = FiltersDecoder.decode(expected);

    assertEquals(new Filters(new UntilFilter(until)), decodedFilters);
  }

  @Test
  public void testFailedAddressableTagMalformedSeparator() {
    log.info("testFailedAddressableTagMalformedSeparator");

    Integer kind = 1;
    String author = "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75";
    String uuidValue1 = "UUID-1";

    String malformedJoin = String.join(",", String.valueOf(kind), author, uuidValue1);
    String expected = "{\"#a\":[\"" + malformedJoin + "\"]}";

    assertThrows(NumberFormatException.class, () -> FiltersDecoder.decode(expected));
  }
}
