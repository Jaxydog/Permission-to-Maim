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

package dev.jaxydog.ptm.mixin.client;

import dev.jaxydog.ptm.api.PtmModelPart;
import dev.jaxydog.ptm.inject.client.PtmGameOptions;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.screen.option.GameOptionsScreen;
import net.minecraft.client.gui.screen.option.SkinOptionsScreen;
import net.minecraft.client.gui.widget.ClickableWidget;
import net.minecraft.client.gui.widget.CyclingButtonWidget;
import net.minecraft.client.gui.widget.OptionListWidget;
import net.minecraft.client.gui.widget.TextWidget;
import net.minecraft.client.option.GameOptions;
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;
import java.util.Objects;
import java.util.function.IntSupplier;

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

    @Unique
    private @NotNull OptionListWidget.WidgetEntry createHeader(final @NotNull Text text) {
        final IntSupplier bodyWidth = () -> Objects.requireNonNull(this.body).getWidth();

        return new OptionListWidget.WidgetEntry(List.of(new TextWidget(text, this.textRenderer)), this) {

            @Override
            public void render(
                final @NotNull DrawContext context,
                final int mouseX,
                final int mouseY,
                final boolean hovered,
                final float deltaTicks
            )
            {
                final @NotNull TextWidget widget = (TextWidget) this.widgets.getFirst();

                widget.setHeight(21);
                widget.setPosition((bodyWidth.getAsInt() / 2) - (widget.getWidth() / 2), this.getContentY());
                widget.render(context, mouseX, mouseY, deltaTicks);
            }
        };
    }

    @Inject(method = "addOptions", at = @At("HEAD"))
    private void addOptions_addVanillaHeader(final @NotNull CallbackInfo ci) {
        Objects.requireNonNull(this.body).addEntry(this.createHeader(Text.translatable("options.ptm.vanillaTitle")));
    }

    @SuppressWarnings("RedundantCast")
    @Inject(method = "addOptions", at = @At("TAIL"))
    private void addOptions_addPtmButtons(final @NotNull CallbackInfo ci) {
        Objects.requireNonNull(this.body).addEntry(this.createHeader(Text.translatable("options.ptm.title")));

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
