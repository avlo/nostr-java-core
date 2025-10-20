package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeDefinitionReputationEvent extends BadgeDefinitionAwardEvent {
  @JsonIgnore @Getter List<BadgeDefinitionReputationFormulaEvent> badgeDefinitionReputationFormulaEvents;

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull BadgeDefinitionReputationFormulaEvent badgeDefinitionReputationFormulaEvent,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(badgeDefinitionReputationFormulaEvent),
        List.of(),
        content);
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull BadgeDefinitionReputationFormulaEvent badgeDefinitionReputationFormulaEvent,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(badgeDefinitionReputationFormulaEvent),
        baseTags,
        content);
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<BadgeDefinitionReputationFormulaEvent> badgeDefinitionReputationFormulaEvents,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        identifierTag,
        Stream.concat(
            badgeDefinitionReputationFormulaEvents.stream()
                .map(BadgeDefinitionReputationFormulaEvent::getId)
                .map(EventTag::new),
            baseTags.stream()).toList(),
        content);
    this.badgeDefinitionReputationFormulaEvents = badgeDefinitionReputationFormulaEvents;
  }

  @JsonIgnore
  public List<EventTag> getEventTags() {
    return badgeDefinitionReputationFormulaEvents.stream()
        .map(BadgeDefinitionReputationFormulaEvent::getId)
        .map(EventTag::new).toList();
  }

  public record BadgeDefinitionReputationEventFormulaMapUtil(@NonNull BadgeDefinitionReputationEvent badgeDefinitionReputationEvent) {
    @JsonIgnore
    public Map<BadgeDefinitionReputationEvent, List<String>> asFormulaUuidMap() {
      return fxn.apply(badgeDefinitionReputationEvent);
    }

    @JsonIgnore
    public Map<BadgeDefinitionReputationEvent, List<String>> asFormulaUuidMapWithAccumulator(@NonNull BadgeDefinitionReputationEventFormulaMapUtil other) {
      asFormulaUuidMapWithAccumulator(other.asFormulaUuidMap());
      return other.asFormulaUuidMap();
    }

    @JsonIgnore
    public Map<BadgeDefinitionReputationEvent, List<String>> asFormulaUuidMapWithAccumulator(@NonNull Map<BadgeDefinitionReputationEvent, List<String>> other) {
      other.putIfAbsent(badgeDefinitionReputationEvent, getUuids(badgeDefinitionReputationEvent));
      return other;
    }

    private static final Function<BadgeDefinitionReputationEvent, Map<BadgeDefinitionReputationEvent, List<String>>> fxn =
        event -> Map.of(event, getUuids(event));

    private static List<String> getUuids(BadgeDefinitionReputationEvent badgeDefinitionReputationEvent) {
      return badgeDefinitionReputationEvent.getBadgeDefinitionReputationFormulaEvents().stream()
          .map(BadgeDefinitionReputationFormulaEvent::getIdentifierTag)
          .map(IdentifierTag::getUuid)
          .toList();
    }
  }
}
