package me.endermenskill.voreplugin.player;

/**
 * Enumerator containing the vore ranks
 */
public enum PlayerRank {
    UNSET(""),
    PREDATOR("§6[§cPRED§6]§r"),
    SWITCH("§6[§1SWITCH§6]§r"),
    PREY("§6[§aPREY§6]§r");

    private final String rankSymbol;

    PlayerRank (String symbol) {
        this.rankSymbol = symbol;
    }

    public String getSymbol() {
        return this.rankSymbol;
    }
}
