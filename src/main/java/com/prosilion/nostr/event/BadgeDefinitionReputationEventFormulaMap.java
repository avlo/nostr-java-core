package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.tag.IdentifierTag;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import org.springframework.lang.NonNull;

public record BadgeDefinitionReputationEventFormulaMap(@NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent) {
  @JsonIgnore
  public Map<BadgeDefinitionReputationEvent, List<String>> asMap() {
    return fxn.apply(badgeDefinitionReputationEvent);
  }

  @JsonIgnore
  public Map<BadgeDefinitionReputationEvent, List<String>> asMapWithAccumulator(@NonNull BadgeDefinitionReputationEventFormulaMap other) {
    asMapWithAccumulator(other.asMap());
    return other.asMap();
  }

  @JsonIgnore
  public Map<BadgeDefinitionReputationEvent, List<String>> asMapWithAccumulator(@NonNull Map<BadgeDefinitionReputationEvent, List<String>> other) {
    other.putIfAbsent(badgeDefinitionReputationEvent, getUuids(badgeDefinitionReputationEvent));
    return other;
  }
  
  private static final Function<BadgeDefinitionReputationEvent, Map<BadgeDefinitionReputationEvent, List<String>>> fxn =
      event -> Map.of(event, getUuids(event));

  private static List<String> getUuids(BadgeDefinitionReputationEvent badgeDefinitionReputationEvent) {
    return badgeDefinitionReputationEvent.getArbitraryCustomAppDataFormulaEvents().stream()
        .map(ArbitraryCustomAppDataFormulaEvent::getIdentifierTag)
        .map(IdentifierTag::getUuid)
        .toList();
  }
}
