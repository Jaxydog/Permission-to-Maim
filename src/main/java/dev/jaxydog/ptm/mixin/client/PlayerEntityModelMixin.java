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

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import com.llamalad7.mixinextras.sugar.Local;
import dev.jaxydog.ptm.api.PtmModelPart;
import dev.jaxydog.ptm.api.PtmPlayerConfig;
import dev.jaxydog.ptm.inject.client.PtmPlayerEntityModel;
import dev.jaxydog.ptm.inject.client.PtmPlayerEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityModel.class)
public abstract class PlayerEntityModelMixin
    extends BipedEntityModel<PlayerEntityRenderState>
    implements PtmPlayerEntityModel
{

    @Unique
    private boolean isArmorModel = false;

    public PlayerEntityModelMixin(final @NotNull ModelPart modelPart) {
        super(modelPart);
    }

    @Override
    public boolean ptm$isArmorModel() {
        return this.isArmorModel;
    }

    @Override
    public void ptm$setArmorModel(final boolean isArmorModel) {
        this.isArmorModel = isArmorModel;
    }

    @SuppressWarnings("RedundantCast")
    @Unique
    private boolean isPartVisible(
        final @NotNull PlayerEntityRenderState renderState,
        final @NotNull PtmModelPart modelPart
    )
    {
        final @NotNull PtmPlayerEntityRenderState ptmState = (PtmPlayerEntityRenderState) renderState;
        final @NotNull PtmPlayerConfig ptmConfig = ptmState.ptm$getPtmPlayerConfig();

        if (!ptmConfig.modEnabled()) return true;

        return this.ptm$isArmorModel()
            ? !ptmConfig.hideFloatingArmor() || ptmConfig.isEnabled(modelPart)
            : ptmConfig.isEnabled(modelPart);
    }

    @Inject(
        method = "setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 0)
    )
    private void setAngles_setHeadVisibility(
        final PlayerEntityRenderState renderState,
        final CallbackInfo ci,
        final @Local boolean notInSpectator
    )
    {
        this.head.visible = this.ptm$isArmorModel()
            ? notInSpectator && this.isPartVisible(renderState, PtmModelPart.HEAD)
            : !notInSpectator || this.isPartVisible(renderState, PtmModelPart.HEAD);
    }

    @WrapOperation(
        method = "setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 0)
    )
    private void setAngles_setBodyVisibility(
        final ModelPart instance,
        final boolean notInSpectator,
        final Operation<Void> original,
        final @Local(argsOnly = true) PlayerEntityRenderState renderState
    )
    {
        instance.visible = notInSpectator && this.isPartVisible(renderState, PtmModelPart.BODY);
    }

    @WrapOperation(
        method = "setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 1)
    )
    private void setAngles_setRightArmVisibility(
        final ModelPart instance,
        final boolean notInSpectator,
        final Operation<Void> original,
        final @Local(argsOnly = true) PlayerEntityRenderState renderState
    )
    {
        instance.visible = notInSpectator && this.isPartVisible(renderState, PtmModelPart.RIGHT_ARM);
    }

    @WrapOperation(
        method = "setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 2)
    )
    private void setAngles_setLeftArmVisibility(
        final ModelPart instance,
        final boolean notInSpectator,
        final Operation<Void> original,
        final @Local(argsOnly = true) PlayerEntityRenderState renderState
    )
    {
        instance.visible = notInSpectator && this.isPartVisible(renderState, PtmModelPart.LEFT_ARM);
    }

    @WrapOperation(
        method = "setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 3)
    )
    private void setAngles_setRightLegVisibility(
        final ModelPart instance,
        final boolean notInSpectator,
        final Operation<Void> original,
        final @Local(argsOnly = true) PlayerEntityRenderState renderState
    )
    {
        instance.visible = notInSpectator && this.isPartVisible(renderState, PtmModelPart.RIGHT_LEG);
    }

    @WrapOperation(
        method = "setAngles(Lnet/minecraft/client/render/entity/state/PlayerEntityRenderState;)V",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 4)
    )
    private void setAngles_setLeftLegVisibility(
        final ModelPart instance,
        final boolean notInSpectator,
        final Operation<Void> original,
        final @Local(argsOnly = true) PlayerEntityRenderState renderState
    )
    {
        instance.visible = notInSpectator && this.isPartVisible(renderState, PtmModelPart.LEFT_LEG);
    }

}
