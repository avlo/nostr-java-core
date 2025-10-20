package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeDefinitionAwardEvent extends AddressableEvent {
  @JsonIgnore @Getter IdentifierTag identifierTag;

  public BadgeDefinitionAwardEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull String content) throws NostrException {
    this(identity, identifierTag, List.of(), content);
  }

  public BadgeDefinitionAwardEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.BADGE_DEFINITION_EVENT,
        Stream.concat(
                Stream.of(identifierTag),
                baseTags.stream())
            .collect(Collectors.toList()),
        content);
    this.identifierTag = identifierTag;
  }

  public boolean matches(@NonNull BadgeDefinitionAwardEvent that) {
    if (!Objects.equals(
            this.getClass().isAssignableFrom(BadgeDefinitionAwardEvent.class),
            that.getClass().isAssignableFrom(BadgeDefinitionAwardEvent.class)))
      return false;
    
    return Objects.equals(this.getPublicKey(), that.getPublicKey()) &&
        Objects.equals(this.identifierTag, that.identifierTag);
  }
}
