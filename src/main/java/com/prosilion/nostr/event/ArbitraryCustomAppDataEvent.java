package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Objects;
import org.springframework.lang.NonNull;

public class ArbitraryCustomAppDataEvent extends UniqueIdentifierTagEvent {
  public ArbitraryCustomAppDataEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull String content) throws NostrException {
    this(identity, identifierTag, List.of(), content);
  }

  public ArbitraryCustomAppDataEvent(
      @NonNull Identity identity,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        Kind.ARBITRARY_CUSTOM_APP_DATA,
        identifierTag,
        baseTags,
        content);
  }

  public ArbitraryCustomAppDataEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }

  @Override
  public boolean equals(Object obj) {
    if (!Objects.equals(this.getClass(), obj.getClass()))
      return false;

    ArbitraryCustomAppDataEvent that = (ArbitraryCustomAppDataEvent) obj;
    return super.equals(that);
  }
}
