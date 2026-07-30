package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import lombok.Getter;
import lombok.NonNull;

@Getter
public class CuratedFormulaEvent extends AbstractSetsEvent implements SetsPairedEventTagIF {
  public CuratedFormulaEvent(
     @NonNull Identity identity,
     @NonNull FormulaEvent formulaEvent,
     @NonNull ReferenceTag formulaEventReferenceTag,
     @NonNull Relay relay) {
    this(identity, formulaEvent.asGenericEventRecord(), formulaEventReferenceTag, relay);
  }

  public CuratedFormulaEvent(
     @NonNull Identity identity,
     @NonNull GenericEventRecord formulaEvent,
     @NonNull ReferenceTag formulaEventReferenceTag,
     @NonNull Relay relay) {
    super(
       identity,
       Kind.CURATION_SETS_FORMULA_EVENT,
       formulaEvent.requireFirstTag(IdentifierTag.class),
       new SetsPairedEvent(
          fillAddressTag(
             formulaEvent.requireFirstTag(AddressTag.class),
             formulaEventReferenceTag),
          new EventTag(
             formulaEvent.getId(),
             formulaEvent.getRelayTag().map(RelayTag::relay).map(Relay::getUrl).orElse(formulaEventReferenceTag.getUrl()))),
       List.of(
          formulaEventReferenceTag,
          new PubKeyTag(formulaEvent.getPublicKey())),
       formulaEvent.getContent(),
       relay);
  }

  public CuratedFormulaEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(
       validateFormula(
          validateRequiredTags(
             genericEventRecord,
             List.of(
                PubKeyTag.class,
                IdentifierTag.class,
                AddressTag.class,
                EventTag.class,
                RelayTag.class,
                ReferenceTag.class))),
       new SetsPairedEvent(
          fillAddressTag(
             genericEventRecord.requireFirstTag(AddressTag.class),
             genericEventRecord.requireFirstTag(ReferenceTag.class)),
          fillEventTag(
             genericEventRecord.requireFirstTag(EventTag.class),
             genericEventRecord.requireFirstTag(ReferenceTag.class))));
  }

  @JsonIgnore
  public final PublicKey getFormulaEventCreatorPublicKey() {
    return super.requireFirstTag(PubKeyTag.class).publicKey();
  }

  @JsonIgnore
  public final String getFormulaEventId() {
    return super.getEventTag().eventId();
  }

  @JsonIgnore
  public final String getFormula() {
    return super.getContent();
  }

  //  TODO (potentially): to accommodate both (necessary) formulaEvent as well as (optional) user-defined comment/text,
//   introduce "summary"/"description" tag as per:
/*
  ["summary", "<brief description of the event>"],
  https://github.com/nostr-protocol/nips/blob/master/52.md
  
  ["description", "Awarded to users demonstrating bravery"],
  A description tag whose value contain meaning behind the badge, or the reason of its issuance.
  https://github.com/nostr-protocol/nips/blob/master/58.md    
*/
  private static GenericEventRecord validateFormula(GenericEventRecord formulaEvent) {
    FormulaEvent.validate(formulaEvent.getContent(), Kind.CURATION_SETS_FORMULA_EVENT, formulaEvent.getKind());
    return formulaEvent;
  }
}
