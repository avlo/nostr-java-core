package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.GenericTag;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.lang.NonNull;

public class CanonicalAuthenticationEvent extends BaseEvent {
  public CanonicalAuthenticationEvent(@NonNull Identity identity, @NonNull String challenge, @NonNull Relay relay) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.CLIENT_AUTH,
        List.of(
            GenericTag.create("challenge", challenge),
            GenericTag.create("relay", relay.getUri())));
  }
}
