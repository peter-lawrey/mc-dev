package net.minecraft.server;

import java.util.ArrayList;
import java.util.Iterator;

public class EntityFallingBlock extends Entity {

    private Block id;
    public int data;
    public int b;
    public boolean dropItem;
    private boolean f;
    private boolean hurtEntities;
    private int fallHurtMax;
    private float fallHurtAmount;
    public NBTTagCompound tileEntityData;

    public EntityFallingBlock(World world) {
        super(world);
        this.dropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0F;
    }

    public EntityFallingBlock(World world, double d0, double d1, double d2, Block block) {
        this(world, d0, d1, d2, block, 0);
    }

    public EntityFallingBlock(World world, double d0, double d1, double d2, Block block, int i) {
        super(world);
        this.dropItem = true;
        this.fallHurtMax = 40;
        this.fallHurtAmount = 2.0F;
        this.id = block;
        this.data = i;
        this.k = true;
        this.a(0.98F, 0.98F);
        this.height = this.length / 2.0F;
        this.setPosition(d0, d1, d2);
        this.motX = 0.0D;
        this.motY = 0.0D;
        this.motZ = 0.0D;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
    }

    protected boolean g_() {
        return false;
    }

    protected void c() {}

    public boolean Q() {
        return !this.dead;
    }

    public void h() {
        if (this.id.getMaterial() == Material.AIR) {
            this.die();
        } else {
            this.lastX = this.locX;
            this.lastY = this.locY;
            this.lastZ = this.locZ;
            ++this.b;
            this.motY -= 0.04;
            this.move(this.motX, this.motY, this.motZ);
            this.motX *= 0.98;
            this.motY *= 0.98;
            this.motZ *= 0.98;
            if (!this.world.isStatic) {
                int i = MathHelper.floor(this.locX);
                int j = MathHelper.floor(this.locY);
                int k = MathHelper.floor(this.locZ);

                if (this.b == 1) {
                    if (this.world.getType(i, j, k) != this.id) {
                        this.die();
                        return;
                    }

                    this.world.setAir(i, j, k);
                }

                if (this.onGround) {
                    this.motX *= 0.7;
                    this.motZ *= 0.7;
                    this.motY *= -0.5D;
                    if (this.world.getType(i, j, k) != Blocks.PISTON_MOVING) {
                        this.die();
                        if (!this.f && this.world.mayPlace(this.id, i, j, k, true, 1, (Entity) null, (ItemStack) null) && !BlockFalling.canFall(this.world, i, j - 1, k) && this.world.setTypeAndData(i, j, k, this.id, this.data, 3)) {
                            if (this.id instanceof BlockFalling) {
                                ((BlockFalling) this.id).a(this.world, i, j, k, this.data);
                            }

                            if (this.tileEntityData != null && this.id instanceof IContainer) {
                                TileEntity tileentity = this.world.getTileEntity(i, j, k);

                                if (tileentity != null) {
                                    NBTTagCompound nbttagcompound = new NBTTagCompound();

                                    tileentity.b(nbttagcompound);
                                    Iterator iterator = this.tileEntityData.c().iterator();

                                    while (iterator.hasNext()) {
                                        String s = (String) iterator.next();
                                        NBTBase nbtbase = this.tileEntityData.get(s);

                                        if (!s.equals("x") && !s.equals("y") && !s.equals("z")) {
                                            nbttagcompound.set(s, nbtbase.clone());
                                        }
                                    }

                                    tileentity.a(nbttagcompound);
                                    tileentity.update();
                                }
                            }
                        } else if (this.dropItem && !this.f) {
                            this.a(new ItemStack(this.id, 1, this.id.getDropData(this.data)), 0.0F);
                        }
                    }
                } else if (this.b > 100 && !this.world.isStatic && (j < 1 || j > 256) || this.b > 600) {
                    if (this.dropItem) {
                        this.a(new ItemStack(this.id, 1, this.id.getDropData(this.data)), 0.0F);
                    }

                    this.die();
                }
            }
        }
    }

    protected void b(float f) {
        if (this.hurtEntities) {
            int i = MathHelper.f(f - 1.0F);

            if (i > 0) {
                ArrayList arraylist = new ArrayList(this.world.getEntities(this, this.boundingBox));
                boolean flag = this.id == Blocks.ANVIL;
                DamageSource damagesource = flag ? DamageSource.ANVIL : DamageSource.FALLING_BLOCK;
                Iterator iterator = arraylist.iterator();

                while (iterator.hasNext()) {
                    Entity entity = (Entity) iterator.next();

                    entity.damageEntity(damagesource, (float) Math.min(MathHelper.d((float) i * this.fallHurtAmount), this.fallHurtMax));
                }

                if (flag && (double) this.random.nextFloat() < 0.05 + (double) i * 0.05D) {
                    int j = this.data >> 2;
                    int k = this.data & 3;

                    ++j;
                    if (j > 2) {
                        this.f = true;
                    } else {
                        this.data = k | j << 2;
                    }
                }
            }
        }
    }

    protected void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Tile", (byte) Block.b(this.id));
        nbttagcompound.setInt("TileID", Block.b(this.id));
        nbttagcompound.setByte("Data", (byte) this.data);
        nbttagcompound.setByte("Time", (byte) this.b);
        nbttagcompound.setBoolean("DropItem", this.dropItem);
        nbttagcompound.setBoolean("HurtEntities", this.hurtEntities);
        nbttagcompound.setFloat("FallHurtAmount", this.fallHurtAmount);
        nbttagcompound.setInt("FallHurtMax", this.fallHurtMax);
        if (this.tileEntityData != null) {
            nbttagcompound.set("TileEntityData", this.tileEntityData);
        }
    }

    protected void a(NBTTagCompound nbttagcompound) {
        if (nbttagcompound.hasKeyOfType("TileID", 99)) {
            this.id = Block.e(nbttagcompound.getInt("TileID"));
        } else {
            this.id = Block.e(nbttagcompound.getByte("Tile") & 255);
        }

        this.data = nbttagcompound.getByte("Data") & 255;
        this.b = nbttagcompound.getByte("Time") & 255;
        if (nbttagcompound.hasKeyOfType("HurtEntities", 99)) {
            this.hurtEntities = nbttagcompound.getBoolean("HurtEntities");
            this.fallHurtAmount = nbttagcompound.getFloat("FallHurtAmount");
            this.fallHurtMax = nbttagcompound.getInt("FallHurtMax");
        } else if (this.id == Blocks.ANVIL) {
            this.hurtEntities = true;
        }

        if (nbttagcompound.hasKeyOfType("DropItem", 99)) {
            this.dropItem = nbttagcompound.getBoolean("DropItem");
        }

        if (nbttagcompound.hasKeyOfType("TileEntityData", 10)) {
            this.tileEntityData = nbttagcompound.getCompound("TileEntityData");
        }

        if (this.id.getMaterial() == Material.AIR) {
            this.id = Blocks.SAND;
        }
    }

    public void a(boolean flag) {
        this.hurtEntities = flag;
    }

    public void a(CrashReportSystemDetails crashreportsystemdetails) {
        super.a(crashreportsystemdetails);
        crashreportsystemdetails.a("Immitating block ID", Integer.valueOf(Block.b(this.id)));
        crashreportsystemdetails.a("Immitating block data", Integer.valueOf(this.data));
    }

    public Block f() {
        return this.id;
    }
}
