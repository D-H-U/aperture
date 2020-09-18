package mchorse.aperture.network.client;

import mchorse.aperture.ClientProxy;
import mchorse.aperture.camera.CameraProfile;
import mchorse.aperture.camera.destination.ServerDestination;
import mchorse.aperture.commands.CommandCamera;
import mchorse.aperture.network.common.PacketCameraProfile;
import mchorse.aperture.utils.L10n;
import mchorse.mclib.network.ClientMessageHandler;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.world.GameType;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * Client handler camera profile
 *
 * This handler is responsible for loading the camera profile received from
 * the server into static field of {@link CommandCamera} (I think it should
 * be transfered to {@link ClientProxy}), and starting the camera profile
 * if the server inform us to.
 */
public class ClientHandlerCameraProfile extends ClientMessageHandler<PacketCameraProfile>
{
    @Override
    @SideOnly(Side.CLIENT)
    public void run(EntityPlayerSP player, PacketCameraProfile message)
    {
        CameraProfile profile = message.profile;

        profile.setDestination(new ServerDestination(message.filename));
        profile.dirty = false;

        ClientProxy.getCameraEditor().profiles.addProfile(profile);

        if (message.play)
        {
            ClientProxy.runner.start(ClientProxy.control.currentProfile);
        }

        if (ClientProxy.getGameMode() != GameType.ADVENTURE)
        {
            L10n.success(player, "profile.load", message.filename);
        }
    }
}