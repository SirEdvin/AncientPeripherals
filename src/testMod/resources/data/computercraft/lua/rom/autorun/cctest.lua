--- Extend the test API with some convenience functions.
--
-- It's much easier to declare these in Lua rather than Java.

local pretty = require("cc.pretty")

local function matches(eq, exact, left, right)
    if left == right then return true end

    local ty = type(left)
    if ty ~= type(right) or ty ~= "table" then return false end

    -- If we've already explored/are exploring the left and right then return
    if eq[left] and eq[left][right] then return true end
    if not eq[left]  then eq[left] = { [right] = true } else eq[left][right] = true end
    if not eq[right] then eq[right] = { [left] = true } else eq[right][left] = true end

    -- Verify all pairs in left are equal to those in right
    for k, v in pairs(left) do
        if not matches(eq, exact, v, right[k]) then return false end
    end

    if exact then
        -- And verify all pairs in right are present in left
        for k in pairs(right) do
            if left[k] == nil then return false end
        end
    end

    return true
end

function test.assert(ok, ...)
    if ok then return ... end

    test.fail(... and tostring(...) or "Assertion failed")
end

function test.eq(expected, actual, msg)
    if expected == actual then return end

    local message = ("Assertion failed:\nExpected %s,\ngot %s"):format(expected, actual)
    if msg then message = ("%s - %s"):format(msg, message) end
    test.fail(message)
end

function test.neq(expected, actual, msg)
    if expected ~= actual then return end

    local message = ("Assertion failed:\nExpected something different to %s"):format(expected)
    if msg then message = ("%s - %s"):format(msg, message) end
    test.fail(message)
end

function test.tableEq(expected, actual)
    if not matches({}, ture, expected, actual) then
        local message = ("Assertion failed:\nTable %s are not same to %s"):format(
            pretty.render(pretty.pretty(expected)),
            pretty.render(pretty.pretty(actual))
        )
        test.fail(message)
    end
end