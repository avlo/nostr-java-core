package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.GenericTag;
import com.prosilion.nostr.user.Identity;
import java.util.stream.Stream;
import lombok.NonNull;

public class CanonicalAuthenticationEvent extends BaseEvent {
  public CanonicalAuthenticationEvent(@NonNull Identity identity, @NonNull String challenge, @NonNull Relay relay) throws NostrException {
    super(identity, Kind.CLIENT_AUTH,
        Stream.of(
            GenericTag.create("challenge", challenge),
            GenericTag.create("relay", relay.getUrl())));
  }

  public CanonicalAuthenticationEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
