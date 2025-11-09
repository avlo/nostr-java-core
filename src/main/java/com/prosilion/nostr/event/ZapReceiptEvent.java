
package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.ZapReceipt;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.GenericTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.stream.Stream;
import org.springframework.lang.NonNull;

public class ZapReceiptEvent extends BaseEvent {
  public ZapReceiptEvent(
      @NonNull Identity identity,
      @NonNull PublicKey zapReciepientPublicKey,
      @NonNull ZapReceipt zapReceipt) {
    super(
        identity,
        Kind.ZAP_RECEIPT,
        Stream.concat(
            Stream.of((BaseTag) new PubKeyTag(zapReciepientPublicKey)),
            Stream.concat(
                Stream.of(GenericTag.create("descriptionSha256", zapReceipt.getDescriptionSha256())),
                Stream.of(GenericTag.create("bolt11", zapReceipt.getBolt11())))).toList());
  }

  public ZapReceiptEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
