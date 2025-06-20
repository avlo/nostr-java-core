package com.prosilion.nostr.event;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.NostrException;
import com.prosilion.nostr.enums.Type;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.user.Signature;
import java.beans.Transient;
import java.nio.ByteBuffer;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class GenericEventKindType implements GenericEventKindTypeIF {
  @JsonIgnore
  private static final Log log = LogFactory.getLog(GenericEventKindType.class);

  @Getter
  private final GenericEventKindIF genericEventKind;
  @Getter
  private final Type type;

  //  TODO: below needs test of counterfactual/non-happy path
  public GenericEventKindType(GenericEventKindIF genericEventKind) {
    this.type = Type.valueOf(
        Objects.requireNonNull(
                Filterable.getTypeSpecificTags(AddressTag.class, genericEventKind).stream()
                    .findFirst().orElseThrow()
                    .getIdentifierTag())
            .getUuid().toUpperCase());
    this.genericEventKind = genericEventKind;
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || (
        this.genericEventKind.getClass() != o.getClass() &&
            this.getClass() != o.getClass())) return false;
    return Objects.equals(
        genericEventKind,
//        TODO: revisit below cast; hack-ey
        ((GenericEventKindTypeIF) o).getGenericEventKind());
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(genericEventKind);
  }

  @Override
  public String toBech32() {
    return getGenericEventKind().toBech32();
  }

  @Transient
  @Override
  public Supplier<ByteBuffer> getByteArraySupplier() throws NostrException {
    return genericEventKind.getByteArraySupplier();
  }

  @Override
  public Kind getKind() {
    return genericEventKind.getKind();
  }

  @Override
  public List<BaseTag> getTags() {
    return genericEventKind.getTags();
  }

  @Override
  public String getContent() {
    return genericEventKind.getContent();
  }

  @Override
  public Signature getSignature() {
    return genericEventKind.getSignature();
  }

  @Override
  public String getId() {
    return genericEventKind.getId();
  }

  @Override
  public PublicKey getPublicKey() {
    return genericEventKind.getPublicKey();
  }

  @Override
  public Long getCreatedAt() {
    return genericEventKind.getCreatedAt();
  }
}
