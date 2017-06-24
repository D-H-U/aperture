package mchorse.aperture.client.gui.panels;

import mchorse.aperture.camera.fixtures.CircularFixture;
import mchorse.aperture.client.gui.GuiTrackpad;
import mchorse.aperture.client.gui.GuiTrackpad.ITrackpadListener;
import mchorse.aperture.client.gui.panels.modules.GuiCircularModule;
import mchorse.aperture.client.gui.panels.modules.GuiPointModule;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiScreen;

/**
 * Circular fixture panel
 *
 * This panel is responsible for editing a circular camera fixture using point
 * and its own circular module (which is basically positionioned like an angle
 * module, but have different set of values).
 */
public class GuiCircularFixturePanel extends GuiAbstractFixturePanel<CircularFixture> implements ITrackpadListener
{
    public GuiPointModule point;
    public GuiCircularModule circular;

    public GuiCircularFixturePanel(FontRenderer font)
    {
        super(font);

        this.point = new GuiPointModule(this, font);
        this.circular = new GuiCircularModule(this, font);

        this.height = 100;
    }

    @Override
    public void setTrackpadValue(GuiTrackpad trackpad, float value)
    {
        if (trackpad == this.point.x)
        {
            this.fixture.start.x = trackpad.value;
        }
        else if (trackpad == this.point.y)
        {
            this.fixture.start.y = trackpad.value;
        }
        else if (trackpad == this.point.z)
        {
            this.fixture.start.z = trackpad.value;
        }
        else if (trackpad == this.circular.offset)
        {
            this.fixture.offset = trackpad.value;
        }
        else if (trackpad == this.circular.pitch)
        {
            this.fixture.pitch = trackpad.value;
        }
        else if (trackpad == this.circular.circles)
        {
            this.fixture.circles = trackpad.value;
        }
        else if (trackpad == this.circular.distance)
        {
            this.fixture.distance = trackpad.value;
        }

        super.setTrackpadValue(trackpad, value);
    }

    @Override
    public void select(CircularFixture fixture)
    {
        super.select(fixture);

        this.point.fill(fixture.start);
        this.circular.fill(fixture);
    }

    @Override
    public void update(GuiScreen screen)
    {
        boolean height = screen.height - 60 > 200;

        this.height = height ? 200 : 100;

        super.update(screen);

        int x = this.area.x + this.area.w - 80;
        int y = this.area.y;

        this.point.update(x, y);

        if (height)
        {
            y += 110;
        }
        else
        {
            x -= 80 + 10;
        }

        this.circular.update(x, y);
    }

    @Override
    public void editFixture()
    {
        this.fixture.start.set(Minecraft.getMinecraft().thePlayer);

        super.editFixture();
    }

    @Override
    public void mouseClicked(int mouseX, int mouseY, int mouseButton)
    {
        super.mouseClicked(mouseX, mouseY, mouseButton);

        this.point.mouseClicked(mouseX, mouseY, mouseButton);
        this.circular.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    public void mouseReleased(int mouseX, int mouseY, int state)
    {
        super.mouseReleased(mouseX, mouseY, state);

        this.point.mouseReleased(mouseX, mouseY, state);
        this.circular.mouseReleased(mouseX, mouseY, state);
    }

    @Override
    public void keyTyped(char typedChar, int keyCode)
    {
        super.keyTyped(typedChar, keyCode);

        this.point.keyTyped(typedChar, keyCode);
        this.circular.keyTyped(typedChar, keyCode);
    }

    @Override
    public void draw(int mouseX, int mouseY, float partialTicks)
    {
        super.draw(mouseX, mouseY, partialTicks);

        this.editor.drawCenteredString(this.font, "Position", this.point.z.area.x + this.point.z.area.w / 2, this.point.z.area.y + this.point.z.area.h + 6, 0xffffffff);
        this.editor.drawCenteredString(this.font, "Circle", this.circular.distance.area.x + this.circular.distance.area.w / 2, this.circular.distance.area.y + this.circular.distance.area.h + 6, 0xffffffff);

        this.point.draw(mouseX, mouseY, partialTicks);
        this.circular.draw(mouseX, mouseY, partialTicks);
    }
}