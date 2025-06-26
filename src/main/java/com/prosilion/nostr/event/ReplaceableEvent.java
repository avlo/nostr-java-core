package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.tag.BaseTag;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import org.springframework.lang.NonNull;

public class ReplaceableEvent extends BaseEvent {

  public ReplaceableEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, validateKind(kind, kindPredicate, errorMessage), tags, content);
  }

  private static final IntPredicate kindPredicate = kindValue -> kindValue == 0 || kindValue == 3 || 10_000 <= kindValue && kindValue < 20_000;
  private static final Function<Kind, String> errorMessage = kind -> String.format("Invalid kind [%s] has value [%s] is not between 10000 and 20000 or 0 or 3", kind, kind.getValue());
}
