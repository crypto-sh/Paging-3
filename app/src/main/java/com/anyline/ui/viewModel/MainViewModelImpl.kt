package com.anyline.ui.viewModel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.anyline.dto.User
import com.anyline.repository.GithubRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*


class MainViewModelImpl(
    private val repository: GithubRepository,
    private val savedStateHandle: SavedStateHandle
) : MainViewModel() {

    companion object {
        const val PAGE_SIZE = 20
        const val KEY_QUERY = "query"
    }
    init {
        if (!savedStateHandle.contains(KEY_QUERY)) {
            savedStateHandle.set(KEY_QUERY, "")
        }
    }

    private val clearListChannel = Channel<Unit>(Channel.CONFLATED)

    override fun onCreated() {
        super.onCreated()
        clearListChannel.offer(Unit)
        savedStateHandle.set(KEY_QUERY, "")
    }

    override fun fetchUsers(query: String) {
        val current = savedStateHandle.get<String>(KEY_QUERY)
        if (current == query) return

        clearListChannel.offer(Unit)
        savedStateHandle.set(KEY_QUERY, query)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun getUsers(): Flow<PagingData<User>> {
        return flowOf(
            clearListChannel.receiveAsFlow().map { PagingData.empty() },
            savedStateHandle.getLiveData<String>(KEY_QUERY)
                .asFlow()
                .flatMapLatest {
                    repository.getUsers(it, PAGE_SIZE)
                }.cachedIn(viewModelScope)
        ).flattenMerge(2)
    }
}
