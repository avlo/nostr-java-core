package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import org.springframework.lang.NonNull;

public class AddressableEvent extends BaseEvent {

  public AddressableEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull List<BaseTag> baseTags, @NonNull String content) throws NostrException {
    super(identity, validateKind(kind, intPredicate, errorMessage), baseTags, content);
  }

  public AddressableEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }

  private static final IntPredicate intPredicate = kindValue -> !(30_000 > kindValue || kindValue > 40_000);
  private static final Function<Kind, String> errorMessage = kind -> String.format("Intended AddressableEvent invalid kind [%s] value [%s] is not between 30000 and 40000", kind, kind.getValue());
}
