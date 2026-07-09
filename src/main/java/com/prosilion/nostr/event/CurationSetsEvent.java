package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.SetsPairedEvent;
import com.prosilion.nostr.tag.SetsPairedEventTagIF;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.NonNull;

public class CurationSetsEvent extends AbstractSetsEvent implements SetsPairedEventTagIF {
  public static final String DEFAULT_CONTENT = "AfterImage generated CurationSetsEvent";

//  @Getter
//  @JsonIgnore
//  protected final BadgeDefinitionGenericEvent badgeDefinitionGenericEvent;

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull Relay relay) {
    this(identity, badgeDefinitionGenericEvent, setsPairedEvent, List.of(), DEFAULT_CONTENT, relay);
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull List<BaseTag> baseTags,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionGenericEvent, setsPairedEvent, baseTags, DEFAULT_CONTENT, relay);
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, badgeDefinitionGenericEvent, setsPairedEvent, List.of(), content, relay);
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull Relay relay) throws NostrException {
    this(
       identity,
       identifierTag,
       setsPairedEvent,
       List.of(),
       DEFAULT_CONTENT,
       relay);
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(
       identity,
       badgeDefinitionGenericEvent.getIdentifierTag(),
       setsPairedEvent,
       baseTags,
       content,
       relay);
//    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull SetsPairedEvent setsPairedEvent,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(
       identity,
       setsPairedEvent.getAwardRecipientPublicKey(),
       identifierTag,
       setsPairedEvent.getAddressTag(),
       setsPairedEvent.getEventTag(),
       baseTags,
       content,
       relay);
//    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull PublicKey recipientPublicKey,
     @NonNull IdentifierTag identifierTag,
     @NonNull AddressTag addressTag,
     @NonNull EventTag eventTag,
     @NonNull Relay relay) throws NostrException {
    this(
       identity,
       recipientPublicKey,
       identifierTag,
       addressTag,
       eventTag,
       DEFAULT_CONTENT,
       relay);
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull PublicKey recipientPublicKey,
     @NonNull IdentifierTag identifierTag,
     @NonNull AddressTag addressTag,
     @NonNull EventTag eventTag,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, recipientPublicKey, identifierTag, addressTag, eventTag, List.of(), content, relay);
  }

  public CurationSetsEvent(
     @NonNull Identity identity,
     @NonNull PublicKey recipientPublicKey,
     @NonNull IdentifierTag identifierTag,
     @NonNull AddressTag addressTag,
     @NonNull EventTag eventTag,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(
       identity,
       Kind.CURATION_SETS,
       identifierTag,
       new SetsPairedEvent(addressTag, eventTag.findRelay().orElse(null), eventTag, recipientPublicKey),
       mapStream(
          identifierTag,
          new SetsPairedEvent(addressTag, eventTag.findRelay().orElse(null),
             eventTag,
             recipientPublicKey),
          baseTags),
       content,
       relay);
//    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
  }

  public CurationSetsEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(
       validateRequiredTags(
          genericEventRecord,
          List.of(
             IdentifierTag.class,
             PubKeyTag.class,
             AddressTag.class,
             EventTag.class)),
       new SetsPairedEvent(
          genericEventRecord.requireFirstTag(AddressTag.class),
          genericEventRecord.requireFirstTag(EventTag.class).findRelay().orElse(null),
          genericEventRecord.requireFirstTag(EventTag.class),
          genericEventRecord.requireFirstTag(PubKeyTag.class).getPublicKey()));
  }

//  public CurationSetsEvent(
//     @NonNull GenericEventRecord genericEventRecord,
//     @NonNull SetsPairedEvent setsPairedEvent,
//     @NonNull BadgeDefinitionGenericEvent badgeDefinitionGenericEvent) {
//    super(genericEventRecord, setsPairedEvent);

  /// /    this.badgeDefinitionGenericEvent = badgeDefinitionGenericEvent;
//  }
  public CurationSetsEvent createNewFromExisting(@NonNull Identity identity, @NonNull SetsPairedEvent addSetsPairedEvent) {
    return new CurationSetsEvent(
       identity,
       addSetsPairedEvent.getDefinitionEventIdentifierTag(),
       addSetsPairedEvent,
       getTags(),
       getContent(),
       getRelay().orElseThrow(() ->
          new NostrException("createNewFromExisting source CurationSetsEvent is missing a Relay")));
  }

  private static List<BaseTag> mapStream(
     IdentifierTag identifierTag,
     SetsPairedEvent setsPairedEvent,
     List<BaseTag> baseTags) {
    return Stream.concat(
       Stream.concat(
          Stream.of(
             new PubKeyTag(setsPairedEvent.getAwardRecipientPublicKey())),
          Stream.of(identifierTag)),
       baseTags.stream()
          .filter(Predicate.not(PubKeyTag.class::isInstance))
          .filter(Predicate.not(IdentifierTag.class::isInstance))).toList();
  }

  private static List<BaseTag> mapStreamGenericEventRecord(
     IdentifierTag identifierTag,
     PubKeyTag pubKeyTag,
     AddressTag addressTag,
     EventTag eventTag,
     List<BaseTag> baseTags) {
    return Stream.concat(
       Stream.of(identifierTag, pubKeyTag, addressTag, eventTag),
       baseTags.stream()
          .filter(Predicate.not(PubKeyTag.class::isInstance))
          .filter(Predicate.not(IdentifierTag.class::isInstance))).toList();
  }
}

