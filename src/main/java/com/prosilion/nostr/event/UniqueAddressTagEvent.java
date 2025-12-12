package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;

public abstract class UniqueAddressTagEvent extends BaseEvent {
  public static final long ADDRESS_TAG_COUNT_LIMIT = 1L;
  public static final String LIMIT = String.format("List<BaseTag> should contain [%s] AddressTag but instead has", ADDRESS_TAG_COUNT_LIMIT);
  public static final String CONCAT = Strings.concat(LIMIT, " [%s]: %s");

  public UniqueAddressTagEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull AddressTag addressTag,
      @NonNull String content) throws NostrException {
    this(identity, kind, addressTag, List.of(), content);
  }

  public UniqueAddressTagEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull AddressTag addressTag,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        kind,
        validateSingleUniqueAddressTag(
            Stream.concat(
                Stream.of(addressTag),
                baseTags.stream()).toList()),
        content);
  }

  public UniqueAddressTagEvent(@NonNull GenericEventRecord genericEventRecord) throws NostrException {
    super(genericEventRecord);
  }

  public AddressTag getAddressTag() {
    return getTypeSpecificTags(AddressTag.class).getFirst();
  }

  private static List<BaseTag> validateSingleUniqueAddressTag(List<BaseTag> baseTags) {
    long count = baseTags.stream().filter(AddressTag.class::isInstance).count();
    assert Objects.equals(ADDRESS_TAG_COUNT_LIMIT, count) : new NostrException(
        String.format(CONCAT, count, baseTags));
    return baseTags;
  }
}
