package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.user.Identity;
import java.util.List;

import static com.prosilion.nostr.tag.SetsPairedEvent.NULL_EVENT_TAG_RELAY;

public class BaseEventTest {
  static final String relayArgUrl = "ws://localhost:5555";
  static final String baseTagsRelayUrl = "ws://localhost-from-relay-tag:5555";

  static final Relay relayArgRelay = new Relay(relayArgUrl);
  static final RelayTag relayArgRelayTag = new RelayTag(relayArgRelay);

  static final Relay baseTagsRelay = new Relay(baseTagsRelayUrl);
  static final RelayTag baseTagsRelayTag = new RelayTag(baseTagsRelay);

  static final String UNIT_UPVOTE = "UNIT_UPVOTE";
  static final String UNIT_DOWNVOTE = "UNIT_DOWNVOTE";

  static final IdentifierTag upvoteIdentifierTag = new IdentifierTag(UNIT_UPVOTE);
  static final IdentifierTag downvoteIdentifierTag = new IdentifierTag(UNIT_DOWNVOTE);
  static final ExternalIdentityTag EXTERNAL_IDENTITY_TAG = new ExternalIdentityTag("afterimage", "badge_definition_reputation", String.valueOf(BadgeDefinitionReputationEvent.class.hashCode()));
  static final String REPUTATION = "TEST_REPUTATION";
  static final IdentifierTag reputationIdentifierTag = new IdentifierTag(REPUTATION);

  static final String auxRelayUrl = "ws://localhost-aux-event-relay:5555";
  static final Relay auxRelay = new Relay(auxRelayUrl);
  static final RelayTag auxRelayTag = new RelayTag(auxRelay);

  static final Identity aImgIdentity =
 // below produces e04e1c1c30df6058433f61681644fd24914f2e02e420496086c61f53eb504c04
     Identity.create("fa11661b5f43c8f18f11861b4d553c47337dac9e351083b27320e311b7b324ac"); 

  //  static final Identity submitter = Identity.generateRandomIdentity();
  static final Identity submitter =
//     Identity.generateRandomIdentity();
     Identity.create("aaa4585483196998204846989544737603523651520600328805626488477202");
  static final Identity upvoteDefnCreator =
//     Identity.generateRandomIdentity();
     Identity.create("bbb4585483196998204846989544737603523651520600328805626488477202");
  static final Identity recipient =
//     Identity.generateRandomIdentity();
     Identity.create("ccc4585483196998204846989544737603523651520600328805626488477202");

  //  BadgeDefinitionGenericEvent
//  _NoNo_   = Defn No  relayArgRelayTag, Defn No  baseTagsRelayTag
  static final BadgeDefinitionGenericEvent defnEvent_NoNo_Upvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag);
  static final BadgeDefinitionGenericEvent defnEvent_NoNo_Downvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag);

  //  _NoYes_  = Defn No  relayArgRelayTag, Defn Yes baseTagsRelayTag
  static final BadgeDefinitionGenericEvent defnEvent_NoYes_Upvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(baseTagsRelayTag), "");

  static final BadgeDefinitionGenericEvent defnEvent_NoYes_Downvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag, List.of(baseTagsRelayTag), "");

  //  _YesNo_  = Defn Yes relayArgRelayTag, Defn No  baseTagsRelayTag
  static final BadgeDefinitionGenericEvent defnEvent_YesNo_Upvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, relayArgRelay);
  static final BadgeDefinitionGenericEvent defnEvent_YesNo_Downvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag, relayArgRelay);

  //  _YesYes_ = Defn Yes relayArgRelayTag, Defn Yes baseTagsRelayTag
  static final BadgeDefinitionGenericEvent defnEvent_YesYes_Upvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, upvoteIdentifierTag, List.of(baseTagsRelayTag), "", relayArgRelay);

  static final BadgeDefinitionGenericEvent defnEvent_YesYes_Downvote = new BadgeDefinitionGenericEvent(upvoteDefnCreator, downvoteIdentifierTag, List.of(baseTagsRelayTag), "", relayArgRelay);

  //  BadgeAwardGenericEvent
//  _NoNo_NoNo_   = _NoNo_  Defn, Award No  relayArgRelayTag, Award No baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, relayArgRelay);
  static final SetsPairedEvent defnAuxNo_defnEvent_NoNo_Upvote = create(award_NoNo_Defn_NoNo_Upvote, null);
  static final SetsPairedEvent eventAuxNo_award_NoNo_defn_NoNo_Upvote = create(award_NoNo_Defn_NoNo_Upvote, null);

  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_NoNo_Downvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Downvote, relayArgRelay);
  static final SetsPairedEvent defnAuxNo_defnEvent_NoNo_Downvote = create(award_NoNo_Defn_NoNo_Downvote, null);
  static final SetsPairedEvent eventAuxNo_award_NoNo_defn_NoNo_Downvote = create(award_NoNo_Defn_NoNo_Downvote, null);

  //  _NoNo_NoYes_  = _NoNo_  Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, List.of(baseTagsRelayTag));

  //  _NoNo_YesNo_  = _NoNo_  Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, relayArgRelay);
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_YesNo_Downvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Downvote, relayArgRelay);

  //  _NoNo_YesYes_ = _NoNo_  Defn, Award Yes relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, List.of(baseTagsRelayTag), relayArgRelay);


  //  _NoYes_NoNo_    = _NoYes_ Defn, Award No  relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, relayArgRelay);

  //  _NoYes_NoYes_   = _NoYes_ Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, List.of(baseTagsRelayTag));
  static final SetsPairedEvent defnAuxNo_defnEvent_NoYes_Downvote = create(award_NoYes_Defn_NoYes_Upvote, null);
  static final SetsPairedEvent defnAuxYes_defnEvent_NoYes_Downvote = create(award_NoYes_Defn_NoYes_Upvote, auxRelay);

  //  _NoYes_YesNo_   = _NoYes_ Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, relayArgRelay);
  static final SetsPairedEvent defnAuxNo_defnEvent_NoYes_Upvote = create(award_NoYes_Defn_NoNo_Upvote, null);
  static final SetsPairedEvent defnAuxYes_defnEvent_NoYes_Upvote = create(award_NoYes_Defn_YesNo_Upvote, auxRelay);

  //  _NoYes_YesYes_  = _NoYes_ Defn, Award Yes relayArgRelayTag, Award Yes  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, List.of(baseTagsRelayTag), relayArgRelay);
  static final SetsPairedEvent defnAuxYes_defnEvent_NoNo_Upvote = create(award_NoYes_Defn_YesYes_Upvote, auxRelay);


  //  _YesNo_NoNo_   = _YesNo_  Defn, Award No  relayArgRelayTag, Award No baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, relayArgRelay);
  static final SetsPairedEvent defnAuxYes_defnEvent_NoNo_Downvote = create(award_YesNo_Defn_NoNo_Upvote, auxRelay);

  //  _YesNo_NoYes_  = _YesNo_  Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag));
  static final SetsPairedEvent eventAuxYes_award_NoNo_defn_NoNo_Upvote = create(award_NoNo_Defn_NoNo_Upvote, auxRelay);

  //  _YesNo_YesNo_  = _YesNo_  Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, relayArgRelay);
  static final SetsPairedEvent eventAuxYes_award_NoNo_defn_NoNo_Downvote = create(award_NoNo_Defn_NoNo_Downvote, auxRelay);

  //  _YesNo_YesYes_ = _YesNo_  Defn, Award Yes relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag), relayArgRelay);
  static final SetsPairedEvent defnAuxNo_defnEvent_YesNo_Upvote = create(award_YesNo_Defn_YesYes_Upvote, null);

  //  _YesYes_NoNo_    = _YesYes Defn, Award No  relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, relayArgRelay);
  static final SetsPairedEvent defnAuxNo_defnEvent_YesNo_Downvote = create(award_YesYes_Defn_NoNo_Upvote, null);

  static final SetsPairedEvent eventAuxNo_award_NoNo_Defn_YesNo_Upvote = create(award_NoNo_Defn_YesNo_Upvote, null);

  //  _YesYes_NoYes_   = _YesYes Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, List.of(baseTagsRelayTag));
  static final SetsPairedEvent eventAuxNo_award_NoNo_Defn_YesNo_Downvote = create(award_NoNo_Defn_YesNo_Downvote, null);

  //  _YesYes_YesNo_   = _YesYes Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, relayArgRelay);
  static final SetsPairedEvent eventAuxNo_award_YesNo_Defn_YesYes_Upvote = create(award_YesNo_Defn_YesYes_Upvote, null);

  //  _YesYes_YesYes_  = _YesYes Defn, Award Yes relayArgRelayTag, Award Yes  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, List.of(baseTagsRelayTag), relayArgRelay);
  static final SetsPairedEvent eventAuxYes_award_YesNo_Defn_YesYes_Upvote = create(award_YesNo_Defn_YesYes_Upvote, auxRelay);

  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoYes_UpvoteExtraRelayTag = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag, relayArgRelayTag));
  static final SetsPairedEvent eventAuxNo_award_YesNo_Defn_YesNo_Upvote = create(award_YesNo_Defn_YesNo_Upvote, null);

  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoYes_UpvoteExtraRelayTagReversed = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(relayArgRelayTag, baseTagsRelayTag));
  static final SetsPairedEvent eventAuxYes_award_YesNo_Defn_YesNo_Upvote = create(award_YesNo_Defn_YesNo_Upvote, auxRelay);

  static final SetsPairedEvent eventAuxNo_award_YesNo_Defn_NoYes_UpvoteExtraRelayTag = create(award_YesNo_Defn_NoYes_UpvoteExtraRelayTag, null);
  static final SetsPairedEvent eventAuxNo_award_YesNo_Defn_NoYes_UpvoteExtraRelayTagReversed = create(award_YesNo_Defn_NoYes_UpvoteExtraRelayTagReversed, null);

  public static SetsPairedEvent create(BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> event, Relay backupRelay) {
    return new SetsPairedEvent(
       event.getBadgeDefinitionEvent().asAddressableEventAddressTag(),
       backupRelay,
       new EventTag(
          event.getId(),
          event.getRelay().map(Relay::getUrl).orElseThrow(() ->
             new NostrException(NULL_EVENT_TAG_RELAY))),
       event.getAwardRecipientPublicKey());
  }
}
