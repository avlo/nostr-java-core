package com.prosilion.nostr;

import com.prosilion.nostr.event.BaseEvent;
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
}

