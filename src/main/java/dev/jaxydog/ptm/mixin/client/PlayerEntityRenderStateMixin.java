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
