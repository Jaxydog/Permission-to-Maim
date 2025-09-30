package dev.jaxydog.ptm.mixin.client;

import com.llamalad7.mixinextras.injector.v2.WrapWithCondition;
import dev.jaxydog.ptm.api.PtmModelPart;
import dev.jaxydog.ptm.inject.client.PtmPlayerEntityRenderState;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.ModelPart;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.feature.ArmorFeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.BipedEntityModel;
import net.minecraft.client.render.entity.state.BipedEntityRenderState;
import net.minecraft.client.render.entity.state.PlayerEntityRenderState;
import net.minecraft.client.util.math.MatrixStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(ArmorFeatureRenderer.class)
public abstract class ArmorFeatureRendererMixin<
    S extends BipedEntityRenderState,
    M extends BipedEntityModel<S>,
    A extends BipedEntityModel<S>>
    extends FeatureRenderer<S, M>
{

    @Unique
    private @Nullable PtmPlayerEntityRenderState cachedState;

    public ArmorFeatureRendererMixin(final @NotNull FeatureRendererContext<S, M> context) {
        super(context);
    }

    @SuppressWarnings("RedundantCast")
    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;" +
            "Lnet/minecraft/client/render/VertexConsumerProvider;" +
            "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
        at = @At("HEAD")
    )
    private void render_cacheRenderState(
        final @NotNull MatrixStack matrixStack,
        final @NotNull VertexConsumerProvider vertexConsumerProvider,
        final int i,
        final @NotNull S bipedEntityRenderState,
        final float f,
        final float g,
        final @NotNull CallbackInfo ci
    )
    {
        if (bipedEntityRenderState instanceof final @NotNull PlayerEntityRenderState renderState &&
            ((PtmPlayerEntityRenderState) renderState).ptm$getPtmPlayerConfig().hideFloatingArmor())
        {
            this.cachedState = (PtmPlayerEntityRenderState) renderState;
        }
    }

    @Inject(
        method = "render(Lnet/minecraft/client/util/math/MatrixStack;" +
            "Lnet/minecraft/client/render/VertexConsumerProvider;" +
            "ILnet/minecraft/client/render/entity/state/BipedEntityRenderState;FF)V",
        at = @At("TAIL")
    )
    private void render_uncacheRenderState(
        final @NotNull MatrixStack matrixStack,
        final @NotNull VertexConsumerProvider vertexConsumerProvider,
        final int i,
        final @NotNull S bipedEntityRenderState,
        final float f,
        final float g,
        final @NotNull CallbackInfo ci
    )
    {
        this.cachedState = null;
    }

    @WrapWithCondition(
        method = "setVisible",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 0)
    )
    private boolean setVisible_setHeadArmorPartVisibility(final @NotNull ModelPart instance, final boolean value) {
        if (Objects.nonNull(this.cachedState)) {
            return this.cachedState.ptm$getPtmPlayerConfig().isEnabled(PtmModelPart.HEAD);
        }

        return value;
    }

    @WrapWithCondition(
        method = "setVisible",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 2)
    )
    private boolean setVisible_setBodyArmorPartVisibility(final @NotNull ModelPart instance, final boolean value) {
        if (Objects.nonNull(this.cachedState)) {
            return this.cachedState.ptm$getPtmPlayerConfig().isEnabled(PtmModelPart.BODY);
        }

        return value;
    }

    @WrapWithCondition(
        method = "setVisible",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 5)
    )
    private boolean setVisible_setLegBodyArmorPartVisibility(final @NotNull ModelPart instance, final boolean value) {
        if (Objects.nonNull(this.cachedState)) {
            return this.cachedState.ptm$getPtmPlayerConfig().isEnabled(PtmModelPart.BODY);
        }

        return value;
    }

    @WrapWithCondition(
        method = "setVisible",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 3)
    )
    private boolean setVisible_setRightArmArmorPartVisibility(final @NotNull ModelPart instance, final boolean value) {
        if (Objects.nonNull(this.cachedState)) {
            return this.cachedState.ptm$getPtmPlayerConfig().isEnabled(PtmModelPart.RIGHT_ARM);
        }

        return value;
    }

    @WrapWithCondition(
        method = "setVisible",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 4)
    )
    private boolean setVisible_setLeftArmArmorPartVisibility(final @NotNull ModelPart instance, final boolean value) {
        if (Objects.nonNull(this.cachedState)) {
            return this.cachedState.ptm$getPtmPlayerConfig().isEnabled(PtmModelPart.LEFT_ARM);
        }

        return value;
    }

    @WrapWithCondition(
        method = "setVisible",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 6)
    )
    private boolean setVisible_setRightLegArmorPartVisibility(final @NotNull ModelPart instance, final boolean value) {
        if (Objects.nonNull(this.cachedState)) {
            return this.cachedState.ptm$getPtmPlayerConfig().isEnabled(PtmModelPart.RIGHT_LEG);
        }

        return value;
    }

    @WrapWithCondition(
        method = "setVisible",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 8)
    )
    private boolean setVisible_setRightFootArmorPartVisibility(final @NotNull ModelPart instance, final boolean value) {
        if (Objects.nonNull(this.cachedState)) {
            return this.cachedState.ptm$getPtmPlayerConfig().isEnabled(PtmModelPart.RIGHT_LEG);
        }

        return value;
    }

    @WrapWithCondition(
        method = "setVisible",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 7)
    )
    private boolean setVisible_setLeftLegArmorPartVisibility(final @NotNull ModelPart instance, final boolean value) {
        if (Objects.nonNull(this.cachedState)) {
            return this.cachedState.ptm$getPtmPlayerConfig().isEnabled(PtmModelPart.LEFT_LEG);
        }

        return value;
    }

    @WrapWithCondition(
        method = "setVisible",
        at = @At(value = "FIELD", target = "Lnet/minecraft/client/model/ModelPart;visible:Z", ordinal = 9)
    )
    private boolean setVisible_setLeftFootArmorPartVisibility(final @NotNull ModelPart instance, final boolean value) {
        if (Objects.nonNull(this.cachedState)) {
            return this.cachedState.ptm$getPtmPlayerConfig().isEnabled(PtmModelPart.LEFT_LEG);
        }

        return value;
    }

}
