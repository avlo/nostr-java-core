package com.prosilion.nostr.event;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.IntPredicate;
import lombok.Getter;
import org.springframework.lang.NonNull;

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

  public BaseEvent(@NonNull Identity identity, @NonNull Kind kind, List<BaseTag> tags, String content) throws NostrException {
    this.genericEventRecord = GenericEventRecordFactory.createInstance(identity, kind, tags, content);
  }

  public BaseEvent(@NonNull GenericEventRecord genericEventRecord) {
    this.genericEventRecord = genericEventRecord;
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
  public String getId() {
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

  public <T extends BaseTag> List<T> getTypeSpecificTags(Class<T> baseTagClassDerivedType) {
    return Filterable.getTypeSpecificTagsStream(baseTagClassDerivedType, this).toList();
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    boolean equals = Objects.equals(genericEventRecord, ((BaseEvent) o).genericEventRecord);
    return equals;
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(genericEventRecord);
  }
}
