package org.jhotdraw.samples.svg.gui;

public enum ExpansionState {
    UNKNOWN, HALF, FULL;

    public static ExpansionState getExpansionState(int state) {
        switch (state) {
            case 1:
                return HALF;
            case 2:
                return FULL;
            default:
                return UNKNOWN;
        }
    }
}
