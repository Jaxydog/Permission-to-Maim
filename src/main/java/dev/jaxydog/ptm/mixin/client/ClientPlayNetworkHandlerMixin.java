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
import dev.jaxydog.ptm.api.PtmSyncPlayerConfigPayload;
import dev.jaxydog.ptm.inject.client.PtmGameOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientCommonNetworkHandler;
import net.minecraft.client.network.ClientConnectionState;
import net.minecraft.client.network.ClientPlayNetworkHandler;
import net.minecraft.network.ClientConnection;
import net.minecraft.network.listener.ClientPlayPacketListener;
import net.minecraft.network.listener.TickablePacketListener;
import net.minecraft.network.packet.s2c.play.GameJoinS2CPacket;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Environment(EnvType.CLIENT)
@Mixin(ClientPlayNetworkHandler.class)
public abstract class ClientPlayNetworkHandlerMixin
    extends ClientCommonNetworkHandler
    implements ClientPlayPacketListener, TickablePacketListener
{

    protected ClientPlayNetworkHandlerMixin(
        final MinecraftClient client,
        final ClientConnection connection,
        final ClientConnectionState connectionState
    )
    {
        super(client, connection, connectionState);
    }

    @SuppressWarnings("RedundantCast")
    @Inject(method = "onGameJoin", at = @At("TAIL"))
    private void onGameJoin_syncPtmPlayerConfig(
        final @NotNull GameJoinS2CPacket packet,
        @NotNull final CallbackInfo ci
    )
    {
        final @NotNull PtmPlayerConfig config = ((PtmGameOptions) this.client.options).ptm$getPlayerConfig();

        ClientPlayNetworking.send(new PtmSyncPlayerConfigPayload(config));
    }

}
