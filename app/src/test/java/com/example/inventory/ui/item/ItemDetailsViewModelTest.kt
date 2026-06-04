/*
 * Copyright (C) 2023 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.example.inventory.ui.item

import androidx.lifecycle.SavedStateHandle
import com.example.inventory.data.Item
import com.example.inventory.data.ItemsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.TestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestWatcher
import org.junit.runner.Description

@OptIn(ExperimentalCoroutinesApi::class)
class ItemDetailsViewModelTest {

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Test
    fun reduceQuantityByOne_whenQuantityGreaterThanZero_updatesRepository() = runTest {
        val item = Item(1, "Apples", 10.0, 2)
        val repository = FakeItemsRepository(item)
        val viewModel = ItemDetailsViewModel(savedStateHandleForItem(item.id), repository)

        viewModel.uiState.first { it.itemDetails.id == item.id }
        viewModel.reduceQuantityByOne()
        advanceUntilIdle()

        assertEquals(1, repository.updatedItems.size)
        assertEquals(item.copy(quantity = 1), repository.updatedItems.first())
    }

    @Test
    fun reduceQuantityByOne_whenQuantityIsZero_doesNotUpdateRepository() = runTest {
        val item = Item(1, "Apples", 10.0, 0)
        val repository = FakeItemsRepository(item)
        val viewModel = ItemDetailsViewModel(savedStateHandleForItem(item.id), repository)

        viewModel.uiState.first { it.itemDetails.id == item.id }
        viewModel.reduceQuantityByOne()
        advanceUntilIdle()

        assertEquals(0, repository.updatedItems.size)
    }

    @Test
    fun deleteItem_callsRepositoryDelete() = runTest {
        val item = Item(1, "Apples", 10.0, 2)
        val repository = FakeItemsRepository(item)
        val viewModel = ItemDetailsViewModel(savedStateHandleForItem(item.id), repository)

        viewModel.uiState.first { it.itemDetails.id == item.id }
        viewModel.deleteItem()

        assertEquals(1, repository.deletedItems.size)
        assertEquals(item, repository.deletedItems.first())
    }

    private fun savedStateHandleForItem(itemId: Int): SavedStateHandle {
        return SavedStateHandle(mapOf(ItemDetailsDestination.itemIdArg to itemId))
    }
}

@OptIn(ExperimentalCoroutinesApi::class)
class MainDispatcherRule(
    private val testDispatcher: TestDispatcher = StandardTestDispatcher()
) : TestWatcher() {
    override fun starting(description: Description) {
        Dispatchers.setMain(testDispatcher)
    }

    override fun finished(description: Description) {
        Dispatchers.resetMain()
    }
}

private class FakeItemsRepository(initialItem: Item) : ItemsRepository {
    private val itemFlow = MutableStateFlow<Item?>(initialItem)
    val updatedItems = mutableListOf<Item>()
    val deletedItems = mutableListOf<Item>()

    override fun getAllItemsStream(): Flow<List<Item>> {
        return itemFlow.map { listOfNotNull(it) }
    }

    override fun getItemStream(id: Int): Flow<Item?> {
        return itemFlow
    }

    override suspend fun insertItem(item: Item) {
        itemFlow.value = item
    }

    override suspend fun deleteItem(item: Item) {
        deletedItems.add(item)
        if (itemFlow.value?.id == item.id) {
            itemFlow.value = null
        }
    }

    override suspend fun updateItem(item: Item) {
        updatedItems.add(item)
        itemFlow.value = item
    }
}
