package com.prosilion.nostr;

import com.prosilion.nostr.event.BaseEvent;
import com.prosilion.nostr.event.RelaySetsEvent;
import com.prosilion.nostr.event.TextNoteEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.message.EventMessage;
import com.prosilion.nostr.tag.RelaysTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.util.Util;
import java.util.stream.Stream;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
@JsonTest
@SpringJUnitConfig
@ActiveProfiles("test")
public class IdentityTest {

  public IdentityTest() {
  }

  @Test
  public final void testSignEvent() throws NostrException {
    System.out.println("testSignEvent");
    Identity identity = Identity.generateRandomIdentity();
    BaseEvent instance = new TextNoteEvent(identity, "some content");
    Assertions.assertNotNull(instance.getSignature());

    assertEquals("""
          1111111111111111111111111111111111111111
          1111111111111111111111111111111111111111
          some content
          1111111111111111111111111111111111111111
          1111111111111111111111111111111111111111""",
       Util.getDebugString(instance.getContent(), '1'));

    assertEquals("""
          2222222222222222222222222222222222222222
          2222222222222222222222222222222222222222
          some content
          3333333333333333333333333333333333333333
          3333333333333333333333333333333333333333""",
       Util.getDebugString(instance.getContent(), '2', '3'));

    assertEquals("""
          
          4444444444444444444444444444444444444444
          4444444444444444444444444444444444444444
          some content
          4444444444444444444444444444444444444444
          4444444444444444444444444444444444444444
          
          """,
       Util.getDebugString(instance.getContent(), true, '4'));

    assertEquals("""
          
          5555555555555555555555555555555555555555
          5555555555555555555555555555555555555555
          some content
          6666666666666666666666666666666666666666
          6666666666666666666666666666666666666666
          
          """,
       Util.getDebugString(instance.getContent(), true, '5', '6'));

    assertEquals("""
          AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
          AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
          some content
          AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
          AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA""",
       Util.getDebugString(instance.getContent(), 'A'));

    assertEquals("""
          BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
          BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
          some content
          CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
          CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC""",
       Util.getDebugString(instance.getContent(), 'B', 'C'));

    assertEquals("""
          
          DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
          DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
          some content
          DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
          DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
          
          """,
       Util.getDebugString(instance.getContent(), true, 'D'));

    assertEquals("""
          
          EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
          EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
          some content
          FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
          FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
          
          """,
       Util.getDebugString(instance.getContent(), true, 'E', 'F'));
  }

  @Test
  public final void testDebugOutput() {
//    non-newline
    Util.debug(log, "single parameter: [{}]", "A", '1');
    Util.debug(log, "two array parameters: [{}], [{}]", new String[]{"B", "C"}, '2');
    Util.debug(log, "two stream parameters: [{}], [{}]", Stream.of("D", "E"), '3', '4');

//  newline
    Util.debug(log, "single parameter: [{}]", "F", true, '5');
    Util.debug(log, "two stream parameters: [{}], [{}]", Stream.of("G", "H"), true, '6');
    Util.debug(log, "two stream parameters: [{}], [{}]", Stream.of("I", "J"), true, '7', '8');
    Util.debug(log, "two array parameters: [{}], [{}]", new String[]{"L", "M"}, true, '9', '0');
  }

  @Test
  public final void testMinimalDebugOutput() {
    Util.debug(log, '1');
    Util.debug(log, true, '2');
  }

  @Test
  public final void testNullParameters() {
    assertThrows(NullPointerException.class, () -> Util.debug(null, "single parameter: [{}]", "A", '1'));
    assertThrows(NullPointerException.class, () -> Util.debug(log, null, "A", '1'));
    assertThrows(NullPointerException.class, () -> Util.debug(log, "parameters:  [{}]", (String) null, '1'));
  }

  @Test
  @SneakyThrows
  final void testRelaySetsEvent() {
    RelaySetsEvent relaySetsEvent = new RelaySetsEvent(
       Identity.generateRandomIdentity(),
       new RelaysTag(
          new Relay("ws://localhost:5555")
       ),
       "Kind.RELAY_SETS");
    EventMessage eventMessage = new EventMessage(relaySetsEvent);
    System.out.println(eventMessage.encode());
  }

//  @Test
//  public void privateKeyPublicKeyConvenience() {
//    System.out.println("testSignDelegationTag");
//    Identity identity = Identity.generateRandomIdentity();
//    System.out.println("private key: " + identity.getPrivateKey());
//    PublicKey publicKey = identity.getPublicKey();
//    System.out.println("public key: " + publicKey);
//  }
    
    
/*
    @Test
    public void testDecryptMessage() {
        try {
            System.out.println("testDecryptMessage");
            var senderPublicKey = Identity.getInstance().getPublicKey();
            
            PrivateKey rcptSecKey = new PrivateKey(NostrUtil.hexToBytes(Bech32.fromBech32("nsec13sntjjh35dd4u3lwy42lnpszydmkwar708y3jzwxr937fy2q73hsmvez4z")));
            PublicKey rcptPubKey = new PublicKey("edd898fc2817ee64f7ee1941d193d53c2daa77db4b8409240565fc9644626878");

            final DirectMessageEvent dmEvent = EntityFactory.Events.createDirectMessageEvent(senderPublicKey, rcptPubKey, "Hello uq7yfx3l!");

            new IdentityHelper(Identity.getInstance()).encryptDirectMessage(dmEvent);

            var rcptId = new Identity(rcptSecKey);
            var msg = new IdentityHelper(rcptId).decryptMessage(dmEvent.getContent(), dmEvent.getPubKey());
            
            Assertions.assertEquals("Hello uq7yfx3l!", msg);
        } catch (NostrException ex) {
            Assertions.fail(ex);
        }
    }
*/

}
