package com.prosilion.nostr.user;

import com.prosilion.nostr.NostrException;
import java.nio.ByteBuffer;
import java.util.function.Supplier;

public interface ISignableEntity {
  //    Signature getSignature();
  Supplier<ByteBuffer> getByteArraySupplier() throws NostrException;
}
