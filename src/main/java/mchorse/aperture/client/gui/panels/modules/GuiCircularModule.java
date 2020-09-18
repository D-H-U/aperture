package mchorse.aperture.client.gui.panels.modules;

import mchorse.aperture.camera.fixtures.CircularFixture;
import mchorse.aperture.client.gui.GuiCameraEditor;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

/**
 * Circular GUI module
 *
 * This class unifies four trackpads into one object which edits
 * {@link CircularFixture}'s other properties, and makes it way easier to reuse
 * in other classes.
 */
public class GuiCircularModule extends GuiAbstractModule
{
    public GuiTrackpadElement offset;
    public GuiTrackpadElement pitch;
    public GuiTrackpadElement circles;
    public GuiTrackpadElement distance;

    public CircularFixture fixture;

    public GuiCircularModule(Minecraft mc, GuiCameraEditor editor)
    {
        super(mc, editor);

        this.offset = new GuiTrackpadElement(mc, (value) ->
        {
            this.fixture.offset = value.floatValue();
            this.editor.updateProfile();
        });
        this.offset.tooltip(IKey.lang("aperture.gui.panels.offset"));

        this.pitch = new GuiTrackpadElement(mc, (value) ->
        {
            this.fixture.pitch = value.floatValue();
            this.editor.updateProfile();
        });
        this.pitch.tooltip(IKey.lang("aperture.gui.panels.pitch"));

        this.circles = new GuiTrackpadElement(mc, (value) ->
        {
            this.fixture.circles = value.floatValue();
            this.editor.updateProfile();
        });
        this.circles.tooltip(IKey.lang("aperture.gui.panels.circles"));

        this.distance = new GuiTrackpadElement(mc, (value) ->
        {
            this.fixture.distance = value.floatValue();
            this.editor.updateProfile();
        });
        this.distance.tooltip(IKey.lang("aperture.gui.panels.distance"));

        this.flex().column(5).vertical().stretch().height(20);
        this.add(Elements.label(IKey.lang("aperture.gui.panels.circle")).background(0x88000000), this.offset, this.pitch, this.circles, this.distance);
    }

    public void fill(CircularFixture fixture)
    {
        this.fixture = fixture;

        this.offset.setValue(fixture.offset);
        this.pitch.setValue(fixture.pitch);
        this.circles.setValue(fixture.circles);
        this.distance.setValue(fixture.distance);
    }
}