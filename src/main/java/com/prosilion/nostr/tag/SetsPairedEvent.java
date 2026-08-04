package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.user.PublicKey;
import java.util.Objects;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;

@Slf4j
public class SetsPairedEvent {
  public static final String NULL_EVENT_TAG_RELAY = "SetsPairedEvent EventTag relay cannot be null";
  private final ATagETagPair aTagETagPair;

  public SetsPairedEvent(@NonNull AddressTag addressTag, @NonNull EventTag eventTag) {
    if (eventTag.findRelay().isEmpty())
      throw new NostrException(NULL_EVENT_TAG_RELAY);
    this.aTagETagPair = new ATagETagPair(addressTag, eventTag);
  }

  @JsonIgnore
  public final EventTag getEventTag() {
    return aTagETagPair.getRight();
  }

  @JsonIgnore
  public final String getEventTagEventId() {
    return getEventTag().getEventId();
  }

  @JsonIgnore
  public final Relay getEventTagRelay() {
    return getEventTag().requireRelay();
  }

  @JsonIgnore
  public final AddressTag getAddressTag() {
    return aTagETagPair.getLeft();
  }

  @JsonIgnore
  public final Kind getDefinitionEventKind() {
    return getAddressTag().getKind();
  }

  @JsonIgnore
  public final PublicKey getDefinitionEventCreatorPublicKey() {
    return getAddressTag().getPublicKey();
  }

  @JsonIgnore
  public final IdentifierTag getDefinitionEventIdentifierTag() {
    return getAddressTag().getIdentifierTag();
  }

  @JsonIgnore
  public final Relay getDefinitionEventRelay() {
    //    Util.debug(log, "relay: [{}]", orElse.getUrl(), true, '1');
    return getAddressTag().findRelay().orElse(getEventTag().requireRelay());
  }

  private static class ATagETagPair extends ImmutablePair<AddressTag, EventTag> implements Comparable<Pair<AddressTag, EventTag>> {
    public ATagETagPair(@NonNull AddressTag left, @NonNull EventTag right) {
      super(left, right);
    }

    @Override
    public final boolean equals(Object that) {
      if (that == null || getClass() != that.getClass()) return false;
      ATagETagPair thatATagETagPair = (ATagETagPair) that;
      return Objects.equals(left, thatATagETagPair.left) &&
         Objects.equals(right, thatATagETagPair.right);
    }
  }

  @Override
  public final boolean equals(Object that) {
    if (that == null || getClass() != that.getClass()) return false;
    SetsPairedEvent thatSetsPairedEvent = (SetsPairedEvent) that;
    return Objects.equals(aTagETagPair, thatSetsPairedEvent.aTagETagPair);
  }

  @Override
  public final int hashCode() {
    return Objects.hash(aTagETagPair);
  }
}
