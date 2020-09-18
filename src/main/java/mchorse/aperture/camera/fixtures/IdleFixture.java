package mchorse.aperture.camera.fixtures;

import com.google.gson.annotations.Expose;

import io.netty.buffer.ByteBuf;
import mchorse.aperture.camera.CameraProfile;
import mchorse.aperture.camera.data.Position;
import net.minecraft.entity.player.EntityPlayer;

/**
 * Idle camera fixture
 *
 * This fixture is the basic fixture type. This fixture is responsible for
 * outputting static values for camera.
 */
public class IdleFixture extends AbstractFixture
{
    @Expose
    public Position position = new Position(0, 0, 0, 0, 0);

    public IdleFixture(long duration)
    {
        super(duration);
    }

    @Override
    public void fromPlayer(EntityPlayer player)
    {
        this.position.set(player);
    }

    @Override
    public void applyFixture(long ticks, float partialTicks, float previewPartialTick, CameraProfile profile, Position pos)
    {
        pos.copy(this.position);
    }

    @Override
    public AbstractFixture create(long duration)
    {
        return new IdleFixture(duration);
    }

    @Override
    public void copy(AbstractFixture from)
    {
        super.copy(from);

        if (from instanceof IdleFixture)
        {
            this.position = ((IdleFixture) from).position.copy();
        }
    }

    /* Save/load methods */

    @Override
    public void fromByteBuf(ByteBuf buffer)
    {
        super.fromByteBuf(buffer);

        this.position = Position.fromByteBuf(buffer);
    }

    @Override
    public void toByteBuf(ByteBuf buffer)
    {
        super.toByteBuf(buffer);

        this.position.toByteBuf(buffer);
    }
}