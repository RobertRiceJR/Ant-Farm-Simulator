import java.util.*;

public class GridCell {
    private Map<Integer, Ant> ants;
    private int foodAmount;
    private int pheromoneLevel;
    private boolean explored;
    private boolean gameOver;

    public GridCell() {
        this.ants = new HashMap<>();
        this.foodAmount = 0;
        this.pheromoneLevel = 0;
        this.explored = false;
        this.gameOver = false;
    }


    public void addAnt(Ant ant) {
        ants.put(ant.getId(), ant);
    }

    public void removeAnt(int antId) {
        ants.remove(antId);
    }

    public Ant getAnt(int antId) {
        return ants.get(antId);
    }

    public Map<Integer, Ant> getAnts() {
        return Collections.unmodifiableMap(this.ants);
    }

    //Pass Envionment to call
    public void updateAnts(Environment environment) {
        List<Integer> antsToRemove = new ArrayList<>();

        // Create a copy of the ants HashMap values
        List<Ant> antsCopy = new ArrayList<>(ants.values());

        for (Ant ant : antsCopy) {
            ant.performAction(this, environment);
            ant.incrementAge();

            if (ant.checkIfDead()) {
                antsToRemove.add(ant.getId());
            }
        }
        // Remove ants after iterating
        for (int antId : antsToRemove) {
            ants.remove(antId);
        }
    }

    public int chooseRandomBalaAnt() {
        List<Integer> balaAntIds = new ArrayList<>();
        for (Ant ant : ants.values()) {
            if (ant instanceof BalaAnt) {
                balaAntIds.add(ant.getId());
            }
        }

        if (balaAntIds.isEmpty()) {
            return -1; // No Bala ants in this cell
        }

        Random rand = new Random();
        return balaAntIds.get(rand.nextInt(balaAntIds.size()));
    }

    public boolean hasFriendlyAnts() {
        return ants.values().stream().anyMatch(ant -> !(ant instanceof BalaAnt));
    }

    public int chooseRandomFriendlyAnt() {
        Random rand = new Random();

        List<Integer> friendlyAntIds = new ArrayList<>();
        for (Ant ant : ants.values()) {
            if (!(ant instanceof BalaAnt)) {
                friendlyAntIds.add(ant.getId());
            }
        }

        if (friendlyAntIds.isEmpty()) {
            return -1;
        }

        return friendlyAntIds.get(rand.nextInt(friendlyAntIds.size()));
    }

    public boolean isValidMove(int x, int y) {
        return x >= 0 && x < Environment.GRID_SIZE && y >= 0 && y < Environment.GRID_SIZE;
    }

    public void endSimulator() {
        gameOver = true;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public int getAntCountByType(Class<? extends Ant> antType) {
        return (int) ants.values().stream().filter(antType::isInstance).count();
    }

    public int getFoodAmount() {
        return this.foodAmount;
    }

    public void setFoodAmount(int foodAmount) {
        this.foodAmount = Math.max(foodAmount, 0); // Ensure the food amount doesn't go below 0
    }

    public int getPheromoneLevel() {
        return pheromoneLevel;
    }

    public void setPheromoneLevel(int pheromoneLevel) {
        this.pheromoneLevel = pheromoneLevel;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored() {
        if (!isExplored()) {
            explored = true;
        }
    }

    public void exploreCell() {
        Random rand = new Random();
        setExplored();
        if (rand.nextInt(100) < 25) {
            // 25% chance to find food in the cell
            setFoodAmount(rand.nextInt(501) + 500); // Random amount between 500 and 1000
        }
        // The cell is now considered explored
    }

    public int getBalaAntCount() {
        return (int) ants.values().stream().filter(ant -> ant instanceof BalaAnt).count();
    }

    public int getSoldierAntCount() {
        return (int) ants.values().stream().filter(ant -> ant instanceof SoldierAnt).count();
    }

    public int getForagerAntCount() {
        return (int) ants.values().stream().filter(ant -> ant instanceof ForagerAnt).count();
    }

    public int getScoutAntCount() {
        return (int) ants.values().stream().filter(ant -> ant instanceof ScoutAnt).count();
    }

    public int getQueenAntAcount() {
        return (int) ants.values().stream().filter(ant -> ant instanceof QueenAnt).count();
    }

    public void decayPheromone() {
        this.pheromoneLevel = Math.max(0, this.pheromoneLevel / 2);
    }

}
