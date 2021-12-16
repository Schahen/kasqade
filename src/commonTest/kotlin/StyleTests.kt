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
            listOf(".some {background-color: green;color: red;font-weight: bold;}"),
            style.serialize()
        )
    }

    @Test
    fun settingMultipleTimes() {
        val style = style {
            ".some" {
                color = "red"
                color = "yellow"
                color = "green"
                color = "maroon"
            }
        }

        assertEquals("maroon", style.selectors.first().color)

        assertContentEquals(
            listOf(".some {color: red;color: yellow;color: green;color: maroon;}"),
            style.serialize()
        )
    }

    @Test
    fun displayTest() {
        val style = style {
            ".some" {
                display = "block"
             }
        }

        assertEquals("block", style.selectors.first().display)

        assertContentEquals(
            listOf(".some {display: block;}"),
            style.serialize()
        )
    }

    @Test
    fun clearTest() {
        val style = style {
            ".some" {
                clear = "right"
            }
        }

        assertEquals("right", style.selectors.first().clear)

        assertContentEquals(
            listOf(".some {clear: right;}"),
            style.serialize()
        )
    }

    @Test
    fun simpleNestedTest() {
        val style = style {
            ".some" {
                ".thing" {
                    color = "yellow"
                }
            }
        }

        assertContentEquals(
            listOf(".some {}", ".some .thing {color: yellow;}"),
            style.serialize()
        )
    }

}