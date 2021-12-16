package org.shabunc.kassqade.core

data class StringProperty(val name: String, val value: String)

interface Selector {
    val selector: String
    var color: String?

    val stringProperties: MutableList<StringProperty>

    operator fun String.invoke(value: String) {
        stringProperties.add(StringProperty(this, value))
    }

    fun serialize(): String {
        var res = ""
        stringProperties.forEach { property ->
            res += "${property.name}: ${property.value};"
        }
        return res
    }
}

private class SelectorImplementation(override val selector: String) : Selector {
    override val stringProperties: MutableList<StringProperty> = mutableListOf()
    override var color: String? = null
        get() = field
        set(value) {
            field = value
            if (value != null) {
                stringProperties.add(StringProperty("color", value))
            }
        }
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