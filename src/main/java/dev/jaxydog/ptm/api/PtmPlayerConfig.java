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
import net.fabricmc.fabric.api.attachment.v1.AttachmentRegistry;
import net.fabricmc.fabric.api.attachment.v1.AttachmentSyncPredicate;
import net.fabricmc.fabric.api.attachment.v1.AttachmentType;
import net.minecraft.entity.PlayerLikeEntity;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.EnumSet;
import java.util.Set;

@SuppressWarnings("UnstableApiUsage")
public record PtmPlayerConfig(int bitSet, boolean hideFloatingArmor, boolean modEnabled) {

    public static final PacketCodec<PacketByteBuf, PtmPlayerConfig> PACKET_CODEC = PacketCodec.tuple(
        PacketCodecs.INTEGER,
        PtmPlayerConfig::bitSet,
        PacketCodecs.BOOLEAN,
        PtmPlayerConfig::hideFloatingArmor,
        PacketCodecs.BOOLEAN,
        PtmPlayerConfig::modEnabled,
        PtmPlayerConfig::new
    );

    public static final AttachmentType<PtmPlayerConfig> ATTACHMENT_TYPE = AttachmentRegistry.create(
        Identifier.of(PermissionToMaim.MOD_ID, "player_config"),
        builder -> builder.initializer(PtmPlayerConfig::createDefault).copyOnDeath().syncWith(
            PtmPlayerConfig.PACKET_CODEC,
            AttachmentSyncPredicate.all()
        )
    );

    public static @NotNull PtmPlayerConfig createDefault() {
        return PtmPlayerConfig.fromEnabledPartSet(EnumSet.allOf(PtmModelPart.class), true, true);
    }

    public static @NotNull PtmPlayerConfig fromEnabledPartSet(
        final @NotNull Set<PtmModelPart> enabledParts,
        final boolean hideFloatingArmor,
        final boolean modEnabled
    )
    {
        int bitSet = 0;

        for (final @NotNull PtmModelPart modelPart : enabledParts) {
            bitSet |= modelPart.getBitFlag();
        }

        return new PtmPlayerConfig(bitSet, hideFloatingArmor, modEnabled);
    }

    public static @NotNull PtmPlayerConfig get(final @NotNull PlayerLikeEntity playerEntity) {
        return playerEntity.getAttachedOrCreate(PtmPlayerConfig.ATTACHMENT_TYPE);
    }

    public static void set(final @NotNull PlayerLikeEntity playerEntity, final @NotNull PtmPlayerConfig config) {
        playerEntity.setAttached(PtmPlayerConfig.ATTACHMENT_TYPE, config);
    }

    public boolean isEnabled(final @NotNull PtmModelPart modelPart) {
        return (this.bitSet & modelPart.getBitFlag()) == modelPart.getBitFlag();
    }

}
