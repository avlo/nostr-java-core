package com.prosilion.nostr;

import com.prosilion.nostr.event.BadgeAwardGenericEvent;
import com.prosilion.nostr.event.BadgeAwardGenericEventAux;
import com.prosilion.nostr.event.BadgeDefinitionGenericEvent;
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

  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_NoNo_Upvote = create(defnEvent_NoNo_Upvote, null);
  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_NoNo_Downvote = create(defnEvent_NoNo_Downvote, null);
  static final BadgeAwardGenericEventAux eventAuxNo_award_NoNo_defn_NoNo_Upvote = create(award_NoNo_Defn_NoNo_Upvote, null);
  static final BadgeAwardGenericEventAux eventAuxNo_award_NoNo_defn_NoNo_Downvote = create(award_NoNo_Defn_NoNo_Downvote, null);

  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_NoYes_Upvote = create(defnEvent_NoYes_Upvote, null);
  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_NoYes_Downvote = create(defnEvent_NoYes_Downvote, null);
  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_NoYes_Upvote = create(defnEvent_NoYes_Upvote, auxRelay);
  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_NoYes_Downvote = create(defnEvent_NoYes_Downvote, auxRelay);

  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_NoNo_Upvote = create(defnEvent_NoNo_Upvote, auxRelay);
  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_NoNo_Downvote = create(defnEvent_NoNo_Downvote, auxRelay);
  static final BadgeAwardGenericEventAux eventAuxYes_award_NoNo_defn_NoNo_Upvote = create(award_NoNo_Defn_NoNo_Upvote, auxRelay);
  static final BadgeAwardGenericEventAux eventAuxYes_award_NoNo_defn_NoNo_Downvote = create(award_NoNo_Defn_NoNo_Downvote, auxRelay);

  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_YesNo_Upvote = create(defnEvent_YesNo_Upvote, null);
  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_YesNo_Downvote = create(defnEvent_YesNo_Downvote, null);

  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_YesNo_Upvote = create(defnEvent_YesNo_Upvote, auxRelay);
  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_YesNo_Downvote = create(defnEvent_YesNo_Downvote, auxRelay);

  static final BadgeAwardGenericEventAux eventAuxNo_award_NoNo_Defn_YesNo_Upvote = create(award_NoNo_Defn_YesNo_Upvote, null);
  static final BadgeAwardGenericEventAux eventAuxNo_award_NoNo_Defn_YesNo_Downvote = create(award_NoNo_Defn_YesNo_Downvote, null);

  static final BadgeDefinitionGenericEventAux defnAuxNo_defnEvent_YesYes_Upvote = create(defnEvent_YesYes_Upvote, null);
  static final BadgeDefinitionGenericEventAux defnAuxYes_defnEvent_YesYes_Upvote = create(defnEvent_YesYes_Upvote, auxRelay);

  static final BadgeAwardGenericEventAux eventAuxNo_award_YesNo_Defn_YesYes_Upvote = create(award_YesNo_Defn_YesYes_Upvote, null);
  static final BadgeAwardGenericEventAux eventAuxYes_award_YesNo_Defn_YesYes_Upvote = create(award_YesNo_Defn_YesYes_Upvote, auxRelay);

  static final BadgeAwardGenericEventAux eventAuxNo_award_YesNo_Defn_YesNo_Upvote = create(award_YesNo_Defn_YesNo_Upvote, null);
  static final BadgeAwardGenericEventAux eventAuxYes_award_YesNo_Defn_YesNo_Upvote = create(award_YesNo_Defn_YesNo_Upvote, auxRelay);

  static final BadgeAwardGenericEventAux eventAuxNo_award_YesNo_Defn_NoYes_UpvoteExtraRelayTag = create(award_YesNo_Defn_NoYes_UpvoteExtraRelayTag, null);
  static final BadgeAwardGenericEventAux eventAuxNo_award_YesNo_Defn_NoYes_UpvoteExtraRelayTagReversed = create(award_YesNo_Defn_NoYes_UpvoteExtraRelayTagReversed, null);

  private static BadgeDefinitionGenericEventAux create(BadgeDefinitionGenericEvent event, Relay relay) {
    return relay == null ? new BadgeDefinitionGenericEventAux(event, null) : new BadgeDefinitionGenericEventAux(event, relay);
  }

  private static BadgeAwardGenericEventAux create(BadgeAwardGenericEvent<BadgeDefinitionGenericEvent> event, Relay relay) {
    return relay == null ? new BadgeAwardGenericEventAux(event, null) : new BadgeAwardGenericEventAux(event, relay);
  }
}
