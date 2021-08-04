package site.siredvin.progressiveperipherals.extra.network.api;

import dan200.computercraft.api.lua.LuaException;

@FunctionalInterface
public interface PeripheralBiFunction<T1, T2, V> {
    V apply(T1 t1, T2 t2) throws LuaException;
}
