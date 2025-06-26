package com.prosilion.nostr.user;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.crypto.bech32.Bech32;
import com.prosilion.nostr.crypto.bech32.Bech32Prefix;
import java.net.URL;
import java.util.Objects;
import java.util.Optional;
import lombok.Getter;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

import static com.prosilion.nostr.event.IEvent.MAPPER_AFTERBURNER;

public record UserProfile(
    @Getter PublicKey publicKey,
    @Getter String nip05,
    @Getter String name,
    @Nullable String about,
    @Nullable URL picture) implements Profile, IBech32Encodable {
  private static final Log log = LogFactory.getLog(UserProfile.class);
  private static final String NAME_PATTERN = "\\w[\\w\\-]+\\w";

  public UserProfile {
    var strNameArr = nip05.split("@");
    assert strNameArr.length == 2 : new AssertionError("Invalid nip05 profile, missing '@': " + nip05);
    assert strNameArr[0].matches(NAME_PATTERN) : new AssertionError("Invalid nip05 profile pattern: " + nip05);
  }

  @Override
  public Optional<String> getAbout() {
    return Optional.ofNullable(about);
  }

  @Override
  public Optional<URL> getPicture() {
    return Optional.ofNullable(picture);
  }

  @Override
  public String toBech32() throws NostrException {
    try {
      return Bech32.encode(Bech32.Encoding.BECH32, Bech32Prefix.NPROFILE.getCode(), this.publicKey.getRawData());
    } catch (Exception ex) {
      log.error(ex);
      throw new NostrException(ex.getMessage());
    }
  }

  @Override
  @NonNull
  public String toString() {
    try {
      return MAPPER_AFTERBURNER.writeValueAsString(this);
    } catch (JsonProcessingException ex) {
      throw new RuntimeException(ex);
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o == null || getClass() != o.getClass()) return false;
    UserProfile that = (UserProfile) o;
    return Objects.equals(publicKey, that.publicKey) && Objects.equals(nip05, that.nip05) && Objects.equals(name, that.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(publicKey, nip05, name);
  }
}
