package com.seldavine.tree_tracking.Model;


public class AllTotal {
    private int afforestation,deforestation;

    public AllTotal() {
    }

    public AllTotal(int afforestation, int deforestation) {
        this.afforestation = afforestation;
        this.deforestation = deforestation;
    }

    public int getAfforestation() {
        return afforestation;
    }

    public void setAfforestation(int afforestation) {
        this.afforestation = afforestation;
    }

    public int getDeforestation() {
        return deforestation;
    }

    public void setDeforestation(int deforestation) {
        this.deforestation = deforestation;
    }
}
