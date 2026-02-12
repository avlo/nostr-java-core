package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.RelaysTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class SearchRelaysListEvent extends ReplaceableEvent {

  public SearchRelaysListEvent(@NonNull Identity identity, @NonNull String content, @NonNull RelaysTag... relaysTags) throws NostrException {
    this(identity, List.of(relaysTags), content);
  }

  public SearchRelaysListEvent(@NonNull Identity identity, @NonNull List<RelaysTag> relaysTags, @NonNull String content) throws NostrException {
    this(identity, relaysTags, List.of(), content);
  }

  public SearchRelaysListEvent(@NonNull Identity identity, @NonNull List<RelaysTag> relaysTags, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException {
    super(identity, Kind.SEARCH_RELAYS_LIST,
        Stream.concat(
            relaysTags.stream(),
            tags.stream()),
        content);
  }

  public SearchRelaysListEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
