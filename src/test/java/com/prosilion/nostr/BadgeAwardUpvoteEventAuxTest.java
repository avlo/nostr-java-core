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
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class BadgeAwardUpvoteEventAuxTest extends BaseEventAuxTest {
  @Test
  final void A_NoNo_No_testEventNullRelayNoRelayTagEventAuxNullRelayTag() {
    assertEquals(Optional.empty(), eventAuxNo_award_NoNo_defn_NoNo_Upvote.getRelay());
    assertEquals(Optional.empty(), eventAuxNo_award_NoNo_defn_NoNo_Upvote.getRelay().map(Relay::getUrl));
    assertThrows(NostrException.class, () -> eventAuxNo_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
  }

  @Test
  final void A_NoNo_Yes_testEventNullRelayNoRelayTagEventAuxNullRelayTag() {
    assertThrows(NostrException.class, () -> eventAuxYes_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
    assertEquals(Optional.empty(), eventAuxYes_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().getRelayTag());

    assertEquals(auxRelayTag.getRelay(), eventAuxYes_award_NoNo_defn_NoNo_Upvote.getRelay().orElseThrow());
    assertEquals(Optional.empty(), eventAuxYes_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().getRelay());
    assertThrows(NoSuchElementException.class, () -> eventAuxYes_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().getRelay().orElseThrow());
    assertEquals(auxRelayTag.getRelay().getUrl(), eventAuxYes_award_NoNo_defn_NoNo_Upvote.getRelay().map(Relay::getUrl).orElseThrow());
    assertEquals(auxRelay, eventAuxYes_award_NoNo_defn_NoNo_Upvote.getRelay().orElseThrow());
    assertThrows(NoSuchElementException.class, () -> eventAuxYes_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().getRelayTag().orElseThrow());
    assertThrows(NostrException.class, () -> eventAuxYes_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
    assertThrows(NoSuchElementException.class, () -> eventAuxYes_award_NoNo_defn_NoNo_Upvote.getBadgeAwardGenericEvent().getTypeSpecificTags(RelayTag.class).stream().findFirst().orElseThrow());
  }

  @Test
  final void B_NoYes_No__testEventNullRelayHasRelayTagEventAuxNullRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             defnEvent_YesNo_Upvote, List.of(relayArgRelayTag, baseTagsRelayTag), ""), null));
  }

  @Test
  final void A_NoYes_Yes_testEventNullRelayHasRelayTagEventAuxHasRelayTag() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             defnEvent_YesNo_Upvote, List.of(relayArgRelayTag, baseTagsRelayTag), ""), auxRelay));
  }

  @Test
  final void C_YesNo_No__testEventHasRelayNoRelayTagEventAuxNullRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             defnEvent_YesNo_Upvote, relayArgRelay), null));
  }

  @Test
  final void C_YesNo_Yes__testEventHasRelayNoRelayTagEventAuxHasRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             defnEvent_YesNo_Upvote, relayArgRelay), auxRelay));
  }

  @Test
  final void D_YesYes_No__testEventHasRelayHasRelayTagEventAuxNullRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             defnEvent_YesNo_Upvote, List.of(relayArgRelayTag, baseTagsRelayTag), "", relayArgRelay), null));
  }

  @Test
  final void D_YesYes_Yes__testEventHasRelayHasRelayTagEventAuxHasRelayTagValue() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             defnEvent_YesNo_Upvote, List.of(relayArgRelayTag, baseTagsRelayTag), "", relayArgRelay), auxRelay));
  }

  @Test
  final void E_NoYes_No__testEventNullRelayMultipleRelayTagsEventAuxNullRelayTagValue() {
    testTags(baseTagsRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
             defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag, relayArgRelayTag), ""), null));
  }

  @Test
  final void testBadgeDefinitionGenericEventCtor() {
    testTags(relayArgRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(
             new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, relayArgRelay)
                .getGenericEventRecord(),
             addressTag -> defnEvent_YesNo_Upvote), null));

    RelayTag anotherRelayTag = new RelayTag(new Relay("ws://localhost-should-not-appear:5555"));
    testTags(baseTagsRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(
             new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
                defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag, anotherRelayTag), "").getGenericEventRecord(),
             addressTag -> defnEvent_YesNo_Upvote), null));

    testTags(anotherRelayTag,
       new BadgeAwardGenericEventAux(
          new BadgeAwardGenericEvent<>(
             new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(),
                defnEvent_YesNo_Upvote, List.of(anotherRelayTag, baseTagsRelayTag), "").getGenericEventRecord(),
             addressTag -> defnEvent_YesNo_Upvote), null));

  }

  @Test
  final void testSingularAddressTag() {
    BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> badgeAwardUpvoteEvent = new BadgeAwardGenericEvent<>(
       submitter,
       recipient.getPublicKey(),
       defnEvent_YesNo_Upvote,
       List.of(award_NoNo_Defn_YesNo_Upvote.getAddressableEvent().asAddressableEventAddressTag()),
       relayArgRelay);

    assertEquals(1, List.of(badgeAwardUpvoteEvent.getAddressableEvent().asAddressableEventAddressTag()).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(AddressTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getBadgeDefinitionEvent().getTypeSpecificTags(IdentifierTag.class).size());

    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(PubKeyTag.class).size());
    assertEquals(1, badgeAwardUpvoteEvent.getTypeSpecificTags(RelayTag.class).size());
  }

  @Test
  final void testEventIFAsGenericEventRecord() {
    EventIF badgeAwardGenericEventAsEventIF = award_NoNo_Defn_YesNo_Upvote;
    assertEquals(
       badgeAwardGenericEventAsEventIF.asGenericEventRecord(),
       award_NoNo_Defn_YesNo_Upvote.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       badgeAwardGenericEventAsEventIF.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       award_NoNo_Defn_YesNo_Upvote.asGenericEventRecord());

    assertEquals_VariantDemonstration(
       EventIF.asGenericEventRecord.apply(award_NoNo_Defn_YesNo_Upvote));

    Function<EventIF, GenericEventRecord> methodInstance_AsGenericEventRecord = EventIF::asGenericEventRecord;
    assertEquals_VariantDemonstration(
       methodInstance_AsGenericEventRecord.apply(award_NoNo_Defn_YesNo_Upvote));
  }

  private void assertEquals_VariantDemonstration(GenericEventRecord genericEventRecordVariant) {
    assertEquals(award_NoNo_Defn_YesNo_Upvote.asGenericEventRecord(), genericEventRecordVariant);
  }

  private void testTags(RelayTag relayTag, BadgeAwardGenericEventAux eventAux) {
    assertEquals(relayTag.getRelay().getUrl(), eventAux.getRelay().map(Relay::getUrl).orElseThrow());
    assertEquals(relayTag.getRelay(), eventAux.getRelay().orElseThrow());
    assertEquals(relayTag.getRelay(), eventAux.getBadgeAwardGenericEvent().getRelay().orElseThrow());

    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().getRelayTag().orElseThrow());
    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().getTypeSpecificTags(RelayTag.class).stream().findFirst().orElseThrow());

    assertEquals(relayTag.getRelay().getUrl(), eventAux.getRelay().map(Relay::getUrl).orElseThrow());
    assertEquals(relayTag.getRelay(), eventAux.getRelay().orElseThrow());
    assertEquals(relayTag.getRelay(), eventAux.getBadgeAwardGenericEvent().getRelay().orElseThrow());

    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().getRelayTag().orElseThrow());
    assertEquals(relayTag, eventAux.getBadgeAwardGenericEvent().requireFirstTag(RelayTag.class));
  }
}
