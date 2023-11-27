import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Environment {
    public static final int GRID_SIZE = 27;
    private GridCell[][] grid;
    private ColonyNodeView[][] colonyNodeViews;
    private int turnCounter;
    int centerX = GRID_SIZE / 2;
    int centerY = GRID_SIZE / 2;
    private boolean simulationRunning;
    private int foodAmount;
    private int pheromoneLevel;
    private final Random rand = new Random();


    public Environment(ColonyView colonyView) {
        this.grid = new GridCell[GRID_SIZE][GRID_SIZE];
        this.colonyNodeViews = new ColonyNodeView[GRID_SIZE][GRID_SIZE];
        this.turnCounter = 0;
        this.simulationRunning = true;
        initializeGrid(colonyView);
    }

    private void initializeGrid(ColonyView colonyView) {
        grid = new GridCell[GRID_SIZE][GRID_SIZE];
        colonyNodeViews = new ColonyNodeView[GRID_SIZE][GRID_SIZE];

        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j] = new GridCell();
                colonyNodeViews[i][j] = new ColonyNodeView();
                colonyView.addColonyNodeView(colonyNodeViews[i][j], i, j);

                //Set Id for Each Grid
                colonyNodeViews[i][j].setID(i + " " + j);

                // Set the initial pheromone level in the GridCell and update the ColonyNodeView
                colonyNodeViews[i][j].setPheromoneLevel(0);

                // Open adjacent squares around the center and update the ColonyNodeView
                if (Math.abs(i - centerX) <= 1 && Math.abs(j - centerY) <= 1) {
                    grid[i][j].setExplored(); // Mark the GridCell as open
                    colonyNodeViews[i][j].showNode(); // Mark the node as open in the view
                    if (i == centerX && j == centerY) {
                        // Initialize the center cell with the queen and other ants
                        initializeCenterCell(grid[i][j]);
                        grid[i][j].setExplored();
                        // Update the ColonyNodeView to reflect the state of the center GridCell
                        colonyNodeViews[i][j].setQueen(true);
                        colonyNodeViews[i][j].setSoldierCount(grid[i][j].getAntCountByType(SoldierAnt.class));
                        colonyNodeViews[i][j].setForagerCount(grid[i][j].getAntCountByType(ForagerAnt.class));
                        colonyNodeViews[i][j].setScoutCount(grid[i][j].getAntCountByType(ScoutAnt.class));
                        colonyNodeViews[i][j].setBalaCount(grid[i][j].getAntCountByType(BalaAnt.class));
                        colonyNodeViews[i][j].setFoodAmount(grid[i][j].getFoodAmount());
                        colonyNodeViews[i][j].showQueenIcon();
                        colonyNodeViews[i][j].showSoldierIcon();
                        colonyNodeViews[i][j].showScoutIcon();
                        colonyNodeViews[i][j].showForagerIcon();
                        colonyNodeViews[i][j].setPheromoneLevel(0);
                    }
                }
            }
        }
    }


    private void initializeCenterCell(GridCell cell) {
        int centerX = GRID_SIZE / 2;
        int centerY = GRID_SIZE / 2;

        // Add Queen Ant
        QueenAnt queenAnt = new QueenAnt(centerX, centerY);
        // Queen ID is still manually set to 0
        cell.addAnt(queenAnt);

        // Add other types of ants
        for (int i = 0; i < 10; i++) {
            cell.addAnt(new SoldierAnt(centerX, centerY));
        }
        for (int i = 0; i < 50; i++) {
            cell.addAnt(new ForagerAnt(centerX, centerY));
        }
        for (int i = 0; i < 4; i++) {
            cell.addAnt(new ScoutAnt(centerX, centerY));
        }
        cell.setFoodAmount(1000);
    }

    public int getTurnCounter() {
        return this.turnCounter;
    }

    // Check if the coordinates are within the grid bounds
    public boolean isValidCoordinate(int x, int y) {
        return x >= 0 && x < GRID_SIZE && y >= 0 && y < GRID_SIZE;
    }

    public void startNewTurn() {
        isGameOver();
        if (turnCounter > 0) {
            for (int x = 0; x < GRID_SIZE; x++) {
                for (int y = 0; y < GRID_SIZE; y++) {
                    GridCell cell = grid[x][y];
                    for (Ant ant : cell.getAnts().values()) {
                        ant.resetActionStatus();
                    }
                }
            }
        }
    }

    public void decayPheromones() {
        for (int x = 0; x < GRID_SIZE; x++) {
            for (int y = 0; y < GRID_SIZE; y++) {
                grid[x][y].decayPheromone();
            }
        }
    }


    public void incrementTurnCounter() {
        this.turnCounter++;
    }

    public void performTurnEvents() {
        for (int i = 0; i < GRID_SIZE; i++) {
            for (int j = 0; j < GRID_SIZE; j++) {
                grid[i][j].updateAnts(this);
            }
        }
        if (this.turnCounter % 10 == 0) {
            decayPheromones();
        }
    }


    public void isGameOver() {
        if (grid[13][13].isGameOver() || grid[13][13].getAntCountByType(QueenAnt.class) == 0) {
            simulationRunning = false;
        }
    }

    public void updateColonyNodeViews() {
        for (int i = 0; i < grid.length; i++) {
            for (int j = 0; j < grid[i].length; j++) {
                GridCell cell = grid[i][j];
                ColonyNodeView nodeView = colonyNodeViews[i][j];
                updateNodeViewFromCell(nodeView, cell);
            }
        }
    }

    public void moveAnt(Ant ant, int newX, int newY) {
        if (!isValidCoordinate(newX, newY) || ant == null) {
            return; // Invalid new position or null ant
        }

        // Remove ant from its current cell
        grid[ant.getxPosition()][ant.getyPosition()].removeAnt(ant.getId());

        // Update ant's position
        ant.setPosition(newX, newY);

        // Add ant to the new cell
        grid[newX][newY].addAnt(ant);

        if (!grid[newX][newY].isExplored()) {
            grid[newX][newY].exploreCell();
        }
    }


    public List<int[]> findAdjacentBalas(int x, int y) {
        List<int[]> adjacentBalas = new ArrayList<>();
        int[][] directions = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

        for (int[] dir : directions) {
            int newX = x + dir[0];
            int newY = y + dir[1];

            // Check if the new coordinates are within the grid bounds
            if (isValidCoordinate(newX, newY)) {
                GridCell cell = grid[newX][newY];
                if (cell.getAntCountByType(BalaAnt.class) > 0) {
                    adjacentBalas.add(new int[]{newX, newY});
                }
            }
        }
        return adjacentBalas;
    }


    public void trySpawnBalaAnt() {
        if (rand.nextInt(100) < 3) { // 3% chance to spawn a Bala ant
            spawnBalaAnt();
        }
    }

    private void spawnBalaAnt() {
        int x, y;

        // Randomly decide to place the Bala ant on the top/bottom row or left/right column
        if (rand.nextBoolean()) {
            // Place on top or bottom row
            x = rand.nextInt(GRID_SIZE); // Anywhere along the width
            y = rand.nextBoolean() ? 0 : GRID_SIZE - 1;
        } else {
            // Place on left or right column
            x = rand.nextBoolean() ? 0 : GRID_SIZE - 1;
            y = rand.nextInt(GRID_SIZE);
        }

        BalaAnt balaAnt = new BalaAnt(x, y);
        grid[x][y].addAnt(balaAnt);
    }


    private void updateNodeViewFromCell(ColonyNodeView nodeView, GridCell grid) {
        // Set the various counts and levels in the node view
        nodeView.setForagerCount(grid.getForagerAntCount());
        nodeView.setScoutCount(grid.getScoutAntCount());
        nodeView.setSoldierCount(grid.getSoldierAntCount());
        nodeView.setBalaCount(grid.getBalaAntCount());
        nodeView.setFoodAmount(grid.getFoodAmount());
        nodeView.setPheromoneLevel(grid.getPheromoneLevel());

        if (grid.getForagerAntCount() > 0) {
            nodeView.showForagerIcon();
        } else {
            nodeView.hideForagerIcon();
        }

        if (grid.getBalaAntCount() > 0) {
            nodeView.showBalaIcon();
        } else {
            nodeView.hideBalaIcon();
        }

        if (grid.getSoldierAntCount() > 0) {
            nodeView.showSoldierIcon();
        } else {
            nodeView.hideSoldierIcon();
        }

        if (grid.getScoutAntCount() > 0) {
            nodeView.showScoutIcon();
        } else {
            nodeView.hideScoutIcon();
        }

        if (grid.getAntCountByType(QueenAnt.class) == 0) {
            nodeView.hideNode();
        }

        if (grid.isExplored()) {
            nodeView.showNode();
        }
    }


    public GridCell getGridCell(int x, int y) {
        if (isValidCoordinate(x, y)) {
            return grid[x][y];
        }
        return null;
    }

    public boolean isSimulationRunning() {
        return this.simulationRunning;
    }

    public int getFoodAmount() {
        return foodAmount;
    }

    public void setFoodAmount(int foodAmount) {
        this.foodAmount = foodAmount;
    }

    public int getPheromoneLevel() {
        return pheromoneLevel;
    }

    public void setPheromoneLevel(int pheromoneLevel) {
        this.pheromoneLevel = pheromoneLevel;
    }


}