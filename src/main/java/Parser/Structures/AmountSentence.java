package Parser.Structures;

public class AmountSentence implements QuantitativeConstraintSentence
{
    private final int amount;
    private boolean isAmountEqualEvery = false;

    public AmountSentence(int amount)
    {
        this.amount = amount;
    }

    public AmountSentence(boolean every) { isAmountEqualEvery = every; amount = 0;}

    public int getAmount() {
        return amount;
    }

    public boolean isAmountEqualEvery() {
        return isAmountEqualEvery;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AmountSentence that = (AmountSentence) o;
        return amount == that.amount && isAmountEqualEvery == that.isAmountEqualEvery;
    }
}
