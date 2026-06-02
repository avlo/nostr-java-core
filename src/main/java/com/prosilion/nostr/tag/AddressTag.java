package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.serializer.AddressTagSerializer;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.util.Util;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

@Tag(code = "a")
@JsonPropertyOrder({"kind", "publicKey", "identifierTag", "relay"})
@JsonSerialize(using = AddressTagSerializer.class)
public record AddressTag(
    @Getter @Key Kind kind,
    @Getter @Key PublicKey publicKey,
    @Getter @Nullable @Key @JsonInclude(JsonInclude.Include.NON_NULL) IdentifierTag identifierTag,
    @Getter @Nullable @Key @JsonInclude(JsonInclude.Include.NON_NULL) Relay relay) implements ReferencedAbstractEventTag {

  public AddressTag(Kind kind, PublicKey publicKey) {
    this(kind, publicKey, null);
  }

  public AddressTag(Kind kind, PublicKey publicKey, IdentifierTag identifierTag) {
    this(kind, publicKey, identifierTag, null);
  }

  public AddressTag(Kind kind, PublicKey publicKey, IdentifierTag identifierTag, Relay relay) {
    this.kind = kind;
    this.publicKey = publicKey;
    this.identifierTag = identifierTag;
    this.relay = relay;
  }

  public static BaseTag deserialize(@NonNull JsonNode node) {
    List<String> list = Arrays.stream(node.get(1).asText().split(":")).toList();

    return new AddressTag(
        Kind.valueOf(Integer.parseInt(Optional.ofNullable(list.get(0)).orElseThrow())),
        new PublicKey(Optional.ofNullable(list.get(1)).orElseThrow()),
        Optional.ofNullable(list.get(2)).map(IdentifierTag::new).orElse(null),
        Optional.ofNullable(node.get(2)).map(n -> new Relay(n.asText())).orElse(null)
    );
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass())
      return false;
    AddressTag that = (AddressTag) o;
    return Objects.equals(kind, that.kind) &&
        Objects.equals(publicKey, that.publicKey) &&
        Objects.equals(identifierTag, that.identifierTag)
//  TODO: revisit AddressTag equals for relay inclusion/superfluity        
//      && (Objects.isNull(relay) && Objects.isNull(that.relay) || Objects.nonNull(relay) && Objects.nonNull(that.relay) && Objects.equals(relay.getUri().toString(), that.relay.getUri().toString()))
        ;
  }

  @Override
  public int hashCode() {
    return Objects.hash(kind, publicKey, identifierTag, relay);
  }

  @JsonIgnore
  @Override
  public Optional<Relay> findRelay() {
    return Optional.ofNullable(relay);
  }

  @JsonIgnore
  public @NonNull String toStringPrettyPrint() {
    return Util.prettyPrintAddressTags(this);
  }
}
