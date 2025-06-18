package com.prosilion.nostr.tag;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.prosilion.nostr.codec.deserializer.TagDeserializer;
import com.prosilion.nostr.codec.serializer.BaseTagSerializer;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import org.apache.commons.lang3.stream.Streams;

@JsonSerialize(using = BaseTagSerializer.class)
@JsonDeserialize(using = TagDeserializer.class)
public interface BaseTag extends ITag {

  default String getCode() {
    return this.getClass().getAnnotation(Tag.class).code();
  }

  default List<Field> getSupportedFields() {
    return Streams.failableStream(
            Arrays.stream(this.getClass().getDeclaredFields()))
        .filter(f -> Objects.nonNull(f.getAnnotation(Key.class)))
        .filter(f -> getFieldValue(f).isPresent())
        .collect(Collectors.toList());
  }

  default Optional<String> getFieldValue(Field field) throws NoSuchFieldException, IllegalAccessException {
    Field declaredField = this.getClass().getDeclaredField(field.getName());
    declaredField.setAccessible(true);
    return Optional.ofNullable(
            declaredField.get(this))
        .map(Object::toString);
  }
}
