package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;

public class BadgeDefinitionGenericEvent extends AddressableEvent {
  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     Relay... relay) throws NostrException {
    this(identity, identifierTag, "", relay);
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull String content,
     Relay... relay) throws NostrException {
    this(identity, identifierTag, List.of(), content, relay);
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull List<BaseTag> baseTags) throws NostrException {
    this(identity, identifierTag, baseTags.stream(), "");
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     Relay... relay) throws NostrException {
    this(identity, identifierTag, baseTags.stream(), content, relay);
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull Stream<BaseTag> baseTags,
     @NonNull String content,
     Relay... relay) throws NostrException {
    this(
       identity,
       identifierTag,
       prependNullableRelayTagStream(baseTags, relay),
       content);
  }

  public BadgeDefinitionGenericEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull Stream<BaseTag> baseTags,
     @NonNull String content) throws NostrException {
    super(identity, Kind.BADGE_DEFINITION_EVENT, identifierTag, baseTags, content);
  }

  public BadgeDefinitionGenericEvent(@NonNull Identity identity, @NonNull GenericEventRecord genericEventRecord, @NonNull Relay backupRelay) {
    this(
       identity,
       genericEventRecord.requireFirstTag(IdentifierTag.class),
       genericEventRecord.getTags().stream(),
       genericEventRecord.getContent(),
       backupRelay);
  }

  public BadgeDefinitionGenericEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }

  public BadgeDefinitionGenericEvent createNewFromExisting(
     @NonNull Identity identity,
     @NonNull Relay appendRelay) {
    return new BadgeDefinitionGenericEvent(
       identity,
       this.getIdentifierTag(),
       this.getTags(),
       this.getContent(),
       appendRelay);
  }
}
