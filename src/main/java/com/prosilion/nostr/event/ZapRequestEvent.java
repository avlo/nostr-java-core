
package com.prosilion.nostr.event;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.internal.ZapRequest;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.GenericTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.util.List;
import java.util.stream.Stream;
import lombok.NonNull;

public class ZapRequestEvent extends BaseEvent {
  public ZapRequestEvent(
      @NonNull Identity identity,
      @NonNull PublicKey zapReciepientPublicKey,
      @NonNull ZapRequest zapRequest,
      @NonNull List<BaseTag> baseTags,
      @NonNull String content) {
    super(
        identity,
        Kind.ZAP_REQUEST,
        Stream.concat(
            Stream.concat(
                Stream.of(
                    new PubKeyTag(zapReciepientPublicKey)),
                Stream.concat(
                    Stream.of(GenericTag.create("lnurl", zapRequest.getLnUrl())),
                    Stream.of(GenericTag.create("amount", zapRequest.getAmount().toString())))),
            baseTags.stream()),
        content);
  }

  public ZapRequestEvent(@NonNull GenericEventRecord genericEventRecord) {
    super(genericEventRecord);
  }
}
