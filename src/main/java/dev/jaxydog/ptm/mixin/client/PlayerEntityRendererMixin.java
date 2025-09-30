/*
 * SPDX-License-Identifier: LGPL-3.0-or-later
 *
 * Copyright © 2025 Jaxydog
 *
 * This file is part of Permission to Maim.
 *
 * Permission to Maim is free software: you can redistribute it and/or modify it under the terms of the GNU Affero
 * General Public License as published by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Permission to Maim is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY; without even the
 * implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU Affero General Public License
 * for more details.
 *
 * You should have received a copy of the GNU Affero General Public License along with Permission to Maim. If not, see
 * <https://www.gnu.org/licenses/>.
 */

package dev.jaxydog.ptm.mixin.client;

import dev.jaxydog.ptm.api.PtmPlayerConfig;
import dev.jaxydog.ptm.inject.client.PtmPlayerEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.network.ClientPlayerLikeEntity;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerEntityRenderer;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.entity.PlayerLikeEntity;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderer.class)
public abstract class PlayerEntityRendererMixin<AvatarLikeEntity extends PlayerLikeEntity & ClientPlayerLikeEntity>
    extends LivingEntityRenderer<AvatarLikeEntity, PlayerEntityRenderState, PlayerEntityModel>
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
        method = "updateRenderState(Lnet/minecraft/entity/PlayerLikeEntity;" +
            "Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;F)V",
        at = @At("TAIL")
    )
    private void updateRenderState_updatePtmPlayerConfig(
        final @NotNull PlayerLikeEntity playerLikeEntity,
        final @NotNull PlayerEntityRenderState renderState,
        final float f,
        final @NotNull CallbackInfo ci
    )
    {
        ((PtmPlayerEntityRenderState) renderState).ptm$setPtmPlayerConfig(PtmPlayerConfig.get(playerLikeEntity));
    }

}
