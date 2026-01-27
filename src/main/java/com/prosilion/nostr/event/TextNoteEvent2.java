package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import org.springframework.lang.NonNull;

public class TextNoteEvent2 extends TextNoteEvent {

  public TextNoteEvent2(@NonNull Identity identity, @NonNull String content) throws NostrException {
    this(identity, List.of(), content);
  }

  public TextNoteEvent2(@NonNull Identity identity, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException {
    super(identity, tags, content);
  }

  @JsonIgnore
  public TextNoteEvent2(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
