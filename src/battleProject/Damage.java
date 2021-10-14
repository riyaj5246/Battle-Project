package battleProject;

public class Damage {
    private int damageID;
    private String damageType;

    //assigns the type of damage (as shown on screen) based on input number
    public Damage(int input){
        damageID = input;

        switch(input){
            case 0:
                damageType = "broken leg";
                break;
            case 1:
                damageType = "broken arm";
                break;
            case 2:
                damageType = "broken ribs";
                break;
            case 3:
                damageType = "concussion";
                break;
        }
    }

    public int returnDamageID(){
        return damageID;
    }

    public String returnDamageType(){
        return damageType;
    }

}
