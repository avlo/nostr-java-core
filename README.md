# Nostr-Java Core Library  

----
## Optimized variant of [nostr-java](https://github.com/tcheeric/nostr-java/tree/develop), differentiation:

- event classes refactored into interfaces backed by record implementations, yields:  
  - immutable events 
    - guarantees event signatures match event content  
- tag classes refactored into interfaces backed by record implementations, yields:
  - immutable tags
    - container classes (Filter, List) no longer require generics
- support for "Kind" subtyping, called "KindType" (req'd by NIP-08 and available for additional Nips per need)
- removal of vulnerable Nip-XX API
- de-coupling web-socket connection from nostr event
  - web-socket functionality provided by optional    [subdivisions](https://github.com/avlo/subdivisions) library  
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
- [subdivisions](https://github.com/avlo/subdivisions) Java web-socket client & related utilities
- [superconductor](https://github.com/avlo/superconductor) Java Nostr-Relay Framework & WebSocket Application Server
- [afterimage](https://github.com/avlo/afterimage) Nostr-Reputation Authority
