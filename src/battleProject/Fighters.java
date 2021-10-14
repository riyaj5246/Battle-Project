package battleProject;

import java.util.ArrayList;

public class Fighters {

    private ArrayList<String> fighterSkills = new ArrayList<>();
    //delete later if not needed (from constructor as well)

    private ArrayList<Integer> fighterSkillVals = new ArrayList<>();

    private String charID;

    private int lives;

    //constructor initializes fighter skills values for the enemy and stores it in array
    public Fighters(int number){
        charID = String.valueOf((char)(number + 64));

        fighterSkills.add("Speed");
        fighterSkills.add("Strength");
        fighterSkills.add("Intelligence");
        fighterSkills.add("Agility");
        fighterSkills.add("Technique");

        for (int i = 0; i < 5; i++){
            fighterSkillVals.add((int) (Math.random()* (7 -2)) + 2);
        }

        lives = 3;
    }

    public String displayID(){
        return charID;
    }

    public int getSkillVal(int index){
        return fighterSkillVals.get(index);
    }

    public void changeSkillVal(int index, int newNum){
        fighterSkillVals.set(index, newNum);
    }

    public void changeLives(int newNum){
        lives = newNum;
    }

    public int getLives(){
        return lives;
    }
}
