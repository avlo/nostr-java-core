package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Stream;
import lombok.NonNull;

public class ReplaceableEvent extends BaseEvent {

  public ReplaceableEvent(
     @NonNull Identity identity,
     @NonNull Kind kind,
     @NonNull List<BaseTag> tags,
     @NonNull String content) throws NostrException {
    super(identity, validateKind(kind, replaceableKindPredicate, errorMessage), tags, content);
  }

  public ReplaceableEvent(
     @NonNull Identity identity,
     @NonNull Kind kind,
     @NonNull Stream<BaseTag> tags,
     @NonNull String content) throws NostrException {
    super(identity, validateKind(kind, replaceableKindPredicate, errorMessage), tags, content);
  }

  public ReplaceableEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(validateKind(genericEventRecord));
  }

  private static final IntPredicate replaceableKindPredicate = kindValue ->
     kindValue == 0 ||
        kindValue == 3 ||
        (10_000 <= kindValue && kindValue < 20_000);

  private static final Function<Kind, String> errorMessage = kind -> String.format("Intended ReplaceableEvent invalid kind [%s] value [%s] is not between 10000 and 20000 or 0 or 3", kind, kind.getValue());

  public static GenericEventRecord validateKind(@NonNull GenericEventRecord genericEventRecord) {
    validateKind(genericEventRecord.getKind(), replaceableKindPredicate, errorMessage);
    return genericEventRecord;
  }
}
