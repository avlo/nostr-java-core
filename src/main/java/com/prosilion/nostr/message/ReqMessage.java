package com.prosilion.nostr.message;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeName;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.enums.Command;
import com.prosilion.nostr.codec.FiltersDecoder;
import com.prosilion.nostr.codec.serializer.ReqMessageSerializer;
import com.prosilion.nostr.filter.Filters;
import java.util.List;
import java.util.stream.IntStream;
import lombok.Getter;
import lombok.SneakyThrows;
import org.apache.commons.lang3.stream.Streams;
import org.springframework.lang.NonNull;

import static com.prosilion.nostr.codec.IDecoder.I_DECODER_MAPPER_AFTERBURNER;

@JsonTypeName("REQ")
@JsonSerialize(using = ReqMessageSerializer.class)
public record ReqMessage(
    @Getter String subscriptionId,
    @Getter List<Filters> filtersList) implements BaseMessage {

  @JsonIgnore
  public static Command command = Command.REQ;
  public static final int FILTERS_START_INDEX = 2;

  public ReqMessage(String subscriptionId, Filters... filtersList) {
    this(subscriptionId, List.of(filtersList));
  }

  public ReqMessage(String subscriptionId, List<Filters> filtersList) {
    this.subscriptionId = BaseMessage.validateSubscriptionId(subscriptionId);
    this.filtersList = filtersList;
  }

//  @Override
//  public String encode() throws JsonProcessingException {
//    var encoderArrayNode = JsonNodeFactory.instance.arrayNode();
//    encoderArrayNode
//        .add(getCommand().name())
//        .add(getSubscriptionId());
//
//    filtersList.stream()
//        .map(FiltersEncoder::new)
//        .map(FiltersEncoder::encode)
//        .map(ReqMessage::createJsonNode)
//        .forEach(encoderArrayNode::add);
//
//    return ENCODER_MAPPED_AFTERBURNER.writeValueAsString(encoderArrayNode);
//  }

  public static <T extends BaseMessage> T decode(@NonNull Object subscriptionId, @NonNull String jsonString) throws JsonProcessingException {
    List<String> jsonFiltersList = getJsonFiltersList(jsonString);
    return (T) new ReqMessage(
        BaseMessage.validateSubscriptionId(subscriptionId.toString()).toString(),
        Streams.failableStream(jsonFiltersList.stream()).map(filtersList ->
            FiltersDecoder.decode(filtersList)).stream().toList());
  }

  private static List<String> getJsonFiltersList(String jsonString) throws JsonProcessingException {
    return IntStream.range(FILTERS_START_INDEX, I_DECODER_MAPPER_AFTERBURNER.readTree(jsonString).size())
        .mapToObj(idx -> readTree(jsonString, idx)).toList();
  }

  @SneakyThrows
  private static String readTree(String jsonString, int idx) {
    return I_DECODER_MAPPER_AFTERBURNER.readTree(jsonString).get(idx).toString();
  }

  @Override
  public Command getCommand() {
    return command;
  }
}
