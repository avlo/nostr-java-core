package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

@EqualsAndHashCode(callSuper = true)
public class ExternalIdentityTagFilter extends AbstractFilterable<ExternalIdentityTag> {
  public static final String FILTER_KEY = "#i";

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
      new ExternalIdentityTagFilter(
          createExternalIdentityTag(node));

  private static ExternalIdentityTag createExternalIdentityTag(@NonNull JsonNode node) {
    ArrayNode arrayNode = I_DECODER_MAPPER_AFTERBURNER.createArrayNode();
    arrayNode.addAll(StreamSupport.stream(node.spliterator(), false).toList());

    List<JsonNode> split = StreamSupport.stream(arrayNode.spliterator(), false).toList();
    List<String> nodes = List.of(split.get(0).asText().split(":"));

    return new ExternalIdentityTag(
        nodes.get(0),
        nodes.get(1),
        split.get(1).asText()
    );
  }
}
