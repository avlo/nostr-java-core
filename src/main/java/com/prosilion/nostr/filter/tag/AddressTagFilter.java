package com.prosilion.nostr.filter.tag;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.GenericEventDtoIF;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.filter.AbstractFilterable;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;
import lombok.EqualsAndHashCode;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

@EqualsAndHashCode(callSuper = true)
public class AddressTagFilter<T extends AddressTag> extends AbstractFilterable<T> {
  public final static String FILTER_KEY = "#a";

  public AddressTagFilter(T addressTag) {
    super(addressTag, FILTER_KEY);
  }

  @Override
  public Predicate<GenericEventDtoIF> getPredicate() {
    return (genericEvent) ->
        Filterable.getTypeSpecificTags(AddressTag.class, genericEvent).stream()
            .anyMatch(addressTag ->
                addressTag.equals(getAddressTag()));
  }

  @Override
  public Object getFilterableValue() {
    String requiredAttributes = Stream.of(
            getAddressTag().getKind(),
            getAddressTag().getPublicKey().toHexString())
        .map(Object::toString).collect(Collectors.joining(":"));

    String identifierTagPortion = Optional.ofNullable(getAddressTag().getIdentifierTag()).map(identifierTag ->
        String.join(":", requiredAttributes, identifierTag.getUuid())).orElse(
        Strings.concat(requiredAttributes, ":"));

    return Optional.ofNullable(getAddressTag().getRelay()).map(relay ->
        String.join("\",\"", identifierTagPortion, relay.getUri())).orElse(identifierTagPortion);
  }

  private T getAddressTag() {
    return super.getFilterable();
  }

  public static Function<JsonNode, Filterable> fxn = node ->
      new AddressTagFilter<>(createAddressTag(node));

  protected static <T extends BaseTag> T createAddressTag(@NonNull JsonNode node) {
    ArrayNode arrayNode = I_DECODER_MAPPER_AFTERBURNER.createArrayNode();
    arrayNode.addAll(StreamSupport.stream(node.spliterator(), false).toList());

    List<JsonNode> list1 = StreamSupport.stream(arrayNode.spliterator(), false).toList();
    List<String> nodes = List.of(arrayNode.get(0).asText().split(":"));

    AtomicReference<AddressTag> addressTagAtomic = new AtomicReference<>(
        new AddressTag(
            Kind.valueOf(Integer.parseInt(nodes.get(0))),
            new PublicKey(nodes.get(1))
        ));

    if (nodes.size() > 2) {
      addressTagAtomic.set(
          new AddressTag(
              Kind.valueOf(Integer.parseInt(nodes.get(0))),
              new PublicKey(nodes.get(1)),
              new IdentifierTag(nodes.get(2))));
    }

    if (list1.size() < 2)
      return (T) addressTagAtomic.get();

    Optional.ofNullable(list1.get(1)).ifPresent(s ->
        addressTagAtomic.set(
            new AddressTag(
                Kind.valueOf(Integer.parseInt(nodes.getFirst())),
                new PublicKey(nodes.get(1)),
                new IdentifierTag(nodes.get(2)),
                new Relay(s.asText().replaceAll("^\"", "")))));

    return (T) addressTagAtomic.get();
  }
}
