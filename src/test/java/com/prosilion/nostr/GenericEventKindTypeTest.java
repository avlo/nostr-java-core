//package com.prosilion.nostr;
//
//import com.prosilion.nostr.enums.Kind;
//import com.prosilion.nostr.event.EventIF;
//import com.prosilion.nostr.event.GenericEventRecord;
//import com.prosilion.nostr.tag.AddressTag;
//import com.prosilion.nostr.tag.BaseTag;
//import com.prosilion.nostr.tag.EventTag;
//import com.prosilion.nostr.tag.IdentifierTag;
//import com.prosilion.nostr.user.PublicKey;
//import com.prosilion.nostr.user.Signature;
//import com.prosilion.nostr.util.TestKindType;
//import java.time.Instant;
//import java.util.Date;
//import java.util.List;
//import lombok.extern.slf4j.Slf4j;
//import org.junit.jupiter.api.Test;
//import org.springframework.test.context.ActiveProfiles;
//
//import static org.junit.jupiter.api.Assertions.assertEquals;
//import static org.junit.jupiter.api.Assertions.assertNotEquals;
//
//@Slf4j
//@ActiveProfiles("test")
//public class GenericEventKindTypeTest {
//
//  public final String eventId = EventTagTest.generateRandomHex64String();
//  public final PublicKey pubkey = new PublicKey(EventTagTest.generateRandomHex64String());
//  public final String signature = EventTagTest.generateRandomHex64String().concat(EventTagTest.generateRandomHex64String());
//
//  @Test
//  void testEqualityInheritedTypes() {
//    long time = Date.from(Instant.now()).getTime();
//    GenericEventRecord firstEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        null,
//        "content",
//        Signature.fromString(signature));
//
//    EventIF firstGenericEventKindType = new GenericEventKindType(firstEvent, TestKindType.UPVOTE);
//
//    GenericEventRecord secondEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        null,
//        "content",
//        Signature.fromString(signature));
//
//    GenericEventKindTypeIF secondGenericEventKindType = new GenericEventKindType(secondEvent, TestKindType.UPVOTE);
//
//    assertEquals(firstGenericEventKindType, secondGenericEventKindType);
//  }
//
//  @Test
//  void testEqualityWithoutTags() {
//    long time = Date.from(Instant.now()).getTime();
//    GenericEventRecord firstEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        null,
//        "content",
//        Signature.fromString(signature));
//
//    GenericEventKindType firstGenericEventKindType = new GenericEventKindType(firstEvent, TestKindType.UPVOTE);
//
//    GenericEventRecord secondEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        null,
//        "content",
//        Signature.fromString(signature));
//
//    GenericEventKindType secondGenericEventKindType = new GenericEventKindType(secondEvent, TestKindType.UPVOTE);
//
//    assertEquals(firstGenericEventKindType, secondGenericEventKindType);
//  }
//
//  @Test
//  void testEqualityWithEmptyTags() {
//    long time = Date.from(Instant.now()).getTime();
//    GenericEventRecord firstEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        List.of(),
//        "content",
//        Signature.fromString(signature));
//
//    GenericEventKindType firstGenericEventKindType = new GenericEventKindType(firstEvent, TestKindType.UPVOTE);
//
//    GenericEventRecord secondEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        List.of(),
//        "content",
//        Signature.fromString(signature));
//
//    GenericEventKindType secondGenericEventKindType = new GenericEventKindType(secondEvent, TestKindType.UPVOTE);
//
//    assertEquals(firstGenericEventKindType, secondGenericEventKindType);
//  }
//
//  @Test
//  void testEqualityWithAddressTag() {
//    List<BaseTag> tags = List.of(new AddressTag(
//        Kind.BADGE_DEFINITION_EVENT,
//        new PublicKey("bbbd79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"),
//        new IdentifierTag(TestKindType.UPVOTE.getName())));
//
//    long time = Date.from(Instant.now()).getTime();
//    GenericEventRecord firstEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        tags,
//        "content",
//        Signature.fromString(signature));
//
//    GenericEventKindType firstGenericEventKindType = new GenericEventKindType(firstEvent, TestKindType.UPVOTE);
//
//    GenericEventRecord secondEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        tags,
//        "content",
//        Signature.fromString(signature));
//
//    GenericEventKindType secondGenericEventKindType = new GenericEventKindType(secondEvent, TestKindType.UPVOTE);
//
//    assertEquals(firstGenericEventKindType, secondGenericEventKindType);
//  }
//
//  @Test
//  void testNonEqualityWithPopulatedTags() {
//    List<BaseTag> tags = List.of(
//        new AddressTag(
//            Kind.BADGE_DEFINITION_EVENT,
//            new PublicKey(EventTagTest.generateRandomHex64String()),
//            new IdentifierTag(TestKindType.UPVOTE.getName())),
//        new EventTag(EventTagTest.generateRandomHex64String()));
//
//    List<BaseTag> tags2 = List.of(
//        new AddressTag(
//            Kind.BADGE_DEFINITION_EVENT,
//            new PublicKey(EventTagTest.generateRandomHex64String()),
//            new IdentifierTag(TestKindType.UPVOTE.getName())),
//        new EventTag(EventTagTest.generateRandomHex64String()));
//
//    long time = Date.from(Instant.now()).getTime();
//    EventIF firstEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        tags,
//        "content",
//        Signature.fromString(signature));
//
//    EventIF secondEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        tags2,
//        "content",
//        Signature.fromString(signature));
//
//    assertNotEquals(firstEvent, secondEvent); // different addy tag pubkeys 
//  }
//
//  @Test
//  void testEqualityWithPopulatedTags() {
//    String hexPubKey = EventTagTest.generateRandomHex64String();
//    String idEvent = EventTagTest.generateRandomHex64String();
//    List<BaseTag> tags = List.of(
//        new AddressTag(
//            Kind.BADGE_DEFINITION_EVENT,
//            new PublicKey(hexPubKey),
//            new IdentifierTag(TestKindType.UPVOTE.getName())),
//        new EventTag(idEvent));
//
//    List<BaseTag> tags2 = List.of(
//        new AddressTag(
//            Kind.BADGE_DEFINITION_EVENT,
//            new PublicKey(hexPubKey),
//            new IdentifierTag(TestKindType.UPVOTE.getName())),
//        new EventTag(idEvent));
//
//    long time = Date.from(Instant.now()).getTime();
//    EventIF firstEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        tags,
//        "content",
//        Signature.fromString(signature));
//
//    EventIF secondEvent = new GenericEventRecord(
//        eventId,
//        pubkey,
//        time,
//        Kind.TEXT_NOTE,
//        tags2,
//        "content",
//        Signature.fromString(signature));
//
//    assertEquals(firstEvent, secondEvent);
//  }
//}
