package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.user.Identity;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeDefinitionEvent extends BaseEvent {
  @Getter
  private final IdentifierTag identifierTag;

  @Getter
  private final ReferenceTag referenceTag;

  public BadgeDefinitionEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull ReferenceTag referenceTag,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.BADGE_DEFINITION_EVENT, List.of(identifierTag, referenceTag), content);
    this.identifierTag = identifierTag;
    this.referenceTag = referenceTag;
  }
}
