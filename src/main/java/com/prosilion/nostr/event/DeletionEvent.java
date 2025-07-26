package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.KindTag;
import com.prosilion.nostr.user.Identity;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;

public class DeletionEvent extends BaseEvent {

  public DeletionEvent(@NonNull Identity identity, @NonNull List<EventTag> eventTags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION, new ArrayList<>(eventTags), content);
  }

  public DeletionEvent(@NonNull Identity identity, @NonNull String content, @NonNull List<AddressTag> addressTags) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION, new ArrayList<>(addressTags), content);
  }

  public DeletionEvent(@NonNull Identity identity, @NonNull List<KindTag> kindTags, @NonNull List<EventTag> eventTags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION,
        Stream.concat(
            kindTags.stream(),
            eventTags.stream()).collect(Collectors.toList()), content);
  }

  public DeletionEvent(@NonNull Identity identity, @NonNull List<KindTag> kindTags, @NonNull String content, @NonNull List<AddressTag> addressTags) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION,
        Stream.concat(
            kindTags.stream(),
            addressTags.stream()).collect(Collectors.toList()), content);
  }

  public DeletionEvent(@NonNull Identity identity, @NonNull List<KindTag> kindTags, @NonNull List<EventTag> eventTags, @NonNull List<AddressTag> addressTags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION,
        Stream.concat(
            Stream.concat(
                kindTags.stream(),
                eventTags.stream()),
            addressTags.stream()).collect(Collectors.toList()), content);
  }
}
