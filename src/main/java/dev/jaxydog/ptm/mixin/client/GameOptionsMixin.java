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

import dev.jaxydog.ptm.PermissionToMaim;
import dev.jaxydog.ptm.api.PtmModelPart;
import dev.jaxydog.ptm.api.PtmPlayerConfig;
import dev.jaxydog.ptm.api.PtmSyncPlayerConfigPayload;
import dev.jaxydog.ptm.inject.client.PtmGameOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Environment(EnvType.CLIENT)
@Mixin(GameOptions.class)
public abstract class GameOptionsMixin
    implements PtmGameOptions
{

    @Unique
    private final Set<PtmModelPart> enabledPtmModelParts = EnumSet.allOf(PtmModelPart.class);
    @Unique
    private AtomicBoolean hideFloatingArmor = new AtomicBoolean(true);
    @Unique
    private AtomicBoolean modEnabled = new AtomicBoolean(true);

    @Shadow
    protected MinecraftClient client;

    @Override
    public boolean ptm$isModEnabled() {
        if (Objects.isNull(this.modEnabled)) {
            this.modEnabled = new AtomicBoolean(true);
        }

        return this.modEnabled.getPlain();
    }

    @Override
    public void ptm$setModEnabled(final boolean enabled) {
        if (Objects.nonNull(this.modEnabled)) {
            this.modEnabled.setPlain(enabled);
        } else {
            this.modEnabled = new AtomicBoolean(enabled);
        }
    }

    @Override
    public boolean ptm$isFloatingArmorHidden() {
        if (Objects.isNull(this.hideFloatingArmor)) {
            this.hideFloatingArmor = new AtomicBoolean(true);
        }

        return this.hideFloatingArmor.getPlain();
    }

    @Override
    public void ptm$setFloatingArmorHidden(final boolean hidden) {
        if (Objects.nonNull(this.hideFloatingArmor)) {
            this.hideFloatingArmor.setPlain(hidden);
        } else {
            this.hideFloatingArmor = new AtomicBoolean(hidden);
        }
    }

    @Override
    public boolean ptm$isModelPartEnabled(final @NotNull PtmModelPart modelPart) {
        return this.enabledPtmModelParts.contains(modelPart);
    }

    @Override
    public @NotNull PtmPlayerConfig ptm$getPlayerConfig() {
        return PtmPlayerConfig.fromEnabledPartSet(
            this.enabledPtmModelParts,
            this.ptm$isFloatingArmorHidden(),
            this.ptm$isModEnabled()
        );
    }

    @Override
    public void ptm$setModelPartEnabled(final @NotNull PtmModelPart modelPart, final boolean enabled) {
        if (enabled) {
            this.enabledPtmModelParts.add(modelPart);
        } else {
            this.enabledPtmModelParts.remove(modelPart);
        }
    }

    @Inject(method = "accept", at = @At("TAIL"))
    private void accept_loadEnabledPtmModelParts(
        final @NotNull GameOptions.Visitor visitor,
        final @NotNull CallbackInfo ci
    )
    {
        final boolean savedModEnabled = visitor.visitBoolean(
            "%s.modEnabled".formatted(PermissionToMaim.MOD_ID),
            this.ptm$isModEnabled()
        );

        if (this.ptm$isModEnabled() != savedModEnabled) {
            this.ptm$setModEnabled(savedModEnabled);
        }

        final boolean savedHideFloatingArmor = visitor.visitBoolean(
            "%s.hideFloatingArmor".formatted(PermissionToMaim.MOD_ID),
            this.ptm$isFloatingArmorHidden()
        );

        if (this.ptm$isFloatingArmorHidden() != savedHideFloatingArmor) {
            this.ptm$setFloatingArmorHidden(savedHideFloatingArmor);
        }

        for (final @NotNull PtmModelPart modelPart : PtmModelPart.values()) {
            final String optionsKey = "%s.modelPart_%s".formatted(PermissionToMaim.MOD_ID, modelPart.getName());

            final boolean localValue = this.enabledPtmModelParts.contains(modelPart);
            final boolean savedValue = visitor.visitBoolean(optionsKey, localValue);

            if (localValue != savedValue) this.ptm$setModelPartEnabled(modelPart, savedValue);
        }
    }

    @Inject(method = "sendClientSettings", at = @At("TAIL"))
    private void sendClientSettings_updatePtmPlayerConfig(final @NotNull CallbackInfo ci) {
        if (Objects.nonNull(this.client.player)) {
            final PtmPlayerConfig ptmPlayerConfig = this.ptm$getPlayerConfig();

            PtmPlayerConfig.set(this.client.player, ptmPlayerConfig);
            ClientPlayNetworking.send(new PtmSyncPlayerConfigPayload(ptmPlayerConfig));
        }
    }

}
