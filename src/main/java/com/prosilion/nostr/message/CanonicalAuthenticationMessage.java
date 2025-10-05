package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.codec.IDecoder;
import com.prosilion.nostr.codec.deserializer.CanonicalAuthenticationMessageDeserializer;
import com.prosilion.nostr.codec.serializer.CanonicalAuthenticationMessageSerializer;
import com.prosilion.nostr.event.BaseEvent;
import com.prosilion.nostr.event.EventIF;
import com.prosilion.nostr.event.GenericEventRecord;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static com.prosilion.nostr.codec.Encoder.ENCODER_MAPPED_AFTERBURNER;

@JsonSerialize(using = CanonicalAuthenticationMessageSerializer.class)
@JsonDeserialize(using = CanonicalAuthenticationMessageDeserializer.class)
@JsonTypeName("AUTH")
public record CanonicalAuthenticationMessage(
    @Getter EventIF event,
    @Getter @Nullable @JsonInclude(JsonInclude.Include.NON_NULL) String subscriptionId) implements BaseAuthenticationMessageIF {

  public CanonicalAuthenticationMessage(@Nullable String subscriptionId, @JsonProperty GenericEventRecord event) {
    this(event, subscriptionId);
  }

  public CanonicalAuthenticationMessage(@JsonProperty BaseEvent event, @Nullable String subscriptionId) {
    this(event.getGenericEventRecord(), subscriptionId);
  }

  public CanonicalAuthenticationMessage(@JsonProperty EventIF event, @Nullable String subscriptionId) {
    this.event = event;
    this.subscriptionId = subscriptionId;
  }

  @Override
  public String encode() throws JsonProcessingException, NostrException {
    return IDecoder.I_DECODER_MAPPER_AFTERBURNER.writeValueAsString(this);
  }

  @SneakyThrows
  public static BaseMessage decode(@NonNull String jsonString) {
    return ENCODER_MAPPED_AFTERBURNER.readValue(jsonString, CanonicalAuthenticationMessage.class);
  }
}
