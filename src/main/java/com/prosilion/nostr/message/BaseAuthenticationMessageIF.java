package com.prosilion.nostr.message;

import com.prosilion.nostr.enums.Command;

public interface BaseAuthenticationMessageIF extends BaseMessage {
  default Command getCommand() {
    return Command.AUTH;
  }
}
