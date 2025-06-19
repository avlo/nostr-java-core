package com.prosilion.nostr.message;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosilion.nostr.enums.Command;
import com.prosilion.nostr.enums.NostrException;
import java.time.temporal.ValueRange;

public interface BaseMessage {
  Command getCommand();

  String encode() throws JsonProcessingException, NostrException;
  
  static String validateSubscriptionId(String subscriptionId) {
    if (!ValueRange.of(1, 64).isValidIntValue(subscriptionId.length())) {
      throw new IllegalArgumentException(String.format("SubscriptionId length must be between 1 and 64 characters but was [%d]", subscriptionId.length()));
    }
    return subscriptionId;
  }
}
