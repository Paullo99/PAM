package com.example.mountaineer.viewmodel

import androidx.lifecycle.ViewModel
import java.util.*

class AddExpeditionActivityViewModel : ViewModel(){

    var mountainName = ""
    var mountainRangePosiotion = 0
    var height = ""
    var conquerDate = setTodayDate()
    var photoFileName = ""

    private fun setTodayDate(): String{
        val calendar = Calendar.getInstance()

        return "%d-%02d-%02d".format(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH) + 1,
            calendar.get(Calendar.DAY_OF_MONTH)
        )
    }
}