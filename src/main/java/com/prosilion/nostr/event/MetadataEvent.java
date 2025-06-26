package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.tag.BaseTag;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.lang.NonNull;

public class MetadataEvent extends BaseEvent {

  public MetadataEvent(@NonNull Identity identity, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.SET_METADATA, content);
  }

  public MetadataEvent(@NonNull Identity identity, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.SET_METADATA, tags, content);
  }
}
