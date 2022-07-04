package model;

public class DeserialiserModel {
    private String attribute;
    private int firstDigit;
    private int lastDigit;

    public String getAttribute() {
        return attribute;
    }

    public void setAttribute(String attribute) {
        this.attribute = attribute;
    }

    public int getFirstDigit() {
        return firstDigit;
    }

    public void setFirstDigit(int firstDigit) {
        this.firstDigit = firstDigit;
    }

    public int getLastDigit() {
        return lastDigit;
    }

    public void setLastDigit(int lastDigit) {
        this.lastDigit = lastDigit;
    }
}
