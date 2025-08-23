package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class RelaySetsEvent extends ReplaceableEvent {

  public RelaySetsEvent(@NonNull Identity identity, @NonNull String content, @NonNull RelayTag... relayTags) throws NostrException, NoSuchAlgorithmException {
    this(identity, List.of(relayTags), content);
  }

  public RelaySetsEvent(@NonNull Identity identity, @NonNull List<RelayTag> relayTags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    this(identity, relayTags, List.of(), content);
  }

  public RelaySetsEvent(@NonNull Identity identity, @NonNull List<RelayTag> relayTags, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.RELAY_SETS,
        Stream.concat(
                relayTags.stream(),
                tags.stream())
            .collect(Collectors.toList()),
        content);
  }
}
