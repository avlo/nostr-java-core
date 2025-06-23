package com.prosilion.nostr.config;

import com.prosilion.nostr.codec.deserializer.EventMessageDeserializer;
import com.prosilion.nostr.enums.KindType;
import com.prosilion.nostr.enums.KindTypeIF;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class SerializerConfig {

  @Bean
  @ConditionalOnMissingBean
  List<KindTypeIF> kindTypes() {
    log.info("Loading default kind types [{}]", (Object[]) KindType.values());
    return List.of(KindType.values());
  }

  @Bean
  public EventMessageDeserializer eventMessageDeserializer(List<KindTypeIF> kindTypes) {
    EventMessageDeserializer eventMessageDeserializer = new EventMessageDeserializer(kindTypes);
    log.info("EventMessageDeserializer instance [{}]", eventMessageDeserializer);
    return eventMessageDeserializer;
  }
}
