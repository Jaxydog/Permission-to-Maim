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

package dev.jaxydog.ptm.api;

import dev.jaxydog.ptm.PermissionToMaim;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

public record PtmSyncPlayerConfigPayload(PtmPlayerConfig config)
    implements CustomPayload
{

    public static final Id<PtmSyncPlayerConfigPayload> ID =
        new Id<>(Identifier.of(PermissionToMaim.MOD_ID, "sync_player_config"));
    public static final @NotNull PacketCodec<PacketByteBuf, PtmSyncPlayerConfigPayload> CODEC = PacketCodec.tuple(
        PtmPlayerConfig.PACKET_CODEC,
        PtmSyncPlayerConfigPayload::config,
        PtmSyncPlayerConfigPayload::new
    );

    @Override
    public Id<? extends CustomPayload> getId() {
        return PtmSyncPlayerConfigPayload.ID;
    }

}
