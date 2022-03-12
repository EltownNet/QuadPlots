package net.eltown.quadplots.components.data;

import lombok.RequiredArgsConstructor;

// Todo
@RequiredArgsConstructor
public enum PlotFlag {

    disable_liquids("Flüssigkeiten Deaktiviert"),
    use_buttons("Knöpfe für jeden aktivieren"),
    description("Beschreibung"),
    name("Name");

    public final String display;

    /* Internal:
    merge, origin
     */

}
