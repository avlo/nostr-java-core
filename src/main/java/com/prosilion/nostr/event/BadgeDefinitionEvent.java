package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.lang.NonNull;

public class BadgeDefinitionEvent extends BaseEvent {
  public BadgeDefinitionEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.BADGE_DEFINITION_EVENT, List.of(identifierTag), content);
  }
}
