package com.prosilion.nostr.event.internal;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.PubKeyTag;
import java.util.Objects;
import org.springframework.lang.NonNull;

public record AwardEvent(AddressTag addressTag, PubKeyTag pubkeyTag) {
  public AwardEvent(@NonNull AddressTag addressTag, @NonNull PubKeyTag pubkeyTag) {
    assert Objects.equals(Kind.BADGE_DEFINITION_EVENT, addressTag.getKind()) :
        new IllegalStateException(String.format("invalid badge award event (address tag) kind [%s], must be of Kind.BADGE_DEFINITION_EVENT", addressTag.getKind()));
    this.addressTag = addressTag;
    this.pubkeyTag = pubkeyTag;
  }
}
