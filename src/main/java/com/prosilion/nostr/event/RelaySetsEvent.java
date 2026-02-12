package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelaysTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class RelaySetsEvent extends BaseEvent {

  public RelaySetsEvent(@NonNull Identity identity, @NonNull IdentifierTag identifierTag, @NonNull String content, @NonNull RelaysTag... relaysTags) throws NostrException {
    this(identity, identifierTag, List.of(relaysTags), content);
  }

  public RelaySetsEvent(@NonNull Identity identity, @NonNull IdentifierTag identifierTag, @NonNull List<RelaysTag> relaysTags, @NonNull String content) throws NostrException {
    this(identity, identifierTag, relaysTags, List.of(), content);
  }

  public RelaySetsEvent(@NonNull Identity identity, @NonNull IdentifierTag identifierTag, @NonNull List<RelaysTag> relaysTags, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.RELAY_SETS,
        Stream.concat(
            Stream.concat(
                Stream.of(identifierTag),
                relaysTags.stream()),
            tags.stream()),
        content);
  }

  public RelaySetsEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
