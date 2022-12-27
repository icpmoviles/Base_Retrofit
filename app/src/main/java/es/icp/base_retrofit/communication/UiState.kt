package es.icp.base_retrofit.communication

sealed class UiState {
    object Success : UiState()
    object Loading: UiState()
    object Empty: UiState()
    data class Error (val code: Int, val message: String): UiState()
}
