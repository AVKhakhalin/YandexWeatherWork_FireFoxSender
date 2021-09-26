package com.example.yandexweatherwork_firefoxsender.model

import com.example.yandexweatherwork_firefoxsender.view.MainActivity
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import javax.net.ssl.HttpsURLConnection

class RepositoryGetCityCoordinates(
    private val cityName: String,
    private val mainActivity: MainActivity
)
    : Thread()
{

    override fun run() {
        // Открытие сессии
        var urlConnection: HttpURLConnection? = null
        val url =
            URL("https://nominatim.openstreetmap.org/search.php?q=$cityName&format=jsonv2")
        urlConnection = url.openConnection() as HttpsURLConnection
        urlConnection.requestMethod = "GET"
        urlConnection.readTimeout = 10000
        val inf = BufferedReader(InputStreamReader(urlConnection.inputStream))
        val answer = StringBuilder()

        // Распознавание ответа
        var line: String?
        while (inf.readLine().also { line = it } != null) {
            answer.append(line).append('\n')
        }

        // Анализ ответа
        val stringAnswer = answer.toString()
        var shortStringAnswer = stringAnswer.substring(0, stringAnswer?.indexOf(cityName))
        shortStringAnswer = shortStringAnswer.substring(shortStringAnswer?.lastIndexOf("\"lat\":") - 1, shortStringAnswer.length - 1)
        shortStringAnswer?.let{
            if ((shortStringAnswer?.indexOf("\"lat\":") + 7 < shortStringAnswer.length)
                && (shortStringAnswer?.indexOf("\"lat\":") + 7 < shortStringAnswer?.indexOf("\"lon\":") - 2)
                && (shortStringAnswer?.indexOf("\"lon\":") + 8 < shortStringAnswer.length)) {
                val strLat = shortStringAnswer?.subSequence(
                    shortStringAnswer?.indexOf("\"lat\":") + 7,
                    shortStringAnswer?.indexOf("\"lon\":") - 2
                ) as String
                val startIndex: Int = shortStringAnswer?.indexOf("\"lon\":") + 8
                if ((startIndex < shortStringAnswer.length)
                    && (shortStringAnswer?.indexOf("\",\"", startIndex) < shortStringAnswer.length)) {
                    val strLon = shortStringAnswer?.subSequence(
                        shortStringAnswer?.indexOf("\"lon\":") + 7,
                        shortStringAnswer?.indexOf("\",\"", startIndex)
                    ) as String
                    val lat: Double = strLat.toDouble()
                    val lon: Double = strLon.toDouble()
                    mainActivity.setLat(lat)
                    mainActivity.setLon(lon)
                }
            }
        }
        // Закрытие сессии
        urlConnection.disconnect()
    }
}