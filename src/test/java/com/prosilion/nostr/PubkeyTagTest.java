package com.prosilion.nostr;

import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.user.PublicKey;
import java.lang.reflect.Field;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

class PubkeyTagTest {

    @Test
    void getSupportedFields() {
        String sha256 = "56adf01ca1aa9d6f1c35953833bbe6d99a0c85b73af222e6bd305b51f2749f6f";
        PubKeyTag pubKeyTag = new PubKeyTag(new PublicKey(sha256));
        assertDoesNotThrow(() -> {
            Field field = pubKeyTag.getSupportedFields().stream().findFirst().orElseThrow();
            assertEquals("com.prosilion.nostr.user.PublicKey", field.getAnnotatedType().toString());
            assertEquals("publicKey", field.getName());
            assertEquals(sha256, pubKeyTag.getFieldValue(field).orElseThrow());
        });
    }

}
