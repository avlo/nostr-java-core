package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class AddressableEvent extends BaseEvent {
  public AddressableEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    this(identity, kind, identifierTag, relay, baseTags.stream(), content);
  }

  public AddressableEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull Stream<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        kind,
        Stream.concat(
            Stream.concat(
                Stream.of(identifierTag),
                Stream.of(new RelayTag(relay))),
            baseTags
                .filter(Predicate.not(RelayTag.class::isInstance)
                    .or(
                        Predicate.not(IdentifierTag.class::isInstance)))),
        content);
  }

  public AddressableEvent(@NonNull GenericEventRecord genericEventRecord) throws NostrException {
    super(genericEventRecord);
  }

  public IdentifierTag getIdentifierTag() {
    return getTypeSpecificTags(IdentifierTag.class).getFirst();
  }

  public AddressTag asAddressTag() {
    return new AddressTag(
        getKind(),
        getPublicKey(),
        getIdentifierTag(),
        Optional.of(getTypeSpecificTags(RelayTag.class)).orElseThrow(() ->
            new NostrException(String.format("%s is missing a RelayTag", getClass().getSimpleName()))).getFirst().getRelay());
  }

  private static final IntPredicate intPredicate = kindValue -> !(30_000 > kindValue || kindValue > 40_000);
  private static final Function<Kind, String> errorMessage = kind -> String.format("Intended AddressableEvent invalid kind [%s] value [%s] is not between 30000 and 40000", kind, kind.getValue());
}
