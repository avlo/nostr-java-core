package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import java.util.List;
import java.util.function.Function;
import org.springframework.lang.NonNull;

public interface AddressableTagsMappedEventsIF {
  default <T extends BaseEvent> List<T> mapAddressableTagsToEvents(
      @NonNull BaseEvent baseEvent,
      @NonNull Function<AddressTag, T> addressTagTFunction) {
    return baseEvent.getTypeSpecificTags(AddressTag.class)
        .stream()
        .map(addressTagTFunction).toList();
  }
  
  List<AddressTag> getBadgeDefinitionAwardEventsAsAddressTags();

  String getId();
  Kind getKind();
}
