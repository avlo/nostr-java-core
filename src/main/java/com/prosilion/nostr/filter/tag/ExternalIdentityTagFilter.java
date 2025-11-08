package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

@EqualsAndHashCode(callSuper = true)
public class ExternalIdentityTagFilter extends AbstractFilterable<ExternalIdentityTag> {
  public final static String FILTER_KEY = "#i";

  public ExternalIdentityTagFilter(ExternalIdentityTag externalIdentityTag) {
    super(externalIdentityTag, FILTER_KEY);
  }

  @Override
  public Predicate<EventIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTagsStream(ExternalIdentityTag.class, genericEvent)
            .anyMatch(externalIdentityTag ->
                externalIdentityTag.equals(getExternalIdentityTag()));
  }

  @Override
  public String getFilterableValue() {
    return Stream.of(
            getExternalIdentityTag().getPlatform(),
            getExternalIdentityTag().getIdentity())
        .map(Object::toString).collect(Collectors.joining(":"));
  }

  private ExternalIdentityTag getExternalIdentityTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node ->
      new ExternalIdentityTagFilter(createExternalIdentityTag(node));

  protected static ExternalIdentityTag createExternalIdentityTag(@NonNull JsonNode node) {
    return ExternalIdentityTag.deserialize(node);
  }
}
