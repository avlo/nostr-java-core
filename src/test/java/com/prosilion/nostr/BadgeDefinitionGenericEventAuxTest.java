package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.stream.Stream;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class BadgeDefinitionGenericEventAuxTest {
  private static final String relayArgUrl = "ws://localhost:5555";
  private static final Relay relayArgRelay = new Relay(relayArgUrl);
  private static final RelayTag relayArgRelayTag = new RelayTag(relayArgRelay);

  private static final String baseTagsRelayUrl = "ws://localhost-from-relay-tag:5555";
  private static final Relay baseTagsRelay = new Relay(baseTagsRelayUrl);
  private static final RelayTag baseTagsRelayTag = new RelayTag(baseTagsRelay);

  private static final String auxRelayUrl = "ws://localhost-aux-event-relay:5555";
  private static final Relay auxRelay = new Relay(auxRelayUrl);
  private static final RelayTag auxRelayTag = new RelayTag(auxRelay);

  private static final String UNIT_UPVOTE = "UNIT_UPVOTE";
  private static final String UNIT_DOWNVOTE = "UNIT_DOWNVOTE";

  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
  public final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_DOWNVOTE);

  public final Identity aImgidentity = Identity.generateRandomIdentity();

  @Test
  final void A_NoNo_No_testEventNullRelayNoRelayTagEventAuxNullRelayTag() {
    BadgeDefinitionGenericEventAux eventAux = new BadgeDefinitionGenericEventAux(
       new BadgeDefinitionGenericEvent(aImgidentity, downvoteIdentifierTag), null);

    assertNull(eventAux.getRelay());
    assertThrows(Exception.class, () -> eventAux.getRelay().getUrl());
    assertThrows(NostrException.class, () -> eventAux.getBadgeDefinitionGenericEvent().requireFirstTag(RelayTag.class));
  }

  @Test
  final void A_NoNo_Yes_testEventNullRelayNoRelayTagEventAuxNullRelayTag() {
    BadgeDefinitionGenericEventAux eventAux =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(aImgidentity, downvoteIdentifierTag),
          auxRelay);

    assertThrows(NostrException.class, () -> eventAux.getBadgeDefinitionGenericEvent().requireFirstTag(RelayTag.class));
    assertEquals(Optional.empty(), eventAux.getBadgeDefinitionGenericEvent().getRelayTag());

    assertEquals(auxRelayTag.getRelay(), eventAux.getRelay());
    assertEquals(Optional.empty(), eventAux.getBadgeDefinitionGenericEvent().getRelay());
    assertThrows(NoSuchElementException.class, () -> eventAux.getBadgeDefinitionGenericEvent().getRelay().orElseThrow());
    assertEquals(auxRelayTag.getRelay().getUrl(), eventAux.getRelay().getUrl());
    assertEquals(auxRelay, eventAux.getRelay());
    assertThrows(NoSuchElementException.class, () -> eventAux.getBadgeDefinitionGenericEvent().getRelayTag().orElseThrow());
    assertThrows(NostrException.class, () -> eventAux.getBadgeDefinitionGenericEvent().requireFirstTag(RelayTag.class));
    assertThrows(NoSuchElementException.class, () -> eventAux.getBadgeDefinitionGenericEvent().getTypeSpecificTags(RelayTag.class).stream().findFirst().orElseThrow());
  }

  @Test
  final void B_NoYes_No__testEventNullRelayHasRelayTagEventAuxNullRelayTagValue() {
    testTags(
       baseTagsRelayTag,
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(baseTagsRelayTag), ""),
          null));
  }

  @Test
  final void A_NoYes_Yes_testEventNullRelayHasRelayTagEventAuxHasRelayTag() {
    BadgeDefinitionGenericEventAux eventAux =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(aImgidentity, downvoteIdentifierTag, List.of(baseTagsRelayTag), ""),
          auxRelay);

    testTags(baseTagsRelayTag, eventAux);
  }

  @Test
  final void C_YesNo_No__testEventHasRelayNoRelayTagEventAuxNullRelayTagValue() {
    BadgeDefinitionGenericEventAux eventAux =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(aImgidentity, downvoteIdentifierTag, relayArgRelay),
          null);

    testTags(relayArgRelayTag, eventAux);
  }

  @Test
  final void C_YesNo_Yes__testEventHasRelayNoRelayTagEventAuxHasRelayTagValue() {
    BadgeDefinitionGenericEventAux eventAux =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(aImgidentity, downvoteIdentifierTag, relayArgRelay),
          auxRelay);

    testTags(relayArgRelayTag, eventAux);
  }

  @Test
  final void D_YesYes_No__testEventHasRelayHasRelayTagEventAuxNullRelayTagValue() {
    BadgeDefinitionGenericEventAux eventAux =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(baseTagsRelayTag), "", relayArgRelay), null);
    testTags(relayArgRelayTag, eventAux);
  }

  @Test
  final void D_YesYes_Yes__testEventHasRelayHasRelayTagEventAuxHasRelayTagValue() {
    BadgeDefinitionGenericEventAux eventAux =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(baseTagsRelayTag), "", relayArgRelay),
          auxRelay);
    testTags(relayArgRelayTag, eventAux);
  }

  @Test
  final void E_NoYes_No__testEventNullRelayMultipleRelayTagsEventAuxNullRelayTagValue() {
    Relay baseTagsAnotherRelay = new Relay("ws://localhost-from-another-relay-tag:5555");
    RelayTag baseTagsAnotherRelayTag = new RelayTag(baseTagsAnotherRelay);

    BadgeDefinitionGenericEventAux eventAux =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(baseTagsRelayTag, baseTagsAnotherRelayTag), ""),
          auxRelay);

    assertTrue(List.of(baseTagsRelay, baseTagsAnotherRelay).contains(eventAux.getRelay()));
    assertTrue(Stream.of(baseTagsRelay, baseTagsAnotherRelay).toList().contains(eventAux.getRelay()));
    testTags(baseTagsRelayTag, eventAux);

    BadgeDefinitionGenericEventAux eventAuxReversedBaseTags =
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(baseTagsAnotherRelayTag, baseTagsRelayTag), ""),
          auxRelay);

    assertTrue(List.of(baseTagsAnotherRelay, baseTagsRelay).contains(eventAuxReversedBaseTags.getRelay()));
    assertTrue(Stream.of(baseTagsAnotherRelay, baseTagsRelay).toList().contains(eventAuxReversedBaseTags.getRelay()));
    testTags(baseTagsAnotherRelayTag, eventAuxReversedBaseTags);
  }

  @Test
  final void testBadgeDefinitionGenericEventCtor() {
    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(
             new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, "content", relayArgRelay).asGenericEventRecord()),
          auxRelay));

    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-should-not-appear:5555"));
    testTags(anotherRelayTag,
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(
             new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, "content", anotherRelayTag.getRelay()).asGenericEventRecord()),
          auxRelay));

    testTags(relayArgRelayTag,
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(
             new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(anotherRelayTag), "content", relayArgRelay).asGenericEventRecord()),
          auxRelay));

    testTags(anotherRelayTag,
       new BadgeDefinitionGenericEventAux(
          new BadgeDefinitionGenericEvent(
             new BadgeDefinitionGenericEvent(aImgidentity, upvoteIdentifierTag, List.of(anotherRelayTag, relayArgRelayTag), "content").asGenericEventRecord()),
          auxRelay));
  }

  private void testTags(RelayTag relayTag, BadgeDefinitionGenericEventAux eventAux) {
    assertEquals(relayTag.getRelay(), eventAux.getRelay());
    assertEquals(relayTag.getRelay(), eventAux.getBadgeDefinitionGenericEvent().getRelay().orElseThrow());
    assertEquals(relayTag.getRelay().getUrl(), eventAux.getRelay().getUrl());
    assertEquals(relayTag.getRelay(), eventAux.getRelay());
    assertEquals(relayTag, eventAux.getBadgeDefinitionGenericEvent().getRelayTag().orElseThrow());
    assertEquals(relayTag, eventAux.getBadgeDefinitionGenericEvent().requireFirstTag(RelayTag.class));
    assertEquals(relayTag, eventAux.getBadgeDefinitionGenericEvent().getTypeSpecificTags(RelayTag.class).stream().findFirst().orElseThrow());
  }
}
