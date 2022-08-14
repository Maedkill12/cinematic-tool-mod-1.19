package net.maed.cinematictool.mixin;

import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PlayerEntityRenderer.class)
public class PlayerEntityRendererMixin {
    private PlayerEntityModel model;

    @ModifyVariable(method = "setModelPose", at = @At("STORE"))
    private PlayerEntityModel playerEntityModel(PlayerEntityModel playerEntityModel) {
        model = playerEntityModel;
       return model;
    }

    @Inject(method = "setModelPose", at = @At("TAIL"))
    private void onSetModelPose(AbstractClientPlayerEntity player, CallbackInfo info) {
        if (player.isSpectator()) {
            model.head.visible = false;
            model.hat.visible = false;
        }
    }
}
