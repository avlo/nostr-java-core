# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is **nostr-java-core**, an optimized Java library for the Nostr protocol. It provides immutable events and tags using Java records, with comprehensive serialization/deserialization support via Jackson.

## Build Commands

```bash
# Build the project
./gradlew build

# Run all tests
./gradlew test

# Run a single test class
./gradlew test --tests "com.prosilion.nostr.EventMessageDeserializerTest"

# Run a single test method
./gradlew test --tests "com.prosilion.nostr.EventMessageDeserializerTest.testSpecificMethod"

# Publish to local Maven repository
./gradlew publishToMavenLocal

# Generate test coverage report (JaCoCo)
./gradlew jacocoTestReport
```

**Requirements:** Java 21, Gradle

## Architecture

### Core Package Structure (`com.prosilion.nostr`)

- **event/** - Event interfaces and record implementations
- **tag/** - Tag interfaces with `@Tag` and `@Key` annotation-driven serialization
- **message/** - Protocol message types (EventMessage, ReqMessage, OkMessage, etc.)
- **codec/** - Jackson serializers/deserializers for Nostr protocol
- **crypto/** - Schnorr signatures, Bech32, NIP-04/NIP-44 encryption
- **filter/** - Event query filtering via `Filters` and `Filterable`
- **user/** - Identity management (PublicKey, PrivateKey, Identity)
- **enums/** - Kind (event types), Command, Marker

### Event Hierarchy

- **`EventIF`** (interface) - Top-level contract with `serialize()`, getters, and `asGenericEventRecord()` conversion
- **`GenericEventRecord`** (record) - Canonical immutable event data structure with JSON annotations
- **`BaseEvent`** (abstract class) - Wraps GenericEventRecord, handles ID calculation and signing via `GenericEventRecordFactory`
- **44+ specific event classes** - TextNoteEvent (Kind 1), AddressableEvent (Kind 30000-40000), etc.

### Tag System

Tags use annotation-driven serialization:
- `@Tag(code="e", name="event")` - Defines the tag identifier
- `@Key` - Marks fields for serialization order

Key implementations: `EventTag` (code="e"), `PubKeyTag` (code="p"), `AddressTag` (code="a"), `IdentifierTag` (code="d"), `GenericTag` (fallback for unknown codes).

### Message Protocol

`BaseMessage` interface with implementations:
- `EventMessage` - Contains an event with optional subscriptionId
- `ReqMessage` - Subscription request with filters
- `OkMessage`, `EoseMessage`, `CloseMessage`, `NoticeMessage` - Protocol responses

### Serialization

Jackson with Afterburner module. Key classes:
- `Encoder` / `IDecoder` - Central ObjectMapper instances
- Custom serializers in `codec/serializer/` (EventMessageSerializer, BaseTagSerializer, etc.)
- Custom deserializers in `codec/deserializer/` (EventMessageDeserializer, TagDeserializer with tag code dispatch)

### Identity & Cryptography

- `Identity` - Wraps PrivateKey, derives PublicKey, signs events
- `GenericEventRecordFactory` - Creates events with SHA-256 hash and Schnorr signature
- Bech32 encoding for npub/nsec/note formats

## Key Patterns

1. **Records for immutability** - All events, messages, and tags are immutable
2. **Interface-first design** - EventIF, BaseTag, BaseMessage define contracts
3. **Annotation-driven codec** - `@Tag`, `@Key`, `@JsonSerialize`, `@JsonDeserialize`
4. **Factory pattern** - `GenericEventRecordFactory` for event creation with signing