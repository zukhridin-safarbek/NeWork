package kg.zukhridin.nework.model

sealed interface StateModel {
    object Idle : StateModel
    object Error : StateModel
    object Loading : StateModel
    object Refreshing : StateModel
}