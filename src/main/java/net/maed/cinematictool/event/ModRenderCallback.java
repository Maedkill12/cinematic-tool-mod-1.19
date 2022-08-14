package net.maed.cinematictool.event;

import com.mojang.blaze3d.systems.RenderSystem;
import net.maed.cinematictool.CinematicTool;
import net.maed.cinematictool.cinematic.Cinematic;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Identifier;

public class ModRenderCallback implements net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback {
    private static final Identifier CINEMATIC = new Identifier(CinematicTool.MOD_ID, "textures/cinematic/background.png");

    @Override
    public void onHudRender(MatrixStack matrixStack, float tickDelta) {
        PlayerEntity player = MinecraftClient.getInstance().player;
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1, 1, 1,1);

        if (Cinematic.PLAYERS_IN_CINEMATIC.contains(player.getUuid())) {
            renderOverlay(CINEMATIC, 1f);
        }
    }

    private static void renderOverlay(Identifier texture, float opacity) {
        int scaledHeight = MinecraftClient.getInstance().getWindow().getScaledHeight();
        int scaledWidth = MinecraftClient.getInstance().getWindow().getScaledWidth();

        RenderSystem.disableDepthTest();
        RenderSystem.depthMask(false);
        RenderSystem.defaultBlendFunc();
        RenderSystem.setShader(GameRenderer::getPositionTexShader);
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, opacity);
        RenderSystem.setShaderTexture(0, texture);
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuffer();
        bufferBuilder.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);
        bufferBuilder.vertex(0.0, scaledHeight, -90.0).texture(0.0f, 1.0f).next();
        bufferBuilder.vertex(scaledWidth, scaledHeight, -90.0).texture(1.0f, 1.0f).next();
        bufferBuilder.vertex(scaledWidth, 0.0, -90.0).texture(1.0f, 0.0f).next();
        bufferBuilder.vertex(0.0, 0.0, -90.0).texture(0.0f, 0.0f).next();
        tessellator.draw();
        RenderSystem.depthMask(true);
        RenderSystem.enableDepthTest();
        RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
    }
}
