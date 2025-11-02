package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;

public class BadgeDefinitionAwardEvent extends UniqueIdentifierTagEvent {
  public BadgeDefinitionAwardEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag) throws NostrException {
    this(identity, identifierTag, "");
  }

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
        identifierTag,
        baseTags,
        content);
  }

  @Override
  public boolean equals(Object obj) {
    if (!obj.getClass().getSuperclass().isAssignableFrom(BadgeDefinitionAwardEvent.class))
      return false;

    BadgeDefinitionAwardEvent that = (BadgeDefinitionAwardEvent) obj;
    return
        Objects.equals(this.getPublicKey(), that.getPublicKey()) &&
        Objects.equals(this.getIdentifierTag(), that.getIdentifierTag()) &&
        Objects.equals(this.getContent(), that.getContent());
  }

  public IdentifierTag getIdentifierTag() {
    return Filterable.getTypeSpecificTagsStream(IdentifierTag.class, this).findFirst().orElseThrow();
  }
}
