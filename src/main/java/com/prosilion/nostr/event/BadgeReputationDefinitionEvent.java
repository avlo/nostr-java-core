package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.ExternalIdentityTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

public class BadgeReputationDefinitionEvent extends BaseEvent {
  @JsonIgnore @Getter IdentifierTag identifierTag;
  @JsonIgnore @Getter ReferenceTag referenceTag;
  @JsonIgnore @Getter PubKeyTag referencePubkeyTag;
  @JsonIgnore @Getter List<ExternalIdentityTag> externalIdentityTags;

  public BadgeReputationDefinitionEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull ReferenceTag referenceTag,
      @NonNull PubKeyTag referencePubkeyTag,
      @NonNull List<ExternalIdentityTag> externalIdentityTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.BADGE_DEFINITION_EVENT,
        Stream.concat(
                Stream.of(identifierTag, referenceTag, referencePubkeyTag),
                externalIdentityTags.stream())
            .collect(Collectors.toList()),
        content);
    this.identifierTag = identifierTag;
    this.referenceTag = referenceTag;
    this.referencePubkeyTag = referencePubkeyTag;
    this.externalIdentityTags = externalIdentityTags;
  }
}
