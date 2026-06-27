package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.RelayTag;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class BadgeDefinitionGenericEventAuxTest extends BaseEventAuxTest {

  @Test
  final void A_NoNo_No_testEventNullRelayNoRelayTagEventAuxNullRelayTag() {
    assertEquals(Optional.empty(), defnAuxNo_defnEvent_NoNo_Upvote.getRelay());
    assertThrows(Exception.class, () -> defnAuxNo_defnEvent_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow());
    assertThrows(NostrException.class, () -> defnAuxNo_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().requireFirstTag(RelayTag.class));
  }

  @Test
  final void A_NoNo_Yes_testEventNullRelayNoRelayTagEventAuxNullRelayTag() {
    assertThrows(NostrException.class, () -> defnAuxYes_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().requireFirstTag(RelayTag.class));
    assertEquals(Optional.empty(), defnAuxYes_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().getRelayTag());

    assertEquals(auxRelayTag.getRelay(), defnAuxYes_defnEvent_NoNo_Upvote.getRelay().orElseThrow());
    assertEquals(Optional.empty(), defnAuxYes_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().getRelay());
    assertThrows(NoSuchElementException.class, () -> defnAuxYes_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().getRelay().orElseThrow());
    assertEquals(auxRelayTag.getRelay().getUrl(), defnAuxYes_defnEvent_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow());
    assertEquals(auxRelay, defnAuxYes_defnEvent_NoNo_Upvote.getRelay().orElseThrow());
    assertThrows(NoSuchElementException.class, () -> defnAuxYes_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().getRelayTag().orElseThrow());
    assertThrows(NostrException.class, () -> defnAuxYes_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().requireFirstTag(RelayTag.class));
    assertThrows(NoSuchElementException.class, () -> defnAuxYes_defnEvent_NoNo_Upvote.getBadgeDefinitionGenericEvent().getTypeSpecificTags(RelayTag.class).stream().findFirst().orElseThrow());
  }

  @Test
  final void B_NoYes_No__testEventNullRelayHasRelayTagEventAuxNullRelayTagValue() {
    testTags(baseTagsRelayTag, defnAuxNo_defnEvent_NoYes_Upvote);
    testTags(baseTagsRelayTag, defnAuxNo_defnEvent_NoYes_Downvote);
  }

  @Test
  final void A_NoYes_Yes_testEventNullRelayHasRelayTagEventAuxHasRelayTag() {
    testTags(baseTagsRelayTag, defnAuxYes_defnEvent_NoYes_Upvote);
    testTags(baseTagsRelayTag, defnAuxYes_defnEvent_NoYes_Downvote);
  }

  @Test
  final void C_YesNo_No__testEventHasRelayNoRelayTagEventAuxNullRelayTagValue() {
    testTags(relayArgRelayTag, defnAuxNo_defnEvent_YesNo_Upvote);
    testTags(relayArgRelayTag, defnAuxNo_defnEvent_YesNo_Downvote);
  }

  @Test
  final void C_YesNo_Yes__testEventHasRelayNoRelayTagEventAuxHasRelayTagValue() {
    testTags(relayArgRelayTag, defnAuxYes_defnEvent_YesNo_Upvote);
    testTags(relayArgRelayTag, defnAuxYes_defnEvent_YesNo_Downvote);
  }

  @Test
  final void D_YesYes_No__testEventHasRelayHasRelayTagEventAuxNullRelayTagValue() {
    testTags(relayArgRelayTag, defnAuxNo_defnEvent_YesYes_Upvote);
  }

  @Test
  final void D_YesYes_Yes__testEventHasRelayHasRelayTagEventAuxHasRelayTagValue() {
    testTags(relayArgRelayTag, defnAuxYes_defnEvent_YesYes_Upvote);
  }

  @Test
  final void E_NoYes_No__testEventNullRelayMultipleRelayTagsEventAuxNullRelayTagValue() {
    Relay baseTagsAnotherRelay = new Relay("ws://localhost-from-another-relay-tag:5555");
    RelayTag baseTagsAnotherRelayTag = new RelayTag(baseTagsAnotherRelay);

    BadgeDefinitionGenericEventAux eventAux =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(baseTagsRelayTag, baseTagsAnotherRelayTag), ""),
          auxRelay);

    assertTrue(List.of(baseTagsRelay, baseTagsAnotherRelay).contains(eventAux.getRelay().orElseThrow()));
    assertTrue(Stream.of(baseTagsRelay, baseTagsAnotherRelay).toList().contains(eventAux.getRelay().orElseThrow()));
    testTags(baseTagsRelayTag, eventAux);

    BadgeDefinitionGenericEventAux eventAuxReversedBaseTags =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(baseTagsAnotherRelayTag, baseTagsRelayTag), ""),
          auxRelay);

    assertTrue(List.of(baseTagsAnotherRelay, baseTagsRelay).contains(eventAuxReversedBaseTags.getRelay().orElseThrow()));
    assertTrue(Stream.of(baseTagsAnotherRelay, baseTagsRelay).toList().contains(eventAuxReversedBaseTags.getRelay().orElseThrow()));
    testTags(baseTagsAnotherRelayTag, eventAuxReversedBaseTags);
  }

  @Test
  final void testBadgeDefinitionGenericEventCtor() {
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(
             new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, "content", relayArgRelay).asGenericEventRecord()),
          auxRelay));

    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-should-not-appear:5555"));
    testTags(anotherRelayTag,
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(
             new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, "content", anotherRelayTag.getRelay()).asGenericEventRecord()),
          auxRelay));

    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(
             new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(anotherRelayTag), "content", relayArgRelay).asGenericEventRecord()),
          auxRelay));

    testTags(anotherRelayTag,
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(
             new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(anotherRelayTag, relayArgRelayTag), "content").asGenericEventRecord()),
          auxRelay));
  }

  private void testTags(RelayTag relayTag, BadgeDefinitionGenericEventAux eventAux) {
    assertEquals(relayTag.getRelay(), eventAux.getRelay().orElseThrow());
    assertEquals(relayTag.getRelay(), eventAux.getBadgeDefinitionGenericEvent().getRelay().orElseThrow());
    assertEquals(relayTag.getRelay().getUrl(), eventAux.getRelay().map(Relay::getUrl).orElseThrow());
    assertEquals(relayTag.getRelay(), eventAux.getRelay().orElseThrow());
    assertEquals(relayTag, eventAux.getBadgeDefinitionGenericEvent().getRelayTag().orElseThrow());
    assertEquals(relayTag, eventAux.getBadgeDefinitionGenericEvent().requireFirstTag(RelayTag.class));
    assertEquals(relayTag, eventAux.getBadgeDefinitionGenericEvent().getTypeSpecificTags(RelayTag.class).stream().findFirst().orElseThrow());
  }
}
