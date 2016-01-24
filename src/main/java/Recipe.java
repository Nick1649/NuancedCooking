/**
 * Created by nicksabelli1694 on 16-01-23.
 */
public class Recipe
{
    public int kdIngreCount = 0;
    public int kdInstrucCount = 0;

    public String[] kdIngredients = new String[50];
    public String[] kdInstructions = new String[100];

    public Recipe()
    {
        kdIngredients[0] = "To make Kraft Dinner You Will Need.";
        kdIngredients[1] = "Six cups of water.";
        kdIngredients[2] = "One package of Kraft Dinner.";
        kdIngredients[3] = "One quarter cup of margarine.";
        kdIngredients[4] = "One quarter cup of milk.";

        kdInstructions[0] = "Bring water to boil in medium saucepan. Add Macaroni; cook 7 to 8 min. or until tender, stirring occasionally.";
        kdInstructions[1] = "Drain water for saucepan and return macaroni to pan.";
        kdInstructions[2] = "Add margarine, milk and Cheese Sauce Mix then mix well.";
        kdInstructions[3] = "Congradulations! Enjoy your Kraft Dinner.";
    }

    public String getNextKDIngre()
    {
        if (kdIngreCount < kdIngredients.length)
        {
            return kdIngredients[kdIngreCount];
        }
        else
        {
            return "";
        }
    }

    public String getNextKDInstruc()
    {
        if (kdInstrucCount <= kdIngredients.length)
        {
            return kdIngredients[kdInstrucCount];
        }
        else
        {
            return "";
        }
    }

    public void resetKDIngre()
    {
        kdIngreCount = 0;
    }

    public void resetKDInstruc()
    {
        kdInstrucCount = 0;
    }
}
