package siredvin.site.progressiveperipherals.testmod.tests

import dan200.computercraft.ingame.api.GameTest
import dan200.computercraft.ingame.api.GameTestHelper
import dan200.computercraft.ingame.api.sequence
import dan200.computercraft.ingame.api.thenComputerOk

class RegistryTest {
    @GameTest(timeoutTicks = 1000, batch = "hated")
    fun item(context: GameTestHelper) = context.sequence {
        thenComputerOk()
    }
}