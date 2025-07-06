package com.prosilion.nostr.tag;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.fasterxml.jackson.databind.JsonNode;
import java.math.BigDecimal;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import org.springframework.lang.NonNull;

@Tag(code = "price")
@JsonPropertyOrder({"number", "currency", "frequency"})
public record PriceTag(
    @Getter @Key @JsonFormat(shape = JsonFormat.Shape.STRING) BigDecimal number,
    @Getter @Key String currency,
    @Getter @Key String frequency) implements BaseTag {

  public static BaseTag deserialize(@NonNull JsonNode node) {
    return new PriceTag(
        new BigDecimal(Optional.of(node.get(1)).orElseThrow().asInt()),
        Optional.of(node.get(2)).orElseThrow().asText(),
        Optional.of(node.get(3)).orElseThrow().asText()
    );
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    PriceTag priceTag = (PriceTag) o;
    return Objects.equals(
        number.stripTrailingZeros(),
        priceTag.number.stripTrailingZeros())
        &&
        Objects.equals(currency, priceTag.currency) && Objects.equals(frequency, priceTag.frequency);
  }

  @Override
  public int hashCode() {
    return Objects.hash(number.stripTrailingZeros(), currency, frequency);
  }
}
