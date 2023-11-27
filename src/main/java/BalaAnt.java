import java.util.Random;

public class BalaAnt extends Ant {
    private static final int lifespan = 3650;
    private static final int[][] DIRECTIONS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};

    public BalaAnt(int xPosition, int yPosition) {
        super(lifespan, xPosition, yPosition);
    }

    @Override
    public void performAction(GridCell grid, Environment environment) {
        if (this.hasActedThisTurn()) {
            return;
        }

        if (grid.hasFriendlyAnts()) {
            attackRandomAnt(grid);
        } else {
            move(grid, environment);
        }

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

    private void attackRandomAnt(GridCell grid) {
        Random rand = new Random();

        int targetAntId = grid.chooseRandomFriendlyAnt();
        if (targetAntId != -1 && rand.nextBoolean()) {
            grid.removeAnt(targetAntId);
        }
    }
}
