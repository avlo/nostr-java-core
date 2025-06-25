package com.prosilion.nostr.filter;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.codec.IDecoder;
import com.prosilion.nostr.filter.event.AuthorFilter;
import com.prosilion.nostr.filter.event.EventFilter;
import com.prosilion.nostr.filter.event.KindFilter;
import com.prosilion.nostr.filter.event.SinceFilter;
import com.prosilion.nostr.filter.event.UntilFilter;
import com.prosilion.nostr.filter.tag.AddressTagFilter;
import com.prosilion.nostr.filter.tag.GenericTagQueryFilter;
import com.prosilion.nostr.filter.tag.GeohashTagFilter;
import com.prosilion.nostr.filter.tag.HashtagTagFilter;
import com.prosilion.nostr.filter.tag.IdentifierTagFilter;
import com.prosilion.nostr.filter.tag.ReferencedEventFilter;
import com.prosilion.nostr.filter.tag.ReferencedPublicKeyFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;
import java.util.stream.StreamSupport;
import lombok.SneakyThrows;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;

public class FilterableProvider {
  public static List<Filterable> getFilterFunction(@NonNull String type, @NonNull JsonNode node) {
    return switch (type) {
      case ReferencedPublicKeyFilter.FILTER_KEY -> getFilterable(node, ReferencedPublicKeyFilter.fxn);
      case ReferencedEventFilter.FILTER_KEY -> getFilterable(node, ReferencedEventFilter.fxn);
      case IdentifierTagFilter.FILTER_KEY -> getFilterable(node, IdentifierTagFilter.fxn);
      case AddressTagFilter.FILTER_KEY -> getFilterableMulti(node, AddressTagFilter.fxn);
      case GeohashTagFilter.FILTER_KEY -> getFilterable(node, GeohashTagFilter.fxn);
      case HashtagTagFilter.FILTER_KEY -> getFilterable(node, HashtagTagFilter.fxn);
//      case VoteTagFilter.FILTER_KEY -> getFilterable(node, VoteTagFilter.fxn);
      case AuthorFilter.FILTER_KEY -> getFilterable(node, AuthorFilter.fxn);
      case EventFilter.FILTER_KEY -> getFilterable(node, EventFilter.fxn);
      case KindFilter.FILTER_KEY -> getFilterable(node, KindFilter.fxn);
      case SinceFilter.FILTER_KEY -> SinceFilter.fxn.apply(node);
      case UntilFilter.FILTER_KEY -> UntilFilter.fxn.apply(node);
      default -> getFilterable(node, GenericTagQueryFilter.fxn(type));
    };
  }

  private static List<Filterable> getFilterable(JsonNode jsonNode, Function<JsonNode, Filterable> filterFunction) {
    return StreamSupport.stream(jsonNode.spliterator(), false).map(filterFunction).toList();
  }

  @SneakyThrows
  private static List<Filterable> getFilterableMulti(JsonNode jsonNode, Function<JsonNode, Filterable> filterFunction) {
    List<Filterable> list = new ArrayList<>();
    jsonNode(jsonNode).iterator().forEachRemaining(node ->
        list.add(filterFunction.apply(node)));
    return list;
  }
// TODO: likely proper other class/location candidate for below, plus- hack-ey
  private static JsonNode jsonNode(JsonNode jsonNode) throws JsonProcessingException {
    return (jsonNode.toString().startsWith("[[")) ?
        jsonNode
        :
        IDecoder.I_DECODER_MAPPER_AFTERBURNER.readTree(Strings.concat(Strings.concat("[", jsonNode.toString()), "]"));
  }
}
