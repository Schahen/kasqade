package org.shabunc.kassqade.core.tests

import org.shabunc.kassqade.core.style
import kotlin.test.Test
import kotlin.test.assertContentEquals
import kotlin.test.assertEquals

class StyleTests {
    @Test
    fun stylesTest() {
        val style = style {
            ".some" {
                "background-color"("green")
                color = "red"
                "font-weight"("bold")
            }
        }

        assertEquals("red", style.selectors.first().color)

        assertContentEquals(
            listOf("background-color: green;color: red;font-weight: bold;"),
            style.serialize()
        )
    }
}