package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.RelaysTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class RelaySetsEvent extends BaseEvent {
  public RelaySetsEvent(@NonNull Identity identity, @NonNull RelaysTag relaysTags, @NonNull String content) throws NostrException {
    this(identity, relaysTags, List.of(), content);
  }

  public RelaySetsEvent(@NonNull Identity identity, @NonNull RelaysTag relaysTags, @NonNull List<BaseTag> baseTags, @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.RELAY_SETS,
        Stream.concat(
            Stream.of(relaysTags),
            baseTags.stream().filter(Predicate.not(RelaysTag.class::isInstance))),
        content);
  }

  public RelaySetsEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
