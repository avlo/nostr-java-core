package com.prosilion.nostr.filter.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.user.PublicKey;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class AuthorFilter extends AbstractFilterable<PublicKey> {
  public final static String FILTER_KEY = "authors";

  public AuthorFilter(PublicKey publicKey) {
    super(publicKey, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        genericEvent.getPublicKey().toHexString().equals(getFilterableValue());
  }

  @Override
  public String getFilterableValue() {
    return getAuthor().toHexString();
  }

  private PublicKey getAuthor() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new AuthorFilter(new PublicKey(node.asText()));
}
