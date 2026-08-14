package com.prosilion.nostr.curated;

import com.prosilion.nostr.EventTestFixtures;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.AbstractSetsEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.curated.CuratedFormulaEvent;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CuratedFormulaEventTest extends EventTestFixtures {

  @Test
  final void testCanonicalCtorAgainstGenericEventRecordCtor() {
    FormulaEvent formulaEvent = new FormulaEvent(
       formulaCreator,
       formulaUpvoteIdentifierTag,
       defnEvent_YesYes_Upvote,
       "+1",
       relayArgRelay);
    ReferenceTag referenceTag = new ReferenceTag(relayArgUrl);

    CuratedFormulaEvent expected = new CuratedFormulaEvent(
       aImgIdentity,
       formulaEvent,
       referenceTag,
       relayArgRelay);
    assertEquals(formulaEvent.getId(), expected.getFormulaEventId());
    assertEquals(formulaEvent.getId(), expected.getEventTag().eventId());
    assertEquals(formulaEvent.getId(), expected.requireFirstTag(EventTag.class).eventId());

    AddressTag downvoteEventAsAddressableAddressTag = defnEvent_YesYes_Downvote.asAddressableEventAddressTag();
    IdentifierTag expectedDownvoteIdentifierTag = AbstractSetsEvent.hashedAddressTag(downvoteEventAsAddressableAddressTag);
    assertEquals(new IdentifierTag("1545192716"), expectedDownvoteIdentifierTag);
    
    AddressTag upvoteEventAsAddressableAddressTag = defnEvent_YesYes_Upvote.asAddressableEventAddressTag();
    IdentifierTag expectedUpvoteIdentifierTag = AbstractSetsEvent.hashedAddressTag(upvoteEventAsAddressableAddressTag);
    assertEquals(new IdentifierTag("1367861445"), expectedUpvoteIdentifierTag); 

    assertEquals(expected.getIdentifierTag(), AbstractSetsEvent.hashedAddressTag(formulaEvent.getAddressTag()));

    CuratedFormulaEvent actualCuratedFormulaEventFromExpected = new CuratedFormulaEvent(expected.asGenericEventRecord());
    assertEquals(formulaEvent.getId(), actualCuratedFormulaEventFromExpected.getFormulaEventId());
    assertEquals(formulaEvent.getId(), actualCuratedFormulaEventFromExpected.getEventTag().eventId());
    assertEquals(formulaEvent.getId(), actualCuratedFormulaEventFromExpected.requireFirstTag(EventTag.class).eventId());
    assertEquals(formulaEvent.asAddressableEventAddressTag(), actualCuratedFormulaEventFromExpected.getAddressTag());
    assertEquals(expected.getIdentifierTag(), actualCuratedFormulaEventFromExpected.getIdentifierTag());

    SetsPairedEvent setsPairedEvent = expected.getSetsPairedEvent();

    assertEquals(formulaEvent.asAddressableEventAddressTag(), setsPairedEvent.getAddressTag());
    assertEquals(formulaEvent.getId(), setsPairedEvent.getEventTag().getEventId());
    assertEquals(formulaEvent.getPublicKey(), expected.requireFirstTag(com.prosilion.nostr.tag.PubKeyTag.class).getPublicKey());
    assertEquals(referenceTag, expected.requireFirstTag(ReferenceTag.class));
    assertEquals(formulaEvent.getFormula(), expected.getFormula());
    assertEquals(expected, actualCuratedFormulaEventFromExpected);
  }

  @Test
  final void testGenericEventRecordCtorRejectsBlankFormula() {
    GenericEventRecord genericEventRecord = mockGenericEventRecordWithContent("");
    assertThrows(NostrException.class, () -> new CuratedFormulaEvent(genericEventRecord));
  }

  @Test
  final void testGenericEventRecordCtorRejectsInvalidFormula() {
    GenericEventRecord genericEventRecordPlus = mockGenericEventRecordWithContent("+");
    assertThrows(NostrException.class, () -> new CuratedFormulaEvent(genericEventRecordPlus));

    GenericEventRecord genericEventRecordMinus = mockGenericEventRecordWithContent("-");
    assertThrows(NostrException.class, () -> new CuratedFormulaEvent(genericEventRecordMinus));

    GenericEventRecord genericEventRecordAlpha = mockGenericEventRecordWithContent("A");
    assertThrows(NostrException.class, () -> new CuratedFormulaEvent(genericEventRecordAlpha));

    GenericEventRecord genericEventRecordMinusAlpha = mockGenericEventRecordWithContent("@");
    assertThrows(NostrException.class, () -> new CuratedFormulaEvent(genericEventRecordMinusAlpha));
  }

  @Test
  final void testGenericRecordDoesNotThrowUsingDivision() {
    GenericEventRecord genericEventRecordPlusOne = mockGenericEventRecordWithContent("/2");
    assertDoesNotThrow(() -> new CuratedFormulaEvent(genericEventRecordPlusOne));
  }

  @Test
  final void testGenericRecordDoesNotThrowUsingMultiplication() {
    GenericEventRecord genericEventRecordPlusOne = mockGenericEventRecordWithContent("*2");
    assertDoesNotThrow(() -> new CuratedFormulaEvent(genericEventRecordPlusOne));
  }

  @Test
  final void testGenericRecordDoesNotThrowUsingFraction() {
    GenericEventRecord genericEventRecordPlusOne = mockGenericEventRecordWithContent("/1/2");
    assertDoesNotThrow(() -> new CuratedFormulaEvent(genericEventRecordPlusOne));
  }

  @Test
  final void testGenericRecordDoesNotThrowUsingPositiveDecimal() {
    GenericEventRecord genericEventRecordPlusDecimal = mockGenericEventRecordWithContent("+.5");
    assertDoesNotThrow(() -> new CuratedFormulaEvent(genericEventRecordPlusDecimal));
  }

  @Test
  final void testGenericRecordDoesNotThrowUsingNegativeDecimal() {
    GenericEventRecord genericEventRecordPlusDecimal = mockGenericEventRecordWithContent("+-.5");
    assertDoesNotThrow(() -> new CuratedFormulaEvent(genericEventRecordPlusDecimal));
  }

  private GenericEventRecord mockGenericEventRecordWithContent(String content) {
    FormulaEvent formulaEvent = new FormulaEvent(
       submitter,
       new IdentifierTag("UNIT_FORMULA"),
       defnEvent_YesYes_Upvote, "+1", relayArgRelay
    );
    CuratedFormulaEvent curatedFormulaEvent = new CuratedFormulaEvent(
       aImgIdentity,
       formulaEvent,
       new ReferenceTag(relayArgUrl),
       relayArgRelay);
    GenericEventRecord genericEventRecord = mock(GenericEventRecord.class);
    when(genericEventRecord.getTags()).thenReturn(curatedFormulaEvent.getTags());
    when(genericEventRecord.getKind()).thenReturn(curatedFormulaEvent.getKind());
    when(genericEventRecord.getId()).thenReturn(curatedFormulaEvent.getId());
    when(genericEventRecord.getPublicKey()).thenReturn(curatedFormulaEvent.getPublicKey());
    when(genericEventRecord.getCreatedAt()).thenReturn(curatedFormulaEvent.getCreatedAt());
    when(genericEventRecord.getSignature()).thenReturn(curatedFormulaEvent.getSignature());
    when(genericEventRecord.requireFirstTag(AddressTag.class)).thenReturn(curatedFormulaEvent.getSetsPairedEvent().getAddressTag());
    when(genericEventRecord.requireFirstTag(EventTag.class)).thenReturn(curatedFormulaEvent.getSetsPairedEvent().getEventTag());
    when(genericEventRecord.requireFirstTag(ReferenceTag.class)).thenReturn(curatedFormulaEvent.requireFirstTag(ReferenceTag.class));
    when(genericEventRecord.getContent()).thenReturn(content);
    return genericEventRecord;
  }
}
