package jp.wataju.util

data class Paging<Entity>(
    val entities: MutableList<Entity?>,
    val max: Int,
    val group: Int
) {
    // ページ（ブロック）数の算出
    private val size = entities.size
    private val pages = (size / max) + if ((size % max) != 0) 1 else 0

    val previous = if (group == 1) 1 else group - 1
    val next = if (group == pages) group else group + 1
    val numerator = group
    val denominator = pages

    val enabled = group <= pages
}

const val MAX = 0
const val MAX_CUSTOMERID = 1
