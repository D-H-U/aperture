package mchorse.aperture.camera.fixtures;

import java.util.List;

import com.google.gson.annotations.Expose;

import io.netty.buffer.ByteBuf;
import mchorse.aperture.camera.CameraProfile;
import mchorse.aperture.camera.data.Position;
import mchorse.aperture.camera.modifiers.AbstractModifier;

public class NullFixture extends AbstractFixture
{
    @Expose
    public boolean previous;

    public NullFixture(long duration)
    {
        super(duration);
    }

    @Override
    public void applyFixture(long ticks, float partialTick, float previewPartialTick, CameraProfile profile, Position pos)
    {
        List<AbstractFixture> list = profile.getAll();
        int index = list.indexOf(this);

        if (index != -1)
        {
            AbstractFixture fixture = profile.get(index + (this.previous ? -1 : 1));

            if (fixture == null || fixture instanceof NullFixture)
            {
                return;
            }

            long target = this.previous ? fixture.getDuration() : 0;
            long offset = profile.calculateOffset(fixture);

            fixture.applyFixture(target, 0, 0, profile, pos);
            AbstractModifier.applyModifiers(profile, fixture, offset, target, 0, 0, pos);
        }
    }

    @Override
    public AbstractFixture create(long duration)
    {
        return new NullFixture(duration);
    }

    @Override
    public void copy(AbstractFixture from)
    {
        super.copy(from);

        if (from instanceof NullFixture)
        {
            NullFixture nullFixture = (NullFixture) from;

            this.previous = nullFixture.previous;
        }
    }

    @Override
    public void toByteBuf(ByteBuf buffer)
    {
        super.toByteBuf(buffer);

        buffer.writeBoolean(this.previous);
    }

    @Override
    public void fromByteBuf(ByteBuf buffer)
    {
        super.fromByteBuf(buffer);

        this.previous = buffer.readBoolean();
    }
}