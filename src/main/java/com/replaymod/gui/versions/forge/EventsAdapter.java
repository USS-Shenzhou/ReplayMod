package com.replaymod.gui.versions.forge;

import com.mojang.blaze3d.vertex.PoseStack;
import com.replaymod.gui.utils.EventRegistrations;
import com.replaymod.gui.versions.callbacks.*;
import net.minecraft.client.gui.components.Widget;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraftforge.client.event.ScreenOpenEvent;
import net.minecraftforge.client.event.ScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.ArrayList;
import java.util.List;

public class EventsAdapter extends EventRegistrations {
    public static Screen getScreen(ScreenEvent event) {
        return event.getScreen();
    }

    public static List<Widget> getButtonList(ScreenEvent.InitScreenEvent event) {
        return event.getScreen().renderables;
    }

    @SubscribeEvent
    public void preGuiInit(ScreenEvent.InitScreenEvent.Pre event) {
        InitScreenCallback.Pre.EVENT.invoker().preInitScreen(getScreen(event));
    }

    @SubscribeEvent
    public void onGuiInit(ScreenEvent.InitScreenEvent.Post event) {
        InitScreenCallback.EVENT.invoker().initScreen(getScreen(event), getButtonList(event));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onGuiClosed(ScreenOpenEvent event) {
        OpenGuiScreenCallback.EVENT.invoker().openGuiScreen(
                event.getScreen()
        );
    }

    public static float getPartialTicks(RenderGameOverlayEvent event) {
        return event.getPartialTicks();
    }

    public static float getPartialTicks(ScreenEvent.DrawScreenEvent.Post event) {
        return event.getPartialTicks();
    }

    @SubscribeEvent
    public void onGuiRender(ScreenEvent.DrawScreenEvent.Post event) {
        PostRenderScreenCallback.EVENT.invoker().postRenderScreen(new PoseStack(), getPartialTicks(event));
    }

    // Even when event was cancelled cause Lunatrius' InGame-Info-XML mod cancels it and we don't actually care about
    // the event (i.e. the overlay text), just about when it's called.
    @SubscribeEvent(receiveCanceled = true)
    public void renderOverlay(RenderGameOverlayEvent.Text event) {
        RenderHudCallback.EVENT.invoker().renderHud(new PoseStack(), getPartialTicks(event));
    }

    @SubscribeEvent
    public void tickOverlay(TickEvent.ClientTickEvent event) {
        if (event.phase == TickEvent.Phase.START) {
            PreTickCallback.EVENT.invoker().preTick();
        }
    }
}
