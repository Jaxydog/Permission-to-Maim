package dev.jaxydog.ptm.mixin.client;

import dev.jaxydog.ptm.api.PtmModelPart;
import dev.jaxydog.ptm.inject.client.PtmGameOptions;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;

@Environment(EnvType.CLIENT)
@Mixin(SkinOptionsScreen.class)
public abstract class SkinOptionsScreenMixin
    extends GameOptionsScreen
{

    public SkinOptionsScreenMixin(
        final @NotNull Screen parent,
        final @NotNull GameOptions gameOptions,
        final @NotNull Text title
    )
    {
        super(parent, gameOptions, title);
    }

    @SuppressWarnings("RedundantCast")
    @Inject(method = "addOptions", at = @At("TAIL"))
    private void addOptions_addPtmButtons(final @NotNull CallbackInfo ci) {
        Objects.requireNonNull(this.body).addAll(List.of(
            new TextWidget((150 * 2) + 10, 20, Text.translatable("options.ptm.title"), this.textRenderer)
        ));

        final @NotNull PtmGameOptions ptmGameOptions = (PtmGameOptions) this.gameOptions;
        final @NotNull List<ClickableWidget> widgetList = new ObjectArrayList<>(PtmModelPart.values().length);

        for (final @NotNull PtmModelPart modelPart : PtmModelPart.values()) {
            final boolean isEnabled = ptmGameOptions.ptm$isModelPartEnabled(modelPart);

            widgetList.add(CyclingButtonWidget.onOffBuilder(isEnabled).build(
                modelPart.getOptionText(),
                (button, enabled) -> ptmGameOptions.ptm$setModelPartEnabled(modelPart, enabled)
            ));
        }

        widgetList.add(CyclingButtonWidget.onOffBuilder(ptmGameOptions.ptm$isFloatingArmorHidden()).build(
            Text.translatable("options.ptm.hideFloatingArmor"),
            (button, hidden) -> ptmGameOptions.ptm$setFloatingArmorHidden(hidden)
        ));

        Objects.requireNonNull(this.body).addAll(widgetList);
    }

}
