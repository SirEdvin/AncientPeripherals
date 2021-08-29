package site.siredvin.progressiveperipherals.utils;

import net.minecraftforge.fml.ModList;
import site.siredvin.progressiveperipherals.ProgressivePeripherals;

import java.util.Optional;

public class Platform {

    public static Optional<Object> maybeLoadIntegration(final String modid, final String path) {
        ProgressivePeripherals.LOGGER.info(String.format("%s not loaded, skip integration loading", modid));
        return ModList.get().isLoaded(modid) ? maybeLoadIntegration(path) : Optional.empty();
    }

    public static Optional<Object> maybeLoadIntegration(final String path) {
        try {
            Class<?> clazz = Class.forName(ProgressivePeripherals.class.getPackage().getName() + ".integrations." + path);
            return Optional.of(clazz.newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException ignored) {
            return Optional.empty();
        }
    }
}
