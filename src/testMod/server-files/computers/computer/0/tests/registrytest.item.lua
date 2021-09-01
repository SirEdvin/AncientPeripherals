local p = peripheral.find("itemRegistry")
local itemDescription = p.getItemDescription("minecraft:oak_log")
test.assert(itemDescription)
test.tableEq(itemDescription, { displayName = "Oak Log", id = "minecraft:oak_log", tags = { "minecraft:logs", "minecraft:logs_that_burn", "minecraft:oak_logs" } })
