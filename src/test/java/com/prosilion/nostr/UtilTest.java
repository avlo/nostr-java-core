package com.prosilion.nostr;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.prosilion.nostr.codec.BaseMessageDecoder;
import com.prosilion.nostr.util.Util;
import java.io.IOException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

@Slf4j
@JsonTest
@ActiveProfiles("test")
public class UtilTest {
  private static final String TARGET_JSON = """
[
  "EVENT",
  {
    "id": "28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a",
    "kind": 1,
    "pubkey": "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984",
    "created_at": 1687765220,
    "content": "手順書が間違ってたら作業者は無理だな",
    "tags": [ ],
    "sig": "86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"
  }
]""";

  @Test
  void testNoBaseTagNoSpaceUtilPrettyPrint() {
    assertEquals(TARGET_JSON, Util.prettyFormatJson(TARGET_JSON));
  }

  @Test
  void testNoBaseWithSpacesTagUtilPrettyPrint() {
    final String json = """
[
  "EVENT",
  {
    "id": "28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a",
    "kind": 1,
    "pubkey": "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984",
    "created_at": 1687765220,
    "content": "手順書が間違ってたら作業者は無理だな",
    "tags": [ ],
    "sig": "86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"
  }
]""";
    assertEquals(TARGET_JSON, Util.prettyFormatJson(json));
  }

  @Test
  void testNoBaseWithCrlfTagUtilPrettyPrint() {
    final String json = """
[
  "EVENT",
  {
    "id": "28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a",
    "kind": 1,
    "pubkey": "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984",
    "created_at": 1687765220,
    "content": "手順書が間違ってたら作業者は無理だな",
    "tags": [ 
    ],
    "sig": "86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"
  }
]""";
    assertEquals(TARGET_JSON, Util.prettyFormatJson(json));
  }
  
  @Test
  void testNoSpaceUtilPrettyPrint() {
    final String json = """
        [
          "EVENT",
          {
            "id": "28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a",
            "kind": 1,
            "pubkey": "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984",
            "created_at": 1687765220,
            "content": "手順書が間違ってたら作業者は無理だな",
            "tags": [
              [
                "a",
                "1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:UUID-1"
              ],
              [
                "p",
                "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"
              ]
            ],
            "sig": "86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"
          }
        ]""";

    assertEquals(json, Util.prettyFormatJson(json));
  }

  @Test
  void testMultiSpaceUtilPrettyPrint() {
    final String json = """
        [
          "EVENT",
          {
            "id": "28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a",
            "kind": 1,
            "pubkey": "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984",
            "created_at": 1687765220,
            "content": "手順書が間違ってたら作業者は無理だな",
            "tags": [
              [
                "a",
                "1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:UUID-1"
              ],
              [
                "p",
                "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"
              ]
            ],
            "sig": "86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"
          }
        ]""";

    assertEquals(json, Util.prettyFormatJson(json));
    assertEquals(json, Util.prettyFormatJson(json, 2));

    assertEquals(json, Util.prettyFormatJson(json, 0));
    assertEquals(json, Util.prettyFormatJson(json, 1));
    assertEquals(json, Util.prettyFormatJson(json, -1));

    assertNotEquals(json, Util.prettyFormatJson(json, 3));
    assertNotEquals(json, Util.prettyFormatJson(json, 4));
  }

  @Test
  void validateEventMessageJson() throws IOException {
    final String json = """
        [
          "EVENT",
          {
            "id": "28f2fc030e335d061f0b9d03ce0e2c7d1253e6fadb15d89bd47379a96b2c861a",
            "pubkey": "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984",
            "created_at": 1687765220,
            "kind": 1,
            "tags": [
              [
                "a",
                "1:f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75:UUID-1"
              ],
              [
                "p",
                "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76984"
              ]
            ],
            "content": "手順書が間違ってたら作業者は無理だな",
            "sig": "86f25c161fec51b9e441bdb2c09095d5f8b92fdce66cb80d9ef09fad6ce53eaa14c5e16787c42f5404905536e43ebec0e463aee819378a4acbe412c533e60546"
          }
        ]""";

    assertEquals(
        Util.prettyFormatJson(json),
        Util.prettyFormatJson(
            BaseMessageDecoder
                .decode(json)
                .encode()));
  }

  @Test
  public void testBaseMessageDecoderEventFilter() throws JsonProcessingException {
    final String parseTarget = """
[
  "REQ",
  "npub17x6pn22ukq3n5yw5x9prksdyyu6ww9jle2ckpqwdprh3ey8qhe6stnpujh",
  {
    "ids": [
      "f1b419a95cb0233a11d431423b41a42734e7165fcab16081cd08ef1c90e0be75"
    ],
    "kinds": [
      1
    ],
    "#p": [
      "fc7f200c5bed175702bd06c7ca5dba90d3497e827350b42fc99c3a4fa276a712"
    ]
  }
]""";

    String prettyFormatJson = Util.prettyFormatJson(BaseMessageDecoder.decode(parseTarget).encode());
    assertEquals(parseTarget, prettyFormatJson);
    assertEquals(Util.prettyFormatJson(parseTarget), prettyFormatJson);
  }
}
