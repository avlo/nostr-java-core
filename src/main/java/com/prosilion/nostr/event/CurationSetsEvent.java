package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class CurationSetsEvent extends ReplaceableEvent {
  public CurationSetsEvent(@NonNull Identity identity, @NonNull PublicKey publicKey, @NonNull List<CurationSet> curationSets, @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.CURATION_SETS,
        Stream.concat(
                curationSets.stream()
                    .map(CurationSet::getTags)
                    .flatMap(Collection::stream),
                Stream.of(
                    new IdentifierTag(publicKey.toHexString())))
            .collect(Collectors.toList()),
        content);
  }

  public record CurationSet(@NonNull EventTag eventTag, @NonNull AddressTag addressTag) {
    public List<BaseTag> getTags() {
      return List.of(eventTag, addressTag);
    }
  }
}
