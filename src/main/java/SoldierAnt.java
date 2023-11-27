import java.util.Random;
import java.util.List;

public class SoldierAnt extends Ant {
    private static final int ANT_LIFESPAN = 3650; // One year in turns
    private static final int[][] DIRECTIONS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    private Random rand = new Random();

    public SoldierAnt(int xPosition, int yPosition) {
        super(ANT_LIFESPAN, xPosition, yPosition);
    }

    @Override
    public void performAction(GridCell grid, Environment environment) {
        if (this.hasActedThisTurn()) {
            return;
        }

        //Attack mode
        if (grid.getBalaAntCount() > 0) {
            attackBala(grid);
        } else {
            //Get AdjacentBalas
            List<int[]> adjacentBalas = environment.findAdjacentBalas(getxPosition(), getyPosition());
            if (!adjacentBalas.isEmpty()) {
                //Move to Random Square with Balas
                moveToBalas(environment, adjacentBalas);
            } else {
                move(grid, environment);
            }
        }
        this.setHasActedThisTurn(true);
    }

    private void attackBala(GridCell grid) {
        // Get a random Bala ant's ID from the current cell
        int balaId = grid.chooseRandomBalaAnt();
        if (rand.nextBoolean()) { // 50% chance to kill the Bala ant
            grid.removeAnt(balaId);
        }
    }

    private void moveToBalas(Environment environment, List<int[]> adjacentBalas) {

        Random rand = new Random();
        // Randomly select one of the adjacent cells containing a Bala ant
        int[] selectedCell = adjacentBalas.get(rand.nextInt(adjacentBalas.size()));
        int newX = selectedCell[0];
        int newY = selectedCell[1];

        // Move the ant to the selected cell
        environment.moveAnt(this, newX, newY);
        this.setPosition(newX, newY);
    }

    private void move(GridCell grid, Environment environment) {

        Random rand = new Random();
        // Randomly choose a direction to move
        int[] direction = DIRECTIONS[rand.nextInt(DIRECTIONS.length)];
        int newX = this.getxPosition() + direction[0];
        int newY = this.getyPosition() + direction[1];

        // Check if the new position is within the grid bounds
        if (grid.isValidMove(newX, newY)) {
            environment.moveAnt(this, newX, newY);
            this.setPosition(newX, newY);
        }
    }

}
