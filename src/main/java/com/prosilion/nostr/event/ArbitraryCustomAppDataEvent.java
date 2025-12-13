package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import org.springframework.lang.NonNull;

public class ArbitraryCustomAppDataEvent extends AddressableEvent {
  public ArbitraryCustomAppDataEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull String content) throws NostrException {
    this(identity, identifierTag, relay, List.of(), content);
  }

  public ArbitraryCustomAppDataEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull Relay relay,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.ARBITRARY_CUSTOM_APP_DATA,
        identifierTag,
        relay,
        baseTags,
        content);
  }

  public ArbitraryCustomAppDataEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
