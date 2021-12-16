package org.shabunc.kassqade.core

interface SelectorContext {
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

private class SelectorContextImplementation(override val selector: String) : SelectorContext {
    override var color: String? = null
}

interface StyleContext {
    val selectors: MutableList<SelectorContext>

    operator fun String.invoke(context: SelectorContext.() -> Unit) {
        val selector = SelectorContextImplementation(this)
        selectors.add(selector)
        context.invoke(selector)
    }

    fun serialize(): List<String> {
        return selectors.map { it.serialize() }
    }
}

private class StyleContextImplementation : StyleContext {
    override val selectors: MutableList<SelectorContext> = mutableListOf()
}

fun style(context: StyleContext.() -> Unit): StyleContext {
    val styleContextImplementation = StyleContextImplementation()
    context.invoke(styleContextImplementation)
    return styleContextImplementation
}

fun main() {
    style {
        ".hey" {
            color =  "red"
        }
    }
}