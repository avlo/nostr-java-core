package com.prosilion.nostr.message;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.enums.Command;
import com.prosilion.nostr.codec.serializer.EventMessageSerializer;
import com.prosilion.nostr.event.GenericEventDto;
import com.prosilion.nostr.event.GenericEventDtoIF;
import com.prosilion.nostr.filter.Filterable;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.GenericTag;
import com.prosilion.nostr.tag.RelaysTag;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import lombok.Getter;
import lombok.SneakyThrows;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

@JsonSerialize(using = EventMessageSerializer.class)
public record CanonicalAuthenticationMessage(
    @Getter GenericEventDtoIF eventDto) implements BaseMessage {

  public static Command command = Command.AUTH;

  public static final String CHALLENGE = "challenge";
  public static final String RELAY = "relay";

  @SneakyThrows
  public static <T extends BaseMessage> T decode(@NonNull Map map) {
    GenericEventDtoIF event = I_DECODER_MAPPER_AFTERBURNER.convertValue(map, new TypeReference<>() {
    });

    List<GenericTag> genericTags = event.getTags().stream()
        .filter(GenericTag.class::isInstance)
        .map(GenericTag.class::cast).toList();

    BaseTag challengeTag = GenericTag.create(CHALLENGE, getAttributeValue(genericTags, CHALLENGE));
    List<RelaysTag> relayTags = Filterable.getTypeSpecificTags(RelaysTag.class, event);

    List<BaseTag> list = Stream.concat(Stream.of(challengeTag), relayTags.stream()).toList();

    GenericEventDtoIF canonEvent = new GenericEventDto(
        map.get("id").toString(),
        event.getPublicKey(),
        event.getCreatedAt(),
        Kind.CLIENT_AUTH,
        list,
        "content",
        event.getSignature());

    return (T) new CanonicalAuthenticationMessage(canonEvent);
  }

  private static String getAttributeValue(List<GenericTag> genericTags, String attributeName) {
//    TODO: stream optional
    return genericTags.stream()
        .filter(tag -> tag.getCode().equalsIgnoreCase(attributeName)).map(GenericTag::getAttributes).toList().get(0).get(0).getValue().toString();
  }

  @Override
  public Command getCommand() {
    return command;
  }
}
