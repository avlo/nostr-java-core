package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import org.springframework.lang.NonNull;

public class ArbitraryCustomAppDataEvent extends AddressableEvent {
  public ArbitraryCustomAppDataEvent(
      @NonNull Identity identity,
      @NonNull String content) throws NostrException {
    this(identity, List.of(), content);
  }

  public ArbitraryCustomAppDataEvent(
      @NonNull Identity identity,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.ARBITRARY_CUSTOM_APP_DATA,
        baseTags,
        content);
  }

  public ArbitraryCustomAppDataEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
