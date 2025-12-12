package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class RelaySetsEvent extends AddressableEvent {

  public RelaySetsEvent(@NonNull Identity identity, @NonNull IdentifierTag identifierTag, @NonNull String content, @NonNull RelayTag... relayTags) throws NostrException {
    this(identity, identifierTag, List.of(relayTags), content);
  }

  public RelaySetsEvent(@NonNull Identity identity, @NonNull IdentifierTag identifierTag, @NonNull List<RelayTag> relayTags, @NonNull String content) throws NostrException {
    this(identity, identifierTag, relayTags, List.of(), content);
  }

  public RelaySetsEvent(@NonNull Identity identity, @NonNull IdentifierTag identifierTag, @NonNull List<RelayTag> relayTags, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException {
    super(identity, Kind.RELAY_SETS, identifierTag,
        Stream.concat(
                relayTags.stream(),
                tags.stream())
            .collect(Collectors.toList()),
        content);
  }

  public RelaySetsEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
