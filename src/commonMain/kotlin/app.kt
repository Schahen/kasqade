package org.shabunc.kassqade.core

interface Selector {
    val selector: String
    var color: String?

    fun serialize(): String {
        var res = ""
        if (color != null) {
            res += "color: $color;"
        }
        return res
    }
}

private class SelectorImplementation(override val selector: String) : Selector {
    override var color: String? = null
}

interface Style {
    val selectors: MutableList<Selector>

    operator fun String.invoke(context: Selector.() -> Unit) {
        val selector = SelectorImplementation(this)
        selectors.add(selector)
        context.invoke(selector)
    }

    fun serialize(): List<String> {
        return selectors.map { it.serialize() }
    }
}

private class StyleImplementation : Style {
    override val selectors: MutableList<Selector> = mutableListOf()
}

fun style(context: Style.() -> Unit): Style {
    val styleImplementation = StyleImplementation()
    context.invoke(styleImplementation)
    return styleImplementation
}

fun main() {
    style {
        ".hey" {
            color =  "red"
        }
    }
}