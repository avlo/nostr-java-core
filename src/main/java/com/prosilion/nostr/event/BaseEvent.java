package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.RelayTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;
import java.util.function.IntPredicate;
import java.util.function.Predicate;
import java.util.stream.Stream;
import lombok.Getter;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;

public abstract class BaseEvent implements EventIF {
  @Getter
  private final GenericEventRecord genericEventRecord;

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind) throws NostrException {
    this(identity, kind, List.of());
  }

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull String content) throws NostrException {
    this(identity, kind, List.of(), content);
  }

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull List<BaseTag> tags) throws NostrException {
    this(identity, kind, tags, "");
  }

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull Stream<BaseTag> tags) throws NostrException {
    this(identity, kind, tags, "");
  }

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind, List<BaseTag> tags, String content) throws NostrException {
    this(identity, kind, tags.stream(), content);
  }

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind, Stream<BaseTag> tags, String content) throws NostrException {
    this.genericEventRecord = GenericEventRecordFactory.createInstance(identity, kind, tags.distinct().toList(), content);
  }

  public BaseEvent(@NonNull GenericEventRecord genericEventRecord) {
    this.genericEventRecord = genericEventRecord;
  }

  @Override
  public final String getContent() {
    return genericEventRecord.getContent();
  }

  @Override
  public final Long getCreatedAt() {
    return genericEventRecord.getCreatedAt();
  }

  @Override
  public final String getId() {
    return genericEventRecord.getId();
  }

  @Override
  public final Kind getKind() {
    return genericEventRecord.getKind();
  }

  @Override
  public final PublicKey getPublicKey() {
    return genericEventRecord.getPublicKey();
  }

  @Override
  public final Signature getSignature() {
    return genericEventRecord.getSignature();
  }

  @Override
  public final List<BaseTag> getTags() {
    return genericEventRecord.tags();
  }

  protected static Kind validateKind(@NonNull Kind kind, @NonNull IntPredicate intPredicate, @NonNull Function<Kind, String> errorMessage) {
    NostrException.testBoolean(
       intPredicate.test(kind.getValue()),
       errorMessage.apply(kind));
    return kind;
  }

  @Override
  public final boolean equals(@NonNull Object o) {
    if (!o.getClass().isAssignableFrom(this.getClass())) return false;
    return Objects.equals(genericEventRecord, ((BaseEvent) o).genericEventRecord);
  }

  @Override
  public final int hashCode() {
    return Objects.hashCode(genericEventRecord);
  }

  protected static List<BaseTag> prependExplicitRelayTag(@NonNull List<BaseTag> baseTags, @Nullable Relay relay) {
    return prependExtraRelayTagStream(baseTags.stream(), relay).toList();
  }

  protected static Stream<BaseTag> prependExtraRelayTagStream(@NonNull Stream<BaseTag> baseTags, @Nullable Relay relay) {
    return Optional.ofNullable(relay)
       .map(r -> Stream.concat(
          baseTags.filter(Predicate.not(RelayTag.class::isInstance)),
          Stream.of(new RelayTag(r))))
       .orElse(baseTags);
  }

  protected static Stream<BaseTag> useFirstRelayTag(@NonNull Stream<BaseTag> baseTags) {
    return baseTags
       .filter(Predicate.not(RelayTag.class::isInstance)
          .or(tag -> new AtomicBoolean(false).compareAndSet(false, true)));
  }
}
