import java.util.Random;

public class QueenAnt extends Ant {
    private static final int queenLifeSpan = 20 * 3650;
    private int turnCounter;
    private static final int turnsPerDay = 10;

    public QueenAnt(int xPosition, int yPosition) {
        super( queenLifeSpan, xPosition, yPosition);
        this.turnCounter = 0;
    }

    @Override
    public void performAction(GridCell grid, Environment environment) {

        if (this.hasActedThisTurn()) {
            return;
        }

        if (this.turnCounter % turnsPerDay == 0) {
            hatchNewAnt(grid);
        }
            consumeFood(grid);
            turnCounter++;
        this.setHasActedThisTurn(true);
    }

    private void consumeFood(GridCell grid) {

        if (grid != null) {
            int foodAmount = grid.getFoodAmount();

            if (foodAmount > 0) {
                // Consume food
                grid.setFoodAmount(foodAmount - 1);
            } else {
            grid.endSimulator();
            }
        }
    }

    protected void hatchNewAnt(GridCell grid) {
        // Randomly determine the type of ant to hatch
        Random rand = new Random();
        int chance = rand.nextInt(100);
        Ant newAnt;

        if (chance < 50) {
            newAnt = new ForagerAnt(xPosition, yPosition);
        } else if (chance < 75) {
            newAnt = new ScoutAnt(xPosition, yPosition);
        } else {
            newAnt = new SoldierAnt(xPosition, yPosition);
        }
        grid.addAnt(newAnt);
    }

}

