package nl.jamiecraane.raytracing.util

/**
 * Very simple (and not correct because only the add(element) is implemented) delegation of MutableList which is bounded by maxCapacity.
 */
class BoundedList<E>(private val maxCapacity: Int, private val list: MutableList<E>) : MutableList<E> by list {
    override fun add(element: E): Boolean {
        if (size < maxCapacity) {
            list.add(element)
            return true
        }

        return false
    }

    override fun toString(): String {
        return "BoundedList(maxCapacity=$maxCapacity, list=$list)"
    }
}