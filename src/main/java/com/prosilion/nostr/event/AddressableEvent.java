package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Stream;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;

public class AddressableEvent extends BaseEvent {
  public static final long IDENTIFIER_TAG_COUNT_LIMIT = 1L;
  public static final String LIMIT = String.format("List<BaseTag> should contain [%s] IdentifierTag but instead has", IDENTIFIER_TAG_COUNT_LIMIT);
  public static final String CONCAT = Strings.concat(LIMIT, " [%s]: %s");

  public AddressableEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull IdentifierTag identifierTag,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        kind,
        validateSingleUniqueIdentifierTag(
            Stream.concat(
                Stream.of(identifierTag),
                baseTags.stream()).toList()),
        content);
  }

  public AddressableEvent(@NonNull GenericEventRecord genericEventRecord) throws NostrException {
    super(genericEventRecord);
  }

  public IdentifierTag getIdentifierTag() {
    return getTypeSpecificTags(IdentifierTag.class).getFirst();
  }

  private static List<BaseTag> validateSingleUniqueIdentifierTag(List<BaseTag> baseTags) {
    long count = baseTags.stream().filter(IdentifierTag.class::isInstance).count();
    assert Objects.equals(IDENTIFIER_TAG_COUNT_LIMIT, count) : new NostrException(
        String.format(CONCAT, count, baseTags));
    return baseTags;
  }

  private static final IntPredicate intPredicate = kindValue -> !(30_000 > kindValue || kindValue > 40_000);
  private static final Function<Kind, String> errorMessage = kind -> String.format("Intended AddressableEvent invalid kind [%s] value [%s] is not between 30000 and 40000", kind, kind.getValue());
}
