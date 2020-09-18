package mchorse.aperture.camera.data;

import com.google.common.base.MoreObjects;
import com.google.gson.annotations.Expose;
import io.netty.buffer.ByteBuf;
import mchorse.mclib.utils.Interpolations;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Position class
 *
 * This class represents a point in the space with specified angle of view.
 */
public class Position
{
    /**
     * Zero position. Not allowed for modification 
     */
    public static final Position ZERO = new Position(0, 0, 0, 0, 0);

    @Expose
    public Point point = new Point(0, 0, 0);

    @Expose
    public Angle angle = new Angle(0, 0);

    public static Position fromByteBuf(ByteBuf buffer)
    {
        return new Position(Point.fromByteBuf(buffer), Angle.fromByteBuf(buffer));
    }

    public Position()
    {}

    public Position(Point point, Angle angle)
    {
        this.point = point;
        this.angle = angle;
    }

    public Position(float x, float y, float z, float yaw, float pitch)
    {
        this.point.set(x, y, z);
        this.angle.set(yaw, pitch);
    }

    public Position(float x, float y, float z, float yaw, float pitch, float roll, float fov)
    {
        this.point.set(x, y, z);
        this.angle.set(yaw, pitch, roll, fov);
    }

    public Position(EntityPlayer player)
    {
        this.set(player);
    }

    public void set(Position position)
    {
        this.point.set(position.point);
        this.angle.set(position.angle);
    }

    public void set(EntityPlayer player)
    {
        this.point.set(player);
        this.angle.set(player);
    }

    public void copy(Position position)
    {
        this.point.set(position.point.x, position.point.y, position.point.z);
        this.angle.set(position.angle.yaw, position.angle.pitch, position.angle.roll, position.angle.fov);
    }

    public void apply(EntityPlayer player)
    {
        player.setPositionAndRotation(this.point.x, this.point.y, this.point.z, this.angle.yaw, this.angle.pitch);
        player.setLocationAndAngles(this.point.x, this.point.y, this.point.z, this.angle.yaw, this.angle.pitch);
        player.motionX = player.motionY = player.motionZ = 0;
        player.rotationYawHead = player.prevRotationYawHead = this.angle.yaw;
    }

    public void interpolate(Position position, float factor)
    {
        this.point.x = Interpolations.lerp(this.point.x, position.point.x, factor);
        this.point.y = Interpolations.lerp(this.point.y, position.point.y, factor);
        this.point.z = Interpolations.lerp(this.point.z, position.point.z, factor);
        this.angle.yaw = Interpolations.lerpYaw(this.angle.yaw, position.angle.yaw, factor);
        this.angle.pitch = Interpolations.lerp(this.angle.pitch, position.angle.pitch, factor);
        this.angle.roll = Interpolations.lerp(this.angle.roll, position.angle.roll, factor);
        this.angle.fov = Interpolations.lerp(this.angle.fov, position.angle.fov, factor);
    }

    public void toByteBuf(ByteBuf buffer)
    {
        this.point.toByteBuf(buffer);
        this.angle.toByteBuf(buffer);
    }

    public Position copy()
    {
        return new Position(this.point.copy(), this.angle.copy());
    }

    @Override
    public String toString()
    {
        return MoreObjects.toStringHelper(this).addValue(this.point).addValue(this.angle).toString();
    }
}