package com.prosilion.nostr.util;

import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.ReferencedAbstractEventTag;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
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

  static String prettyPrintGenericEventRecords(@NonNull GenericEventRecord... genericEventRecords) {
    return prettyPrintGenericEventRecords(List.of(genericEventRecords));
  }

  static String prettyPrintGenericEventRecords(@NonNull List<GenericEventRecord> genericEventRecords) {
    return genericEventRecords.stream().map(GenericEventRecord::createPrettyPrintJson).collect(Collectors.joining(",\n  "));
  }

  static String prettyPrintEventTags(@NonNull EventTag... eventTag) {
    return prettyPrintEventTags(List.of(eventTag));
  }

  static String prettyPrintEventTags(@NonNull List<EventTag> eventTags) {
    return prettyPrintReferencedAbstractEventTags(eventTags, "EventTag.*idEvent", "EventTag[\n  idEvent");
  }

  static String prettyPrintAddressTags(@NonNull AddressTag... addressTag) {
    return prettyPrintAddressTags(List.of(addressTag));
  }

  static String prettyPrintAddressTags(@NonNull List<AddressTag> addressTags) {
    return prettyPrintReferencedAbstractEventTags(addressTags, "AddressTag.*kind", "AddressTag[\n  kind");
  }

  static String prettyPrintReferencedAbstractEventTag(@NonNull ReferencedAbstractEventTag abstractTag) {
    return (abstractTag instanceof AddressTag) ?
        prettyPrintAddressTags((AddressTag) abstractTag) :
        prettyPrintEventTags((EventTag) abstractTag);
  }

  static String prettyPrintReferencedAbstractEventTags(@NonNull List<? extends ReferencedAbstractEventTag> abstractTags) {
    return abstractTags.stream().map(Util::prettyPrintReferencedAbstractEventTag).collect(Collectors.joining());
  }

  static String prettyPrintReferencedAbstractEventTags(@NonNull List<? extends ReferencedAbstractEventTag> abstractTags, final String regex, final String delimiter) {
    return abstractTags.stream().map(ReferencedAbstractEventTag::toString)
        .map(tagProperty ->
            String.join("\n  ",
                tagProperty.split(", ")))
        .map(addressTagLiteral ->
            String.join(delimiter,
                addressTagLiteral.split(regex)))
        .collect(Collectors.joining(",\n"));
  }
}
