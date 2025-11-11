//package com.prosilion.nostr.filter.tag;
//
//import com.fasterxml.jackson.databind.JsonNode;
//import com.prosilion.nostr.event.EventIF;
//import com.prosilion.nostr.event.internal.Relay;
//import com.prosilion.nostr.filter.AbstractFilterable;
//import com.prosilion.nostr.filter.Filterable;
//import com.prosilion.nostr.tag.RelaysTag;
//import java.net.URI;
//import java.util.function.Function;
//import java.util.function.Predicate;
//import lombok.EqualsAndHashCode;
//
//@EqualsAndHashCode(callSuper = true)
//public class RelaysTagFilter extends AbstractFilterable<RelaysTag> {
//  public static final String FILTER_KEY = "relays";
//
//  public RelaysTagFilter(RelaysTag relaysTag) {
//    super(relaysTag, FILTER_KEY);
//  }
//
/// /  public Predicate<EventIF> getPredicateOld() {
/// /    return (genericEvent) ->
/// /        Filterable.getTypeSpecificTags(RelaysTag.class, genericEvent).stream()
/// /            .anyMatch(relaysTag ->
/// /                relaysTag.getUri().toString().equals(getFilterableValue()));
/// /  }
//
//  @Override
//  public Predicate<EventIF> getPredicate() {
//    return (genericEvent) ->
//        Filterable.getTypeSpecificTags(RelaysTag.class, genericEvent).stream()
//            .anyMatch(relaysTag ->
//                relaysTag.getRelays().stream()
//                    .map(Relay::getUri)
//                    .map(URI::toString).toList().contains(getFilterableValue()));
//  }
//
//  @Override
//  public String getFilterableValue() {
//    return getRelaysTag().getUri().toString();
//  }
//
//  private RelaysTag getRelaysTag() {
//    return super.getFilterable();
//  }
//
//  public static Function<JsonNode, Filterable> fxn = node -> new RelaysTagFilter(new RelaysTag(node.asText()));
//}
