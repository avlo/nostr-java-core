package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class FollowSetsEvent extends BaseEvent {
  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<EventTagAddressTagPair> pairedTags,
      @NonNull String content) throws NostrException {
    this(identity, recipientPublicKey, identifierTag, pairedTags, List.of(), content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey recipientPublicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<EventTagAddressTagPair> pairedTags,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.FOLLOW_SETS,
        Stream.concat(
                Stream.concat(
                    Stream.concat(
                        pairedTags.stream()
                            .map(EventTagAddressTagPair::getTags)
                            .flatMap(Collection::stream),
                        Stream.of(
                            new PubKeyTag(recipientPublicKey))),
                    Stream.of(identifierTag)),
                baseTags.stream()
                    .filter(Predicate.not(EventTag.class::isInstance))
                    .filter(Predicate.not(AddressTag.class::isInstance)))
            .collect(Collectors.toList()),
        content);
  }

  public FollowSetsEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }

  //  TODO: potentially replace w/ CurationSetsEvent, needs further investigation
  public record EventTagAddressTagPair(@NonNull EventTag eventTag, @NonNull AddressTag addressTag) {
    public List<BaseTag> getTags() {
      return List.of(eventTag, addressTag);
    }
  }
}
