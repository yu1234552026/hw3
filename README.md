# Inventory 庫存管理 App

這是 Android Basics with Compose 課程的 Inventory app 作業專案。

## 專案簡介

本 App 是一個庫存管理工具，可以新增、更新、售出與刪除本機資料庫中的商品資料。

專案使用的主要技術如下：

- Jetpack Compose
- Room Database
- ViewModel
- Flow / StateFlow
- Navigation
- Kotlin Coroutines

## 作業測試內容

本專案已加入作業要求的自動化測試。

### 要求 1：測試相同 ID 的資料只保留第一筆

測試檔案：

```text
app/src/androidTest/java/com/example/inventory/ItemDaoTest.kt
```

測試方法：

```kotlin
daoInsert_sameId_ignoresSecondInsert()
```

測試內容：

- 先新增第一筆 `id = 1` 的資料。
- 再新增第二筆相同 `id = 1` 的資料。
- 因為 `ItemDao.insert()` 使用 `OnConflictStrategy.IGNORE`，Room 會忽略第二筆資料。
- 測試會確認資料庫只保留第一筆資料，且不會丟出例外。

### 要求 2：ItemDetailsViewModel Unit Test

測試檔案：

```text
app/src/test/java/com/example/inventory/ui/item/ItemDetailsViewModelTest.kt
```

包含以下三個測試：

```kotlin
reduceQuantityByOne_whenQuantityGreaterThanZero_updatesRepository()
reduceQuantityByOne_whenQuantityIsZero_doesNotUpdateRepository()
deleteItem_callsRepositoryDelete()
```

測試內容：

- `reduceQuantityByOne()` 在數量大於 0 時，會呼叫 repository 的 `updateItem()`。
- `reduceQuantityByOne()` 在數量等於 0 時，不會呼叫 `updateItem()`。
- `deleteItem()` 會正確呼叫 `itemsRepository.deleteItem()`。

### 要求 3：自動新增學號商品並確認成功

測試檔案：

```text
app/src/androidTest/java/com/example/inventory/ItemDaoTest.kt
```

測試方法：

```kotlin
daoInsert_studentItem_insertsItemIntoDb()
```

測試會自動新增以下資料：

```text
Item Name: b11309055
Item Price: 100
Quantity in Stock: 10
```

測試會再從資料庫讀取該筆資料，確認新增成功。

## 如何執行測試

### 執行 Unit Test

此測試不需要模擬器。

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

### 執行 Android Instrumented Test

此測試需要先啟動模擬器或連接 Android 實體裝置。

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

## 測試通過截圖

測試通過截圖位於：

```text
test-screenshots/test-results-summary.png
```

截圖內容包含：

- `ItemDetailsViewModelTest` 共 3 個 Unit Test 通過。
- `ItemDaoTest` 共 7 個 Instrumented Test 通過。
- 測試裝置為 `Pixel_6(AVD) - 14`。

## 版本資訊

因為本專案使用 `compileSdk = 35`，需搭配支援 Android API 35 的 Android Gradle Plugin。

目前版本：

- Java：JDK 17
- Android Gradle Plugin：8.6.0
- Gradle Wrapper：8.11.1
- Kotlin：2.1.0
- Room：2.6.1

## 測試結果

目前測試結果：

```text
BUILD SUCCESSFUL
Unit Test: 3 passed
Instrumented Test: 7 passed
```
