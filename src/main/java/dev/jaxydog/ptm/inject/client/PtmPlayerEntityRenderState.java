package dev.jaxydog.ptm.inject.client;

import dev.jaxydog.ptm.api.PtmPlayerConfig;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public interface PtmPlayerEntityRenderState {

    @NotNull PtmPlayerConfig ptm$getPtmPlayerConfig();

    void ptm$setPtmPlayerConfig(final @Nullable PtmPlayerConfig config);

}
