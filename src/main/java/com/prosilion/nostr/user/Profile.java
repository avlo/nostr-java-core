package com.prosilion.nostr.user;

import java.net.URL;
import java.util.Optional;

public interface Profile {
    String getName();
    Optional<String> getAbout();
    Optional<URL> getPicture();
}
