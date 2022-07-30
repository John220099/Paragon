package com.paragon.client.ui.configuration.discord.settings.impl

import com.paragon.api.setting.Setting
import com.paragon.api.util.string.StringUtil
import com.paragon.client.systems.module.impl.client.ClientFont
import com.paragon.client.systems.module.impl.client.Colours
import com.paragon.client.ui.configuration.discord.settings.DiscordSetting
import com.paragon.client.ui.util.Click
import org.lwjgl.util.Rectangle

/**
 * @author SooStrator1136
 */
class DiscordEnum(val setting: Setting<Enum<*>>) : DiscordSetting(setting) {

    private val options = setting.value.javaClass.enumConstants
    private val optionRects = arrayOfNulls<Rectangle>(options.size)

    init {
        for (i in options.indices) {
            optionRects[i] = Rectangle()
        }
    }

    override fun render(mouseX: Int, mouseY: Int) {
        super.render(mouseX, mouseY)

        var currY = bounds.y + fontHeight + 1F
        var currX = bounds.x
        optionRects.forEachIndexed { i, rect ->
            (rect ?: return@forEachIndexed).setBounds(
                currX,
                currY.toInt(),
                getStringWidth(getFormattedName(options[i])).toInt(),
                fontHeight.toInt()
            )
            if (rect.x + rect.width > bounds.x + bounds.width) {
                currX = bounds.x
                currY += fontHeight + 1F
                rect.x = bounds.x
                rect.y = currY.toInt()
            }

            currX += (rect.width + getStringWidth(", ")).toInt()

            renderText(
                getFormattedName(options[i]) + if (i != options.size - 1) "," else "",
                rect.x.toFloat(),
                rect.y.toFloat(),
                if (rect.contains(mouseX, mouseY)) Colours.mainColour.value.rgb else -1
            )
        }

        val lastRect = optionRects[optionRects.size - 1]
        if (lastRect != null) {
            bounds.height = (lastRect.y + lastRect.height) - bounds.y
        }
    }

    private fun getFormattedName(enum: Enum<*>): String {
        val str = StringUtil.getFormattedText(enum)
        return if (str == StringUtil.getFormattedText(setting.value)) if (ClientFont.isEnabled) str.uppercase() else "§n$str" else str
    }

    override fun onClick(mouseX: Int, mouseY: Int, button: Int) {
        if (button != Click.LEFT.button) {
            return
        }

        optionRects.forEachIndexed { i, rect ->
            if (rect == null) {
                return@forEachIndexed
            }

            if (rect.contains(mouseX, mouseY)) {
                setting.setValue(options[i])
                return
            }
        }
    }

    override fun onKey(keyCode: Int) {}

}