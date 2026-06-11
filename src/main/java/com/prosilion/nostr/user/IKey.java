
package com.prosilion.nostr.user;

public interface IKey {
  byte[] getRawData();
  String toBech32String();
}
