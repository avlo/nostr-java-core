package com.prosilion.nostr.service.plugin;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.TextNoteEvent;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;

@Component
public class TextNoteEventKindPlugin<T extends TextNoteEvent> implements EventKindPluginIF<T> {
  private static final Log log = LogFactory.getLog(TextNoteEventKindPlugin.class);

  @Override
  public void processIncomingEvent(@NonNull T event) {
    log.debug(String.format("processing incoming TEXT NOTE EVENT: [%s]", event.getKind()));
    log.info(event.getKind());
  }

  @Override
  public Kind getKind() {
    return Kind.TEXT_NOTE;
  }
}
