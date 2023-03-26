package ru.netology.nmedia.model

import ru.netology.nmedia.model.ActionType

data class FeedModelState(
    val loading: Boolean = false,
    val error: Boolean = false,
    val refreshing: Boolean = false,
    val actionType: ActionType = ActionType.NULL,
    val actionId: Long = 0
)