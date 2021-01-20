package com.codinginflow.mvvmnewsapp.features.searchnews

import androidx.hilt.Assisted
import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.cachedIn
import com.codinginflow.mvvmnewsapp.data.NewsArticle
import com.codinginflow.mvvmnewsapp.data.NewsRepository
import kotlinx.coroutines.launch

class SearchNewsViewModel @ViewModelInject constructor(
    private val repository: NewsRepository,
    @Assisted state: SavedStateHandle
) : ViewModel() {

    private val currentQuery = state.getLiveData<String?>("currentQuery")

    val newsArticles = currentQuery.switchMap { query ->
        repository.getSearchResults(query).asLiveData().cachedIn(viewModelScope)
    }

    fun searchArticles(query: String) {
        currentQuery.value = query
    }

    fun onManualRefresh() {
        // TODO: 20.01.2021 Let Zhuinden check this
        currentQuery.value = currentQuery.value
    }

    fun onBookmarkClick(article: NewsArticle) {
        val currentlyBookmarked = article.isBookmarked
        val updatedArticle = article.copy(isBookmarked = !currentlyBookmarked)
        viewModelScope.launch {
            repository.update(updatedArticle)
        }
    }
}