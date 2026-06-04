Inventory app
==================================

Solution code for Android Basics with Compose.

Introduction
------------

This app is an Inventory tracking app. Demos how to add, update, sell, and delete items from the local database.
This app demonstrated the use of Android Jetpack component [Room](https://developer.android.com/training/data-storage/room) database.
The app also leverages [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel),
[Flow](https://developer.android.com/kotlin/flow),
and [Navigation](https://developer.android.com/topic/libraries/architecture/navigation/).

Pre-requisites
--------------

You need to know:
- How to create and use composables.
- How to navigate between composables, and pass data between them.
- How to use architecture components including ViewModel, Flow, StateFlow and StateUi.
- How to use coroutines for long-running tasks.
- SQLite database and the SQLite query language


Getting Started
---------------

1. Download and run the app.

Testing
-------

This project includes comprehensive unit and instrumented tests:

### Requirement 1: ItemDaoTest - Duplicate ID Insert Handling
- **Test**: `daoInsert_sameId_ignoresSecondInsert()` (Lines 112-120)
- **Description**: Verifies that when inserting two items with the same ID, Room silently ignores the second insert and retains only the first item.
- **Student Item Test**: `daoInsert_studentItem_insertsItemIntoDb()` (Lines 123-130)
  - Item Name: b11309055 (Student ID)
  - Price: 100
  - Quantity: 10

### Requirement 2: ItemDetailsViewModel Unit Tests
- **File**: `app/src/test/java/com/example/inventory/ui/item/ItemDetailsViewModelTest.kt`
- **Tests**:
  1. `reduceQuantityByOne_whenQuantityGreaterThanZero_updatesRepository()` - Verifies that reducing quantity calls updateItem when quantity > 0
  2. `reduceQuantityByOne_whenQuantityIsZero_doesNotUpdateRepository()` - Verifies that reducing quantity does NOT call updateItem when quantity = 0
  3. `deleteItem_callsRepositoryDelete()` - Verifies that deleteItem correctly calls itemsRepository.deleteItem()

### Requirement 3: ItemDaoTest - Student Item Auto-Insert
- **Test**: `daoInsert_studentItem_insertsItemIntoDb()` (Lines 123-130)
- **Auto-inserts**: Item with Name=b11309055, Price=100, Quantity=10
- **Verification**: Confirms successful insertion and retrieval

### Running Tests
```bash
# Unit tests
./gradlew test

# Instrumented tests (requires connected device/emulator)
./gradlew connectedAndroidTest
```

### Build Information
- **Java Version**: JDK 17 (Eclipse Adoptium)
- **Android Gradle Plugin**: 8.2.0 (upgraded from 8.1.4 to fix jlink compatibility)
- **Kotlin**: 2.1.0
- **Compose**: Latest stable

All tests: ✅ PASSING
