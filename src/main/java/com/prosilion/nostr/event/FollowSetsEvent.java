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
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class FollowSetsEvent extends BaseEvent {
  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey publicKey,
      @NonNull List<EventTagAddressTagPair> pairedTags,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(
        identity,
        Kind.FOLLOW_SETS,
        Stream.concat(
                pairedTags.stream()
                    .map(EventTagAddressTagPair::getTags)
                    .flatMap(Collection::stream),
                Stream.of(
                    new PubKeyTag(publicKey)))
            .collect(Collectors.toList()),
        content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey publicKey,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<EventTagAddressTagPair> pairedTags,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(
        identity,
        Kind.FOLLOW_SETS,
        Stream.concat(
                Stream.concat(
                    pairedTags.stream()
                        .map(EventTagAddressTagPair::getTags)
                        .flatMap(Collection::stream),
                    Stream.of(
                        new PubKeyTag(publicKey))),
                Stream.of(identifierTag))
            .collect(Collectors.toList()),
        content);
  }

  public FollowSetsEvent(
      @NonNull Identity identity,
      @NonNull PublicKey publicKey,
      @NonNull List<EventTagAddressTagPair> pairedTags,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(
        identity,
        Kind.FOLLOW_SETS,
        Stream.concat(
                Stream.concat(
                    pairedTags.stream()
                        .map(EventTagAddressTagPair::getTags)
                        .flatMap(Collection::stream),
                    Stream.of(
                        new PubKeyTag(publicKey))),
                baseTags.stream()
                    .filter(Predicate.not(EventTag.class::isInstance))
                    .filter(Predicate.not(AddressTag.class::isInstance)))
            .collect(Collectors.toList()),
        content);
  }

  public record EventTagAddressTagPair(@NonNull EventTag eventTag, @NonNull AddressTag addressTag) {
    public List<BaseTag> getTags() {
      return List.of(eventTag, addressTag);
    }

//    @Override
//    public boolean equals(Object o) {
//      if (o == null || getClass() != o.getClass()) return false;
//      EventTagAddressTagPair that = (EventTagAddressTagPair) o;
//      boolean equals = Objects.equals(eventTag, that.eventTag);
//      boolean equals1 = Objects.equals(addressTag, that.addressTag);
//      return equals && equals1;
//    }
//
//    @Override
//    public int hashCode() {
//      return Objects.hash(eventTag, addressTag);
//    }
  }
}
