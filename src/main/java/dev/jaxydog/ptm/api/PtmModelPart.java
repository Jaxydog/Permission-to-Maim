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
