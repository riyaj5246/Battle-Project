package battleProject;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;

import java.util.ArrayList;
import java.util.Random;

public class Controller {

    //ArrayList instance fields that are used throughout the game
    private ArrayList<Fighters> opponents = new ArrayList<>(); //contains all opponents
    private ArrayList<Powerups> powerUps = new ArrayList<>(); //contains all available powerups
    private ArrayList<String> skills = new ArrayList<>(); //contains the String names of all skills
    private ArrayList<Integer> skillVals = new ArrayList<>(); //contains the int values of the user's skills

    //all other relevant user stats
    private int livesLeft = 3, battlesWon = 0, battlesLost = 0, winStreak = 0, bank = 200;

    //boolean which establishes precondition before the game is initialized
    private boolean isUsernameSet;

    //UI related features
    @FXML
    Button start, battleBtn, powerUpBtn, skillLvlUpBtn, enemyInfoBtn;

    @FXML
    ListView opponentsList, scoresList, recordList, powerupsList, updatesList;

    @FXML
    Label battlesWonLbl, battlesLostLbl, winStreakLbl, livesLbl, bankLbl,userHeader, opponentsHeader, statsHeader, powerupsHeader;

    @FXML
    Text instructionsTxt;

    @FXML
    Rectangle instructionsRect;

    @FXML
    TextField usernameTxt;

    //sets the instructions either visible or invisible when button is clicked
    @FXML
    private void instructionsBtn(){
        instructionsRect.setVisible(!instructionsRect.isVisible());
        instructionsTxt.setVisible(!instructionsTxt.isVisible());
    }

    //Makes sure username is entered into the correct box so that initialization can happen
    @FXML
    private void usernameEntered(){
        isUsernameSet = true;
    }

    //if username is set, then initializes game by setting multiple texts to correct headings and calling 3 separate initialize functions
    @FXML
    private void startButton(){
        if(isUsernameSet){

            //sets relevant text to all the headers on top of the listview
            userHeader.setText(usernameTxt.getText() + "'s Info");
            opponentsHeader.setText("Opponents");
            statsHeader.setText("Battle Stats");
            powerupsHeader.setText("Powerups");

            //disables start button and usernameTxt because game cannot be re-initialized unless reset
            start.setDisable(true);
            usernameTxt.setDisable(true);

            //initialization of three components of the game
            initializeMainCharacter();
            initializePowerups();
            initializeEnemy();
        }
        else{
            updatesList.getItems().add(0, "Please enter in a username before pressing start to begin");
        }

    }

    //initializes 5 powerups from the Powerups class and adds them to the powerUps arrayList
    private void initializePowerups(){
        // powerups (String "displayed name of powerup", money it consts to use)
        powerUps.add(new Powerups("Level up one skill", 50));
        powerUps.add(new Powerups("Reveal enemy skills", 100));
        powerUps.add(new Powerups("Level up all skills", 200));
        powerUps.add(new Powerups("Add a life", 400));
        powerUps.add(new Powerups("Find most winnable opponent", 750));

        //all 5 are displayed in the powerups Listview
        for(int i = 0; i < powerUps.size(); i++){
            powerupsList.getItems().add(powerUps.get(i).getPowerUpName() + ", $" + powerUps.get(i).getCost());
        }
    }

    //initializes main character by setting all the skills and skill values
    private void initializeMainCharacter(){
        //beginning number of lives
        livesLeft = 3;

        //Adds the display name of all 5 skills
        skills.add("Speed");
        skills.add("Strength");
        skills.add("Intelligence");
        skills.add("Agility");
        skills.add("Technique");

        //indexes of skills match those of skillVals
        //random values between 3 to 7 as starting values for skills, which are all then added to the scores Listview
        for (int i = 0; i < 5; i++){
            skillVals.add((int) (Math.random()* (7 - 3)) + 3);
            scoresList.getItems().add(skills.get(i) + ": " + skillVals.get(i));
        }

        //called in multiple methods to update the UI with the most recent/relvant information about the user
        updateDisplay();
    }

    //initializes all the opponents in one for loop
    private void initializeEnemy(){

       //initializes 10 objects from the Fighters class and adds them to the opponents arrayList and ListView
        //skill values for opponents randomly determined in the Fighters class
        for(int i = 0; i < 10; i++){
            opponents.add(new Fighters(i + 1));
            opponentsList.getItems().add("Opponent " + opponents.get(i).displayID());
        }
    }

    //method used to simulate the entire battle between the user and 1 opponent
    private void battle(Fighters enemy){
        //instance of Random class created, used primarily for random booleans (Math.rand() used for random ints)
        Random rand = new Random();

        //keeps track of how many skills the main character/enemy each win
        int totalHeroWins = 0;
        int totalVillainWins = 0;

        //battle carried out for each of the 5 skills
        //loop used to determine the number of hero wins and the number of enemy wins
        for(int i = 0; i < 5; i++){
            //adds an element of randomization to the raw score: the actual score used can be plus/minus 3 of original for both villain and hero
            int heroScore = skillVals.get(i);
            heroScore = (int) (Math.random() * ((heroScore + 3) - (heroScore - 3)) + (heroScore - 3));

            int villainScore = enemy.getSkillVal(i);
            villainScore = (int) (Math.random() * ((villainScore + 3) - (villainScore - 3)) + (villainScore - 3));

            //after establishing scores, adds a win to whoever had the higher score
            if(heroScore > villainScore){
                totalHeroWins++;
            }
            else if(villainScore > heroScore){
                totalVillainWins++;
            }
            else{ //in the case of a tie, where both scores are the same, a random boolean is used to decide the winner
                boolean randBoolean = rand.nextBoolean();
                if(randBoolean){ //if true, user wins
                    totalHeroWins++;
                }
                else{ //if false, opponent wins
                    totalVillainWins++;
                }
            }

        }

        //used to evaluate the winner of the battle and change data accordingly
        if(totalHeroWins > totalVillainWins){
            //FOR WINNER (main character)

            //randomly increases skills if random boolean evaluates to true and skills aren't already at max
            for(int i = 0; i < skillVals.size(); i++){
                boolean randBoolean = rand.nextBoolean();

                if(skillVals.get(i) != 10 && randBoolean){
                    skillVals.set(i, skillVals.get(i) + 1);
                }
            }
            //stats changed accordingly
            livesLeft = 3;
            winStreak++;
            battlesWon++;
            bank += 50 + (winStreak-1)*10;

            //FOR LOSER (opponent)
            //certain skills values are randomly dropped
            for(int i = 0; i < skillVals.size(); i++){
                boolean randBoolean = rand.nextBoolean();

                if(enemy.getSkillVal(i) > 0 && randBoolean){
                    enemy.changeSkillVal(i, enemy.getSkillVal(i) - 1);
                }
            }

            //correct message displayed on screen, and enemy loses a life
            enemy.changeLives(enemy.getLives() - 1);
            recordList.getItems().add(0, enemy.displayID() + ": W, " + totalHeroWins + "-" + totalVillainWins);
            updatesList.getItems().add(0,"You won against opponent " + enemy.displayID() + ". You dominated in " + totalHeroWins + "/5 skills.");

        }
        //happens if user loses the battle
        else if(totalVillainWins > totalHeroWins){

            //FOR LOSER (main character)
            //random skills dropped
            for(int i = 0; i < skillVals.size(); i++){
                boolean randBoolean = rand.nextBoolean();

                if(skillVals.get(i) > 0 && randBoolean){
                    skillVals.set(i, skillVals.get(i) - 1);
                }
            }
            //stats of user changed accordingly
            livesLeft--;
            winStreak = 0;
            battlesLost++;

            //FOR WINNER (opponent)
            //skills are randomly improved for the enemy that won
            for(int i = 0; i < skillVals.size(); i++){
                boolean randBoolean = rand.nextBoolean();

                if(enemy.getSkillVal(i) != 10 && randBoolean){
                    enemy.changeSkillVal(i, skillVals.get(i) + 1);
                }
            }

            //correct messages shown on the screen
            updatesList.getItems().add(0,"You lost against opponent " + enemy.displayID() + ". You dominated in " + totalHeroWins + "/5 skills.");
            recordList.getItems().add(enemy.displayID() + ": L, " + totalHeroWins + "-" + totalVillainWins);

        }
        updateDisplay();
    }

    //enables user to click battle button once an opponent is selected
    @FXML
    private void opponentClicked(){
        battleBtn.setDisable(false);
    }

    //called when user clicks the battle button, makes sure that chosen opponent can be battled, and battles the opponent
    @FXML
    private void battleButton(){
        battleBtn.setDisable(true);
        int opponentChosen = opponentsList.getSelectionModel().getSelectedIndex();
        Fighters villain = opponents.get(opponentChosen);

        //evaluates if the villain chosen still has lives left
        if(villain.getLives() == 0){
           updatesList.getItems().add(0, "This opponent lost all its lives, so pick someone else.");
        }
        else{
            //calls the battle function and checks for certain conditions after battle
            battle(villain);
            checkDamage();
            checkIfWon();
        }
    }

    //Update display with most current information
    private void updateDisplay(){
        livesLbl.setText("Lives Left: " + livesLeft);
        winStreakLbl.setText("Win Streak: " + winStreak);
        battlesWonLbl.setText("Battles Won: " + battlesWon);
        battlesLostLbl.setText("Battles Lost: " + battlesLost);
        bankLbl.setText("Bank: $" + bank);

        scoresList.getItems().clear();
        
        for (int i = 0; i < skills.size(); i++){
            scoresList.getItems().add(skills.get(i) + ": " + skillVals.get(i));
        }
    }

    //enables user to click powerups button once powerup is selected from listview
    @FXML
    private void powerUpClicked(){
        powerUpBtn.setDisable(false);
    }

    //called when the buy powerups button is selected to complete purchase
    @FXML
    private void buyPowerup(){
        int powerUpChosen = powerupsList.getSelectionModel().getSelectedIndex();
        Powerups powerUp = powerUps.get(powerUpChosen);

        //makes sure user has enough money in the bank
        if(powerUp.getCost() > bank){
            updatesList.getItems().add(0, "You do not have enough money for this purchase");
        }
        else{
            powerUp.purchase(powerUpChosen);
            bank -= powerUp.getCost();

            //displays message to confirm that powerup was bought in the updates listView, and if further action is necessary, allows for it
            switch(powerUpChosen){
                case 0:
                    skillLvlUpBtn.setVisible(true);
                    updatesList.getItems().add(0, "Power up bought. Click the skill you want to upgrade and then press level up.");
                    break;

                case 1:
                    enemyInfoBtn.setVisible(true);
                    updatesList.getItems().add(0, "Power up bought. Click the enemy you want to examine and then press display info.");
                    break;

                case 2:
                    for(int i = 0; i < skillVals.size(); i++){
                        skillVals.set(i, skillVals.get(i) + 1);
                    }
                    updatesList.getItems().add(0, "Power up bought. Each skill has increased by 1");
                    break;

                case 3:
                    livesLeft += 1;
                    updatesList.getItems().add(0, "Power up bought. Lives have increased by 1");

                    break;

                case 4:
                    determineBestLevel();
                    break;

            }
            updateDisplay();
            powerUpBtn.setDisable(true);
        }

    }

    //called when a button specific to the skill level up powerup is clicked
    //levels up chosen skill
    @FXML
    private void skillLvlUp(){
        int chosenSkill = scoresList.getSelectionModel().getSelectedIndex();
        skillVals.set(chosenSkill, skillVals.get(chosenSkill) + 1);
        skillLvlUpBtn.setVisible(false);
        updateDisplay();
    }

    //called when a button specific to display enemy scores powerup is clicked
    //displays scores of the chosen enemy in updates listview
    @FXML
    private void enemyInfo(){
        int chosenEnemy = opponentsList.getSelectionModel().getSelectedIndex();
        String enemyScores = "";
        enemyScores += "Opponent " + opponents.get(chosenEnemy).displayID() + "...";

        for(int i = 0; i < skills.size(); i++){
            enemyScores += skills.get(i) + ": " + opponents.get(chosenEnemy).getSkillVal(i) + ", ";
        }
        updatesList.getItems().add(0, enemyScores.substring(0, enemyScores.length() - 2));

        enemyInfoBtn.setVisible(false);
        updateDisplay();
    }

    //called when the "optimum battle" powerup is bought
    //determines the most winnable level based on the opponent's scores and enemy scores
    private void determineBestLevel(){
        int[] winDifference = new int[opponents.size()];

        //iterates through every enemy
        for (int enemy = 0; enemy < opponents.size(); enemy++){
            int skillDifference = 0;
            int skillsWon = 0;
            //iterates through all 5 skills for each enemy
            for (int skill = 0; skill < skills.size(); skill++){
                //sees if the the user's skill level is stronger than opponents, and stores that data in Skill Difference
                if(skillVals.get(skill) > opponents.get(enemy).getSkillVal(skill) && opponents.get(enemy).getLives() > 0){
                    skillsWon++;
                    skillDifference += skillVals.get(skill) - opponents.get(enemy).getSkillVal(skill);
                }
            }
            if(skillsWon >= 3){
                winDifference[enemy] = skillDifference;
            }
            else{
                winDifference[enemy] = 0;
            }
        }
        //identifies best opponent based on highest winDifference
        int bestOpponent = 0;
        for(int i = 1; i < winDifference.length; i++){
            if(winDifference[i] > winDifference[bestOpponent]){
                bestOpponent = i;
            }
        }
        //displays in updatesList
        if(winDifference[bestOpponent] > 0){
            updatesList.getItems().add(0, "Your best chances are against enemy " + opponents.get(bestOpponent).displayID() + ". But no guarantees :)");
        }
        else{
            updatesList.getItems().add(0, "All your skill scores suck. You are predicted to lost against everyone. Tough luck.");
        }
    }

    //Checks if user has won the game, or if they lost, and is called after every battle
    //if user has neither won or lost, game keeps going
    private void checkIfWon(){
        int skillsSum = 0;
        for(int i = 0; i < skillVals.size(); i++){
            skillsSum += skillVals.get(i);
        }
        //one of the two preconditions has to be met in order to win: 25+ battles won, or all skills at a 10
        if(battlesWon > 25 || skillsSum == 50){
            updatesList.getItems().add(0, "You won! Great job :)");
            battleBtn.setDisable(true);
            powerUpBtn.setDisable(true);
        }
        else if(livesLeft == 0){
            updatesList.getItems().add(0, "You lost all your lives! Game over :(");
            battleBtn.setDisable(true);
            powerUpBtn.setDisable(true);
        }
    }

    //Called after every battle, checks to see if the user has undergone any "damage", which happens randomly, but the chance of damage increases with higher scores
    private void checkDamage(){
        int skillSums = 0;
        for(int i = 0; i < skillVals.size(); i++){
            skillSums += skillVals.get(i);
        }
        int randNum = (int) (Math.random() * 50);

        Random rand = new Random();
        boolean newBoolean = rand.nextBoolean();

        //higher the score, the higher the chance of suffering from a casualty
        if(skillSums > randNum && newBoolean){
            int randCasualty = (int) (Math.random() * 4);
            Damage d = new Damage(randCasualty);
            String updatedScores = "You have suffered from a " + d.returnDamageType() + "! ";

            //goes through every case of damage, sees which one is most relevant, and changes scores accordingly
            switch(d.returnDamageID()){
                case 0:
                    updatedScores += "Speed dropped from " + skillVals.get(0) + " to ";
                    skillVals.set(0, skillVals.get(0) - (int)((Math.random()* (4 - 1)) + 1));
                    if(skillVals.get(0) < 0){
                        skillVals.set(0, 0);
                    }
                    updatedScores += skillVals.get(0) + ".";

                    break;
                case 1:
                    updatedScores += "Strength dropped from " + skillVals.get(1) + " to ";
                    skillVals.set(1, skillVals.get(1) - (int)((Math.random()* (4 - 1)) + 1));
                    if(skillVals.get(1) < 0){
                        skillVals.set(1, 0);
                    }
                    updatedScores += skillVals.get(1) + ".";

                    break;
                case 2:
                    updatedScores += "Agility dropped from " + skillVals.get(3) + " to ";
                    skillVals.set(3, skillVals.get(3) - (int)((Math.random()* (4 - 1)) + 1));
                    if(skillVals.get(3) < 0){
                        skillVals.set(3, 0);
                    }
                    updatedScores += skillVals.get(3) + ".";

                    break;
                case 3:
                    for(int i = 0; i < skillVals.size(); i++){
                        if(skillVals.get(i) > 0){
                            skillVals.set(i, skillVals.get(i) - 1);
                        }
                    }
                    updatedScores += "All scores dropped by 1.";
                    break;
            }
            updatesList.getItems().add(0, updatedScores);
            updateDisplay();
        }
    }

    //resets all variables, allows the user to start the game over
    @FXML
    private void resetButton(){
        updatesList.getItems().clear();
        scoresList.getItems().clear();
        recordList.getItems().clear();
        powerupsList.getItems().clear();
        opponentsList.getItems().clear();

        start.setDisable(false);

        userHeader.setText("");
        opponentsHeader.setText("");
        statsHeader.setText("");
        powerupsHeader.setText("");
        usernameTxt.setDisable(false);

        opponents.clear();
        powerUps.clear();
        skills.clear();
        skillVals.clear();

        battleBtn.setDisable(true);
        powerUpBtn.setDisable(true);

        livesLeft = 3;
        battlesWon = 0;
        battlesLost = 0;
        winStreak = 0;
        bank = 200;

        updateDisplay();

    }
}
