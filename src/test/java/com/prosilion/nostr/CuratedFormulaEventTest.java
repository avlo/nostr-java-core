package com.prosilion.nostr;

import com.ezylang.evalex.parser.ParseException;
import com.prosilion.nostr.event.CuratedFormulaEvent;
import com.prosilion.nostr.event.FormulaEvent;
import com.prosilion.nostr.event.GenericEventRecord;
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
  final void testCanonicalCtorAgainstGenericEventRecordCtor() throws ParseException {
    FormulaEvent formulaEvent = new FormulaEvent(
       submitter,
       new IdentifierTag("UNIT_FORMULA"),
       relayArgRelay,
       defnEvent_YesYes_Upvote,
       "+1");
    ReferenceTag referenceTag = new ReferenceTag(relayArgUrl);

    CuratedFormulaEvent expected = new CuratedFormulaEvent(
       aImgIdentity,
       formulaEvent,
       referenceTag,
       relayArgRelay);
    CuratedFormulaEvent actual = new CuratedFormulaEvent(expected.asGenericEventRecord());

    SetsPairedEvent setsPairedEvent = expected.getSetsPairedEvent();
    assertEquals(defnEvent_YesYes_Upvote.asAddressableEventAddressTag(), setsPairedEvent.getAddressTag());
    assertEquals(formulaEvent.getId(), setsPairedEvent.getEventTag().getEventId());
    assertEquals(formulaEvent.getPublicKey(), expected.requireFirstTag(com.prosilion.nostr.tag.PubKeyTag.class).getPublicKey());
    assertEquals(referenceTag, expected.requireFirstTag(ReferenceTag.class));
    assertEquals(formulaEvent.getFormula(), expected.getFormula());
    assertEquals(expected, actual);
  }

  @Test
  final void testGenericEventRecordCtorRejectsBlankFormula() throws ParseException {
    GenericEventRecord genericEventRecord = mockGenericEventRecordWithContent("");

    assertThrows(ParseException.class, () -> new CuratedFormulaEvent(genericEventRecord));
  }

  @Test
  final void testGenericEventRecordCtorRejectsInvalidFormula() throws ParseException {
    GenericEventRecord genericEventRecordPlus = mockGenericEventRecordWithContent("+");
    assertThrows(ParseException.class, () -> new CuratedFormulaEvent(genericEventRecordPlus));

    GenericEventRecord genericEventRecordMinus = mockGenericEventRecordWithContent("-");
    assertThrows(ParseException.class, () -> new CuratedFormulaEvent(genericEventRecordMinus));

    GenericEventRecord genericEventRecordAlpha = mockGenericEventRecordWithContent("A");
    assertThrows(ParseException.class, () -> new CuratedFormulaEvent(genericEventRecordAlpha));

//    GenericEventRecord genericEventRecordPlusOne = mockGenericEventRecordWithContent("+1");
//    assertDoesNotThrow(() -> new CuratedFormulaEvent(genericEventRecordPlusOne));
    
//    GenericEventRecord genericEventRecordPlusDecimal = mockGenericEventRecordWithContent("+.5");
//    assertDoesNotThrow(() -> new CuratedFormulaEvent(genericEventRecordPlusDecimal));

    GenericEventRecord genericEventRecordMinusAlpha = mockGenericEventRecordWithContent("@");
    assertThrows(ParseException.class, () -> new CuratedFormulaEvent(genericEventRecordMinusAlpha));
  }

  private GenericEventRecord mockGenericEventRecordWithContent(String content) throws ParseException {
    FormulaEvent formulaEvent = new FormulaEvent(
       submitter,
       new IdentifierTag("UNIT_FORMULA"),
       relayArgRelay,
       defnEvent_YesYes_Upvote,
       "+1");
    CuratedFormulaEvent curatedFormulaEvent = new CuratedFormulaEvent(
       aImgIdentity,
       formulaEvent,
       new ReferenceTag(relayArgUrl),
       relayArgRelay);
    GenericEventRecord genericEventRecord = mock(GenericEventRecord.class);
    when(genericEventRecord.getTags()).thenReturn(curatedFormulaEvent.getTags());
    when(genericEventRecord.getContent()).thenReturn(content);
    return genericEventRecord;
  }
}
