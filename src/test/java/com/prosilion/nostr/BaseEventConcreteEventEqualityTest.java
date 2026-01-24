package com.prosilion.nostr;

import com.google.common.base.Function;
import com.google.common.base.Supplier;
import com.prosilion.nostr.event.BaseEvent;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.TextNoteEvent;
import com.prosilion.nostr.user.Identity;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class BaseEventConcreteEventEqualityTest {
  BaseEvent baseEvent;
  TextNoteEvent textNoteEventHardEquality;
  TextNoteEvent textNoteEqualsWithValues;

  public BaseEventConcreteEventEqualityTest() {
    Identity identity = Identity.generateRandomIdentity();
    String identicalContent = "some content";
    this.baseEvent = new TextNoteEvent(identity, identicalContent);
    this.textNoteEventHardEquality = new TextNoteEvent(baseEvent.getGenericEventRecord());
    this.textNoteEqualsWithValues = new TextNoteEvent(identity, identicalContent);
  }

  @Test
  public void testEquality() throws NostrException {
    assertEquals(baseEvent, textNoteEventHardEquality);
  }

  @Test
  public void testInequalityEventCopies() throws NostrException {
    assertNotEquals(textNoteEqualsWithValues, textNoteEventHardEquality);
  }

  @Test
  void testEventIFAsGenericEventRecord() {
    EventIF textNoteEventAsEventIF = textNoteEventHardEquality;
    assertEquals(
        textNoteEventAsEventIF.asGenericEventRecord(),
        textNoteEventHardEquality.asGenericEventRecord());

    assertEquals_VariantDemonstration(
        ((Supplier<GenericEventRecord>) textNoteEventAsEventIF::asGenericEventRecord).get());
    
    assertEquals_VariantDemonstration(
        ((Supplier<GenericEventRecord>) textNoteEventHardEquality::asGenericEventRecord).get());

    assertEquals_VariantDemonstration(
        EventIF.asGenericEventRecord.apply(textNoteEventHardEquality));

    Function<EventIF, GenericEventRecord> methodInstance_AsGenericEventRecord = EventIF::asGenericEventRecord;
    assertEquals_VariantDemonstration(
        methodInstance_AsGenericEventRecord.apply(textNoteEventHardEquality));
  }

  private void assertEquals_VariantDemonstration(GenericEventRecord genericEventRecordVariant) {
    assertEquals(textNoteEventHardEquality.asGenericEventRecord(), genericEventRecordVariant);
  }
}

