package mchorse.aperture.client.gui.panels.modifiers;

import mchorse.aperture.camera.modifiers.FollowModifier;
import mchorse.aperture.client.gui.GuiModifiersManager;
import mchorse.aperture.client.gui.utils.GuiTextHelpElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiToggleElement;
import mchorse.mclib.client.gui.framework.elements.input.GuiTrackpadElement;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;

public class GuiFollowModifierPanel extends GuiAbstractModifierPanel<FollowModifier>
{
    public GuiTextHelpElement selector;
    public GuiTrackpadElement x;
    public GuiTrackpadElement y;
    public GuiTrackpadElement z;
    public GuiToggleElement relative;

    public GuiFollowModifierPanel(Minecraft mc, FollowModifier modifier, GuiModifiersManager modifiers)
    {
        super(mc, modifier, modifiers);

        this.selector = new GuiTextHelpElement(mc, 500, (str) ->
        {
            this.modifier.selector = str;
            this.modifier.tryFindingEntity();
            this.modifiers.editor.updateProfile();
        });
        this.selector.link(GuiLookModifierPanel.TARGET_SELECTOR_HELP).tooltip(IKey.lang("aperture.gui.panels.selector"));

        this.x = new GuiTrackpadElement(mc, (value) ->
        {
            this.modifier.offset.x = value;
            this.modifiers.editor.updateProfile();
        });
        this.x.tooltip(IKey.lang("aperture.gui.panels.x"));

        this.y = new GuiTrackpadElement(mc, (value) ->
        {
            this.modifier.offset.y = value;
            this.modifiers.editor.updateProfile();
        });
        this.y.tooltip(IKey.lang("aperture.gui.panels.y"));

        this.z = new GuiTrackpadElement(mc, (value) ->
        {
            this.modifier.offset.z = value;
            this.modifiers.editor.updateProfile();
        });
        this.z.tooltip(IKey.lang("aperture.gui.panels.z"));

        this.relative = new GuiToggleElement(mc, IKey.lang("aperture.gui.modifiers.panels.relative"), false, (b) ->
        {
            this.modifier.relative = b.isToggled();
            this.modifiers.editor.updateProfile();
        });
        this.relative.tooltip(IKey.lang("aperture.gui.modifiers.panels.relative_tooltip"));

        this.fields.add(this.selector, Elements.row(mc, 5, 0, 20, this.x, this.y, this.z), this.relative);
    }

    @Override
    public void resize()
    {
        super.resize();

        this.selector.setText(this.modifier.selector);
        this.x.setValue((float) this.modifier.offset.x);
        this.y.setValue((float) this.modifier.offset.y);
        this.z.setValue((float) this.modifier.offset.z);
        this.relative.toggled(this.modifier.relative);
    }
}