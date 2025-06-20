package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.NostrException;
import com.prosilion.nostr.enums.Type;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public abstract class AbstractBadgeAwardEvent<T extends Type> extends BaseEvent {
  private final T type;

  public AbstractBadgeAwardEvent(
      @NonNull T type,
      @NonNull Identity identity, @NonNull List<BaseTag> tags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.BADGE_AWARD_EVENT, tags, content);
    this.type = type;
  }
}
