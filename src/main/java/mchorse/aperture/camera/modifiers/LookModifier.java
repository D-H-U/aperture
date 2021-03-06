package mchorse.aperture.camera.modifiers;

import com.google.gson.annotations.Expose;

import io.netty.buffer.ByteBuf;
import mchorse.aperture.camera.CameraProfile;
import mchorse.aperture.camera.data.Point;
import mchorse.aperture.camera.data.Position;
import mchorse.aperture.camera.fixtures.AbstractFixture;
import net.minecraft.util.math.MathHelper;

/**
 * Look modifier
 * 
 * This modifier locks fixture's angle so it would always look in the 
 * direction of entity. Relative yaw and pitch is also supported.
 */
public class LookModifier extends EntityModifier
{
    @Expose
    public boolean relative;

    @Expose
    public boolean atBlock;

    @Expose
    public boolean forward;

    @Expose
    public Point block = new Point(0, 0, 0);

    @Override
    public void modify(long ticks, long offset, AbstractFixture fixture, float partialTick, CameraProfile profile, Position pos)
    {
        if (this.entity == null || this.entity.isDead)
        {
            this.tryFindingEntity();
        }

        if (this.entity == null && !(this.atBlock || this.forward))
        {
            return;
        }

        if (this.forward)
        {
            fixture.applyFixture(offset - 1, partialTick, profile, this.position);
        }
        else
        {
            fixture.applyFixture(0, 0, profile, this.position);
        }

        double x = 0;
        double y = 0;
        double z = 0;

        if (this.atBlock)
        {
            x = this.block.x;
            y = this.block.y;
            z = this.block.z;
        }
        else if (!this.forward)
        {
            x = this.entity.lastTickPosX + (this.entity.posX - this.entity.lastTickPosX) * partialTick;
            y = this.entity.lastTickPosY + (this.entity.posY - this.entity.lastTickPosY) * partialTick;
            z = this.entity.lastTickPosZ + (this.entity.posZ - this.entity.lastTickPosZ) * partialTick;
        }

        double dX = x - pos.point.x;
        double dY = y - pos.point.y;
        double dZ = z - pos.point.z;

        if (this.forward)
        {
            dX = pos.point.x - this.position.point.x;
            dY = pos.point.y - this.position.point.y;
            dZ = pos.point.z - this.position.point.z;
        }

        double horizontalDistance = MathHelper.sqrt_double(dX * dX + dZ * dZ);

        float yaw = (float) (MathHelper.atan2(dZ, dX) * (180D / Math.PI)) - 90.0F;
        float pitch = (float) (-(MathHelper.atan2(dY, horizontalDistance) * (180D / Math.PI)));

        if (this.relative && !this.forward)
        {
            yaw += pos.angle.yaw - this.position.angle.yaw;
            pitch += pos.angle.pitch - this.position.angle.pitch;
        }

        pos.angle.set(yaw, pitch);
    }

    @Override
    public AbstractModifier clone()
    {
        LookModifier modifier = new LookModifier();

        modifier.enabled = this.enabled;
        modifier.selector = this.selector;
        modifier.relative = this.relative;
        modifier.atBlock = this.atBlock;
        modifier.forward = this.forward;
        modifier.block = this.block.clone();

        return modifier;
    }

    @Override
    public void toByteBuf(ByteBuf buffer)
    {
        super.toByteBuf(buffer);

        buffer.writeBoolean(this.relative);
        buffer.writeBoolean(this.atBlock);
        buffer.writeBoolean(this.forward);
        this.block.toByteBuf(buffer);
    }

    @Override
    public void fromByteBuf(ByteBuf buffer)
    {
        super.fromByteBuf(buffer);

        this.relative = buffer.readBoolean();
        this.atBlock = buffer.readBoolean();
        this.forward = buffer.readBoolean();
        this.block = Point.fromByteBuf(buffer);
    }
}