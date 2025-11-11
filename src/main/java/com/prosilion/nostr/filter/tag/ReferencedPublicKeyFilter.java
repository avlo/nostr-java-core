package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.PublicKey;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ReferencedPublicKeyFilter extends AbstractFilterable<PubKeyTag> {
  public static final String FILTER_KEY = "#p";

  public ReferencedPublicKeyFilter(PubKeyTag referencedPubKeyTag) {
    super(referencedPubKeyTag, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTagsStream(PubKeyTag.class, genericEvent)
            .anyMatch(pubKeyTag ->
                pubKeyTag.getPublicKey().toHexString().equals(getFilterableValue()));
  }

  @Override
  public String getFilterableValue() {
    return getReferencedPublicKey().getPublicKey().toHexString();
  }

  private PubKeyTag getReferencedPublicKey() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new ReferencedPublicKeyFilter(new PubKeyTag(new PublicKey(node.asText())));
}
