package com.example.yandexweatherwork_firefoxsender

class Constants {
    companion object {
        @JvmField
        val ACTION_SEND_TITLE: String = "Send_title"
        @JvmField
        val SEND_TITLE: String = "Title"
        @JvmField
        // Эта константа спрятана в Intent классе,
        // Но, именно посредством её можно поднять приложение
        val FLAG_RECEIVER_INCLUDE_BACKGROUND: Int = 0x01000000
        @JvmField
        // Имя ACTION для Broadcast, по нему Receiver и будет определяться
        val ACTION_SEND_MSG: String = "broadcastsender.city"
        @JvmField
        val NAME_MSG_CITY_NAME = "MSG_CITY_NAME"
        @JvmField
        val NAME_MSG_CITY_LAT = "MSG_CITY_LAT"
        @JvmField
        val NAME_MSG_CITY_LON = "MSG_CITY_LON"
        @JvmField
        val NAME_MSG_CITY_COUNTRY = "MSG_CITY_COUNTRY"
    }
    annotation class JvmField
}