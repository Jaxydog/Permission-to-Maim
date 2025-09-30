package dev.jaxydog.ptm.mixin.client;

import dev.jaxydog.ptm.api.PtmPlayerConfig;
import dev.jaxydog.ptm.inject.client.PtmPlayerEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin
    extends LivingEntityRenderer<AbstractClientPlayerEntity, PlayerEntityRenderState, PlayerEntityModel>
{

    public PlayerEntityRendererMixin(
        final @NotNull EntityRendererFactory.Context ctx,
        final @NotNull PlayerEntityModel model,
        final float shadowRadius
    )
    {
        super(ctx, model, shadowRadius);
    }

    @SuppressWarnings("RedundantCast")
    @Inject(
        method = "updateRenderState(Lnet/minecraft/client/network/AbstractClientPlayerEntity;" +
            "Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
        at = @At("TAIL")
    )
    private void updateRenderState_updatePtmPlayerConfig(
        final @NotNull AbstractClientPlayerEntity playerEntity,
        final @NotNull PlayerEntityRenderState renderState,
        final float f,
        final @NotNull CallbackInfo ci
    )
    {
        ((PtmPlayerEntityRenderState) renderState).ptm$setPtmPlayerConfig(PtmPlayerConfig.get(playerEntity));
    }

}
