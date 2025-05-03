import static java.lang.System.*;

import java.io.*;
import java.util.*;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.FileReader;
import java.util.Arrays;
import java.util.EventObject;
import java.util.Scanner;
// An interface to be implemented by everyone interested in OnEnemyDeath events
interface DeathListener {
    void onDeath(int XP);
}
public class GameMain {
    static Player player;
    static Dungeon dung;
    // Scanners
    static Scanner gameScanner1 = new Scanner(System.in);
    static Scanner gameScanner2;
    // vars
    static boolean gamestate = false;
    static String alrdyPlayed = "";
    static String savedGameName = "";
    static File gameFile1 = new File("playerData.txt");
    static FileWriter gameWriter2;
    static PrintWriter gameWriter;
    static String playerName;
    static String playerClass;
    static int lvl;
    static int exp;
    static int damage;
    static int defense;
    static int maxHealth;
    static int hp;
    // Start the game
    public static void startGame(boolean gamestate) throws IOException {
        if (!gamestate) {
            out.println("Welcome Player!\nHave you played before?\nYes/No");
            alrdyPlayed = gameScanner1.next();
            if (alrdyPlayed.equalsIgnoreCase("Yes")) {
                out.println(
                        "What is the name of the file which the game was saved to?");
                savedGameName = gameScanner1.next();
                out.println("Reading path of the file which the game was saved to.");
                try {
                    File file = new File(savedGameName);
                    if (!file.exists()) {
                        throw new IOException(
                                "No File Found\nPlease create the file or answer no next time.");
                    } else if (file.exists()) {
                        out.println("File Found!\nStarting the game!");
                        gamestate = true;
                        FileReader freader = new FileReader(savedGameName);
                        gameScanner2 = new Scanner(freader);
                        beginGame(alrdyPlayed);
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else if (alrdyPlayed.equalsIgnoreCase("No")) {
                gameFile1.createNewFile();
                out.println(
                        "File Created!: " + gameFile1.getName() + "\nStarting the game!");
                gamestate = true;
                beginGame(alrdyPlayed);
            }
        } else {
            out.println("Game is already running!");
        }
    }

    // End the game
    public static void endGame(boolean gamestate, Player ob) {
        // ALL of this is to end the game
        if (gamestate) {
            out.println("Saving current playerData!");
            gameWriter.print(ob.toString());
            out.println("Thanks for playing come back next time!");
            gameScanner1.close();
            gameScanner2.close();
            gameWriter.close();
            gamestate = false;
            System.exit(0);
        } else if (!gamestate) {
            out.println("Can't end the game since it's closed!\nStart the game to end it!");
        }
    }

    public static void beginGame(String playedBefore) {
        if (playedBefore.equalsIgnoreCase("No")) {
            try {
                gameWriter = new PrintWriter(new FileWriter("playerData.txt"));
                out.println("Choose your name");
                playerName = gameScanner1.next();
                gameWriter.println(playerName);
                String classes[] = {
                        "Warrior",
                        "Knight",
                        "Berserker",
                        "Ranger",
                        "Magic_Swordsman",
                        "Swarm_Lord",
                        "Warframe",
                };
                out.println("Choose your class\n" + Arrays.toString(classes));
                playerClass = gameScanner1.next();
                for (int i = 0; i < classes.length; i++) {
                    if (playerClass.equalsIgnoreCase(classes[i])) {
                        playerClass = classes[i];
                        break;
                    }
                }
                gameWriter.println(playerClass);
                gamestate = true;
                tuTorial(playerName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (playedBefore.equalsIgnoreCase("Yes")) {
            try {
                
            } catch (Exception e) {
            }
            playerName = gameScanner2.next();
            playerClass = gameScanner2.next();
             lvl = gameScanner2.nextInt();
            exp = gameScanner2.nextInt();
            damage = gameScanner2.nextInt();
            defense = gameScanner2.nextInt();
            maxHealth = gameScanner2.nextInt();
            hp = gameScanner2.nextInt();
            try {
                gameWriter = new PrintWriter(new FileWriter("playerData.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }
            gamestate = true;
            player = new Player(playerName, playerClass);
            dung = new Dungeon();
            command(player, dung);
        }
    }

    public static void tuTorial(String nme) {
        int op;
        String options[] = { "Who are you?", "Where am I?" };
        out.println(
                "You are asleep on a boat that's being attacked by unknown beings.\nThese being seem to look like humans, but their power far exceeds human capabilities.\nAs you listen in on a conversation between the crew and these 'humans' whilst you sleep someone evacuates you out of the boat.\nThese are the events and convos that take place.");
        out.println(
                "Someone: *faintly* Wake up! The ship it's sinking!\n*louder* Wake up! Dammit the ????????? are here we have no choice but to run!\nBoss ????: *angrily* Where is he! I won't stop till I've crushed his skull and melted his body!\nSubordiante ????: Boss it seems they have escaped with the host.\nBoss ????: Grrr Dammit!! Guess we'll have to search on the mainland.\nPrepare a chariot and a boat we'll have to tell that guy that he escaped.\nLet's hope he isn't angry.\n#?%?#: The time for rest is over.\nMy successor, make use of the strength I have given you, and show those bastards that even after my death they won't be able to rest.\nYou are too weak to make use of my gift now, so I shall give you a helper to protect you and guide you until you are strong enough to use it yourself.\nNow wake from your slumber, and carry out my will.");
        out.println();
        out.println(
                "You awake on land after drifting along the sea for an uncountabe amount of time.\nYou seem to be covered in bandages, and you feel your body is scorching hot like fire.\nYou see an old man near you.\nWhat do you say to him?\n(1 or 2)\n"
                        +
                        Arrays.toString(options) +
                        "\n");
        op = gameScanner1.nextInt();
        if (op == 1) {
            out.println(nme + ": " + options[0]);
            out.println(
                    "Old Man: Young one you mustn't move now. You'll open your wounds.");
            out.println(nme + ": " + "Did you save me?");
            out.println(
                    "Old Man: I found you on the shore. You seemed pretty banged up and malnutritioned.");
            out.println(nme + ": " + "Where is this place?");
            out.println(
                    "Old Man: You are in Glaymore, ruled by the royal family Glayden.\nI live in the outskirts so not much happens around here.\nAnyways why were you on the shore? Do you remember your name?");
            out.println(
                    nme +
                            ": " +
                            "My name is " +
                            nme +
                            ". I don't remember much, but I was on a boat we were going to an excavation site by the lord of my household.\nAt some point I fell asleep and that's pretty much al I remember.");
            out.println(
                    "Old Man: So you were on a boat huh? Well you probably won't be able to go to that excavation site for quite a while.\n I suggest you go to the nearest town (DICATHEN) for more information, and to make yourself a livin.");
        } else {
            out.println(nme + ": " + options[1]);
            out.println(
                    "Old Man: You are in Glaymore, ruled by the royal family Glayden.\nI live in the outskirts so not much happens around here.\nAnyways why were you on the shore? Do you remember your name?");
            out.println(
                    nme +
                            ": " +
                            "My name is " +
                            nme +
                            ". I don't remember much, but I was on a boat we were going to an excavation site by the lord of my household.\nAt some point I fell asleep and that's pretty much al I remember.");
            out.println(
                    "Old Man: So you were on a boat huh? Well you probably won't be able to go to that excavation site for quite a while.\n I suggest you go to the nearest town (DICATHEN) for more information, and to make yourself a livin.");
        }
        out.println("System: This is your first time moving player!");
        out.println("System: To go to places type go *name of place here*!");
        out.println("System: The game will display places that YOU the player can go to when you type trvLoc");
        out.println("System: In this case the only place you can go to right now is (DICATHEN).");
        out.println("System: Try using this command right now.");
        player = new Player(playerName,playerClass);
        dung = new Dungeon();
        command(player, dung);
    }
    public static void command(Player l,Dungeon o) {
        while(gamestate !=false) {
            String command = gameScanner1.nextLine();
            String arrCommand[] = command.split(" ");
                if (arrCommand[0].equalsIgnoreCase("go")) {
                    if (arrCommand[1] == " " || arrCommand[1] == null) {
                        System.out.println("Please enter a valid location.");
                    }
                    World.changeLoc(l, arrCommand[1]);  
                }
                if (arrCommand[0].equalsIgnoreCase("trvLoc")) {
                    World.showTravelLoc(l);
                }
                if (arrCommand[0] == "EnterDungeuon") {
                    o.enterDungeon(l.isInDungeon,l);
                    break;
                }
                if (arrCommand[0].equalsIgnoreCase("endGame")) {
                    try {
                        gameWriter2 = new FileWriter("playerData.txt");
                        gameWriter2.write(l.toString());
                        System.out.println("Player data written exiting.");
                        gameWriter2.close();
                        System.exit(0);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }     

    // MAIN
    public static void main(String[] args) throws IOException {
        GameMain.startGame(false);
       
    }
}

////////////////////////////////////////////////////////////////////////////////////////////////////
class Enemies {

    private int hp = 100;
    private int damage = 20;
    public int def = 200;
    public int elvl;
    private String enemyName;
    private List<DeathListener> listeners = new ArrayList<DeathListener>();

    public void addListener(DeathListener toAdd) {
        listeners.add(toAdd);
    }

    public Enemies(
            int lvl,
            String enemyName) {
        this.enemyName = enemyName;
        this.elvl = lvl;
    }

    public boolean isAlive() {
        return hp > 0;
    }

    public String eName() {
        return this.enemyName;
    }

    public int takeDamage(int damageDone) {
        if (elvl < 80) {
            hp = (int) (hp * (1 + 0.015 * Math.pow((elvl - 1), 2.0)));
        }
        if (elvl >= 80) {
            hp = (int) (hp * (1 + ((24 * Math.sqrt(5.0))/5) * Math.pow((elvl - 1), 0.5)));
        }
        hp -= damageDone;
        return hp;
    }
    public int attack() {
        
        int scaling = (int) (1 + 0.015 * Math.pow((elvl - 1), 1.55)) ;
        int damageDone = damage * scaling;
        return damageDone;
    }
    public void EnemyDeath() {    
        out.println(enemyName + " has died.");
        int dropedXP =(int) (10 * (1 + 0.1425 * Math.pow(elvl,0.5)));
        for (DeathListener dl : listeners)
            dl.onDeath(dropedXP);

    }
}

class Player implements DeathListener {

    // vars
    private int phealth;
    private int pdefense = 200;
    private int pdamage = 50;
    private int xp;
    private int lvl;
    private int pmaxHealth = 100;
    private String pname = "NULL";
    private String title = "NULL";
    private String pclass = "Classless";
    boolean isInDungeon = false;
    String worldLocation = "The Old Man's House";

    // Constructors
    public Player(String name, String clss) {
        int phealth = pmaxHealth;
        int def = pdefense;
        int str = pdamage;
        pname = name;
        pclass = clss;
    }

    public Player(
            String name,
            String clss,
            int plvl,
            int def,
            int damage,
            int pmaxHealth,
            int xp,
            int hp) {
        this.pname = name;
        this.pclass = clss;
        this.pdefense = def;
        this.xp = xp;
        this.pdamage = damage;
        this.lvl = plvl;
        this.phealth = hp;
        this.pmaxHealth = pmaxHealth;
    }
    public void updateLoc(String loc) {
        worldLocation = loc;
    }
    // Methods
    public String currentLoc() {
        return worldLocation;
    }
    // just a helpful boolean method to check if the player is alive
    public boolean isAlive() {
        return phealth > 0;
    }

    public void attack(
            int pD,
            int pTrueDamage,
            Enemies e) {
        // Deal Damage to enemy based on current attack level weapon damage and enemy
        // defense!
        int pBaseDamage = pD;
        int eDamageReduction = 0;
        // Enemy defense health and shield also scale with level! yes
            if (e.elvl < 80) {
                eDamageReduction = (int) (e.def * 1 + 0.005 * Math.pow((e.elvl - 1), 1.75));
            }
            if (e.elvl >= 80) {
                eDamageReduction = (int) (e.def * 1 + 0.4 * Math.pow((e.elvl - 1), .75));
            }
        /*
         * | This makes a damage reduction curve
         * | Allows players and enemies to not become immortal via armor
         * | Also makes players have different layouts for different enemies
         */
        int pTotalDamage = pBaseDamage * eDamageReduction + pTrueDamage; // True Damage ignores shield and armor it's
        // guaranteed damage
        // Apply this to the enemy and print
        out.println("You have attacked " + e.eName() + " for " + pTotalDamage);
        out.println("Enemy Health: " + e.takeDamage(pTotalDamage));
    }

    public void takeDamage(Enemies e) {
        int damageReduction = pdefense/(pdefense + 300);
        
        int damageTaken = e.attack() * damageReduction;
        phealth -= damageTaken;
        out.println("You defended and took: " + damageTaken + "!\nYou have: " + phealth +" left!");
    }
    
    // Movement Methods
    public void pMove(String movement) {
        // Only move after creating the board
        //will add when i change this to an applet
    }

    public String toString() {
        return (pname +
                "\n" +
                pclass +
                "\n" +
                String.valueOf(lvl) +
                "\n" +
                String.valueOf(xp) +
                "\n" +
                String.valueOf(pdamage) +
                "\n" +
                String.valueOf(pdefense) +
                "\n" +
                String.valueOf(pmaxHealth) +
                "\n" +
                String.valueOf(phealth));
    }

    @Override
    public void onDeath(int XP) {
    
        System.out.println("Enemy has  died. \nYou have gained: " + XP + " xp!");
        this.xp += XP;
    }
}

class Npcs {
    // Constructors
}

class Dungeon {
public boolean indungeon = false;
    public void enterDungeon(boolean inDungeon, Player p) {
        if (inDungeon) {
            out.println("You can't enter a dungeon while inside a dungeon!");
        } else {
            out.println(
                    "You have entered the Dungeon.\nTo progress in the Dungeon you have to mave and descend through the levels.\nRemember you can leave at any time while in here!");
            indungeon = true;
            DungeonFloors d = new DungeonFloors();
            d.spawnEnemy(p);
            leaveDungeon(indungeon);

        }
    }
    public void leaveDungeon(boolean inDungeon) {
        if (inDungeon) {
            out.println("Leaving Dungeon!");
            indungeon = false;
        } else {
          out.println("You can't leave a dungeon when you aren't in one.");  
        }
    }
    class DungeonFloors extends Dungeon {
        public boolean bossDead;
    public int dungeonLevel = 1;
        int levelRange = 1 + (int)(Math.random() * ((5 - 1) + 1));
        int wtg = 0 + (int)(Math.random() * ((5-0) +1));
        public String EnemyNames[] = {"Skeleton","Zombie","Lich","Orc Knight", "Undead Specter", "Orc Magician", "ADD more soon"};
        public void spawnEnemy(Player p) {
            int l = dungeonLevel * levelRange;
            Enemies e1 = new Enemies(l,EnemyNames[wtg]);
            startFight(e1,p);
            
        }
        public void startFight(Enemies e, Player p) {
            Scanner optionScanner = new Scanner(System.in);
            int chOp = 0;
            out.println("You have encountered an enemy.");
            while (p.isAlive() && e.isAlive()) {
                out.println("What do you do?\n Attack Defend\n(1 or 2)");
                chOp = optionScanner.nextInt();
                if (chOp == 1) {
                    p.attack(50, 0, e);
                }
                else if (chOp == 2) {
                    p.takeDamage(e);
                }
            }
            if (!e.isAlive()) {
                e.EnemyDeath();
            }
        }

        public void spawnBoss(int dLevel) {
            int l = dLevel * levelRange;
            Enemies b1 = new Enemies((3 * l), "Floor Boss");

        }
    }
}

class World {
    static String locations[] = {"Dicathen", "Einrehn","Glaymore_Outskirts", "Glaymore_InnerCity","The_City_With_No_Wind","City_of_Dragons","Holy_City_Manthel","Chrislthe","The_Old_Man's_House","City_of_??????"};
    /**
     * This method changes the current location of the player to the new specified location
     * @param ob The Player object that will be travelling to the location specified
     * @param location the location the player will travel to this is case insensitive
     */
    public static void changeLoc(Player ob,String location) {
        if ((ob.currentLoc() == "The Old Man's House") && (location.equalsIgnoreCase(locations[0]))) {
            out.println("You have travelled to: " + location);
            ob.worldLocation = locations[0];
        }
        else if ((ob.currentLoc() == "Dicathen") && (location.equalsIgnoreCase(locations[1]))) {
            out.println("You have travelled to: " + location);
            ob.worldLocation = locations[1];
        }
        else if ((ob.currentLoc() == "Dicathen") && (location.equalsIgnoreCase(locations[2]))) {
            out.println("You have travelled to: " + location);
            ob.worldLocation = locations[2];
        }
        else if ((ob.currentLoc() == "Dicathen") && (location.equalsIgnoreCase(locations[8]))) {
            out.println("You have travelled to: " + location);
            ob.worldLocation = locations[8];
        }
    }
    /**
     * This method shows the locations the player can travel to based on it's currrent location.
     * @param ob the player object sent to this method to get the player's current location
     */
    public static void showTravelLoc(Player ob) {
        //Send a part of locations based on current location
        if (ob.currentLoc().equalsIgnoreCase("The Old Man's House")) {
            out.println(locations[0]);
        }
        else if (ob.currentLoc().equalsIgnoreCase("Dicathen")) {
            out.println(locations[1] + "\n" + locations[2] + "\n" + locations[8]);
        }
        else if (ob.currentLoc().equalsIgnoreCase("Einrehn")) {
            out.println(locations[6] + "\n" + locations[7] + "\n" + locations[0]);
        }
        else if (ob.currentLoc().equalsIgnoreCase("Glaymore Outskirts")) {
            out.println(locations[3] + "\n" + locations[0]);
        }
        else if (ob.currentLoc().equalsIgnoreCase("Glaymore InnerCity")) {
            out.println(locations[2]);
        }
        else if (ob.currentLoc().equalsIgnoreCase("Chrislthe")) {
            out.println(locations[1] + "\n" + locations[4]);
        }
        else if (ob.currentLoc().equalsIgnoreCase("The City With No Wind")) {
            out.println(locations[5] + "\n" + locations[7]);
        }
        else if (ob.currentLoc().equalsIgnoreCase("City of Dragons")) {
            out.println(locations[4] + "\n" + locations[9]);
        }
    }
}

class Shop {
}
