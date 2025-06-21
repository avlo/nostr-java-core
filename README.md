# Nostr-Java Core Library
## Optimized variant of [nostr-java](https://github.com/tcheeric/nostr-java/tree/develop) library, specifically:

- event classes refactored into interfaces implemented by java record, yields:  
  - immutable events 
    - insures event signatures provably match event content and are both unchangeable
- tag classes refactored into interfaces implemented by java record, yields:
  - immutable tags
  - container classes (Filter, List, Services) no longer require use of generics
- support for "Kind" subtyping, called "KindType" (req'd by NIP-08 and available for additional Nips per need)
- removal of mutation-vulnerable Nip-XX API
- de-coupling of web-socket connection from nostr event
  - web-socket functionality (event publishing, request subscriptions) available in optional [subdivisions](https://github.com/avlo/subdivisions) library (details, bottom of page)
- smaller compilation footprint:
  - single jar, 194004 (194K) bytes  
  vs
  - multiple (9) jars, 518027 (518K) bytes
- [SOLID](https://www.digitalocean.com/community/conceptual-articles/s-o-l-i-d-the-first-five-principles-of-object-oriented-design) engineering principles.  Simple.  Clean.  OO.
    - understandability
    - extensibility / modularization
    - testing
    - customization

----

used extensively by nostr-java projects:
- [subdivisions](https://github.com/avlo/subdivisions) Reactive java web-socket client and related utilities:
  - nostr event publisher
  - nostr request subscriber (reactive spring web-socket client)
  - request subscriptions pool manager
- [superconductor](https://github.com/avlo/superconductor) Java Nostr-Relay Framework & WebSocket Application Server
  - available as jar, war and/or spring-boot starter 
- [afterimage](https://github.com/avlo/afterimage) Nostr-Reputation Authority
