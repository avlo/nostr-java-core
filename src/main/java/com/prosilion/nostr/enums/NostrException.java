package com.prosilion.nostr.enums;

import lombok.experimental.StandardException;

@StandardException
public class NostrException extends Exception {
    public NostrException(String message) {
        super(message);
    }
}
