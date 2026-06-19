package com.prosilion.nostr.util;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import org.slf4j.Logger;

/**
 * future intention to have methods annotated with @Debug to display w/ syntax highlighting (like @Deprecated does)
 * @see Util#debug(Logger, Character) 
 * 
 */
@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.METHOD)
public @interface Debug {
}
