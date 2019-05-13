package com.android.shashavs.guardianclient.view_model

import com.android.shashavs.guardianclient.repository.Repository
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner
import org.junit.Assert.*
import org.mockito.Mockito.*

@RunWith(MockitoJUnitRunner::class)
class NewsViewModelTest {

    @Mock
    private lateinit var repository: Repository
    private lateinit var viewModel: NewsViewModel
    private val apiKey = "apiKey"

    @Before
    fun setUp() {
        viewModel = NewsViewModel(repository)
        viewModel.initDataSourceLiveData(apiKey)
    }

    @Test
    fun initDataSourceLiveData() {
        assertNotNull(viewModel.factory)
        assertNotNull(viewModel.pagedListLiveData)
    }

    @Test
    fun initRefresh() {
        viewModel.initRefresh(apiKey)
        verify(repository).refresh(apiKey, 1)
    }

    @Test
    fun refreshLiveData() {
        viewModel.refreshLiveData()
        verify(repository).getRefreshLiveData()
    }

    @Test
    fun search() {
        viewModel.factory = spy(viewModel.factory)
        val query = "query"
        viewModel.search(query)
        verify(viewModel.factory)?.search(query)
    }

    @Test
    fun getDescription() {
        val id = "id"
        viewModel.getDescription(apiKey, id)
        verify(repository).getDescription(apiKey, "id")
    }

    @Test
    fun clear() {
        viewModel.clear()
        verify(repository).clear()
        assertNull(viewModel.factory)
        assertNull(viewModel.pagedListLiveData)
    }

}