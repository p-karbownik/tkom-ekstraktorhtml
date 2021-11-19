package Structures;

public class AmountSentence
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
}
