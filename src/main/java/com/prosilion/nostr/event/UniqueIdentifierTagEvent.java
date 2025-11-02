package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public abstract class UniqueIdentifierTagEvent extends AddressableEvent {

  public UniqueIdentifierTagEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    this(
        identity,
        kind,
        Stream.concat(
            Stream.of(identifierTag),
            baseTags.stream()).toList(),
        content);
  }

  public UniqueIdentifierTagEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        kind,
        validateSingleUniqueIdentifierTag(baseTags),
        content);
  }

  protected static List<BaseTag> validateSingleUniqueIdentifierTag(List<BaseTag> baseTags) {
    long count = baseTags.stream().filter(IdentifierTag.class::isInstance).count();
    long limit = 1L;
    assert Objects.equals(limit, count) : new NostrException(
        String.format("%s List<BaseTag> should contain [%s] IdentifierTag but instead has [%s]: %s", UniqueIdentifierTagEvent.class.getName(), limit, count, baseTags));
    return baseTags;
  }
}
