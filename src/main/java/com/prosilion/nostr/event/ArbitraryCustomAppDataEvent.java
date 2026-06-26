package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import lombok.NonNull;

public class ArbitraryCustomAppDataEvent extends AddressableEvent {
  public ArbitraryCustomAppDataEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    this(identity, identifierTag, List.of(), content, relay);
  }

  public ArbitraryCustomAppDataEvent(
     @NonNull Identity identity,
     @NonNull IdentifierTag identifierTag,
     @NonNull List<BaseTag> baseTags,
     @NonNull String content,
     @NonNull Relay relay) throws NostrException {
    super(identity, Kind.ARBITRARY_CUSTOM_APP_DATA, identifierTag, baseTags, content, relay);
  }

//  public ArbitraryCustomAppDataEvent(
//     @NonNull Identity identity,
//     @NonNull IdentifierTag identifierTag,
//     @NonNull Stream<BaseTag> baseTags,
//     @NonNull String content,
//     @NonNull Relay relay) throws NostrException {
//    super(
//       identity,
//       Kind.ARBITRARY_CUSTOM_APP_DATA,
//       identifierTag,
//       baseTags.toList(), content, relay);
//  }

  public ArbitraryCustomAppDataEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
