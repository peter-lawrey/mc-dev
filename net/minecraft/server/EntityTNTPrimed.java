package net.minecraft.server;

public class EntityTNTPrimed extends Entity {

    public int fuseTicks;
    private EntityLiving source;

    public EntityTNTPrimed(World world) {
        super(world);
        this.k = true;
        this.a(0.98F, 0.98F);
        this.height = this.length / 2.0F;
    }

    public EntityTNTPrimed(World world, double d0, double d1, double d2, EntityLiving entityliving) {
        this(world);
        this.setPosition(d0, d1, d2);
        float f = (float) (Math.random() * Math.PI * 2.0D);

        this.motX = (double) (-((float) Math.sin((double) f)) * 0.02F);
        this.motY = 0.2;
        this.motZ = (double) (-((float) Math.cos((double) f)) * 0.02F);
        this.fuseTicks = 80;
        this.lastX = d0;
        this.lastY = d1;
        this.lastZ = d2;
        this.source = entityliving;
    }

    protected void c() {}

    protected boolean g_() {
        return false;
    }

    public boolean Q() {
        return !this.dead;
    }

    public void h() {
        this.lastX = this.locX;
        this.lastY = this.locY;
        this.lastZ = this.locZ;
        this.motY -= 0.04;
        this.move(this.motX, this.motY, this.motZ);
        this.motX *= 0.98;
        this.motY *= 0.98;
        this.motZ *= 0.98;
        if (this.onGround) {
            this.motX *= 0.7;
            this.motZ *= 0.7;
            this.motY *= -0.5D;
        }

        if (this.fuseTicks-- <= 0) {
            this.die();
            if (!this.world.isStatic) {
                this.explode();
            }
        } else {
            this.world.addParticle("smoke", this.locX, this.locY + 0.5D, this.locZ, 0.0D, 0.0D, 0.0D);
        }
    }

    private void explode() {
        float f = 4.0F;

        this.world.explode(this, this.locX, this.locY, this.locZ, f, true);
    }

    protected void b(NBTTagCompound nbttagcompound) {
        nbttagcompound.setByte("Fuse", (byte) this.fuseTicks);
    }

    protected void a(NBTTagCompound nbttagcompound) {
        this.fuseTicks = nbttagcompound.getByte("Fuse");
    }

    public EntityLiving getSource() {
        return this.source;
    }
}
