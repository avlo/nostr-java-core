package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import java.util.Arrays;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;
import lombok.NonNull;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;
import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;

@EqualsAndHashCode(callSuper = true)
public class ExternalIdentityTagFilter extends AbstractFilterable<ExternalIdentityTag> {
  public static final String FILTER_KEY = "#i";

  public ExternalIdentityTagFilter(ExternalIdentityTag externalIdentityTag) {
    super(externalIdentityTag, FILTER_KEY);
  }

  @Override
  public final Predicate<EventIF> getPredicate() {
    return getPredicate(ExternalIdentityTag.class, getExternalIdentityTag());
  }

  @Override
  public final String getFilterableValue() {
    return String.join("\",\"",
        Stream.of(
                getExternalIdentityTag().getPlatform(),
                getExternalIdentityTag().getIdentity())
            .map(Object::toString).collect(Collectors.joining(":")),
        getExternalIdentityTag().getProof());
  }

  private ExternalIdentityTag getExternalIdentityTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node ->
      new ExternalIdentityTagFilter(
          createExternalIdentityTag(node));

  public static ExternalIdentityTag createExternalIdentityTag(@NonNull JsonNode node) {
    ArrayNode arrayNode = I_DECODER_MAPPER_AFTERBURNER.createArrayNode();
    arrayNode.addAll(StreamSupport.stream(node.spliterator(), false).toList());
    List<String> nodes =
        List.of(
            StreamSupport.stream(arrayNode.spliterator(), false).toList()
                .getFirst().asText().split(":"));

    String platform = nodes.get(0);

    List<String> list = Arrays.stream(nodes.get(1).split("\",\"")).toList();
    String identity = list.get(0);
    String proof = list.get(1);

    return new ExternalIdentityTag(
        platform,
        identity,
        proof);
  }

  @Override
  public final void addToArrayNode(ArrayNode arrayNode) {
    arrayNode.add(
        MAPPER_AFTERBURNER.createArrayNode()
            .add(getFilterableValue())
//            .add(getExternalIdentityTag().getProof())
    );
  }
}
