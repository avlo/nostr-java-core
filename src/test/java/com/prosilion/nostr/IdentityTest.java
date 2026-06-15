package com.prosilion.nostr;

import com.prosilion.nostr.event.BaseEvent;
import com.prosilion.nostr.event.RelaySetsEvent;
import com.prosilion.nostr.event.TextNoteEvent;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.message.EventMessage;
import com.prosilion.nostr.tag.RelaysTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import com.prosilion.nostr.util.Util;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@Slf4j
public class IdentityTest {

  public IdentityTest() {
  }

  @Test
  public void testSignEvent() throws NostrException {
    System.out.println("testSignEvent");
    Identity identity = Identity.generateRandomIdentity();
    PublicKey publicKey = identity.getPublicKey();
    BaseEvent instance = new TextNoteEvent(identity, "some content");
    Assertions.assertNotNull(instance.getSignature());

    assertEquals("""
          1111111111111111111111111111111111111111
          1111111111111111111111111111111111111111
          some content
          1111111111111111111111111111111111111111
          1111111111111111111111111111111111111111""",
       Util.debug(instance.getContent(), 1));

    assertEquals("""
          2222222222222222222222222222222222222222
          2222222222222222222222222222222222222222
          some content
          3333333333333333333333333333333333333333
          3333333333333333333333333333333333333333""",
       Util.debug(instance.getContent(), 2, 3));

    assertEquals("""
          
          4444444444444444444444444444444444444444
          4444444444444444444444444444444444444444
          some content
          4444444444444444444444444444444444444444
          4444444444444444444444444444444444444444
          
          """,
       Util.debug(instance.getContent(), true, 4));

    assertEquals("""
          
          5555555555555555555555555555555555555555
          5555555555555555555555555555555555555555
          some content
          6666666666666666666666666666666666666666
          6666666666666666666666666666666666666666
          
          """,
       Util.debug(instance.getContent(), true, 5, 6));

    assertEquals("""
          AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
          AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
          some content
          AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA
          AAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAA""",
       Util.debug(instance.getContent(), 'A'));

    assertEquals("""
          BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
          BBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBBB
          some content
          CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC
          CCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCCC""",
       Util.debug(instance.getContent(), 'B', 'C'));

    assertEquals("""
          
          DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
          DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
          some content
          DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
          DDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDDD
          
          """,
       Util.debug(instance.getContent(), true, 'D'));

    assertEquals("""
          
          EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
          EEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE
          some content
          FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
          FFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFFF
          
          """,
       Util.debug(instance.getContent(), true, 'E', 'F'));

    assertThrows(NullPointerException.class, () -> Util.debug(null, 1));

    Util.debug(log, "the ext {}", "value", 1);
  }

  @Test
  final void tryit() {
    Util.debug(log, "the ext {}", "value", 1);
  }

  @Test
  @SneakyThrows
  void testRelaySetsEvent() {
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
