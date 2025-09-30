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