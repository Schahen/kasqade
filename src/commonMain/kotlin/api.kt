package org.shabunc.kassqade.core

import kotlin.reflect.KProperty

data class StringProperty(val name: String, val value: String)

interface Selector {
    val selector: String
    var color: String?
    var display: String?
    var clear: String?

    val stringProperties: MutableList<StringProperty>
    val selectors: MutableList<Selector>

    operator fun String.invoke(value: String) {
        stringProperties.add(StringProperty(this, value))
    }

    operator fun String.invoke(context: Selector.() -> Unit) {
        val selector = SelectorImplementation("$selector $this")
        selectors.add(selector)
        context.invoke(selector)
    }

    fun serialize(): String {
        var res = "$selector {"
        stringProperties.forEach { property ->
            res += "${property.name}: ${property.value};"
        }
        res += "}"
        return res
    }
}

private class StringPropertyDelegate(private var value: String?) {
    operator fun getValue(owner: SelectorImplementation, property: KProperty<*>): String? {
        return value
    }

    operator fun setValue(owner: SelectorImplementation, property: KProperty<*>, value: String?) {
        if (value != null) {
            owner.stringProperties.add(StringProperty(property.name, value))
        }
        this.value = value
    }
}

private class SelectorImplementation(override val selector: String) : Selector {
    override val stringProperties: MutableList<StringProperty> = mutableListOf()
    override val selectors: MutableList<Selector> = mutableListOf()

    override var color: String? by StringPropertyDelegate(null)
    override var display: String? by StringPropertyDelegate(null)
    override var clear: String? by StringPropertyDelegate(null)


}

interface Style {
    val selectors: MutableList<Selector>

    operator fun String.invoke(context: Selector.() -> Unit) {
        val selector = SelectorImplementation(this)
        selectors.add(selector)
        context.invoke(selector)
    }

    fun serialize(): List<String> {
        return selectors.flatMap {
            listOf(it.serialize()) + it.selectors.map { selector -> selector.serialize() }
        }
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