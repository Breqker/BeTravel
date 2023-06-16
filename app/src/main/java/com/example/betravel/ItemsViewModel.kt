package com.example.betravel

class ItemsViewModel(val image: Int, val text: String) {

    var isItemSelected: Boolean = false
        private set

    fun selectItem() {
        isItemSelected = true
    }

    fun deselectItem() {
        isItemSelected = false
    }
    override fun toString(): String {
        return "$text"
    }
}