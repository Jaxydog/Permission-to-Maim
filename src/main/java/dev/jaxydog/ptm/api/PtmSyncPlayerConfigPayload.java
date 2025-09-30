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
