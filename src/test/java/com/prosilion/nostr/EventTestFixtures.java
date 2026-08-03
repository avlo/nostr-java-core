package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.curated.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.user.Identity;
import java.util.List;

import static com.prosilion.nostr.tag.SetsPairedEvent.NULL_EVENT_TAG_RELAY;

public class EventTestFixtures {
  public static final String relayUrl = "ws://localhost:5555";
  public static final Relay relay = new Relay(relayUrl);
  public static final String relayArgUrl = "ws://localhost:5555";
  public static final String baseTagsRelayUrl = "ws://localhost-from-relay-tag:5555";

  public static final Relay relayArgRelay = new Relay(relayArgUrl);
  public static final RelayTag relayArgRelayTag = new RelayTag(relayArgRelay);

  public static final Relay baseTagsRelay = new Relay(baseTagsRelayUrl);
  public static final RelayTag baseTagsRelayTag = new RelayTag(baseTagsRelay);

  public static final ExternalIdentityTag EXTERNAL_IDENTITY_TAG = new ExternalIdentityTag("afterimage", "badge_definition_reputation", String.valueOf(BadgeDefinitionReputationEvent.class.hashCode()));

  public static final String auxRelayUrl = "ws://localhost-aux-event-relay:5555";
  public static final Relay auxRelay = new Relay(auxRelayUrl);
  public static final RelayTag auxRelayTag = new RelayTag(auxRelay);

  public static final String TEST_UNIT_REPUTATION = "BADGE_DEFN_UNIT_REP";
  public static final String AWARD_UNIT_UPVOTE = "BDG_DEF_UNIT_UP";
  public static final String AWARD_UNIT_DOWNVOTE = "BDG_DEF_UNIT_DOWN";
  public static final String FORMULA_UNIT_UPVOTE = "FORMULA_UNIT_UPVOTE";
  public static final String FORMULA_UNIT_DOWNVOTE = "FORMULA_UNIT_DOWNVOTE";
  public static final String PLUS_ONE_FORMULA = "+1";
  public static final String MINUS_ONE_FORMULA = "-1";

  public static final IdentifierTag reputationIdentifierTag = new IdentifierTag(TEST_UNIT_REPUTATION);
  public static final IdentifierTag upvoteIdentifierTag = new IdentifierTag(AWARD_UNIT_UPVOTE);
  public static final IdentifierTag downvoteIdentifierTag = new IdentifierTag(AWARD_UNIT_DOWNVOTE);
  public static final IdentifierTag formulaUpvoteIdentifierTag = new IdentifierTag(FORMULA_UNIT_UPVOTE);
  public static final IdentifierTag formulaDownvoteIdentifierTag = new IdentifierTag(FORMULA_UNIT_DOWNVOTE);
  public static final Identity submitter =
//     Identity.generateRandomIdentity();
     Identity.create("aaa4585483196998204846989544737603523651520600328805626488477202");
  public static final Identity upvoteDefnCreator =
//     Identity.generateRandomIdentity();
     Identity.create("bbb4585483196998204846989544737603523651520600328805626488477202");
  public static final Identity recipient =
//     Identity.generateRandomIdentity();
     Identity.create("ccc4585483196998204846989544737603523651520600328805626488477202");
  public static final Identity formulaCreator =
//     Identity.generateRandomIdentity();
     Identity.create("ddd4585483196998204846989544737603523651520600328805626488477202");
  public static final Identity repDefnCreator =
//     Identity.generateRandomIdentity();
     Identity.create("eee4585483196998204846989544737603523651520600328805626488477202");
  public static final Identity aImgIdentity =
     Identity.create("fa11661b5f43c8f18f11861b4d553c47337dac9e351083b27320e311b7b324ac");

  //  BadgeDefinitionGenericEvent
//  _NoNo_   = Defn No  relayArgRelayTag, Defn No  baseTagsRelayTag
  public static final BadgeDefinitionGenericEvent defnEvent_NoNo_Upvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag);
  public static final BadgeDefinitionGenericEvent defnEvent_NoNo_Downvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag);

  //  _NoYes_  = Defn No  relayArgRelayTag, Defn Yes baseTagsRelayTag
  public static final BadgeDefinitionGenericEvent defnEvent_NoYes_Upvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(baseTagsRelayTag), "");

  public static final BadgeDefinitionGenericEvent defnEvent_NoYes_Downvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag, List.of(baseTagsRelayTag), "");

  //  _YesNo_  = Defn Yes relayArgRelayTag, Defn No  baseTagsRelayTag
  public static final BadgeDefinitionGenericEvent defnEvent_YesNo_Upvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, relayArgRelay);
  public static final BadgeDefinitionGenericEvent defnEvent_YesNo_Downvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag, relayArgRelay);

  //  _YesYes_ = Defn Yes relayArgRelayTag, Defn Yes baseTagsRelayTag
  public static final BadgeDefinitionGenericEvent defnEvent_YesYes_Upvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(baseTagsRelayTag), "", relayArgRelay);

  public static final BadgeDefinitionGenericEvent defnEvent_YesYes_Downvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag, List.of(baseTagsRelayTag), "", relayArgRelay);

  //  BadgeAwardGenericEvent
//  _NoNo_NoNo_   = _NoNo_  Defn, Award No  relayArgRelayTag, Award No baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, relayArgRelay);
  public static final SetsPairedEvent defnAuxNo_defnEvent_NoNo_Upvote = create(award_NoNo_Defn_NoNo_Upvote, null);
  public static final SetsPairedEvent eventAuxNo_award_NoNo_defn_NoNo_UpvoteSetsPairedEvent = create(award_NoNo_Defn_NoNo_Upvote, null);

  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_NoNo_Downvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Downvote, relayArgRelay);
  public static final SetsPairedEvent defnAuxNo_defnEvent_NoNo_Downvote = create(award_NoNo_Defn_NoNo_Downvote, null);
  public static final SetsPairedEvent eventAuxNo_award_NoNo_defn_NoNo_Downvote = create(award_NoNo_Defn_NoNo_Downvote, null);

  //  _NoNo_NoYes_  = _NoNo_  Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, List.of(baseTagsRelayTag));
  public static final SetsPairedEvent eventAuxNo_award_NoNo_defn_YesNo_UpvoteSetsPairedEvent = create(award_NoNo_Defn_NoYes_Upvote, null);

  //  _NoNo_YesNo_  = _NoNo_  Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, relayArgRelay);
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_YesNo_Downvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Downvote, relayArgRelay);

  //  _NoNo_YesYes_ = _NoNo_  Defn, Award Yes relayArgRelayTag, Award Yes baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, List.of(baseTagsRelayTag), relayArgRelay);


  //  _NoYes_NoNo_    = _NoYes_ Defn, Award No  relayArgRelayTag, Award No  baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, relayArgRelay);

  //  _NoYes_NoYes_   = _NoYes_ Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, List.of(baseTagsRelayTag));
  public static final SetsPairedEvent defnAuxNo_defnEvent_NoYes_Downvote = create(award_NoYes_Defn_NoYes_Upvote, null);
  public static final SetsPairedEvent defnAuxYes_defnEvent_NoYes_Downvote = create(award_NoYes_Defn_NoYes_Upvote, auxRelay);

  //  _NoYes_YesNo_   = _NoYes_ Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, relayArgRelay);
  public static final SetsPairedEvent defnAuxNo_defnEvent_NoYes_Upvote = create(award_NoYes_Defn_NoNo_Upvote, null);
  public static final SetsPairedEvent defnAuxYes_defnEvent_NoYes_Upvote = create(award_NoYes_Defn_YesNo_Upvote, auxRelay);

  //  _NoYes_YesYes_  = _NoYes_ Defn, Award Yes relayArgRelayTag, Award Yes  baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, List.of(baseTagsRelayTag), relayArgRelay);
  public static final SetsPairedEvent defnAuxYes_defnEvent_NoNo_Upvote = create(award_NoYes_Defn_YesYes_Upvote, auxRelay);


  //  _YesNo_NoNo_   = _YesNo_  Defn, Award No  relayArgRelayTag, Award No baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, relayArgRelay);
  public static final SetsPairedEvent defnAuxYes_defnEvent_NoNo_Downvote = create(award_YesNo_Defn_NoNo_Upvote, auxRelay);

  //  _YesNo_NoYes_  = _YesNo_  Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag));
  public static final SetsPairedEvent eventAuxYes_award_NoNo_defn_NoNo_Upvote = create(award_NoNo_Defn_NoNo_Upvote, auxRelay);

  //  _YesNo_YesNo_  = _YesNo_  Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, relayArgRelay);
  public static final SetsPairedEvent eventAuxYes_award_NoNo_defn_NoNo_Downvote = create(award_NoNo_Defn_NoNo_Downvote, auxRelay);

  //  _YesNo_YesYes_ = _YesNo_  Defn, Award Yes relayArgRelayTag, Award Yes baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag), relayArgRelay);
  public static final SetsPairedEvent defnAuxNo_defnEvent_YesNo_Upvote = create(award_YesNo_Defn_YesYes_Upvote, null);

  //  _YesYes_NoNo_    = _YesYes Defn, Award No  relayArgRelayTag, Award No  baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, relayArgRelay);
  public static final SetsPairedEvent defnAuxNo_defnEvent_YesNo_Downvote = create(award_YesYes_Defn_NoNo_Upvote, null);

  public static final SetsPairedEvent eventAuxNo_award_NoNo_Defn_YesNo_Upvote = create(award_NoNo_Defn_YesNo_Upvote, null);

  //  _YesYes_NoYes_   = _YesYes Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, List.of(baseTagsRelayTag));
  public static final SetsPairedEvent eventAuxNo_award_NoNo_Defn_YesNo_Downvote = create(award_NoNo_Defn_YesNo_Downvote, null);

  //  _YesYes_YesNo_   = _YesYes Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, relayArgRelay);
  public static final SetsPairedEvent eventAuxNo_award_YesNo_Defn_YesYes_Upvote = create(award_YesNo_Defn_YesYes_Upvote, null);

  //  _YesYes_YesYes_  = _YesYes Defn, Award Yes relayArgRelayTag, Award Yes  baseTagsRelayTag
  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, List.of(baseTagsRelayTag), relayArgRelay);
  public static final SetsPairedEvent defnAuxYes_defnEvent_YesYes_Upvote = create(award_YesYes_Defn_YesYes_Upvote, auxRelay);
  public static final SetsPairedEvent eventAuxYes_award_YesNo_Defn_YesYes_Upvote = create(award_YesNo_Defn_YesYes_Upvote, auxRelay);

  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoYes_UpvoteExtraRelayTag = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag, relayArgRelayTag));
  public static final SetsPairedEvent eventAuxNo_award_YesNo_Defn_YesNo_Upvote = create(award_YesNo_Defn_YesNo_Upvote, null);

  public static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoYes_UpvoteExtraRelayTagReversed = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(relayArgRelayTag, baseTagsRelayTag));
  public static final SetsPairedEvent eventAuxYes_award_YesNo_Defn_YesNo_Upvote = create(award_YesNo_Defn_YesNo_Upvote, auxRelay);

  public static final SetsPairedEvent eventAuxNo_award_YesNo_Defn_NoYes_UpvoteExtraRelayTag = create(award_YesNo_Defn_NoYes_UpvoteExtraRelayTag, null);
  public static final SetsPairedEvent eventAuxNo_award_YesNo_Defn_NoYes_UpvoteExtraRelayTagReversed = create(award_YesNo_Defn_NoYes_UpvoteExtraRelayTagReversed, null);

  public static final SetsPairedEvent eventAuxYes_award_YesYes_Defn_YesYes_Upvote = create(award_YesYes_Defn_YesYes_Upvote, auxRelay);

  public static SetsPairedEvent create(BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> event, Relay backupRelay) {
    return new SetsPairedEvent(
       event.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       new EventTag(
          event.getId(),
          event.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY)))
    );
  }
}
