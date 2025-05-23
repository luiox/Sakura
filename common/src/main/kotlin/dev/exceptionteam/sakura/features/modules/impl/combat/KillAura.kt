package dev.exceptionteam.sakura.features.modules.impl.combat

import dev.exceptionteam.sakura.events.impl.TickEvents
import dev.exceptionteam.sakura.events.nonNullListener
import dev.exceptionteam.sakura.features.modules.Category
import dev.exceptionteam.sakura.features.modules.Module
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTarget
import dev.exceptionteam.sakura.managers.impl.TargetManager.getTargetPlayer
import dev.exceptionteam.sakura.utils.math.distanceSqTo
import dev.exceptionteam.sakura.utils.player.InteractionUtils.attack
import dev.exceptionteam.sakura.utils.timing.TimerUtils
import net.minecraft.world.item.Item
import net.minecraft.world.item.Items

object KillAura: Module(
    name = "kill-aura",
    category = Category.COMBAT
) {
    private val attackRange by setting("attack-range", 3.0f, 2.5f..6.0f)
    private val targetRange by setting("target-range", 3.0f, 2.5f..6.0f)
    private val delay by setting("delay", 500, 0..3000)
    private val onlyPlayers by setting("only-players", true)
    private val onlySword by setting("only-sword", true)
    private val rotation by setting("rotation", true)
    private val swing by setting("swing", true)

    private val timer = TimerUtils().apply { reset() }

    init {
        nonNullListener<TickEvents.Update> {
            if (!timer.passedAndReset(delay)) return@nonNullListener

            if (onlySword && !isSword(player.mainHandItem.item)) return@nonNullListener

            if (onlyPlayers) getTargetPlayer(targetRange)?.let {
                if (it.distanceSqTo(mc.player!!) <= attackRange) attack(it, rotation, swing)
            } else getTarget(targetRange)?.let {
                if (it.distanceSqTo(mc.player!!) <= attackRange) attack(it, rotation, swing)
            }
        }
    }

    private fun isSword(item: Item): Boolean = when (item) {
        Items.WOODEN_SWORD -> true
        Items.STONE_SWORD -> true
        Items.GOLDEN_SWORD -> true
        Items.IRON_SWORD -> true
        Items.DIAMOND_SWORD -> true
        Items.NETHERITE_SWORD -> true
        else -> false
    }

}
