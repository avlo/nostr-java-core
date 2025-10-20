package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.springframework.lang.NonNull;

public record BadgeDefinitionReputationEventFormulaMap(@NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent) {
  @JsonIgnore
  public Map<BadgeDefinitionAwardEvent, List<String>> asMap() {
    return Map.of(
        badgeDefinitionReputationEvent,
        badgeDefinitionReputationEvent.getArbitraryCustomAppDataFormulaEvents().stream()
            .map(ArbitraryCustomAppDataFormulaEvent::getFormula)
            .toList());
  }

  @JsonIgnore
  public Map<BadgeDefinitionAwardEvent, List<String>> asMapWithAccumulator(@NonNull BadgeDefinitionReputationEventFormulaMap other) {
    Map<BadgeDefinitionAwardEvent, List<String>> acc = new HashMap<>(other.asMap());
    acc.putAll(this.asMap());
    return acc;
  }

  @JsonIgnore
  public Map<BadgeDefinitionAwardEvent, List<String>> asMapWithAccumulator(@NonNull Map<BadgeDefinitionAwardEvent, List<String>> other) {
    other.putAll(this.asMap());
    return other;
  }
}
