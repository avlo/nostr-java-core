package com.prosilion.nostr.event;

import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Stream;
import lombok.NonNull;

public class BadgeAwardGenericEvent<T extends BadgeDefinitionGenericEvent> extends BadgeAwardAbstractEvent<T> {

  public BadgeAwardGenericEvent(
     @NonNull Identity authorIdentity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull T badgeDefinitionGenericEvent) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericEvent, List.of());
  }

  public BadgeAwardGenericEvent(
     @NonNull Identity authorIdentity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull Relay relay) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericEvent, "", relay);
  }

  public BadgeAwardGenericEvent(
     @NonNull Identity authorIdentity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull List<BaseTag> tags) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericEvent, tags, "");
  }

  public BadgeAwardGenericEvent(
     @NonNull Identity authorIdentity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull List<BaseTag> tags,
     @NonNull Relay relay) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericEvent, tags, "", relay);
  }

  public BadgeAwardGenericEvent(
     @NonNull Identity authorIdentity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull String content,
     @NonNull Relay relay) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericEvent, List.of(), content, relay);
  }

  public BadgeAwardGenericEvent(
     @NonNull Identity authorIdentity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull List<BaseTag> tags,
     @NonNull String content) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericEvent, tags.stream(), content);
  }

  public BadgeAwardGenericEvent(
     @NonNull Identity authorIdentity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull List<BaseTag> tags,
     @NonNull String content,
     @NonNull Relay relay) {
    this(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericEvent,
       prependExplicitRelayTag(tags, relay).stream(), content);
  }

  public BadgeAwardGenericEvent(
     @NonNull Identity authorIdentity,
     @NonNull PublicKey awardRecipientPublicKey,
     @NonNull T badgeDefinitionGenericEvent,
     @NonNull Stream<BaseTag> tags,
     @NonNull String content) {
    super(authorIdentity, awardRecipientPublicKey, badgeDefinitionGenericEvent, tags, content);
  }

  public BadgeAwardGenericEvent(
     @NonNull GenericEventRecord genericEventRecord,
     @NonNull Function<AddressTag, T> fxn) {
    super(genericEventRecord, fxn);
  }
}
