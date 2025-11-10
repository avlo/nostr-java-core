package com.prosilion.nostr.event.internal;

import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import java.util.List;
import org.springframework.lang.NonNull;

public record EventTagAddressTagPair(@NonNull EventTag eventTag, @NonNull AddressTag addressTag) {
  public List<BaseTag> getTags() {
    return List.of(eventTag, addressTag);
  }
}
