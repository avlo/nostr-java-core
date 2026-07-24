package com.prosilion.nostr.event;

import com.ezylang.evalex.Expression;
import com.ezylang.evalex.parser.ParseException;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
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
import java.util.List;
import lombok.Getter;
import lombok.NonNull;
import org.apache.commons.lang3.StringUtils;

@Getter
public class CuratedFormulaEvent extends AbstractSetsEvent implements SetsPairedEventTagIF {
  public CuratedFormulaEvent(
     @NonNull Identity identity,
     @NonNull FormulaEvent formulaEvent,
     @NonNull ReferenceTag formulaEventReferenceTag,
     @NonNull Relay relay) {
    super(
       identity,
       Kind.CURATION_SETS_FORMULA_EVENT,
       formulaEvent.getIdentifierTag(),
       new SetsPairedEvent(
          formulaEvent.getBadgeDefinitionGenericEvent().asAddressableEventAddressTag(),
          new EventTag(
             formulaEvent.getId(),
             formulaEvent.getRelay().map(Relay::getUrl).orElse(
                formulaEventReferenceTag.getUrl()))),
       List.of(
          formulaEventReferenceTag,
          new PubKeyTag(formulaEvent.getPublicKey())), formulaEvent.getFormula(), relay);
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
          genericEventRecord.requireFirstTag(AddressTag.class),
          genericEventRecord.requireFirstTag(EventTag.class)));
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
    if (StringUtils.isBlank(formulaEvent.getContent()))
      throw new NostrException("formula event supplied formula is blank:\n  " + formulaEvent.createPrettyPrintJson());
//    TODO: store expression in global expression map
    try {
      new Expression(
         String.format("%s %s", "validate", formulaEvent.getContent())).validate();
    } catch (ParseException e) {
      throw new NostrException(e);
    }
    return formulaEvent;
  }
}
