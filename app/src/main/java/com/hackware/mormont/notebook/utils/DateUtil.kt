package com.hackware.mormont.notebook.utils

import java.text.SimpleDateFormat
import java.util.*

 fun Date.toSimpleString(format: String): String {
     val format = SimpleDateFormat(format)
     return format.format(this)
 }
object DateUtil {
    fun getCurrentDate(): Date {
        return Calendar.getInstance().time
    }
}
