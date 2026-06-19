package com.prosilion.nostr.util;

import com.prosilion.nostr.NostrException;
import com.prosilion.nostr.event.GenericEventRecord;
import com.prosilion.nostr.tag.AddressTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.ReferencedAbstractEventTag;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import lombok.NonNull;
import org.slf4j.Logger;

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


  @Debug
  static void debug(@NonNull Logger logger, @NonNull Character marker) {
    debug(logger, false, marker);
  }

  @Debug
  static void debug(@NonNull Logger logger, boolean newline, @NonNull Character marker, @NonNull Character... markers) {
    debug(logger, "", new Object[]{}, newline, marker, markers);
  }

  @Debug
  static void debug(@NonNull Logger logger, @NonNull String value, @NonNull String arg, @NonNull Character marker, @NonNull Character... markers) {
    debug(logger, value, new Object[]{arg}, false, marker, markers);
  }

  @Debug
  static void debug(@NonNull Logger logger, @NonNull String value, @NonNull Object[] args, @NonNull Character marker, @NonNull Character... markers) {
    debug(logger, value, args, false, marker, markers);
  }

  @Debug
  static void debug(@NonNull Logger logger, @NonNull String value, @NonNull Stream<String> arg, @NonNull Character marker, @NonNull Character... markers) {
    debug(logger, value, arg.toArray(), false, marker, markers);
  }

  @Debug
  static void debug(@NonNull Logger logger, @NonNull String value, @NonNull String arg, boolean newline, @NonNull Character marker, @NonNull Character... markers) {
    debug(logger, value, new Object[]{arg}, newline, marker, markers);
  }

  @Debug
  static void debug(@NonNull Logger logger, @NonNull String value, @NonNull Stream<String> arg, boolean newline, @NonNull Character marker, @NonNull Character... markers) {
    debug(logger, value, arg.toArray(), newline, marker, markers);
  }

  @Debug
  static void debug(@NonNull Logger logger, @NonNull String value, @NonNull Object[] args, boolean newline, @NonNull Character marker, @NonNull Character... markers) {
    if (!logger.isDebugEnabled()) {
      return;
    }
    logger.debug(getDebugString(value, newline, marker, markers), args);
  }

  @Debug
  static String getDebugString(@NonNull String value, @NonNull Character marker, @NonNull Character... markers) {
    return getDebugString(value, false, marker, markers);
  }

  @Debug
  static String getDebugString(@NonNull String value, boolean newline, @NonNull Character marker, @NonNull Character... markers) {
    return getDebugString(value, newline, Stream.concat(Stream.of(marker), Arrays.stream(markers)).toList());
  }

  @Debug
  static String getDebugString(@NonNull String value, boolean newline, @NonNull List<Character> markers) {
    if (markers.isEmpty())
      throw new NostrException("Util.debug(...) requires at least one character marker");

    char prefixChar = markers.getFirst();
    char postfixChar = markers.size() > 1 ? markers.get(1) : prefixChar;

    String prefix = String.valueOf(prefixChar).repeat(40);
    String postfix = String.valueOf(postfixChar).repeat(40);

    String result = value.isEmpty() ?
       String.join("\n", prefix, prefix, postfix, postfix) :
       String.join("\n", prefix, prefix, value, postfix, postfix);

    return newline ? "\n" + result + "\n" : result;
  }
}
