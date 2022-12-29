package es.icp.base_retrofit.models

sealed class UiState {
    object Initial : UiState()
    object Success : UiState()
    object Loading: UiState()
    object Empty: UiState()
    data class Error(val code: Int, val message: String) : UiState()
}
