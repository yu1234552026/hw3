# Inventory 庫存管理 App 作業說明

本專案是 Android Basics with Compose 課程中的 Inventory app 作業。這次作業重點是使用 AI 協助產生與修正自動化測試，並確認測試可以成功執行。

## 使用工具

本次作業使用以下工具完成：

- Android Studio
- Kotlin
- Jetpack Compose
- Room Database
- ViewModel
- Gradle
- Android Emulator
- Codex AI 助手

## 使用 AI 的 Prompt 與需求

我使用 AI 協助完成下列測試需求：

### Prompt 1：產生 ItemDaoTest 相同 ID 測試

請 AI 產生一個測試，內容為：

```text
在程式中，如果插入 ID 相同的第二筆資料時，Room 會靜默忽略，
資料庫只保留第一筆，不丟例外。
請在 ItemDaoTest.kt 加入一個測試，可以測試新增兩筆同 ID
的資料時，只會保留原先的資料。
```

完成的測試方法：

```kotlin
daoInsert_sameId_ignoresSecondInsert()
```

測試檔案位置：

```text
app/src/androidTest/java/com/example/inventory/ItemDaoTest.kt
```

### Prompt 2：產生 ItemDetailsViewModel Unit Test

請 AI 為 `ItemDetailsViewModel` 產生 unit test，測試內容包含：

```text
reduceQuantityByOne() 在數量 > 0 時，會更新 repository
reduceQuantityByOne() 在數量 = 0 時，不呼叫 updateItem()
deleteItem() 正確呼叫 itemsRepository.deleteItem()
```

完成的測試方法：

```kotlin
reduceQuantityByOne_whenQuantityGreaterThanZero_updatesRepository()
reduceQuantityByOne_whenQuantityIsZero_doesNotUpdateRepository()
deleteItem_callsRepositoryDelete()
```

測試檔案位置：

```text
app/src/test/java/com/example/inventory/ui/item/ItemDetailsViewModelTest.kt
```

### Prompt 3：產生自動新增學號商品的測試

請 AI 產生一個會自動執行的測試，內容為：

```text
自動新增一個 Item Name 為自己的學號、
Item Price 為 100、
Quantity in Stock 為 10 的資料，
並確認新增成功。
```

完成的測試方法：

```kotlin
daoInsert_studentItem_insertsItemIntoDb()
```

新增測試資料：

```text
Item Name: b11309055
Item Price: 100
Quantity in Stock: 10
```

測試檔案位置：

```text
app/src/androidTest/java/com/example/inventory/ItemDaoTest.kt
```

## 我做了哪些修正

AI 產生測試後，我有再檢查並修正以下內容：

1. 修正 `ItemDetailsViewModelTest` 的非同步狀態讀取問題。

   原本測試直接使用：

   ```kotlin
   viewModel.uiState.first()
   ```

   這樣可能只取得初始空狀態，導致測試結果不穩定。因此改成等待真正載入指定 Item：

   ```kotlin
   viewModel.uiState.first { it.itemDetails.id == item.id }
   ```

2. 將測試 Dispatcher 改成 `StandardTestDispatcher`。

   這樣可以讓 coroutine 測試行為更穩定，並搭配：

   ```kotlin
   advanceUntilIdle()
   ```

   確認 ViewModel 內部的 coroutine 都執行完成。

3. 使用 Fake Repository 測試 ViewModel。

   `ItemDetailsViewModelTest.kt` 中建立 `FakeItemsRepository`，用來記錄：

   ```kotlin
   updatedItems
   deletedItems
   ```

   這樣可以確認 ViewModel 是否真的呼叫 `updateItem()` 或 `deleteItem()`。

4. 修正版本相容問題。

   因為專案使用：

   ```kotlin
   compileSdk = 35
   ```

   舊版 Android Gradle Plugin 會出現 `JdkImageTransform` / `jlink.exe` 相關錯誤，所以將 Android Gradle Plugin 升級為：

   ```kotlin
   id("com.android.application") version "8.6.0" apply false
   id("com.android.library") version "8.6.0" apply false
   ```

   並在 `gradle.properties` 加入：

   ```properties
   android.disableJdkImageTransform=true
   ```

## 要執行哪些測試程式

本作業需要執行兩類測試：Unit Test 與 Android Instrumented Test。

### 1. 執行 ItemDetailsViewModel Unit Test

測試檔案：

```text
app/src/test/java/com/example/inventory/ui/item/ItemDetailsViewModelTest.kt
```

執行指令：

```powershell
.\gradlew.bat :app:testDebugUnitTest
```

此測試不需要模擬器。

測試內容包含：

- 數量大於 0 時，`reduceQuantityByOne()` 會呼叫 `updateItem()`。
- 數量等於 0 時，`reduceQuantityByOne()` 不會呼叫 `updateItem()`。
- `deleteItem()` 會呼叫 `itemsRepository.deleteItem()`。

### 2. 執行 ItemDaoTest Android Instrumented Test

測試檔案：

```text
app/src/androidTest/java/com/example/inventory/ItemDaoTest.kt
```

執行指令：

```powershell
.\gradlew.bat :app:connectedDebugAndroidTest
```

此測試需要先啟動 Android 模擬器或連接實體 Android 裝置。

測試內容包含：

- Room 新增資料測試。
- Room 查詢資料測試。
- Room 更新資料測試。
- Room 刪除資料測試。
- 新增相同 ID 時，只保留第一筆資料。
- 自動新增學號 `b11309055`、價格 `100`、庫存 `10` 的資料並確認成功。

## 測試結果

已執行以下測試：

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:connectedDebugAndroidTest
```

測試結果：

```text
BUILD SUCCESSFUL
ItemDetailsViewModelTest: 3 tests passed
ItemDaoTest: 7 tests passed
```

測試通過截圖位於：

```text
test-screenshots/test-results-summary.png
```

## 版本資訊

目前專案版本：

- Java：JDK 17
- Android Gradle Plugin：8.6.0
- Gradle Wrapper：8.11.1
- Kotlin：2.1.0
- Room：2.6.1
- compileSdk：35
- targetSdk：35
