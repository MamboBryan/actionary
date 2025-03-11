package dev.mambo.lib.ui.presentation.helpers

sealed interface ItemUiState<out T> {
    object Loading : ItemUiState<Nothing>
    data class Error(val message: String) : ItemUiState<Nothing>
    data class Success<T>(val data: T) : ItemUiState<T>
}

sealed interface ListUiState<out T> {
    object Empty: ListUiState<Nothing>
    object Loading : ListUiState<Nothing>
    data class Error(val message: String) : ListUiState<Nothing>
    data class NotEmpty<T>(val data: List<T>) : ListUiState<T>
}
