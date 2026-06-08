package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.RelaysTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.NonNull;

public class SearchRelaysListEvent extends ReplaceableEvent {

  public SearchRelaysListEvent(@NonNull Identity identity, @NonNull RelaysTag relaysTags, @NonNull String content) throws NostrException {
    this(identity, relaysTags, List.of(), content);
  }

  public SearchRelaysListEvent(@NonNull Identity identity, @NonNull RelaysTag relaysTags, @NonNull List<BaseTag> baseTags, @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.SEARCH_RELAYS_LIST,
        Stream.concat(
            Stream.of(relaysTags),
            baseTags.stream().filter(Predicate.not(RelaysTag.class::isInstance))),
        content);
  }

  public SearchRelaysListEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
