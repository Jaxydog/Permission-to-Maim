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
