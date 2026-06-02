package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import org.apache.logging.log4j.util.Strings;
import org.springframework.lang.NonNull;

public abstract class UniqueAddressTagEvent<T extends AddressableEvent> extends BaseEvent implements TagMappedEventIF {
  public static final long ADDRESS_TAG_COUNT_LIMIT = 1L;
  public static final String LIMIT = String.format("List<BaseTag> should contain [%s] AddressTag but instead has", ADDRESS_TAG_COUNT_LIMIT);
  public static final String CONCAT = Strings.concat(LIMIT, " [%s]: %s");

  @Getter
  @JsonIgnore
  private final T addressableEvent; // aTag

  public UniqueAddressTagEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull T addressableEvent,
      @NonNull String content) throws NostrException {
    this(identity, kind, addressableEvent, List.of(), content);
  }

  public UniqueAddressTagEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull T addressableEvent,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    this(identity, kind, addressableEvent, baseTags.stream(), content);
  }

  public UniqueAddressTagEvent(
      @NonNull Identity identity,
      @NonNull Kind kind,
      @NonNull T addressableEvent,
      @NonNull Stream<BaseTag> baseTags,
      @NonNull String content) throws NostrException {
    super(
        identity,
        kind,
        Stream.concat(
            Stream.of(addressableEvent.asAddressableEventAddressTag()),
            baseTags
                .filter(Predicate.not(AddressTag.class::isInstance))),
        content);
    this.addressableEvent = addressableEvent;
  }

  public UniqueAddressTagEvent(
      @NonNull GenericEventRecord genericEventRecord,
      @NonNull Function<AddressTag, T> fxn) {
    super(genericEventRecord);
    this.addressableEvent = mapTagsToEvents(this, fxn, AddressTag.class).getFirst();
  }

  @JsonIgnore
  public AddressTag getAddressTag() {
    return requireFirstTag(AddressTag.class);
  }
}
