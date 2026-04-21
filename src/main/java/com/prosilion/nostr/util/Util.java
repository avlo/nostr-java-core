package com.prosilion.nostr.util;

import java.util.UUID;
import org.springframework.lang.NonNull;

public interface Util {
  String EMPTY_TAGS_VARIANTS_REGEX = "\\[\\s+]";
  String EMPTY_TAGS_SUBSTITUTION = "[ ]";

  static String prettyFormatJson(@NonNull String json_str) {
    return prettyFormatJson(json_str, 0);
  }

  static String prettyFormatJson(@NonNull String json_str, int indent_width) {
    indent_width = Math.max(indent_width, 2);
    final char[] chars = json_str.toCharArray();
    final String newline = System.lineSeparator();

    StringBuilder ret = new StringBuilder();
    boolean begin_quotes = false;

    for (int i = 0, indent = 0; i < chars.length; i++) {
      char c = chars[i];

      if (c == '\"') {
        ret.append(c);
        begin_quotes = !begin_quotes;
        continue;
      }

      if (!begin_quotes) {
        switch (c) {
          case '{':
          case '[':
            ret.append(c).append(newline).append(String.format("%" + (indent += indent_width) + "s", ""));
            continue;
          case '}':
          case ']':
            ret.append(newline).append((indent -= indent_width) > 0 ? String.format("%" + indent + "s", "") : "").append(c);
            continue;
          case ':':
            ret.append(c).append(" ");
            continue;
          case ',':
            ret.append(c).append(newline).append(indent > 0 ? String.format("%" + indent + "s", "") : "");
            continue;
          default:
            if (Character.isWhitespace(c)) continue;
        }
      }

      ret.append(c).append(c == '\\' ? "" + chars[++i] : "");
    }

    return ret.toString().replaceAll(
        EMPTY_TAGS_VARIANTS_REGEX,
        EMPTY_TAGS_SUBSTITUTION);
  }

  static String generateRandomHex64String() {
    return UUID.randomUUID().toString().concat(UUID.randomUUID().toString()).replaceAll("[^A-Za-z0-9]", "");
  }
}
