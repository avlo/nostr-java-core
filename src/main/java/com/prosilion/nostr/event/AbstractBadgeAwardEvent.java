package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.AwardEvent;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Getter
public abstract class AbstractBadgeAwardEvent<KindTypeIF> extends BaseEvent {
  private final KindTypeIF kindType;

  public AbstractBadgeAwardEvent(
      @NonNull KindTypeIF kindType,
      @NonNull Identity identity,
      @NonNull AwardEvent awardEvent,
      @NonNull String content) throws NostrException {
    super(identity, Kind.BADGE_AWARD_EVENT,
        Stream.concat(
                awardEvent.pubkeyTags().stream(),
                Stream.of(
                    awardEvent.addressTag()))
            .collect(Collectors.toList()),
        content);
    this.kindType = kindType;
  }

  public AbstractBadgeAwardEvent(
      @NonNull KindTypeIF kindType,
      @NonNull Identity identity,
      @NonNull AwardEvent awardEvent,
      @NonNull List<BaseTag> tags,
      @NonNull String content) throws NostrException {
    super(identity, Kind.BADGE_AWARD_EVENT,
        Stream.concat(
                Stream.concat(tags.stream(), Stream.of(awardEvent.addressTag())),
                awardEvent.pubkeyTags().stream())
            .toList(),
        content);
    this.kindType = kindType;
  }
}
