package dev.jaxydog.ptm.inject.client;

import dev.jaxydog.ptm.api.PtmModelPart;
import dev.jaxydog.ptm.api.PtmPlayerConfig;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import org.jetbrains.annotations.NotNull;

@Environment(EnvType.CLIENT)
public interface PtmGameOptions {

    boolean ptm$isFloatingArmorHidden();

    void ptm$setFloatingArmorHidden(final boolean hidden);

    boolean ptm$isModelPartEnabled(final @NotNull PtmModelPart modelPart);

    void ptm$setModelPartEnabled(final @NotNull PtmModelPart modelPart, final boolean enabled);

    @NotNull PtmPlayerConfig ptm$getPlayerConfig();

}
