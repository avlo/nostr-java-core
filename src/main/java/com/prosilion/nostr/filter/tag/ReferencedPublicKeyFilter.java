package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.GenericEventKindIF;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.PubKeyTag;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class ReferencedPublicKeyFilter<T extends PubKeyTag> extends AbstractFilterable<T> {
  public final static String FILTER_KEY = "#p";

  public ReferencedPublicKeyFilter(T referencedPubKeyTag) {
    super(referencedPubKeyTag, FILTER_KEY);
  }

  @Override
  public Predicate<GenericEventKindIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTags(PubKeyTag.class, genericEvent).stream()
            .anyMatch(pubKeyTag ->
                pubKeyTag.getPublicKey().toHexString().equals(getFilterableValue()));
  }

  @Override
  public String getFilterableValue() {
    return getReferencedPublicKey().getPublicKey().toHexString();
  }

  private T getReferencedPublicKey() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new ReferencedPublicKeyFilter<>(new PubKeyTag(new PublicKey(node.asText())));
}
