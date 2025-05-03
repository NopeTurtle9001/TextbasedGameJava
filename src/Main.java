import static java.lang.System.*;
import java.io.*;
import java.util.*;
import java.util.Arrays;
import java.util.Scanner;
// An interface to be implemented by everyone interested in OnEnemyDeath events and for handling enemy death events (used to grant XP on defeat)
interface DeathListener {
    void onDeath(int XP);
}
public class Main {
    static Player player;
    static Dungeon dungeonInstance;

    // Input handling
    static Scanner gameScanner1 = new Scanner(System.in);
    static Scanner gameScanner2 = null; // Ensure it's initialized properly

    // Game state
    static boolean gamestate = false;
    static String hasPlayedBefore = "";
    static String savedGameName = "";
    static File playerDataFile = new File("playerData.txt");
    static FileWriter gameWriter2;
    static PrintWriter gameWriter;

    // Player attributes
    static String playerName;
    static String playerClass;
    static int lvl, exp, damage, defense, maxHealth, hp;

    // Start the game
    public static void startGame() throws IOException {
        if (!gamestate) {
            out.println("Welcome Player!\nHave you played before?\nYes/No");
            hasPlayedBefore = gameScanner1.next();

            if (hasPlayedBefore.equalsIgnoreCase("Yes")) {
                out.println("Enter the name of your saved game file:");
                savedGameName = gameScanner1.next();
                File file = new File(savedGameName);

                if (!file.exists()) {
                    out.println("No save file found. Starting a new game.");
                    startNewGame();
                } else {
                    out.println("Save file found! Loading game...");
                    FileReader freader = new FileReader(savedGameName);
                    gameScanner2 = new Scanner(freader);
                    gamestate = true;
                    Main.beginGame(hasPlayedBefore);
                }
            } else {
                startNewGame();
            }
        } else {
            out.println("Game is already running!");
        }
    }

    // Helper method to start a new game
    private static void startNewGame() throws IOException {
        playerDataFile.createNewFile();
        out.println("New game file created: " + playerDataFile.getName() + "\nStarting fresh...");
        gamestate = true;
        beginGame("No");
    }

    // End the game safely
    public static void endGame(Player playerInstance) {
        if (gamestate) {
            out.println("Saving current player data...");

            try (FileWriter saveWriter = new FileWriter("playerData.txt")) {
                saveWriter.write(playerInstance.toString());
                out.println("Player data successfully saved.");
            } catch (IOException e) {
                out.println("Error saving player data!");
                e.printStackTrace();
            }

            // Closing resources
            if (gameScanner1 != null) gameScanner1.close();
            if (gameScanner2 != null) gameScanner2.close();

            out.println("Thanks for playing! Come back next time.");
            gamestate = false;
            System.exit(0);
        } else {
            out.println("Game is already closed. Start it first before ending.");
        }
    }

    public static void beginGame(String hasPlayedBefore) {
        if (hasPlayedBefore.equalsIgnoreCase("No")) {
            try {
                gameWriter = new PrintWriter(new FileWriter("playerData.txt"));
                out.println("Choose your name:");
                playerName = gameScanner1.next();
                gameWriter.println(playerName);

                String[] classes = {"Warrior", "Knight", "Berserker", "Ranger", "Magic_Swordsman", "Swarm_Lord", "Warframe"};
                boolean validClass = false;

                while (!validClass) {
                    out.println("Choose your class:\n" + Arrays.toString(classes));
                    playerClass = gameScanner1.next();

                    if (Arrays.asList(classes).contains(playerClass)) {
                        validClass = true;
                        gameWriter.println(playerClass);
                    } else {
                        out.println("Invalid class selection. Please choose from the list.");
                    }
                }

                gamestate = true;
                tuTorial(playerName);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (hasPlayedBefore.equalsIgnoreCase("Yes")) {
            if (gameScanner2 != null) {
                playerName = gameScanner2.next();
                playerClass = gameScanner2.next();
                lvl = gameScanner2.nextInt();
                exp = gameScanner2.nextInt();
                damage = gameScanner2.nextInt();
                defense = gameScanner2.nextInt();
                maxHealth = gameScanner2.nextInt();
                hp = gameScanner2.nextInt();
            } else {
                out.println("Error: No saved game data found.");
                return;
            }

            try {
                gameWriter = new PrintWriter(new FileWriter("playerData.txt"));
            } catch (IOException e) {
                e.printStackTrace();
            }

            gamestate = true;
            player = new Player(playerName, playerClass);
            dungeonInstance = new Dungeon();
            command(player, dungeonInstance);
        }
    }

    public static void tuTorial(String playerName) {
        int choice;
        String[] options = {"Who are you?", "Where am I?"};

        out.println("""
                You are asleep on a boat that is under attack by unknown beings.
                They appear human but possess power beyond human capabilities.
                As you listen in on the conversation between the crew and these 'humans' while you sleep, 
                someone evacuates you from the boat. These are the events that take place:
                """);

        out.println("""
                    Someone (faintly): Wake up! The ship... it's sinking! 
                    (louder) Wake up! Dammit, the ????????? are here! We have no choice but to run!
                
                    Boss ????? (angrily): Where is he!? I won't stop until I've crushed his skull and melted his body!
                    Subordinate ?????: Boss, it seems they have escaped with the host.
                    Boss ?????: Grrr... dammit! We'll have to search the mainland. 
                    Prepare a chariot and a boat—we need to inform that guy that he escaped. 
                    Let's hope he isn't angry.
                
                    #?%?#: The time for rest is over.
                    My successor, make use of the strength I have given you, and show those bastards 
                    that even after my death, they won’t be able to rest.
                    You are too weak to make use of my gift now, so I shall grant you a helper 
                    to protect and guide you until you are strong enough to wield it yourself.
                    Now wake from your slumber and carry out my will.
                """);

        out.println("""
                    You awake on land after drifting along the sea for an uncountable amount of time.
                    You are wrapped in bandages, and your body burns as if it's on fire.
                    Nearby, an old man watches over you. What do you say to him?
                """);

        out.println("(1) " + options[0] + "\n(2) " + options[1]);

        do {
            choice = gameScanner1.nextInt();
            if (choice != 1 && choice != 2) {
                out.println("Invalid choice. Please select (1) or (2).");
            }
        } while (choice != 1 && choice != 2);

        out.println(playerName + ": " + options[choice - 1]);

        if (choice == 1) {
            out.println("""
                    Old Man: Young one, you mustn't move yet—you'll open your wounds.
                    """);
        }

        out.println("""
                    System: This is your first time moving, player!
                    System: To move, type 'go <name of place>'.
                    System: You can view available locations by typing 'trvLoc'.
                    System: Right now, the only place you can go is (DICATHEN).
                    System: Try using this command now.
                """);

        player = new Player(playerName, playerClass);
        dungeonInstance = new Dungeon();
        command(player, dungeonInstance);
    }

    public static void command(Player playerInstance, Dungeon dungeonInstance) {
        while (gamestate) {
            String command = gameScanner1.nextLine().trim();
            String[] arrCommand = command.split(" ");

            if (arrCommand.length == 0) continue; // Skip empty input

            switch (arrCommand[0].toLowerCase()) {
                case "go":
                    if (arrCommand.length < 2 || arrCommand[1].trim().isEmpty()) {
                        System.out.println("Please enter a valid location.");
                    } else {
                        World.changeLoc(playerInstance, arrCommand[1]);
                    }
                    break;

                case "trvLoc":
                    World.showTravelLoc(playerInstance);
                    break;

                case "enterdungeon":
                    dungeonInstance.enterDungeon(playerInstance.isInDungeon, playerInstance);
                    break;

                case "endgame":
                    try (FileWriter saveWriter = new FileWriter("playerData.txt")) {
                        saveWriter.write(playerInstance.toString());
                        System.out.println("Player data written. Exiting.");
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.exit(0);
                    break;

                default:
                    System.out.println("Unknown command. Please try again.");
                    break;
            }
        }
    }

    static class Enemies {
        private int hp = 100;
        private int damage = 20;
        public int def = 200;
        public int elvl;
        private String enemyName;
        private final List<DeathListener> listeners = new ArrayList<>();

        public void addListener(DeathListener toAdd) {
            listeners.add(toAdd);
        }

        public Enemies(int lvl, String enemyName) {
            this.enemyName = enemyName;
            this.elvl = lvl;
            this.hp *= 1 + (0.02 * Math.pow((elvl - 1), 1.5)); // Improved HP scaling
        }

        public boolean isAlive() {
            return hp > 0;
        }

        public String getEnemyName() {
            return this.enemyName;
        }

        public int takeDamage(int damageDone) {
            hp -= damageDone;
            return Math.max(hp, 0); // Prevents negative HP values
        }

        public int attack() {
            int scalingFactor = (int) (1 + 0.01 * Math.pow((elvl - 1), 1.3)); // Smoother scaling
            return damage * scalingFactor;
        }

        public void handleDeath() {
            out.println(enemyName + " has died.");
            int droppedXP = (int) (10 * (1 + 0.15 * Math.pow(elvl, 0.4))); // Balanced XP reward

            for (DeathListener dl : listeners) {
                dl.onDeath(droppedXP);
            }
        }
    }

    static class Player implements DeathListener {
        private int phealth;
        private int pdefense = 200;
        private int pdamage = 50;
        private int xp = 0;
        private int lvl = 1;
        private int pmaxHealth = 100;
        private String pname;
        private String pclass;
        boolean isInDungeon = false;
        String worldLocation = "The Old Man's House";

        // Constructors
        public Player(String name, String clss) {
            this.pname = name;
            this.pclass = clss;
            this.phealth = this.pmaxHealth;
        }

        public Player(String name, String clss, int plvl, int def, int damage, int pmaxHealth, int xp, int hp) {
            this.pname = name;
            this.pclass = clss;
            this.pdefense = def;
            this.pdamage = damage;
            this.lvl = plvl;
            this.xp = xp;
            this.pmaxHealth = pmaxHealth;
            this.phealth = hp;
        }

        //World Methods
        public String currentLoc() {
            return worldLocation;
        }

        // Combat methods
        public void attack(int baseDamage, int trueDamage, Enemies enemy) {
            int netDamage = Math.max(baseDamage - enemy.def, 0) + trueDamage;
            out.println("You have attacked " + enemy.getEnemyName() + " for " + netDamage);
            out.println("Enemy Health: " + enemy.takeDamage(netDamage));
        }

        public void takeDamage(Enemies enemy) {
            double damageReduction = pdefense / (double) (pdefense + 300);
            int damageTaken = (int) (enemy.attack() * (1 - damageReduction));
            phealth -= damageTaken;
            out.println("You defended and took: " + damageTaken + " damage!\nYou have: " + phealth + " HP left.");
        }

        @Override
        public void onDeath(int XP) {
            out.println("Enemy has died. \nYou have gained: " + XP + " XP!");
            this.xp += XP;
        }

        public void updateLoc(String loc) {
            worldLocation = loc;
        }
    }

    static class Dungeon {
        public boolean indungeon = false;

        public void enterDungeon(boolean isInDungeon, Player p) {
            if (indungeon) {
                out.println("You can't enter a dungeon while inside another one!");
            } else {
                out.println("""
                            You have entered the Dungeon.
                            To progress, move and descend through levels.
                            Remember, you can leave at any time!
                        """);
                indungeon = true;
                DungeonFloors dungeonFloors = new DungeonFloors();
                dungeonFloors.spawnEnemy(p);
            }
        }

        public void leaveDungeon() {
            if (indungeon) {
                out.println("Leaving Dungeon!");
                indungeon = false;
            } else {
                out.println("You can't leave a dungeon when you aren't in one.");
            }
        }

        class DungeonFloors extends Dungeon {
            private boolean bossDead;
            private int dungeonLevel = 1;
            private final int levelRange = 1 + (int) (Math.random() * 5);
            private final String[] EnemyNames = {"Skeleton", "Zombie", "Lich", "Orc Knight", "Undead Specter", "Orc Magician"};

            private final int enemyIndex = (int) (Math.random() * EnemyNames.length);

            public void spawnEnemy(Player p) {
                int enemyLevel = dungeonLevel * levelRange;
                Enemies enemy = new Enemies(enemyLevel, EnemyNames[enemyIndex]);
                startFight(enemy, p);
            }

            public void startFight(Enemies enemy, Player player) {
                Scanner optionScanner = new Scanner(System.in);
                int choice;

                out.println("You have encountered a " + enemy.getEnemyName() + "!");

                do {
                    out.println("""
                                Choose an action:
                                (1) Attack
                                (2) Defend
                            """);

                    while (!optionScanner.hasNextInt()) {
                        out.println("Invalid choice. Please enter (1) or (2).");
                        optionScanner.next();
                    }
                    choice = optionScanner.nextInt();
                } while (choice != 1 && choice != 2);

                if (choice == 1) {
                    player.attack(50, 0, enemy);
                } else {
                    player.takeDamage(enemy);
                }

                if (!enemy.isAlive()) {
                    enemy.handleDeath();
                }
            }

            public void spawnBoss() {
                int bossLevel = dungeonLevel * 3 * levelRange;
                Enemies boss = new Enemies(bossLevel, "Dungeon Boss");
                out.println("A powerful Dungeon Boss has appeared!");
                startFight(boss, player);
            }
        }
    }

    class World {
        private static final String[] locations = {
                "Dicathen", "Einrehn", "Glaymore_Outskirts", "Glaymore_InnerCity",
                "The_City_With_No_Wind", "City_of_Dragons", "Holy_City_Manthel",
                "Chrislthe", "The_Old_Man's_House", "City_of_??????"
        };

        private static final Map<String, List<String>> travelRoutes = new HashMap<>();

        static {
            travelRoutes.put("The Old Man's House", List.of("Dicathen"));
            travelRoutes.put("Dicathen", List.of("Einrehn", "Glaymore_Outskirts", "The Old Man's House"));
            travelRoutes.put("Einrehn", List.of("Holy_City_Manthel", "Chrislthe", "Dicathen"));
            travelRoutes.put("Glaymore_Outskirts", List.of("Glaymore_InnerCity", "Dicathen"));
            travelRoutes.put("Glaymore_InnerCity", List.of("Glaymore_Outskirts"));
            travelRoutes.put("Chrislthe", List.of("Einrehn", "The City With No Wind"));
            travelRoutes.put("The City With No Wind", List.of("City_of_Dragons", "Chrislthe"));
            travelRoutes.put("City_of_Dragons", List.of("The City With No Wind", "City_of_??????"));
        }

        /**
         * Change player's location if destination is valid.
         */
        public static void changeLoc(Player player, String location) {
            if (!Arrays.asList(locations).contains(location)) {
                out.println("Invalid location. Please select a valid destination.");
                return;
            }

            if (travelRoutes.getOrDefault(player.currentLoc(), Collections.emptyList()).contains(location)) {
                out.println("You have traveled to: " + location);
                player.updateLoc(location);
            } else {
                out.println("You cannot travel to " + location + " from your current location.");
            }
        }

        /**
         * Show valid locations based on the player's current position.
         */
        public static void showTravelLoc(Player player) {
            List<String> availableLocations = travelRoutes.getOrDefault(player.currentLoc(), Collections.emptyList());
            if (!availableLocations.isEmpty()) {
                out.println("Available travel locations:\n" + String.join("\n", availableLocations));
            } else {
                out.println("There are no accessible travel locations from here.");
            }
        }
    }
}