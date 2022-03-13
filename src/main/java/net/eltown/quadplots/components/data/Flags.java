package net.eltown.quadplots.components.data;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Flags {

    DISABLE_LIQUIDS("Fließen von Flüssigkeiten deaktivieren"),
    USE_BUTTONS("Knöpfe für jeden erlauben"),
    USE_PRESSURE_PLATES("Druckplatten für jeden erlauben"),
    USE_LEVERS("Hebel für jeden erlauben"),
    USE_DOORS("Türen für jeden erlauben"),
    USE_TRAPDOORS("Falltüren für jeden erlauben");

    public final String description;

}
