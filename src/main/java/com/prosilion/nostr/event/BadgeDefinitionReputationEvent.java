package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class BadgeDefinitionReputationEvent extends BadgeDefinitionAwardEvent {

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull EventTag eventTag) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(eventTag));
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<EventTag> eventTags) throws NostrException {
    this(
        identity,
        identifierTag,
        eventTags,
        "");
  }

  public BadgeDefinitionReputationEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull EventTag eventTag,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(eventTag),
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
      @NonNull EventTag eventTag,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    this(
        identity,
        identifierTag,
        List.of(eventTag),
        baseTags,
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
        Stream.concat(
            eventTags.stream(),
            baseTags.stream()).toList(),
        content);
  }
}
