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

package dev.jaxydog.ptm;

import dev.jaxydog.ptm.api.PtmPlayerConfig;
import dev.jaxydog.ptm.api.PtmSyncPlayerConfigPayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class PermissionToMaim
    implements ModInitializer
{

    public static final @NotNull String MOD_ID = "ptm";
    public static final @NotNull Logger LOGGER = LoggerFactory.getLogger(PermissionToMaim.class);

    @Override
    public void onInitialize() {
        PayloadTypeRegistry.playC2S().register(PtmSyncPlayerConfigPayload.ID, PtmSyncPlayerConfigPayload.CODEC);
        ServerPlayNetworking.registerGlobalReceiver(
            PtmSyncPlayerConfigPayload.ID,
            (payload, context) -> PtmPlayerConfig.set(context.player(), payload.config())
        );

        final @NotNull ModMetadata metadata =
            FabricLoader.getInstance().getModContainer(PermissionToMaim.MOD_ID).orElseThrow().getMetadata();

        PermissionToMaim.LOGGER.info(
            "{} v{} has loaded! Happy (limb) hacking!",
            metadata.getName(),
            metadata.getVersion().getFriendlyString()
        );
    }

}