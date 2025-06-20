package com.prosilion.nostr.filter.event;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.GenericEventKindIF;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import java.util.function.Function;
import java.util.function.Predicate;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
public class AuthorFilter<T extends PublicKey> extends AbstractFilterable<T> {
  public final static String FILTER_KEY = "authors";

  public AuthorFilter(T publicKey) {
    super(publicKey, FILTER_KEY);
  }

  @Override
  public Predicate<GenericEventKindIF> getPredicate() {
    return (genericEvent) ->
        genericEvent.getPublicKey().toHexString().equals(getFilterableValue());
  }

  @Override
  public String getFilterableValue() {
    return getAuthor().toHexString();
  }

  private T getAuthor() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node -> new AuthorFilter<>(new PublicKey(node.asText()));
}
