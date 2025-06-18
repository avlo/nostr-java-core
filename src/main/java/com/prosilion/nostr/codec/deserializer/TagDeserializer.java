package com.prosilion.nostr.codec.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.prosilion.nostr.event.internal.ElementAttribute;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.GenericTag;
import com.prosilion.nostr.tag.GeohashTag;
import com.prosilion.nostr.tag.HashtagTag;
import com.prosilion.nostr.tag.IdentifierTag;
import com.prosilion.nostr.tag.PriceTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.RelaysTag;
import com.prosilion.nostr.tag.SubjectTag;
import java.io.IOException;
import java.util.Optional;
import java.util.stream.IntStream;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

public class TagDeserializer extends JsonDeserializer<BaseTag> {
  @Override
  public BaseTag deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
    JsonNode node = jsonParser.getCodec().readTree(jsonParser);
    return switch (Optional.ofNullable(node.get(0)).orElseThrow().asText()) {
      case "a" -> AddressTag.deserialize(node);
      case "d" -> IdentifierTag.deserialize(node);
      case "e" -> EventTag.deserialize(node);
      case "g" -> GeohashTag.deserialize(node);
      case "p" -> PubKeyTag.deserialize(node);
      case "t" -> HashtagTag.deserialize(node);
//                case "v" -> VoteTag.deserialize(node);

//                case "nonce" -> NonceTag.deserialize(node);
      case "price" -> PriceTag.deserialize(node);
      case "relays" -> RelaysTag.deserialize(node);
      case "subject" -> SubjectTag.deserialize(node);
      default -> decode(node);
    };
  }

  public static BaseTag decode(@NonNull String json) throws JsonProcessingException {
    return decode(I_DECODER_MAPPER_AFTERBURNER.readValue(json, String[].class));
  }
  
  private static BaseTag decode(@NonNull JsonNode json) throws JsonProcessingException {
    return decode(I_DECODER_MAPPER_AFTERBURNER.readValue(json.toString(), String[].class));
  }

  private static BaseTag decode(@NonNull String[] jsonElements) {
    return new GenericTag(
        jsonElements[0],
        IntStream.of(1, jsonElements.length - 1)
            .mapToObj(i -> new ElementAttribute(
                "param".concat(String.valueOf(i - 1)),
                jsonElements[i]))
            .distinct()
            .toList());
  }
}
