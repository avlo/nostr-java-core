package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.NostrException;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.event.internal.ClassifiedListing;
import com.prosilion.nostr.tag.BaseTag;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class ClassifiedListingEvent extends AddressableEvent {
  public ClassifiedListingEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull ClassifiedListing classifiedListing, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    this(identity, kind, classifiedListing, List.of(), content);
  }

  public ClassifiedListingEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull ClassifiedListing classifiedListing, @NonNull List<BaseTag> baseTags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, validateKind(kind, kindPredicate, errorMessage), Stream.concat(baseTags.stream(), Stream.of(classifiedListing.getPriceTag())).toList(), content);
  }

  private static final IntPredicate kindPredicate = kindValue -> 30_402 == kindValue || kindValue == 30403;
  private static final Function<Kind, String> errorMessage = kind -> String.format("Invalid kind value [%s]. Classified Listing must be either 30402 or 30403 but was [%s]", kind, kind.getValue());
}
