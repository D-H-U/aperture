package mchorse.aperture.client.gui;

import mchorse.aperture.ClientProxy;
import mchorse.aperture.camera.CameraAPI;
import mchorse.aperture.camera.CameraProfile;
import mchorse.aperture.camera.destination.AbstractDestination;
import mchorse.aperture.camera.destination.ClientDestination;
import mchorse.aperture.camera.destination.ServerDestination;
import mchorse.aperture.network.Dispatcher;
import mchorse.aperture.network.common.PacketRequestCameraProfiles;
import mchorse.aperture.utils.APIcons;
import mchorse.mclib.client.gui.framework.elements.GuiDelegateElement;
import mchorse.mclib.client.gui.framework.elements.GuiElement;
import mchorse.mclib.client.gui.framework.elements.buttons.GuiIconElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiListElement;
import mchorse.mclib.client.gui.framework.elements.list.GuiSearchListElement;
import mchorse.mclib.client.gui.framework.elements.modals.GuiConfirmModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiModal;
import mchorse.mclib.client.gui.framework.elements.modals.GuiPromptModal;
import mchorse.mclib.client.gui.framework.elements.utils.GuiContext;
import mchorse.mclib.client.gui.framework.elements.utils.GuiLabel;
import mchorse.mclib.client.gui.utils.Elements;
import mchorse.mclib.client.gui.utils.Icons;
import mchorse.mclib.client.gui.utils.keys.IKey;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.math.MathHelper;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

/**
 * Camera profile manager GUI
 * 
 * This GUI is responsible managing currently loaded and possible for loading 
 * camera profiles. 
 */
public class GuiProfilesManager extends GuiElement
{
    public GuiCameraEditor editor;

    public GuiCameraProfilesSearchList profiles;
    public GuiCurves curves;

    public GuiIconElement keyframes;
    public GuiIconElement rename;
    public GuiIconElement convert;
    public GuiIconElement add;
    public GuiIconElement dupe;
    public GuiIconElement remove;

    public GuiProfilesManager(Minecraft mc, GuiCameraEditor editor)
    {
        super(mc);

        this.editor = editor;

        this.profiles = new GuiCameraProfilesSearchList(mc, (entry) -> this.pickProfile(entry.get(0)));
        this.profiles.label(IKey.lang("aperture.gui.search"));
        this.keyframes = new GuiIconElement(mc, APIcons.KEYFRAMES, (b) -> this.toggleKeyframes());
        this.keyframes.tooltip(IKey.lang("aperture.gui.profiles.keyframes"));
        this.rename = new GuiIconElement(mc, Icons.EDIT, (b) -> this.rename());
        this.rename.tooltip(IKey.lang("aperture.gui.profiles.rename_tooltip"));
        this.convert = new GuiIconElement(mc, Icons.SERVER, (b) -> this.convert());
        this.convert.tooltip(IKey.lang("aperture.gui.profiles.convert_tooltip"));
        this.add = new GuiIconElement(mc, Icons.ADD, (b) -> this.add());
        this.add.tooltip(IKey.lang("aperture.gui.profiles.add_tooltip"));
        this.dupe = new GuiIconElement(mc, Icons.DUPE, (b) -> this.dupe());
        this.dupe.tooltip(IKey.lang("aperture.gui.profiles.dupe_tooltip"));
        this.remove = new GuiIconElement(mc, Icons.REMOVE, (b) -> this.remove());
        this.remove.tooltip(IKey.lang("aperture.gui.profiles.remove_tooltip"));

        this.profiles.flex().relative(this).set(10, 28, 0, 0).w(1, -20).h(1, -38);
        this.remove.flex().relative(this).set(0, 4, 20, 20).x(1, -30);
        this.dupe.flex().relative(this.remove).set(-20, 0, 20, 20);
        this.add.flex().relative(this.dupe).set(-20, 0, 20, 20);
        this.rename.flex().relative(this.add).set(-20, 0, 20, 20);
        this.convert.flex().relative(this.rename).set(-20, 0, 20, 20);
        this.keyframes.flex().relative(this.convert).set(-20, 0, 20, 20);

        this.curves = new GuiCurves(mc, editor);
        this.curves.flex().relative(this).set(0, 28, 0, 0).w(1F).h(1, -28);
        this.curves.setVisible(false);

        GuiLabel label = Elements.label(IKey.lang("aperture.gui.profiles.title")).background(0x88000000);

        label.flex().relative(this).set(10, 10, 0, 20);

        this.add(label, this.profiles, this.curves, this.remove, this.dupe, this.add, this.rename, this.convert, this.keyframes);
    }

    public void pickProfile(CameraProfile profile)
    {
        if (!profile.exists)
        {
            profile.getDestination().load();
        }

        this.editor.setProfile(profile);
    }

    public CameraProfile createTemporary()
    {
        CameraProfile profile = new CameraProfile(AbstractDestination.create("default"));

        this.profiles.list.add(profile);
        this.editor.setProfile(profile);

        return profile;
    }

    private void add()
    {
        GuiModal.addFullModal(this, () -> new GuiPromptModal(this.mc, IKey.lang("aperture.gui.profiles.add_modal"), this::add).filename());
    }

    private void add(String name)
    {
        if (name.isEmpty())
        {
            return;
        }

        CameraProfile profile = new CameraProfile(AbstractDestination.create(name));

        this.profiles.filter("", true);
        this.profiles.list.add(profile);
        this.editor.setProfile(profile);
    }

    private void dupe()
    {
        CameraProfile entry = this.profiles.list.getCurrentFirst();

        if (entry == null)
        {
            return;
        }

        GuiModal.addFullModal(this, () ->
        {
            String filename = entry.getDestination().getFilename();
            GuiPromptModal modal = new GuiPromptModal(this.mc, IKey.lang("aperture.gui.profiles.dupe_modal"), (name) ->
            {
                if (!name.equals(filename))
                {
                    this.dupe(name);
                }
            });

            return modal.filename().setValue(filename);
        });
    }

    private void dupe(String name)
    {
        CameraProfile entry = this.profiles.list.getCurrentFirst();

        if (entry != null)
        {
            CameraProfile profile = entry.copy();

            profile.getDestination().setFilename(name);
            profile.dirty();

            this.profiles.filter("", true);
            this.profiles.list.add(profile);
            this.editor.setProfile(profile);
        }
    }

    private void rename()
    {
        CameraProfile entry = this.profiles.list.getCurrentFirst();

        if (entry == null)
        {
            return;
        }

        GuiModal.addFullModal(this, () -> new GuiPromptModal(this.mc, IKey.lang("aperture.gui.profiles.rename_modal"), this::rename).filename().setValue(entry.getDestination().getFilename()));
    }

    private void rename(String name)
    {
        CameraProfile entry = this.profiles.list.getCurrentFirst();
        AbstractDestination dest = entry.getDestination();

        dest.rename(name);
    }

    private void toggleKeyframes()
    {
        boolean profiles = this.profiles.isVisible();

        this.profiles.setVisible(!profiles);
        this.curves.setVisible(!this.profiles.isVisible());

        if (this.curves.isVisible())
        {
            this.curves.update();
        }
    }

    private void convert()
    {
        CameraProfile entry = this.profiles.list.getCurrentFirst();

        if (entry == null)
        {
            return;
        }

        AbstractDestination dest = entry.getDestination();
        String filename = dest.getFilename();
        AbstractDestination newDest = dest instanceof ClientDestination ? new ServerDestination(filename) : new ClientDestination(filename);

        if (!this.hasSimilar(newDest))
        {
            entry.setDestination(newDest);
            entry.dirty();
        }

        this.init();
    }

    private boolean hasSimilar(AbstractDestination dest)
    {
        for (CameraProfile profile : this.profiles.list.getList())
        {
            if (profile.getDestination().equals(dest))
            {
                return true;
            }
        }

        return false;
    }

    private void remove()
    {
        GuiModal.addFullModal(this, () -> new GuiConfirmModal(this.mc, IKey.lang("aperture.gui.profiles.remove_modal"), this::remove));
    }

    private void remove(boolean confirmed)
    {
        if (confirmed)
        {
            CameraProfile entry = this.profiles.list.getCurrentFirst();

            if (entry == null)
            {
                return;
            }

            int index = this.profiles.list.getIndex();

            this.profiles.list.remove(entry);
            this.profiles.filter("", true);
            entry.getDestination().remove();

            if (this.editor.getProfile() == entry)
            {
                this.selectFirstAvailable(index);
            }
        }
    }

    public void selectFirstAvailable(int lastIndex)
    {
        if (this.profiles.list.getList().isEmpty())
        {
            this.createTemporary();
        }
        else
        {
            if (!this.profiles.list.exists(lastIndex))
            {
                lastIndex = MathHelper.clamp(lastIndex, 0, this.profiles.list.getList().size() - 1);
            }

            this.profiles.list.setIndex(lastIndex);
            this.pickProfile(this.profiles.list.getCurrentFirst());
        }
    }

    public void selectProfile(CameraProfile profile)
    {
        this.profiles.list.setCurrent(profile);
    }

    /**
     * Rename camera profile (callback from the network handlers)
     */
    public void rename(AbstractDestination from, String to)
    {
        CameraProfile profile = this.profiles.getBy(from);

        if (profile != null)
        {
            profile.getDestination().setFilename(to);
        }
    }

    /**
     * Remove camera profile (callback from the network handlers)
     */
    public void remove(ServerDestination serverDestination)
    {
        int index = this.profiles.list.getIndex();

        CameraProfile profile = this.profiles.getBy(serverDestination);

        if (profile != null && profile.exists)
        {
            this.profiles.list.remove(profile);
            this.selectFirstAvailable(index);
        }
    }

    public void init()
    {
        if (ClientProxy.server)
        {
            Dispatcher.sendToServer(new PacketRequestCameraProfiles());
        }
        else
        {
            for (String filename : CameraAPI.getClientProfiles())
            {
                this.addProfile(new ClientDestination(filename));
            }

            this.profiles.filter("", true);
            this.selectProfile(ClientProxy.control.currentProfile);
        }
    }

    public void addProfile(AbstractDestination destination)
    {
        for (CameraProfile profile : this.profiles.list.getList())
        {
            if (profile.getDestination().equals(destination))
            {
                return;
            }
        }

        CameraProfile profile = new CameraProfile(destination);

        profile.exists = false;
        this.profiles.list.add(profile);
    }

    public void addProfile(CameraProfile newProfile)
    {
        for (CameraProfile profile : this.profiles.list.getList())
        {
            if (profile.getDestination().equals(newProfile.getDestination()))
            {
                profile.copyFrom(newProfile);
                this.editor.setProfile(profile);

                return;
            }
        }

        this.profiles.list.add(newProfile);
        this.editor.setProfile(newProfile);
    }

    @Override
    public void draw(GuiContext context)
    {
        Gui.drawRect(this.area.x, this.area.y, this.area.ex(), this.area.ey(), 0xaa000000);

        super.draw(context);
    }

    /**
     * Search list of camera profiles 
     */
    public static class GuiCameraProfilesSearchList extends GuiSearchListElement<CameraProfile>
    {
        public GuiCameraProfilesSearchList(Minecraft mc, Consumer<List<CameraProfile>> callback)
        {
            super(mc, callback);
        }

        @Override
        protected GuiListElement<CameraProfile> createList(Minecraft mc, Consumer<List<CameraProfile>> callback)
        {
            return new GuiCameraProfilesList(mc, callback);
        }

        public CameraProfile getBy(AbstractDestination from)
        {
            for (CameraProfile profile : this.list.getList())
            {
                if (profile.getDestination().equals(from))
                {
                    return profile;
                }
            }

            return null;
        }
    }

    /**
     * Camera profile list, all in one 
     */
    public static class GuiCameraProfilesList extends GuiListElement<CameraProfile>
    {
        public GuiCameraProfilesList(Minecraft mc, Consumer<List<CameraProfile>> callback)
        {
            super(mc, callback);
        }

        @Override
        protected boolean sortElements()
        {
            Collections.sort(this.list, (o1, o2) -> o1.getDestination().getFilename().compareToIgnoreCase(o2.getDestination().getFilename()));

            return true;
        }

        @Override
        protected void drawElementPart(CameraProfile element, int i, int x, int y, boolean hover, boolean selected)
        {
            boolean hasProfile = element.exists;

            if (hasProfile)
            {
                GlStateManager.color(1, 1, 1, 1);
            }
            else
            {
                GlStateManager.color(0.5F, 0.5F, 0.5F, 1);
            }

            (element.getDestination() instanceof ClientDestination ? Icons.FOLDER : Icons.SERVER).render(x + 2, y + 2);

            this.font.drawStringWithShadow(element.getDestination().getFilename(), x + 4 + 16, y + 6, hasProfile ? (hover ? 16777120 : 0xffffff) : 0x888888);
        }

        @Override
        protected String elementToString(CameraProfile element)
        {
            return element.getDestination().getFilename();
        }
    }
}