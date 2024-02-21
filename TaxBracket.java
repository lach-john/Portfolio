//Lachlan McQualter c3205442 - SENG4500 A1

public class TaxBracket implements Comparable<TaxBracket>
{
    private String bracketID;
    private Double leftLim;
    private Double rightLim;
    private Double baseTax;
    private Double centsPerDollar;

    public TaxBracket()
    {
        //Default constructor
    }

    public TaxBracket(Double leftLimit, Double rightLimit, Double base, Double centsPer)
    {
        leftLim = leftLimit;
        rightLim = rightLimit;
        bracketID = (leftLim + " - " + rightLim).toString();
        baseTax = base;
        centsPerDollar = centsPer;
    }

    public void setLeft(Double newLeft)
    {
        leftLim = newLeft;
        bracketID = (leftLim + " - " + rightLim).toString();
    }

    public void setRight(Double newRight)
    {
        rightLim = newRight;
        bracketID = (leftLim + " - " + rightLim).toString();
    }
    
    public Double getLeft()
    {
        return leftLim;
    }

    public Double getRight()
    {
        return rightLim;
    }

    public void setBaseTax(Double newBase)
    {
        baseTax = newBase;
    }

    public Double getBaseTax()
    {
        return baseTax;
    }

    public void setCentsPer(Double newCentsPer)
    {
        centsPerDollar = newCentsPer;
    }

    public Double getCentsPer()
    {
        return centsPerDollar;
    }

    public String getBracket()
    {
        return bracketID;
    }

    //Compare to method used by collections to sort list
    public int compareTo(TaxBracket x) 
    {
        return Double.compare(getLeft(), x.getLeft());
    }

}