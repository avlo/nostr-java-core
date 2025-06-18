
package com.prosilion.nostr.user;

import java.io.Serializable;

public interface IKey extends Serializable {

    byte[] getRawData();

    String toBech32String();
}
