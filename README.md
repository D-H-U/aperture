![Aperture](https://i.imgur.com/Wras78u.png)

[Planet Minecraft page](https://www.planetminecraft.com/mod/aperture-3978432/) – [CurseForge page](https://www.curseforge.com/minecraft/mc-mods/aperture) – [GitHub](https://github.com/mchorse/aperture) – [Wiki](https://github.com/mchorse/aperture/wiki) 

**Disclaimer**: this mod has nothing to do with Aperture Science from Portal games. See the article on what is [Aperture](https://en.wikipedia.org/wiki/Aperture) actually is.

**Aperture** is a Minecraft mod which allows you to create advanced camera movement (for cinematics or machinimas) using camera editor GUI. It works with Forge for Minecraft 1.12.2 (past versions available for 1.10.2 and 1.11.2).

## Features

**Aperture** mod provides a lot of features for flexible camera editing.

* **Loadable and saveable camera profiles**. You don't need to worry about losing your camera setup. With Aperture's camera profiles, you can save your camera setup on the server (if the Aperture is installed on the server) or on the client (in `config/aperture/cameras` folder).
* **Duration is measured in ticks**. A tick is basically an update in game's logic. Minecraft's logic loop running at 20 ticks per second. Why ticks? They're static, while frames depend on framerate, and can be easily converted into seconds (while not depending on real-time).
* **Flexible camera setup with multiple camera fixtures within a camera profile**. Other camera mods usually gives you an ability to setup only one path at the time. Aperture allows you to have as much camera paths (and not only) as you need to within same camera profile. There are few types of camera fixtures in Aperture: 
    * Idle fixture – holds camera in given position and angle.
    * Circular fixture – circulate around the center point and facing at it.
    * Path fixture – animates the camera through a set of given points using one of the three interpolations: linear, cubic or hermite. Also supports keyframeable velocity control and per point durations.
    * Keyframe fixture – allows you create a flexible camera behavior using keyframes (which can have different interpolations, including bézier, and easing).
    * Null fixture – a placeholder fixture, which mimics next fixture's first position or previous fixture's last position.
    * Manual fixture — allows you to record completely custom movement.
* **More camera flexibility with camera modifiers**. Camera modifiers are modular blocks which post-process camera fixture's output. With these modifiers, you can add camera shake, apply math equation, make a GoPro-like behavior, look at some entity while traveling a path, and much more combined. See [wiki](https://github.com/mchorse/aperture/wiki) for more information.
* **Provides a smooth camera** as a Minema-friendly alternative of vanilla cinematic camera.
* **Compatible with [Minema](https://github.com/daipenger/minema/releases)**, if want to record smooth videos on your Potato PC, install [Minema](https://github.com/daipenger/minema/releases) mod and record smooth videos!

## Videos

This playlist is a **tutorial series**. It should teach you how to use Aperture mod from scratch. It bases of Aperture `1.3.4`. Once you watch these videos and learn how to use it, feel free to watch the change log videos for more information about new features.

<a href="https://youtu.be/_KLU8VnMiCQ?list=PLLnllO8nnzE8MGDb6QzE2kt4-KVC1dRRl"><img src="https://img.youtube.com/vi/_KLU8VnMiCQ/0.jpg"></a> 

Meanwhile, this playlist contains a list of videos about **Aperture's updates**. These are so-called **change log** videos, which showcase new changes added to Aperture mod.

<a href="https://youtu.be/6eil_zvv1KI?list=PL6UPd2Tj65nFLGMBqKaeKOPNp2HOO86Uw"><img src="https://img.youtube.com/vi/6eil_zvv1KI/0.jpg"></a> 

## Install

Install [Minecraft Forge](http://files.minecraftforge.net/), download the latest stable version of jar file for available Minecraft version. Also install following mods: [McLib](https://www.curseforge.com/minecraft/mc-mods/mchorses-mclib) (version **2.0**). Put it in minecraft's `mods` folder, and launch the game.

After that, Aperture mod should be installed and will appear in Minecraft's mods menu. If Aperture didn't appear in the mods menu, then something went wrong. 

If you're interested in this project, you might as well follow me on any of social media accounts listed below:

[![YouTube](http://i.imgur.com/yA4qam9.png)](https://www.youtube.com/channel/UCSLuDXxxql4EVK_Ktd6PNbw) [![Discord](http://i.imgur.com/gI6JEpJ.png)](https://discord.gg/qfxrqUF) [![Twitter](http://i.imgur.com/6b8vHcX.png)](https://twitter.com/McHorsy) [![GitHub](http://i.imgur.com/DmTn1f1.png)](https://github.com/mchorse) 

Also, I would really appreciate if you will support me on Patreon!

[![Become my Patron](https://i.imgur.com/4pQZ2xW.png)](https://www.patreon.com/McHorse) 

## Bug reports

If you found a bug, or this mod crashed your game. I'll appreciate if you could report a bug or a crash to me either on [issue tracker](https://github.com/mchorse/aperture/issues/), on PM or on [Twitter](https://twitter.com/McHorsy). Please, make sure to attach a crash log ([pastebin](http://pastebin.com) please) and description of a bug or crash and the way to reproduce it. Thanks!