package dev.exceptionteam.sakura.features.gui.shared.component

import dev.exceptionteam.sakura.features.modules.impl.client.UiSetting
import dev.exceptionteam.sakura.features.settings.*
import dev.exceptionteam.sakura.graphics.utils.RenderUtils2D
import dev.exceptionteam.sakura.graphics.font.FontRenderers
import dev.exceptionteam.sakura.utils.control.MouseButtonType

class SliderComponent(
    width: Float, height: Float, private val setting: NumberSetting<*>
): AbstractComponent(0f, 0f, width, height) {

    override val visible: Boolean
        get() = setting.visibility.invoke()

    private var isSliding = false

    override fun render() {

        if (isSliding) {
            val startX = x
            val endX = x + width
            val value0 = (mouseX - startX) / (endX - startX) * (setting.maxValue.toFloat() - setting.minValue.toFloat()) + setting.minValue.toFloat()

            val value = value0.coerceIn(setting.minValue.toFloat(), setting.maxValue.toFloat())

            when (setting) {
                is IntSetting -> setting.value = value.toInt()
                is LongSetting -> setting.value = value.toLong()
                is FloatSetting -> setting.value = value
                is DoubleSetting -> setting.value = value.toDouble()
                else -> {}
            }
        }

        val value = String.format("%.2f", setting.value.toFloat())
        FontRenderers.drawString("${setting.key.translation}: $value", x + 5f, y + 4f, UiSetting.textColor, UiSetting.shadow)

        val sliderLength =
            width * ((setting.value.toFloat() - setting.minValue.toFloat()) / (setting.maxValue.toFloat() - setting.minValue.toFloat()))

        RenderUtils2D.drawRectFilled(x, y + height - 2.5f, sliderLength, 2.5f , UiSetting.sliderColor)
    }

    override fun mouseClicked(type: MouseButtonType): Boolean {
        if (type == MouseButtonType.LEFT) {
            isSliding = true
            return true
        }
        return false
    }

    override fun mouseReleased(type: MouseButtonType): Boolean {
        if (type == MouseButtonType.LEFT) {
            isSliding = false
            return true
        }
        return false
    }

}