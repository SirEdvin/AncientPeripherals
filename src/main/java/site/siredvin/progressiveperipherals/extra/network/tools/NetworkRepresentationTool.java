package site.siredvin.progressiveperipherals.extra.network.tools;

import de.srendi.advancedperipherals.common.util.LuaConverter;
import net.minecraft.util.math.BlockPos;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetwork;
import site.siredvin.progressiveperipherals.extra.network.EnderwireNetworkElement;

import java.util.*;

public class NetworkRepresentationTool {
    public static Map<String, Object> shortRepresentation(EnderwireNetwork network) {
        Map<String, Object> representation = new HashMap<>();
        representation.put("name", network.getName());
        representation.put("type", network.getType().name().toLowerCase());
        return representation;
    }

    public static Map<String, Object> fullRepresentation(EnderwireNetwork network, BlockPos center) {
        Map<String, Object> representation = shortRepresentation(network);
        representation.put("range", network.getReachableRange());
        representation.put("interdimensional", network.isInterdimensional());
        Map<String, EnderwireNetworkElement> elements = network.getElements();
        if (elements != null) {
            List<Object> elementsRepresentation = new ArrayList<>();
            elements.values().forEach(networkElement -> elementsRepresentation.add(elementRepresentation(networkElement, center)));
            representation.put("elements", elementsRepresentation);
        } else {
            representation.put("elements", null);
        }
        return representation;
    }

    public static Map<String, Object> elementRepresentation(EnderwireNetworkElement networkElement, BlockPos center) {
        Map<String, Object> representation = new HashMap<>();
        representation.put("name", networkElement.getName().toString());
        representation.put("elementType", networkElement.getCategory().name().toLowerCase());
        representation.put("deviceType", networkElement.getElementType());
        representation.put("position", LuaConverter.posToObject(networkElement.getPos().subtract(center)));
        return representation;
    }
}
