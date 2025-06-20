package com.prosilion.nostr.event.internal;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.PubKeyTag;
import java.util.List;
import java.util.Objects;

public record AwardEvent(AddressTag addressTag, List<PubKeyTag> pubkeyTags) {
  public AwardEvent(AddressTag addressTag, List<PubKeyTag> pubkeyTags) {
    assert Objects.equals(Kind.BADGE_DEFINITION_EVENT, addressTag.getKind()) :
        new IllegalStateException(String.format("invalid badge award event (address tag) kind [%s], must be of Kind.BADGE_DEFINITION_EVENT", addressTag.getKind()));
    this.addressTag = addressTag;

    assert !pubkeyTags.isEmpty() : new IllegalStateException("Pubkey list must not be empty");
    this.pubkeyTags = pubkeyTags;
  }

  public AwardEvent(AddressTag addressTag, PubKeyTag pubkeyTag) {
    this(addressTag, List.of(pubkeyTag));
  }
}
