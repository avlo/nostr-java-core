package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.NostrException;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.tag.BaseTag;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import org.springframework.lang.NonNull;

public class ContactListEvent extends BaseEvent {
  public ContactListEvent(@NonNull Identity identity, @NonNull List<BaseTag> tags) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.CONTACT_LIST, tags);
  }
}
