package com.prosilion.nostr.tag;

import lombok.NonNull;
import org.apache.commons.lang3.tuple.ImmutablePair;

public class TupleATagETag extends ImmutablePair<AddressTag, EventTag> {
  
  public TupleATagETag(@NonNull AddressTag left, @NonNull EventTag right) {
    super(left, right);
  }
}
