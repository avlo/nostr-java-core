package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;

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
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote);
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_NoNo_Downvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Downvote);

  //  _NoNo_NoYes_  = _NoNo_  Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, List.of(baseTagsRelayTag));

  //  _NoNo_YesNo_  = _NoNo_  Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, relayArgRelay);

  //  _NoNo_YesYes_ = _NoNo_  Defn, Award Yes relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoNo_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoNo_Upvote, List.of(baseTagsRelayTag), relayArgRelay);


  //  _NoYes_NoNo_    = _NoYes_ Defn, Award No  relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote);

  //  _NoYes_NoYes_   = _NoYes_ Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, List.of(baseTagsRelayTag));

  //  _NoYes_YesNo_   = _NoYes_ Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, relayArgRelay);

  //  _NoYes_YesYes_  = _NoYes_ Defn, Award Yes relayArgRelayTag, Award Yes  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_NoYes_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_NoYes_Upvote, List.of(baseTagsRelayTag), relayArgRelay);


  //  _YesNo_NoNo_   = _YesNo_  Defn, Award No  relayArgRelayTag, Award No baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote);

  //  _YesNo_NoYes_  = _YesNo_  Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag));

  //  _YesNo_YesNo_  = _YesNo_  Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, relayArgRelay);

  //  _YesNo_YesYes_ = _YesNo_  Defn, Award Yes relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesNo_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesNo_Upvote, List.of(baseTagsRelayTag), relayArgRelay);


  //  _YesYes_NoNo_    = _YesYes Defn, Award No  relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_NoNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote);

  //  _YesYes_NoYes_   = _YesYes Defn, Award No  relayArgRelayTag, Award Yes baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_NoYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, List.of(baseTagsRelayTag));

  //  _YesYes_YesNo_   = _YesYes Defn, Award Yes relayArgRelayTag, Award No  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_YesNo_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, relayArgRelay);

  //  _YesYes_YesYes_  = _YesYes Defn, Award Yes relayArgRelayTag, Award Yes  baseTagsRelayTag
  static final BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> award_YesYes_Defn_YesYes_Upvote = new BadgeAwardGenericEvent<>(submitter, recipient.getPublicKey(), defnEvent_YesYes_Upvote, List.of(baseTagsRelayTag), relayArgRelay);
}
