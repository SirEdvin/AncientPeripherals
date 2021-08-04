package site.siredvin.progressiveperipherals.extra.network.api;

import dan200.computercraft.api.lua.LuaException;

@FunctionalInterface
public interface PeripheralFunction<T, V> {
    V apply(T t) throws LuaException;
}
