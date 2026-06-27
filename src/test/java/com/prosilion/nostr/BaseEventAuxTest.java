package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionReputationEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;

public class BaseEventAuxTest extends BaseEventTest {
  static final ExternalIdentityTag EXTERNAL_IDENTITY_TAG = new ExternalIdentityTag("afterimage", "badge_definition_reputation", String.valueOf(BadgeDefinitionReputationEvent.class.hashCode()));
  static final String REPUTATION = "TEST_REPUTATION";
  static final IdentifierTag reputationIdentifierTag = new IdentifierTag(REPUTATION);

  static final String auxRelayUrl = "ws://localhost-aux-event-relay:5555";
  static final Relay auxRelay = new Relay(auxRelayUrl);
  static final RelayTag auxRelayTag = new RelayTag(auxRelay);

  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_NoNo_Upvote = new BadgeDefinitionGenericEventAux(defnEvent_NoNo_Upvote, null);
  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_NoNo_Downvote = new BadgeDefinitionGenericEventAux(defnEvent_NoNo_Downvote, null);
  static final BadgeAwardGenericEventAux eventAuxNo_award_NoNo_defn_NoNo_Upvote = new BadgeAwardGenericEventAux(award_NoNo_Defn_NoNo_Upvote, null);
  static final BadgeAwardGenericEventAux eventAuxNo_award_NoNo_defn_NoNo_Downvote = new BadgeAwardGenericEventAux(award_NoNo_Defn_NoNo_Downvote, null);

  
  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_NoYes_Upvote = new BadgeDefinitionGenericEventAux(defnEvent_NoYes_Upvote, null);
  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_NoYes_Downvote = new BadgeDefinitionGenericEventAux(defnEvent_NoYes_Downvote, null);

  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_NoNo_Upvote = new BadgeDefinitionGenericEventAux(defnEvent_NoNo_Upvote, auxRelay);
  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_NoNo_Downvote = new BadgeDefinitionGenericEventAux(defnEvent_NoNo_Downvote, auxRelay);
  static final BadgeAwardGenericEventAux eventAuxYes_award_NoNo_defn_NoNo_Upvote = new BadgeAwardGenericEventAux(award_NoNo_Defn_NoNo_Upvote, auxRelay);
  static final BadgeAwardGenericEventAux eventAuxYes_award_NoNo_defn_NoNo_Downvote = new BadgeAwardGenericEventAux(award_NoNo_Defn_NoNo_Downvote, auxRelay);

  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_YesNo_Upvote = new BadgeDefinitionGenericEventAux(defnEvent_YesNo_Upvote, null);
  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_YesNo_Downvote = new BadgeDefinitionGenericEventAux(defnEvent_YesNo_Downvote, null);

  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_YesNo_Upvote = new BadgeDefinitionGenericEventAux(defnEvent_YesNo_Upvote, auxRelay);
  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_YesNo_Downvote = new BadgeDefinitionGenericEventAux(defnEvent_YesNo_Downvote, auxRelay);
  
  static final BadgeAwardGenericEventAux eventAuxNo_award_NoNo_Defn_YesNo_Upvote = new BadgeAwardGenericEventAux(award_NoNo_Defn_YesNo_Upvote, null);
  static final BadgeAwardGenericEventAux eventAuxNo_award_NoNo_Defn_YesNo_Downvote = new BadgeAwardGenericEventAux(award_NoNo_Defn_YesNo_Downvote, null);

  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_YesYes_Upvote = new BadgeDefinitionGenericEventAux(defnEvent_YesYes_Upvote, null);
  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_YesYes_Upvote = new BadgeDefinitionGenericEventAux(defnEvent_YesYes_Upvote, auxRelay);
}
