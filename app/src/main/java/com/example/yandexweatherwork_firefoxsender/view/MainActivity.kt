package com.example.yandexweatherwork_firefoxsender.view

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.yandexweatherwork_firefoxsender.Constants
import com.example.yandexweatherwork_firefoxsender.R
import com.example.yandexweatherwork_firefoxsender.databinding.ActivityMainBinding
import com.example.yandexweatherwork_firefoxsender.model.City
import com.example.yandexweatherwork_firefoxsender.model.RepositoryGetCityCoordinates
import java.lang.Thread.sleep

class MainActivity : AppCompatActivity() {
    //region ЗАДАНИЕ ПЕРЕМЕННЫХ
    private var _binding: ActivityMainBinding? = null
    private val binding: ActivityMainBinding
        get() {
            return _binding!!
        }
    private var lat: Double = 0.0
    private var lon: Double = 0.0
    private var city: City? = null
    //endregion

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        _binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(_binding!!.root)

        // Инициализация элементов
        initElements()
    }

    // Инициализация элементов
    private fun initElements() {
        // Передача нового места в погодное приложение
        binding.inputCityInfoButtonOk.setOnClickListener {
            val cityName: String = binding.inputCityInfoCityField.text.toString()
            val cityLat: String = binding.inputCityInfoLatField.text.toString()
            val cityLon: String = binding.inputCityInfoLonField.text.toString()
            val cityCountry: String = binding.inputCityInfoCountryField.text.toString()
            if ((cityLat.length == 0) || (cityLon.length == 0)) {
                searchLatLonCoordinates(cityName)
                sleep(1000)
            } else {
                lat = cityLat.toDouble()
                lon = cityLon.toDouble()
            }
            city = City(cityName, lat, lon, cityCountry)

            // Формируем интент
            val intent = Intent()
            intent.action = Constants.ACTION_SEND_MSG
            // Добавим параметр
            city?.let{
                intent.putExtra(Constants.NAME_MSG_CITY_NAME, city?.name)
                intent.putExtra(Constants.NAME_MSG_CITY_LAT, city?.lat)
                intent.putExtra(Constants.NAME_MSG_CITY_LON, city?.lon)
                intent.putExtra(Constants.NAME_MSG_CITY_COUNTRY, city?.country)
            }
            // Указываем флаг поднятия приложения
            // (без него будут получать уведомления только
            // загруженные приложения)
            intent.addFlags(Constants.FLAG_RECEIVER_INCLUDE_BACKGROUND)
            // Отправка сообщения
            sendBroadcast(intent)
        }
        // Выход из приложения
        binding.inputCityInfoButtonCancel.setOnClickListener {
            // Закрыть приложение
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                finishAndRemoveTask()
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                finishAffinity()
            } else {
                finish()
            }
            System.exit(0)
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    private fun searchLatLonCoordinates(cityName: String) {
        // Поиск координат места в случае, если их не заполнили для данного места
        val repositoryGetCityCoordinates: RepositoryGetCityCoordinates
                = RepositoryGetCityCoordinates(cityName, this@MainActivity)
        // Запуск класса repositoryGetCityCoordinates в новом потоке
        val thread = Thread {
            try {
                repositoryGetCityCoordinates.start()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
        thread.start()
    }

    fun setLat(lat: Double) {
        this.lat = lat
    }
    fun setLon(lon: Double) {
        this.lon = lon
    }
}