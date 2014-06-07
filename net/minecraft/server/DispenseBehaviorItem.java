package net.minecraft.server;

public class DispenseBehaviorItem implements IDispenseBehavior {

    public DispenseBehaviorItem() {}

    public final ItemStack a(ISourceBlock isourceblock, ItemStack itemstack) {
        ItemStack itemstack1 = this.b(isourceblock, itemstack);

        this.a(isourceblock);
        this.a(isourceblock, BlockDispenser.b(isourceblock.h()));
        return itemstack1;
    }

    protected ItemStack b(ISourceBlock isourceblock, ItemStack itemstack) {
        EnumFacing enumfacing = BlockDispenser.b(isourceblock.h());
        IPosition iposition = BlockDispenser.a(isourceblock);
        ItemStack itemstack1 = itemstack.a(1);

        a(isourceblock.k(), itemstack1, 6, enumfacing, iposition);
        return itemstack;
    }

    public static void a(World world, ItemStack itemstack, int i, EnumFacing enumfacing, IPosition iposition) {
        double d0 = iposition.getX();
        double d1 = iposition.getY();
        double d2 = iposition.getZ();
        EntityItem entityitem = new EntityItem(world, d0, d1 - 0.3D, d2, itemstack);
        double d3 = world.random.nextDouble() * 0.1D + 0.2D;

        entityitem.motX = (double) enumfacing.getAdjacentX() * d3;
        entityitem.motY = 0.2;
        entityitem.motZ = (double) enumfacing.getAdjacentZ() * d3;
        entityitem.motX += world.random.nextGaussian() * 0.0075 * (double) i;
        entityitem.motY += world.random.nextGaussian() * 0.0075 * (double) i;
        entityitem.motZ += world.random.nextGaussian() * 0.0075 * (double) i;
        world.addEntity(entityitem);
    }

    protected void a(ISourceBlock isourceblock) {
        isourceblock.k().triggerEffect(1000, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), 0);
    }

    protected void a(ISourceBlock isourceblock, EnumFacing enumfacing) {
        isourceblock.k().triggerEffect(2000, isourceblock.getBlockX(), isourceblock.getBlockY(), isourceblock.getBlockZ(), this.a(enumfacing));
    }

    private int a(EnumFacing enumfacing) {
        return enumfacing.getAdjacentX() + 1 + (enumfacing.getAdjacentZ() + 1) * 3;
    }
}
