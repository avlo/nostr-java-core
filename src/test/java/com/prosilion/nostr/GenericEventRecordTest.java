package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import com.prosilion.nostr.util.TestKindType;
import java.util.List;
import java.util.Objects;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class GenericEventRecordTest {
  public static final String CONTENT = EventTagTest.generateRandomHex64String();
  public static final String EVENT_TAG_CONTENT = EventTagTest.generateRandomHex64String();
  public final Identity identity = Identity.generateRandomIdentity();

  private final String id;
  private final PublicKey publicKey;
  private final Long createdAt;
  private final Kind kind;
  private final Signature signature;

  public GenericEventRecordTest() {
    this.id = EVENT_TAG_CONTENT;
    this.publicKey = identity.getPublicKey();
    this.createdAt = System.currentTimeMillis();
    this.kind = Kind.TEXT_NOTE;
    this.signature = Signature.fromString(id.concat(CONTENT));
  }

  @Test
  void testEqualityEmptyTags() {
    GenericEventRecord expectedRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, List.of(), CONTENT, signature);

    GenericEventRecord testRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, List.of(), CONTENT, signature);

    assertEquals(expectedRecord, testRecord);
  }

  @Test
  void testEqualityPopulatedTags() {
    List<BaseTag> tags1 = List.of(new AddressTag(
        Kind.BADGE_DEFINITION_EVENT,
        publicKey,
        new IdentifierTag(TestKindType.UPVOTE.getName())));

    List<BaseTag> tags2 = List.of(new AddressTag(
        Kind.BADGE_DEFINITION_EVENT,
        publicKey,
        new IdentifierTag(TestKindType.UPVOTE.getName())));

    GenericEventRecord firstRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags1, CONTENT, signature);

    GenericEventRecord secondRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags2, CONTENT, signature);

    assertEquals(firstRecord, secondRecord);
  }

  @Test
  void testComplesTags() {
    List<BaseTag> tags1 = List.of(
        new AddressTag(
            Kind.BADGE_DEFINITION_EVENT,
            publicKey,
            new IdentifierTag(TestKindType.UPVOTE.getName())),
        new EventTag(EVENT_TAG_CONTENT));

    List<BaseTag> tags2 = List.of(
        new AddressTag(
            Kind.BADGE_DEFINITION_EVENT,
            publicKey,
            new IdentifierTag(TestKindType.UPVOTE.getName())),
        new EventTag(EVENT_TAG_CONTENT));

    GenericEventRecord firstRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags1, CONTENT, signature);

    GenericEventRecord secondRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags2, CONTENT, signature);

    assertEquals(firstRecord, secondRecord);
  }

  @Test
  void testRearrangedIdenticalTags() {
    List<BaseTag> tags1 = List.of(
        new EventTag(EVENT_TAG_CONTENT),
        new AddressTag(
            Kind.BADGE_DEFINITION_EVENT,
            publicKey,
            new IdentifierTag(TestKindType.UPVOTE.getName()))
    );

    List<BaseTag> tags2 = List.of(
        new AddressTag(
            Kind.BADGE_DEFINITION_EVENT,
            publicKey,
            new IdentifierTag(TestKindType.UPVOTE.getName())),
        new EventTag(EVENT_TAG_CONTENT)
    );

    GenericEventRecord firstRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags1, CONTENT, signature);

    GenericEventRecord secondRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags2, CONTENT, signature);

    assertEquals(firstRecord, secondRecord);
  }

  @Test
  void testRearrangedIdenticalTags2() {
    List<BaseTag> tags1 = List.of(
        new EventTag(EVENT_TAG_CONTENT),
        new PubKeyTag(publicKey)
    );

    List<BaseTag> tags2 = List.of(
        new PubKeyTag(publicKey),
        new EventTag(EVENT_TAG_CONTENT)
    );

    GenericEventRecord firstRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags1, CONTENT, signature);

    GenericEventRecord secondRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags2, CONTENT, signature);

    assertEquals(firstRecord, secondRecord);
  }

  @Test
  void testUnequalTags() {
    List<BaseTag> tags1 = List.of(
        new PubKeyTag(publicKey)
    );

    List<BaseTag> tags2 = List.of(
        new PubKeyTag(publicKey),
        new EventTag(EVENT_TAG_CONTENT)
    );

    GenericEventRecord firstRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags1, CONTENT, signature);

    GenericEventRecord secondRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags2, CONTENT, signature);

    assertNotEquals(firstRecord, secondRecord);
  }

  @Test
  void testUnequalContent() {
    List<BaseTag> tags = List.of(
        new PubKeyTag(publicKey)
    );

    GenericEventRecord firstRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags, EVENT_TAG_CONTENT, signature);

    GenericEventRecord secondRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags, CONTENT, signature);

    assertNotEquals(firstRecord, secondRecord);
  }
  
  @Test
  void testUnequalContentWithEqualTags() {
    List<BaseTag> tags1 = List.of(
        new PubKeyTag(publicKey)
    );

    List<BaseTag> tags2 = List.of(
        new PubKeyTag(publicKey)
    );

    GenericEventRecord firstRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags1, EVENT_TAG_CONTENT, signature);

    GenericEventRecord secondRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags2, CONTENT, signature);

    assertNotEquals(firstRecord, secondRecord);
  }

  @Test
  void testHashCodeEquality() {
    List<BaseTag> tags1 = List.of(
        new PubKeyTag(publicKey)
    );

    List<BaseTag> tags2 = List.of(
        new PubKeyTag(publicKey)
    );

    GenericEventRecord firstRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags1, CONTENT, signature);

    GenericEventRecord secondRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags2, CONTENT, signature);

    assertEquals(firstRecord.hashCode(), secondRecord.hashCode());
  }

  @Test
  void testRearrangedIdenticalTagsHashCodeEquality() {
    List<BaseTag> tags1 = List.of(
        new EventTag(EVENT_TAG_CONTENT),
        new PubKeyTag(publicKey)
    );

    List<BaseTag> tags2 = List.of(
        new PubKeyTag(publicKey),
        new EventTag(EVENT_TAG_CONTENT)
    );

    GenericEventRecord firstRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags1, CONTENT, signature);

    GenericEventRecord secondRecord = new GenericEventRecord(
        id, publicKey, createdAt, kind, tags2, CONTENT, signature);

    assertEquals(firstRecord.hashCode(), secondRecord.hashCode());
  }
}
