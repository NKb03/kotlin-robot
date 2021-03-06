/**
 *@author Nikolaus Knop
 */

package krobot.api

import krobot.impl.KFile

@KRobotDsl
class KTypeParametersRobot private constructor(private val file: KFile) {
    private var isFirst = true

    private fun addParameter(variance: String?, type: KtType, upperBound: KtType?) {
        with(file) {
            if (isFirst) write("<")
            else write(", ")
            if (variance != null) {
                write(variance)
                write(" ")
            }
            write(type.toString())
            if (upperBound != null) {
                write(": ")
                write(upperBound.toString())
            }
        }
        isFirst = false
    }

    fun invariant(type: KtType, upperBound: KtType? = null) {
        addParameter(null, type, upperBound)
    }

    fun covariant(type: KtType, upperBound: KtType? = null) {
        addParameter("out", type, upperBound)
    }

    fun contravariant(type: KtType, upperBound: KtType? = null) {
        addParameter("in", type, upperBound)
    }

    fun invariant(type: String, upperBound: KtType? = null) {
        addParameter(null, type(type), upperBound)
    }

    fun covariant(type: String, upperBound: KtType? = null) {
        addParameter("out", type(type), upperBound)
    }

    fun contravariant(type: String, upperBound: KtType? = null) {
        addParameter("in", type(type), upperBound)
    }

    internal fun finish() {
        file.write(">")
    }

    @PublishedApi internal companion object {
        fun write(file: KFile, typeParameters: KTypeParametersRobot.() -> Unit) {
            val robot = KTypeParametersRobot(file)
            robot.typeParameters()
            if (!robot.isFirst) {
                robot.finish()
            }
        }
    }
}