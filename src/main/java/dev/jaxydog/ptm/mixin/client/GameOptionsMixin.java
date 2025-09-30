package dev.jaxydog.ptm.mixin.client;

import dev.jaxydog.ptm.PermissionToMaim;
import dev.jaxydog.ptm.api.PtmModelPart;
import dev.jaxydog.ptm.api.PtmPlayerConfig;
import dev.jaxydog.ptm.api.PtmSyncPlayerConfigPayload;
import dev.jaxydog.ptm.inject.client.PtmGameOptions;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.GameOptions;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.EnumSet;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;

@Environment(EnvType.CLIENT)
@Mixin(GameOptions.class)
public abstract class GameOptionsMixin
    implements PtmGameOptions
{

    @Unique
    private final Set<PtmModelPart> enabledPtmModelParts = EnumSet.allOf(PtmModelPart.class);
    @Unique
    private AtomicBoolean hideFloatingArmor = new AtomicBoolean(true);

    @Shadow
    protected MinecraftClient client;

    @Override
    public boolean ptm$isFloatingArmorHidden() {
        if (this.hideFloatingArmor == null) {
            this.hideFloatingArmor = new AtomicBoolean(true);
        }

        return this.hideFloatingArmor.getPlain();
    }

    @Override
    public void ptm$setFloatingArmorHidden(final boolean hidden) {
        this.hideFloatingArmor.setPlain(hidden);
    }

    @Override
    public boolean ptm$isModelPartEnabled(final @NotNull PtmModelPart modelPart) {
        return this.enabledPtmModelParts.contains(modelPart);
    }

    @Override
    public @NotNull PtmPlayerConfig ptm$getPlayerConfig() {
        return PtmPlayerConfig.fromEnabledPartSet(this.enabledPtmModelParts, this.ptm$isFloatingArmorHidden());
    }

    @Override
    public void ptm$setModelPartEnabled(final @NotNull PtmModelPart modelPart, final boolean enabled) {
        if (enabled) {
            this.enabledPtmModelParts.add(modelPart);
        } else {
            this.enabledPtmModelParts.remove(modelPart);
        }
    }

    @Inject(method = "accept", at = @At("TAIL"))
    private void accept_loadEnabledPtmModelParts(
        final @NotNull GameOptions.Visitor visitor,
        final @NotNull CallbackInfo ci
    )
    {
        final boolean savedHideFloatingArmor = visitor.visitBoolean(
            "%s.hideFloatingArmor".formatted(PermissionToMaim.MOD_ID),
            this.ptm$isFloatingArmorHidden()
        );

        if (this.ptm$isFloatingArmorHidden() != savedHideFloatingArmor) {
            this.ptm$setFloatingArmorHidden(savedHideFloatingArmor);
        }

        for (final @NotNull PtmModelPart modelPart : PtmModelPart.values()) {
            final String optionsKey = "%s.modelPart_%s".formatted(PermissionToMaim.MOD_ID, modelPart.getName());

            final boolean localValue = this.enabledPtmModelParts.contains(modelPart);
            final boolean savedValue = visitor.visitBoolean(optionsKey, localValue);

            if (localValue != savedValue) this.ptm$setModelPartEnabled(modelPart, savedValue);
        }
    }

    @Inject(method = "sendClientSettings", at = @At("TAIL"))
    private void sendClientSettings_updatePtmPlayerConfig(final @NotNull CallbackInfo ci) {
        if (Objects.nonNull(this.client.player)) {
            final PtmPlayerConfig ptmPlayerConfig = this.ptm$getPlayerConfig();

            PtmPlayerConfig.set(this.client.player, ptmPlayerConfig);
            ClientPlayNetworking.send(new PtmSyncPlayerConfigPayload(ptmPlayerConfig));
        }
    }

}
