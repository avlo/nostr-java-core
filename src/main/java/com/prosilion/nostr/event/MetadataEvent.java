package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.ReferenceTag;
import com.prosilion.nostr.user.Identity;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class MetadataEvent extends BaseEvent {

  public MetadataEvent(@NonNull Identity identity, @NonNull String content, @NonNull ReferenceTag... referenceTag) throws NostrException, NoSuchAlgorithmException {
    this(identity, List.of(referenceTag), content);
  }

  public MetadataEvent(@NonNull Identity identity, @NonNull List<ReferenceTag> referenceTag, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    this(identity, referenceTag, List.of(), content);
  }

  public MetadataEvent(@NonNull Identity identity, @NonNull List<ReferenceTag> referenceTag, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.RELAY_LIST_METADATA,
        Stream.concat(
                referenceTag.stream(),
                tags.stream())
            .collect(Collectors.toList()),
        content);
  }
}
