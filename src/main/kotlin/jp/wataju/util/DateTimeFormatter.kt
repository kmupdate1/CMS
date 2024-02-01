package jp.wataju.util

import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

fun formatDateTime(localDateTime: LocalDateTime): String
= localDateTime.format(DateTimeFormatter.ofPattern(
    "yyyy年MM月dd日（E曜日）HH時mm分ss秒",
    Locale.JAPAN
))

fun formatDateTime(localDateTime: String): String {
    val datas = localDateTime.split("T")
    val dates = datas[0].split("-")
    val times = datas[1].split(":")

    return "${dates[0]}年${dates[1]}月${dates[2]}日 ${times[0]}時${times[1]}分"
}
