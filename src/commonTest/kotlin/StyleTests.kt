package org.shabunc.kassqade.core.tests

import org.shabunc.kassqade.core.style
import kotlin.test.Test
import kotlin.test.assertContentEquals

class StyleTests {
    @Test
    fun stylesTest() {
        val style = style {
            ".some" {
                color = "red"
                "background-color"("green")
            }
        }

        assertContentEquals(
            listOf("color: red;background-color: green;"),
            style.serialize()
        )
    }
}