package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@Slf4j
@ActiveProfiles("test")
public class GenericEventKindTest {

  public final String eventId = EventTagTest.generateRandomHex64String();
  public final PublicKey pubkey = new PublicKey(EventTagTest.generateRandomHex64String());
  public final String signature = EventTagTest.generateRandomHex64String().concat(EventTagTest.generateRandomHex64String());

  @Test
  void testEqualityWithoutTags() {
    long time = Date.from(Instant.now()).getTime();
    EventIF firstEvent = new GenericEventRecord(
        eventId,
        pubkey,
        time,
        Kind.TEXT_NOTE,
        null,
        "content",
        Signature.fromString(signature));

    EventIF secondEvent = new GenericEventRecord(
        eventId,
        pubkey,
        time,
        Kind.TEXT_NOTE,
        null,
        "content",
        Signature.fromString(signature));

    assertEquals(firstEvent, secondEvent);
  }


  @Test
  void testEqualityWithEmptyTags() {
    long time = Date.from(Instant.now()).getTime();
    EventIF firstEvent = new GenericEventRecord(
        eventId,
        pubkey,
        time,
        Kind.TEXT_NOTE,
        List.of(),
        "content",
        Signature.fromString(signature));

    EventIF secondEvent = new GenericEventRecord(
        eventId,
        pubkey,
        time,
        Kind.TEXT_NOTE,
        List.of(),
        "content",
        Signature.fromString(signature));

    assertEquals(firstEvent, secondEvent);
  }

  @Test
  void testEqualityWithAddressTag() {
    List<BaseTag> tags = List.of(new AddressTag(
        Kind.BADGE_DEFINITION_EVENT,
        new PublicKey("bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"),
        new IdentifierTag(EventMessageSerializerTest.UPVOTE)));

    long time = Date.from(Instant.now()).getTime();
    EventIF firstEvent = new GenericEventRecord(
        eventId,
        pubkey,
        time,
        Kind.TEXT_NOTE,
        tags,
        "content",
        Signature.fromString(signature));

    EventIF secondEvent = new GenericEventRecord(
        eventId,
        pubkey,
        time,
        Kind.TEXT_NOTE,
        tags,
        "content",
        Signature.fromString(signature));

    assertEquals(firstEvent, secondEvent);
  }

  @Test
  void testEqualityWithPopulatedTags() {
    List<BaseTag> tags = List.of(new AddressTag(
        Kind.BADGE_DEFINITION_EVENT,
        new PublicKey("bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"),
        new IdentifierTag(EventMessageSerializerTest.UPVOTE)));

    long time = Date.from(Instant.now()).getTime();
    EventIF firstEvent = new GenericEventRecord(
        eventId,
        pubkey,
        time,
        Kind.TEXT_NOTE,
        tags,
        "content",
        Signature.fromString(signature));

    EventIF secondEvent = new GenericEventRecord(
        eventId,
        pubkey,
        time,
        Kind.TEXT_NOTE,
        tags,
        "content",
        Signature.fromString(signature));

    assertEquals(firstEvent, secondEvent);
  }

  @Test
  void testEqualityInheritedTypes() {
    String pubkey = "bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984";
    List<BaseTag> tags1 = List.of(new AddressTag(
        Kind.BADGE_DEFINITION_EVENT,
        new PublicKey(pubkey),
        new IdentifierTag(EventMessageSerializerTest.UPVOTE)));

    List<BaseTag> tags2 = List.of(new AddressTag(
        Kind.BADGE_DEFINITION_EVENT,
        new PublicKey(pubkey),
        new IdentifierTag(EventMessageSerializerTest.UPVOTE)));

    long time = Date.from(Instant.now()).getTime();
    EventIF firstEvent = new GenericEventRecord(
        eventId,
        this.pubkey,
        time,
        Kind.TEXT_NOTE,
        tags1,
        "content",
        Signature.fromString(signature));

    EventIF secondEvent = new GenericEventRecord(
        eventId,
        this.pubkey,
        time,
        Kind.TEXT_NOTE,
        tags2,
        "content",
        Signature.fromString(signature));

    assertEquals(firstEvent, secondEvent);
  }
}
