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
  @JsonIgnore @Getter List<EventTag> eventTags;

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull EventTag eventTags,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(eventTags),
        List.of(),
        content);
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<EventTag> eventTags,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        eventTags,
        List.of(),
        content);
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<EventTag> eventTags,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        identifierTag,
        Stream.concat(eventTags.stream(), baseTags.stream()).toList(),
        content);
    this.identifierTag = identifierTag;
    this.eventTags = eventTags;
  }
}
