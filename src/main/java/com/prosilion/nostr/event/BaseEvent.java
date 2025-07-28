package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Signature;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.function.Function;
import java.util.function.IntPredicate;
import lombok.Getter;
import org.springframework.lang.NonNull;

public abstract class BaseEvent implements EventIF {
  @Getter
  private final GenericEventRecord genericEventRecord;

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind) throws NostrException, NoSuchAlgorithmException {
    this(identity, kind, List.of());
  }

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull String content) throws NostrException, NoSuchAlgorithmException {
    this(identity, kind, List.of(), content);
  }

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind, @NonNull List<BaseTag> tags) throws NostrException, NoSuchAlgorithmException {
    this(identity, kind, tags, "");
  }

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind, List<BaseTag> tags, String content) throws NostrException, NoSuchAlgorithmException {
    this.genericEventRecord = GenericEventRecordFactory.createInstance(identity, kind, tags, content);
  }

  @Override
  public String getContent() {
    return genericEventRecord.getContent();
  }

  @Override
  public Long getCreatedAt() {
    return genericEventRecord.getCreatedAt();
  }

  @Override
  public String getEventId() {
    return genericEventRecord.getId();
  }

  @Override
  public Kind getKind() {
    return genericEventRecord.getKind();
  }

  @Override
  public PublicKey getPublicKey() {
    return genericEventRecord.getPublicKey();
  }

  @Override
  public Signature getSignature() {
    return genericEventRecord.getSignature();
  }

  @Override
  public List<BaseTag> getTags() {
    return genericEventRecord.tags();
  }

  protected static Kind validateKind(@NonNull Kind kind, @NonNull IntPredicate intPredicate, @NonNull Function<Kind, String> errorMessage) {
    assert intPredicate.test(kind.getValue()) : errorMessage.apply(kind);
    return kind;
  }
}
