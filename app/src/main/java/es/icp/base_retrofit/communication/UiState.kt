package es.icp.base_retrofit.communication

sealed class UiState {
    object Initial : UiState()
    object Success : UiState()
    data class Succes(val baseApiResponse: BaseApiResponse<*>?): UiState()
    object Loading: UiState()
    object Empty: UiState()
    data class Error (val code: Int, val message: String): UiState()
}
