package com.prosilion.nostr.filter;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;

public record GenericTagQuery(
    @Getter String tagName,
    @Getter @JsonProperty String value) {
}
