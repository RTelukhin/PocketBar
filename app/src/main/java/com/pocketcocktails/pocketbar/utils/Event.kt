package com.pocketcocktails.pocketbar.utils

import com.pocketcocktails.pocketbar.utils.Constants.EMPTY_STRING


data class Event<out T>(val status: Status, val data: T, val message: String) {

    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object {
        fun <T> success(data: T): Event<T> =
            Event(status = Status.SUCCESS, data = data, message = EMPTY_STRING)

        fun <T> loading(data: T): Event<T> =
            Event(status = Status.LOADING, data = data, message = EMPTY_STRING)

        fun <T> error(data: T, message: String): Event<T> =
            Event(status = Status.ERROR, data = data, message = message)
    }
}