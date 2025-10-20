package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeDefinitionReputationEvent extends BadgeDefinitionAwardEvent {
  @JsonIgnore @Getter IdentifierTag identifierTag;
  @JsonIgnore @Getter List<ArbitraryCustomAppDataFormulaEvent> arbitraryCustomAppDataFormulaEvents;

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull ArbitraryCustomAppDataFormulaEvent arbitraryCustomAppDataFormulaEvent,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(arbitraryCustomAppDataFormulaEvent),
        List.of(),
        content);
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull ArbitraryCustomAppDataFormulaEvent arbitraryCustomAppDataFormulaEvent,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(arbitraryCustomAppDataFormulaEvent),
        baseTags,
        content);
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<ArbitraryCustomAppDataFormulaEvent> arbitraryCustomAppDataFormulaEvents,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        identifierTag,
        Stream.concat(
            arbitraryCustomAppDataFormulaEvents.stream()
                .map(ArbitraryCustomAppDataFormulaEvent::getId)
                .map(EventTag::new),
            baseTags.stream()).toList(),
        content);
    this.arbitraryCustomAppDataFormulaEvents =  arbitraryCustomAppDataFormulaEvents;
    this.identifierTag = identifierTag;
  }

  @JsonIgnore
  public List<EventTag> getEventTags() {
    return arbitraryCustomAppDataFormulaEvents.stream()
        .map(ArbitraryCustomAppDataFormulaEvent::getId)
        .map(EventTag::new).toList();
  }
}
