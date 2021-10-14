package battleProject;

public class Powerups {
    private String powerUpName;
    private int quantity;
    private int cost;

    //powerups constructor
    public Powerups(String name, int money){
        powerUpName = name;
        cost = money;
        quantity = 0;
    }

    public String getPowerUpName(){
        return powerUpName;
    }

    public int getCost(){
        return cost;
    }

    public void purchase(int chosenPowerUp){
        quantity += 1;
    }
}
