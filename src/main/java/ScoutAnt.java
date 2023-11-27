import java.util.Random;

public class ScoutAnt extends Ant {
    private static final int ANT_LIFESPAN = 3650; // One year in turns
    private static final int[][] DIRECTIONS = {
            {-1, -1}, {-1, 0}, {-1, 1},
            {0, -1}, {0, 1},
            {1, -1}, {1, 0}, {1, 1}
    };

    public ScoutAnt(int xPosition, int yPosition) {
        super(ANT_LIFESPAN, xPosition, yPosition);
    }

    @Override
    public void performAction(GridCell grid, Environment environment) {
        if (this.hasActedThisTurn()) {
            return;
        }

        move(grid, environment);

        this.setHasActedThisTurn(true);
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
