package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyDescription;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.codec.deserializer.CanonicalAuthenticationMessageDeserializer;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.event.internal.ElementAttribute;
import com.prosilion.nostr.tag.GenericTag;
import java.util.List;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;

//@JsonSerialize(using = EventMessageSerializer.class)
@JsonDeserialize(using = CanonicalAuthenticationMessageDeserializer.class)
@JsonTypeName("AUTH")
public record CanonicalAuthenticationMessage(
    @Getter @JsonPropertyDescription("AUTH") EventIF event,
    @Getter @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) String subscriptionId) implements BaseAuthenticationMessageIF {

  public static final String CHALLENGE = "challenge";
  public static final String RELAY = "relay";

  public CanonicalAuthenticationMessage(@JsonProperty GenericEventRecord event) {
    this(event, null);
  }

  public CanonicalAuthenticationMessage(@JsonProperty EventIF event, @Nullable String subscriptionId) {
    this.event = event;
    this.subscriptionId = subscriptionId;
  }

  @Override
  public String encode() throws JsonProcessingException, NostrException {
    return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(
        JsonNodeFactory.instance.arrayNode()
            .add(getCommand().name)
            .add(event.serialize()));
  }

  @SneakyThrows
  public static BaseMessage decode(@NonNull String jsonString) {
    CanonicalAuthenticationMessage canonicalAuthenticationMessage = ENCODER_MAPPED_AFTERBURNER.readValue(jsonString, CanonicalAuthenticationMessage.class);
    return canonicalAuthenticationMessage;
  }

  private static String getAttributeValue(List<GenericTag> genericTags) {
//    TODO: stream optional
    Stream<List<ElementAttribute>> list = getList(genericTags);
    List<ElementAttribute> first = list.toList().getFirst();
    ElementAttribute first1 = first.getFirst();
    Object value = first1.getValue();
    String string = value.toString();
    return string;
  }

  private static Stream<List<ElementAttribute>> getList(List<GenericTag> genericTags) {
    Stream<List<ElementAttribute>> list = genericTags.stream()
        .filter(tag -> tag.getCode().equalsIgnoreCase(CHALLENGE)).map(GenericTag::getAttributes);
    return list;
  }
}
