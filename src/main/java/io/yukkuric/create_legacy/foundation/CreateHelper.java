package io.yukkuric.create_legacy.foundation;

import net.minecraft.item.ItemStack;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class CreateHelper {
    public static List<ItemStack> multipliedOutput(ItemStack in, ItemStack out) {
        List<ItemStack> stacks = new ArrayList();
        ItemStack result = out.copy();
        result.setCount(in.getCount() * out.getCount());

        while (result.getCount() > result.getMaxStackSize()) {
            stacks.add(result.split(result.getMaxStackSize()));
        }

        stacks.add(result);
        return stacks;
    }

    public static Vec3d getCenterOf(Vec3i pos) {
        return new Vec3d(pos).add(.5f, .5f, .5f);
    }

    public static Vec3d offsetRandomly(Vec3d vec, Random r, float radius) {
        return new Vec3d(vec.x + (r.nextFloat() - .5f) * 2 * radius, vec.y + (r.nextFloat() - .5f) * 2 * radius,
                vec.z + (r.nextFloat() - .5f) * 2 * radius);
    }
}
