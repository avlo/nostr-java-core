package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class RelayListMetadataEvent extends ReplaceableEvent {

  public RelayListMetadataEvent(@NonNull Identity identity, @NonNull String content, @NonNull ReferenceTag... referenceTag) throws NostrException {
    this(identity, List.of(referenceTag), content);
  }

  public RelayListMetadataEvent(@NonNull Identity identity, @NonNull List<ReferenceTag> referenceTag, @NonNull String content) throws NostrException {
    this(identity, referenceTag, List.of(), content);
  }

  public RelayListMetadataEvent(@NonNull Identity identity, @NonNull List<ReferenceTag> referenceTag, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException {
    super(identity, Kind.RELAY_LIST_METADATA,
        Stream.concat(
                referenceTag.stream(),
                tags.stream())
            .collect(Collectors.toList()),
        content);
  }

  public RelayListMetadataEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
