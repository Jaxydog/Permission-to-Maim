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
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(PlayerEntityRenderState.class)
public abstract class PlayerEntityRenderStateMixin
    extends BipedEntityRenderState
    implements PtmPlayerEntityRenderState
{

    @Unique
    private @NotNull PtmPlayerConfig ptmPlayerConfig = PtmPlayerConfig.createDefault();

    @Override
    public @NotNull PtmPlayerConfig ptm$getPtmPlayerConfig() {
        return this.ptmPlayerConfig;
    }

    @Override
    public void ptm$setPtmPlayerConfig(final @Nullable PtmPlayerConfig config) {
        this.ptmPlayerConfig = Objects.requireNonNullElseGet(config, PtmPlayerConfig::createDefault);
    }

}
