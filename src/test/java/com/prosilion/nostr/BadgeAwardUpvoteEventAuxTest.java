package com.prosilion.nostr;

import com.google.common.base.Function;
import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadgeAwardUpvoteEventAuxTest {
  private static final String relayArgUrl = "ws://localhost:5555";
  private static final Relay relayArgRelay = new Relay(relayArgUrl);
  private static final RelayTag relayArgRelayTag = new RelayTag(relayArgRelay);

  private static final String baseTagsRelayUrl = "ws://localhost-from-relay-tag:5555";
  private static final Relay baseTagsRelay = new Relay(baseTagsRelayUrl);
  private static final RelayTag baseTagsRelayTag = new RelayTag(baseTagsRelay);

  private static final String auxRelayUrl = "ws://localhost-aux-event-relay:5555";
  private static final Relay auxRelay = new Relay(auxRelayUrl);
  private static final RelayTag auxRelayTag = new RelayTag(auxRelay);

  public static final String UNIT_UPVOTE = "UNIT_UPVOTE";
  public final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);

  protected final Identity submitter =
//     Identity.generateRandomIdentity();
     Identity.create("aaa4585483196998204846989544737603523651520600328805626488477202");

  protected final Identity upvoteDefnCreator =
//     Identity.generateRandomIdentity();
     Identity.create("bbb4585483196998204846989544737603523651520600328805626488477202");

  protected final Identity recipient =
//     Identity.generateRandomIdentity();
     Identity.create("ccc4585483196998204846989544737603523651520600328805626488477202");

  private final BadgeDefinitionGenericEvent badgeDefnUpvoteEvent = new BadgeDefinitionGenericEvent(
     upvoteDefnCreator, upvoteIdentifierTag, relayArgRelay);

  BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent;

  public BadgeAwardUpvoteEventAuxTest() {
    this.badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       badgeDefnUpvoteEvent,
       relayArgRelay);
  }

  @Test
  final void A_NoNo_No_testEventNullRelayNoRelayTagEventAuxNullRelayTag() {
    BadgeAwardGenericEventAux eventAux = new BadgeAwardGenericEventAux(
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnUpvoteEvent), null);

    assertNull(eventAux.getRelay());
    assertThrows(Exception.class, () -> eventAux.getRelay().getUrl());
    assertThrows(NostrException.class, () -> eventAux.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
  }

  @Test
  final void A_NoNo_Yes_testEventNullRelayNoRelayTagEventAuxNullRelayTag() {
    BadgeAwardGenericEventAux eventAux = new BadgeAwardGenericEventAux(
       new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnUpvoteEvent), auxRelay);

    assertThrows(NostrException.class, () -> eventAux.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
    assertEquals(Optional.empty(), eventAux.getBadgeAwardGenericEvent().getRelayTag());

    assertEquals(auxRelayTag.getRelay(), eventAux.getRelay());
    assertEquals(Optional.empty(), eventAux.getBadgeAwardGenericEvent().getRelay());
    assertThrows(NoSuchElementException.class, () -> eventAux.getBadgeAwardGenericEvent().getRelay().orElseThrow());
    assertEquals(auxRelayTag.getRelay().getUrl(), eventAux.getRelay().getUrl());
    assertEquals(auxRelay, eventAux.getRelay());
    assertThrows(NoSuchElementException.class, () -> eventAux.getBadgeAwardGenericEvent().getRelayTag().orElseThrow());
    assertThrows(NostrException.class, () -> eventAux.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
    assertThrows(NoSuchElementException.class, () -> eventAux.getBadgeAwardGenericEvent().getTypeSpecificTags(RelayTag.class).stream().findFirst().orElseThrow());
  }

  @Test
  final void B_NoYes_No__testEventNullRelayHasRelayTagEventAuxNullRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             badgeDefnUpvoteEvent, List.of(relayArgRelayTag, baseTagsRelayTag), ""), null));
  }

  @Test
  final void A_NoYes_Yes_testEventNullRelayHasRelayTagEventAuxHasRelayTag() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             badgeDefnUpvoteEvent, List.of(relayArgRelayTag, baseTagsRelayTag), ""), auxRelay));
  }

  @Test
  final void C_YesNo_No__testEventHasRelayNoRelayTagEventAuxNullRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             badgeDefnUpvoteEvent, relayArgRelay), null));
  }

  @Test
  final void C_YesNo_Yes__testEventHasRelayNoRelayTagEventAuxHasRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             badgeDefnUpvoteEvent, relayArgRelay), auxRelay));
  }

  @Test
  final void D_YesYes_No__testEventHasRelayHasRelayTagEventAuxNullRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             badgeDefnUpvoteEvent, List.of(relayArgRelayTag, baseTagsRelayTag), "", relayArgRelay), null));
  }

  @Test
  final void D_YesYes_Yes__testEventHasRelayHasRelayTagEventAuxHasRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             badgeDefnUpvoteEvent, List.of(relayArgRelayTag, baseTagsRelayTag), "", relayArgRelay), auxRelay));
  }

  @Test
  final void E_NoYes_No__testEventNullRelayMultipleRelayTagsEventAuxNullRelayTagValue() {
    testTags(baseTagsRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             badgeDefnUpvoteEvent, List.of(baseTagsRelayTag, relayArgRelayTag), ""), null));
  }

  @Test
  final void testBadgeDefinitionGenericEventCtor() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(
             new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), badgeDefnUpvoteEvent, relayArgRelay)
                .getGenericEventRecord(),
             addressTag -> badgeDefnUpvoteEvent), null));

    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-should-not-appear:5555"));
    testTags(baseTagsRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(
             new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
                badgeDefnUpvoteEvent, List.of(baseTagsRelayTag, anotherRelayTag), "").getGenericEventRecord(),
             addressTag -> badgeDefnUpvoteEvent), null));

    testTags(anotherRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(
             new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
                badgeDefnUpvoteEvent, List.of(anotherRelayTag, baseTagsRelayTag), "").getGenericEventRecord(),
             addressTag -> badgeDefnUpvoteEvent), null));

  }

  @Test
  final void testSingularAddressTag() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       badgeDefnUpvoteEvent,
       List.of(this.badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()),
       relayArgRelay);

    assertEquals(1, List.of(badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(AddressTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getBadgeDefinitionEvent().getTypeSpecificTags(IdentifierTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(PubKeyTag.class).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(RelayTag.class).size());
  }

  @Test
  final void testEventIFAsGenericEventRecord() {
    EventIF badgeAwardGenericEventAsEventIF = badgeAwardUpvoteEvent;
    assertEquals(
       badgeAwardGenericEventAsEventIF.asGenericEventRecord(),
       badgeAwardUpvoteEvent.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       badgeAwardGenericEventAsEventIF.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       badgeAwardUpvoteEvent.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       EventIF.asGenericEventRecord.apply(badgeAwardUpvoteEvent));

    Function<EventIF, GenericEventRecord> methodInstance_AsGenericEventRecord = EventIF::asGenericEventRecord;
    assertEquals_VariantDemonstration(
       methodInstance_AsGenericEventRecord.apply(badgeAwardUpvoteEvent));
  }

  private void assertEquals_VariantDemonstration(GenericEventRecord genericEventRecordVariant) {
    assertEquals(badgeAwardUpvoteEvent.asGenericEventRecord(), genericEventRecordVariant);
  }

  private void testTags(RelayTag relayTag, BadgeAwardGenericEventAux eventAux) {
    assertEquals(relayTag.getRelay().getUrl(), eventAux.getRelay().getUrl());
    assertEquals(relayTag.getRelay(), eventAux.getRelay());
    assertEquals(relayTag.getRelay(), eventAux.getBadgeAwardGenericEvent().getRelay().orElseThrow());

    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().getRelayTag().orElseThrow());
    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().getTypeSpecificTags(RelayTag.class).stream().findFirst().orElseThrow());

    assertEquals(relayTag.getRelay().getUrl(), eventAux.getRelay().getUrl());
    assertEquals(relayTag.getRelay(), eventAux.getRelay());
    assertEquals(relayTag.getRelay(), eventAux.getBadgeAwardGenericEvent().getRelay().orElseThrow());

    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().getRelayTag().orElseThrow());
    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
  }
}
