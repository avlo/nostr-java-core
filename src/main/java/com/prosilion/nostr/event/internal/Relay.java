package com.prosilion.nostr.event.internal;

import com.prosilion.nostr.tag.Key;
import java.util.Optional;
import java.util.regex.Pattern;
import org.springframework.lang.NonNull;
import org.springframework.web.util.InvalidUrlException;

public record Relay(@Key String url) {
  public String getUrl() {
    return url;
  }

  public Relay(@NonNull String url) {
    this.url = asLocalOrCanonicalUrl(url);
  }

  private static String asLocalOrCanonicalUrl(@NonNull String url) {
    return asLocalHostUrl(url) ? url : asCanonicalUrl(url);
  }

  private static boolean asLocalHostUrl(String url) {
    return LOCALHOST.matcher(url).matches();
  }

  private static String asCanonicalUrl(String url) {
    return Optional.of(url)
        .filter(s -> URL_PATTERN_W_PORT_PASSTHROUGH.matcher(s).matches())
        .orElseThrow(() -> new InvalidUrlException(
            String.format("Invalid Relay url: [%s]", url)));
  }

  public static final Pattern URL_PATTERN_W_PORT_PASSTHROUGH
      = Pattern.compile("^((https?|wss?)://)?(((\\w+\\-*\\w+\\.)*(\\w+\\-*\\w+)(\\.[\\w]{2,})+)|(((1|2){0,1}[0-9]{1,2}\\.){3}(1|2){0,1}[0-9]{1,2}))(:[0-9]{2,5})?(/(#/)?[\\w0-9+\\/\\-]*(\\?([\\w0-9 ]+=([\\w0-9]|%[0-7][0-9a-f])+&?)*)?(#[\\w0-9]*)?)*$");

  public static final Pattern LOCALHOST
      = Pattern.compile("^((https?|wss?)://localhost)?(:[0-9]{2,5})?(/(#/)?[\\w0-9+\\/\\-]*(\\?([\\w0-9 ]+=([\\w0-9]|%[0-7][0-9a-f])+&?)*)?(#[\\w0-9]*)?)*$");
}
