package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.HashMap;
import java.util.Map;
import org.springframework.lang.NonNull;

public record BadgeDefinitionAwardEventFormulaMap(
    @NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent,
    @NonNull String formula) {

  @JsonIgnore
  public Map<BadgeDefinitionAwardEvent, String> asMap() {
    return Map.of(badgeDefinitionReputationEvent, formula);
  }

  @JsonIgnore
  public Map<BadgeDefinitionAwardEvent, String> asMapWithAccumulator(@NonNull BadgeDefinitionAwardEventFormulaMap other) {
    Map<BadgeDefinitionAwardEvent, String> acc = new HashMap<>(other.asMap());
    acc.putIfAbsent(this.badgeDefinitionReputationEvent, this.formula);
    return acc;
  }

  @JsonIgnore
  public Map<BadgeDefinitionAwardEvent, String> asMapWithAccumulator(@NonNull Map<BadgeDefinitionAwardEvent, String> other) {
    other.putIfAbsent(this.badgeDefinitionReputationEvent, this.formula);
    return other;
  }
}
