package model;

public final class SaleEntry {

    private final String item;
    private final String category;
    private final int units;
    private final double unitPrice;

    public SaleEntry(String item, String category, int units, double unitPrice) {
        this.item = item;
        this.category = category;
        this.units = units;
        this.unitPrice = unitPrice;
    }

    public String getItem() {
        return item;
    }

    public String getCategory() {
        return category;
    }

    public int getUnits() {
        return units;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public double revenue() {
        return units * unitPrice;
    }

    @Override
    public String toString() {
        return "SaleEntry[" + item + ", " + category + ", " + units + " x " + unitPrice + "]";
    }
}
