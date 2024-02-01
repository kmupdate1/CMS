package jp.wataju.util

class DivideRecords<Entity>(
    val entities: MutableList<Entity?>,
    val max: Int,
    val group: Int,
) {
    private val size = entities.size
    private val fromIndex = max * (group - 1)
    private val toIndex   = fromIndex + if ((fromIndex + max) <= size) max else size % max

    val divided = if (size > max) entities.subList(fromIndex, toIndex) else entities
}
