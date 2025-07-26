package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.KindTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class DeletionEvent extends BaseEvent {

  public DeletionEvent(@NonNull Identity identity, @NonNull List<EventTag> eventTags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION, new ArrayList<>(eventTags), content);
  }

  public DeletionEvent(@NonNull Identity identity, @NonNull String content, @NonNull List<AddressTag> addressTags) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION, new ArrayList<>(
            addressTags.stream().map(
                DeletionEvent::validatePubkeyMatch).toList()),
        content);
  }

  public DeletionEvent(@NonNull Identity identity, @NonNull List<KindTag> kindTags, @NonNull List<EventTag> eventTags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION,
        Stream.concat(
            kindTags.stream(),
            eventTags.stream()).collect(Collectors.toList()), content);
  }

  public DeletionEvent(@NonNull Identity identity, @NonNull List<KindTag> kindTags, @NonNull String content, @NonNull List<AddressTag> addressTags) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION,
        Stream.concat(
            kindTags.stream(),
            addressTags.stream().map(
                DeletionEvent::validatePubkeyMatch)).collect(Collectors.toList()), content);
  }

  public DeletionEvent(@NonNull Identity identity, @NonNull List<KindTag> kindTags, @NonNull List<EventTag> eventTags, @NonNull List<AddressTag> addressTags, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    super(identity, Kind.DELETION,
        Stream.concat(
            Stream.concat(
                kindTags.stream(),
                eventTags.stream()),
            addressTags.stream().map(
                DeletionEvent::validatePubkeyMatch)).collect(Collectors.toList()), content);
  }

  private static final BiPredicate<AddressTag, PublicKey> addressTagPredicate = (addressTag, publicKey) ->
      addressTag.getPublicKey().equals(publicKey);

  private static final BiFunction<AddressTag, PublicKey, String> errorMessage = (addressTag, publicKey) -> String.format("Deletion Event contains an AddressTag with PublicKey [%s] that does not match event-creator's PublicKey [%s]", addressTag.getPublicKey().toString(), publicKey.toString());

//  TODO: revisit below- commented impl may not match NIP-09 requirement
  protected static AddressTag validatePubkeyMatch(@NonNull AddressTag addressTag) {
//    assert ((Predicate<PublicKey>) DeletionEvent.addressTagPredicate).test(addressTag.getPublicKey()) : ((Function<AddressTag, String>) DeletionEvent.errorMessage).apply(addressTag);
    return addressTag;
  }
}
