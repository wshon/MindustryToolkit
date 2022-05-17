package toolkit;

import arc.*;
import arc.util.*;
import mindustry.world.*;

import java.lang.reflect.*;

public class ToolkitUtils {
    public static Field getField(Class clazz, String name) {
        try {
            Field field = clazz.getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Object object, String name) {
        try {
            Field field = object.getClass().getDeclaredField(name);
            field.setAccessible(true);
            return field;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static Object setValue(Field field, Object object, Object value) {
        try {
            field.set(object, value);
            return object;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getValue(Field field, Object object) {
        try {
            return (T) field.get(object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getValue(Class clazz, String name, Object object) {
        try {
            return getValue(getField(clazz, name), object);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static boolean isBothOutCamera(Tile tile1, Tile tile2) {
        return !isInCamera(tile1) && !isInCamera(tile2);
    }

    public static boolean isBothInCamera(Tile tile1, Tile tile2) {
        return isInCamera(tile1) && isInCamera(tile2);
    }

    public static boolean isOutCamera(Tile tile) {
        return !isInCamera(tile);
    }

    public static boolean isInCamera(Tile tile) {
        return isInCamera(tile.worldx(), tile.worldy(), 0);
    }

    public static boolean isOutCamera(float x, float y) {
        return !isInCamera(x, y, 0);
    }

    public static boolean isInCamera(float x, float y, float size) {
        Core.camera.bounds(Tmp.r3);
        Tmp.r2.setCentered(x, y, size);
        return Tmp.r3.overlaps(Tmp.r2);
    }
}
