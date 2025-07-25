package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.tag.BaseTag;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.lang.NonNull;

public class TextNoteEvent extends BaseEvent {

  public TextNoteEvent(@NonNull Identity identity, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.TEXT_NOTE, content);
  }

  public TextNoteEvent(@NonNull Identity identity, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.TEXT_NOTE, tags, content);
  }
}
