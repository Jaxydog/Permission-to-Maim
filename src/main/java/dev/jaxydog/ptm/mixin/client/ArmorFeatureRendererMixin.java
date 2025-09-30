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

import com.llamalad7.mixinextras.sugar.Local;
import dev.jaxydog.ptm.api.PtmModelPart;
import dev.jaxydog.ptm.api.PtmPlayerConfig;
import dev.jaxydog.ptm.inject.client.PtmPlayerEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.command.OrderedRenderCommandQueue;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<
    S extends BipedEntityRenderState,
    M extends BipedEntityModel<S>,
    A extends BipedEntityModel<S>>
    extends FeatureRenderer<S, M>
{

    @Unique
    private @Nullable PtmPlayerEntityRenderState cachedState;

    public ArmorFeatureRendererMixin(final @NotNull FeatureRendererContext<S, M> context) {
        super(context);
    }

    @SuppressWarnings("RedundantCast")
    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;" +
            "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;" +
            "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
        at = @At("HEAD")
    )
    private void render_cacheRenderState(
        final @NotNull MatrixStack matrixStack,
        final @NotNull OrderedRenderCommandQueue orderedRenderCommandQueue,
        final int i,
        final @NotNull S bipedEntityRenderState,
        final float f,
        final float g,
        final @NotNull CallbackInfo ci
    )
    {
        if (bipedEntityRenderState instanceof final @NotNull PlayerEntityRenderState renderState &&
            ((PtmPlayerEntityRenderState) renderState).ptm$getPtmPlayerConfig().hideFloatingArmor())
        {
            this.cachedState = (PtmPlayerEntityRenderState) renderState;
        }
    }

    @ModifyVariable(method = "renderArmor", at = @At(value = "STORE"))
    private A renderArmor_setArmorPartVisibility(
        final @NotNull A entityModel,
        final @NotNull @Local(argsOnly = true) EquipmentSlot slot
    )
    {
        if (Objects.nonNull(this.cachedState)) {
            final @NotNull PtmPlayerConfig ptmPlayerConfig = this.cachedState.ptm$getPtmPlayerConfig();

            entityModel.head.visible = ptmPlayerConfig.isEnabled(PtmModelPart.HEAD);
            entityModel.hat.visible = entityModel.head.visible;
            entityModel.body.visible = ptmPlayerConfig.isEnabled(PtmModelPart.BODY);
            entityModel.rightArm.visible = ptmPlayerConfig.isEnabled(PtmModelPart.RIGHT_ARM);
            entityModel.leftArm.visible = ptmPlayerConfig.isEnabled(PtmModelPart.LEFT_ARM);
            entityModel.rightLeg.visible = ptmPlayerConfig.isEnabled(PtmModelPart.RIGHT_LEG);
            entityModel.leftLeg.visible = ptmPlayerConfig.isEnabled(PtmModelPart.LEFT_LEG);
        }

        return entityModel;
    }

    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;" +
            "Lnet/minecraft/client/render/command/OrderedRenderCommandQueue;" +
            "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
        at = @At("TAIL")
    )
    private void render_uncacheRenderState(
        final @NotNull MatrixStack matrixStack,
        final @NotNull OrderedRenderCommandQueue orderedRenderCommandQueue,
        final int i,
        final @NotNull S bipedEntityRenderState,
        final float f,
        final float g,
        final @NotNull CallbackInfo ci
    )
    {
        this.cachedState = null;
    }

}
