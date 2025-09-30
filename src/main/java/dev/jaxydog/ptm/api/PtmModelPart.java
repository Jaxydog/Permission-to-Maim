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
import net.minecraft.text.Text;
import org.jetbrains.annotations.NotNull;

import java.util.Locale;

public enum PtmModelPart {

    HEAD,
    BODY,
    LEFT_ARM,
    RIGHT_ARM,
    LEFT_LEG,
    RIGHT_LEG;

    private final @NotNull String name;
    private final @NotNull Text optionText;
    private final int bitFlag;

    PtmModelPart() {
        this.name = this.name().toLowerCase(Locale.ROOT);
        this.optionText = Text.translatable("options.%s.modelPart.%s".formatted(PermissionToMaim.MOD_ID, this.name));
        this.bitFlag = 1 << this.ordinal();
    }

    public @NotNull String getName() {
        return this.name;
    }

    public @NotNull Text getOptionText() {
        return this.optionText;
    }

    public int getBitFlag() {
        return this.bitFlag;
    }
}
