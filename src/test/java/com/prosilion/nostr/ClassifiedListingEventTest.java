package com.prosilion.nostr;

import com.prosilion.nostr.enums.Kind;
import com.prosilion.nostr.event.ClassifiedListingEvent;
import com.prosilion.nostr.event.internal.ClassifiedListing;
import com.prosilion.nostr.event.internal.Relay;
import com.prosilion.nostr.tag.BaseTag;
import com.prosilion.nostr.tag.EventTag;
import com.prosilion.nostr.tag.GeohashTag;
import com.prosilion.nostr.tag.HashtagTag;
import com.prosilion.nostr.tag.PriceTag;
import com.prosilion.nostr.tag.PubKeyTag;
import com.prosilion.nostr.tag.SubjectTag;
import com.prosilion.nostr.user.Identity;
import com.prosilion.nostr.user.PublicKey;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ClassifiedListingEventTest {
  public static final Relay relay = new Relay("ws://localhost:5555");
  
  public final Identity identity;
  public final PublicKey senderPubkey;
  public static final String CLASSIFIED_LISTING_CONTENT = "classified listing content";

  public static final String PTAG_HEX = "2bed79f81439ff794cf5ac5f7bff9121e257f399829e472c7a14d3e86fe76985";
  public static final String ETAG_HEX = "494001ac0c8af2a10f60f23538e5b35d3cdacb8e1cc956fe7a16dfa5cbfc4347";

  public static final PubKeyTag P_TAG = new PubKeyTag(new PublicKey(PTAG_HEX));
  public static final EventTag E_TAG = new EventTag(ETAG_HEX, relay.getUrl());

  public static final String SUBJECT = "Classified Listing Test Subject Tag";
  public static final SubjectTag SUBJECT_TAG = new SubjectTag(SUBJECT);
  public static final GeohashTag G_TAG = new GeohashTag("Classified Listing Test Geohash Tag");
  public static final HashtagTag T_TAG = new HashtagTag("Classified Listing Test Hashtag Tag");

  public static final BigDecimal NUMBER = new BigDecimal("2.71");
  public static final String FREQUENCY = "NANOSECOND";
  public static final String CURRENCY = "BTC";
  public static final PriceTag PRICE_TAG = new PriceTag(NUMBER, CURRENCY, FREQUENCY);

  public static final String CLASSIFIED_LISTING_TITLE = "classified listing title";
  public static final String CLASSIFIED_LISTING_SUMMARY = "classified listing summary";
  public static final String CLASSIFIED_LISTING_LOCATION = "classified listing location";

  private final ClassifiedListingEvent instance;

  public ClassifiedListingEventTest() throws NostrException {
    identity = Identity.generateRandomIdentity();
    senderPubkey = new PublicKey(identity.getPublicKey().toString());

    List<BaseTag> tags = new ArrayList<>();
    tags.add(E_TAG);
    tags.add(P_TAG);
    tags.add(SUBJECT_TAG);
    tags.add(G_TAG);
    tags.add(T_TAG);

    ClassifiedListing classifiedListing = new ClassifiedListing(
        CLASSIFIED_LISTING_TITLE, CLASSIFIED_LISTING_SUMMARY, PRICE_TAG, CLASSIFIED_LISTING_LOCATION);
    instance = new ClassifiedListingEvent(identity, Kind.CLASSIFIED_LISTING, classifiedListing, tags, CLASSIFIED_LISTING_CONTENT);
  }

  @Test
  void testConstructClassifiedListingEvent() {
    System.out.println("testConstructClassifiedListingEvent");

    assertEquals(CLASSIFIED_LISTING_CONTENT, instance.getContent());
    assertEquals(Kind.CLASSIFIED_LISTING, instance.getKind());
    assertEquals(senderPubkey.toString(), instance.getPublicKey().toString());
    assertEquals(senderPubkey.toBech32String(), instance.getPublicKey().toBech32String());
    assertEquals(senderPubkey.toHexString(), instance.getPublicKey().toHexString());
    assertEquals(CLASSIFIED_LISTING_CONTENT, instance.getContent());
  }
}
