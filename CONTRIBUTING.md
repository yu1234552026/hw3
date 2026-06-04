# 貢獻說明

本專案是 Android Basics with Compose 課程作業專案，主要用途為練習 Room、ViewModel、Repository 與自動化測試。

## 提交修改前

請先確認專案可以正常建置，並執行相關測試：

```powershell
.\gradlew.bat :app:testDebugUnitTest
.\gradlew.bat :app:connectedDebugAndroidTest
```

其中 `connectedDebugAndroidTest` 需要先啟動 Android 模擬器或連接實體裝置。

## 程式碼風格

- Kotlin 程式碼請維持官方 Kotlin code style。
- 測試名稱需清楚描述測試情境與預期結果。
- 修改資料庫、Repository 或 ViewModel 行為時，請同步補上對應測試。

## Pull Request

若要提交 Pull Request，請在說明中簡短列出：

- 修改了哪些功能或測試。
- 執行過哪些測試。
- 是否有需要老師或 reviewer 特別注意的版本或環境問題。
