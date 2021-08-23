package site.siredvin.progressiveperipherals.utils;

import net.minecraft.nbt.ByteArrayNBT;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.IntNBT;
import org.jetbrains.annotations.Nullable;
import site.siredvin.progressiveperipherals.utils.quad.QuadList;

import java.awt.*;
import java.io.*;

public class NBTUtils {
    public static @Nullable INBT serialize(QuadList data) {
        ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
        ObjectOutputStream stream;
        try {
            stream = new ObjectOutputStream(byteStream);
            stream.writeObject(data);
            return new ByteArrayNBT(byteStream.toByteArray());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static INBT serializer(Color color) {
        return IntNBT.valueOf(color.getRGB());
    }

    public static @Nullable QuadList readQuadList(byte[] data) {
        ByteArrayInputStream byteStream = new ByteArrayInputStream(data);
        ObjectInputStream stream;
        try {
            stream = new ObjectInputStream(byteStream);
            return (QuadList) stream.readObject();
        } catch (InvalidClassException e) {
            // just do nothing
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static Color readColor(int value) {
        return new Color(value);
    }

    public static boolean isSubSet(CompoundNBT set, CompoundNBT subset) {
        for (String key: subset.getAllKeys()) {
            if (!set.contains(key))
                return false;
            INBT original = subset.get(key);
            INBT setData = set.get(key);
            if (original instanceof CompoundNBT) {
                if (!(setData instanceof CompoundNBT))
                    return false;
                if (!isSubSet((CompoundNBT) setData, (CompoundNBT) original))
                    return false;
            }
            if (original != null && !original.equals(setData))
                return false;
        }
        return true;
    }

}
