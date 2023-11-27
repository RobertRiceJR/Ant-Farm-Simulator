import java.util.*;

public class ForagerAnt extends Ant {
    private static final int lifespan = 3650;
    private boolean carryingFood;
    private Stack<int[]> movementHistory; // To track the path taken by the ant
    private Random rand = new Random();
    private static final int[][] DIRECTIONS = {{-1, -1}, {-1, 0}, {-1, 1}, {0, -1}, {0, 1}, {1, -1}, {1, 0}, {1, 1}};
    private boolean firstMove;


    public ForagerAnt(int xPosition, int yPosition) {
        super(lifespan, xPosition, yPosition);
        this.carryingFood = false;
        this.movementHistory = new Stack<>();
        this.firstMove = true;
    }

    @Override
    public void performAction(GridCell grid, Environment environment) {
        if (this.hasActedThisTurn()) {
            return; // Skip action if already acted in this turn
        }

        if (carryingFood) {
            returnToNest(grid, environment);
        } else {
            forage(grid, environment);
        }
        this.setHasActedThisTurn(true);
    }


    private void forage(GridCell grid, Environment environment) {
        if (firstMove){
            movementHistory.add(new int []{13,13});
            firstMove = false;
        }

        List<int[]> possibleMoves = new ArrayList<>();
        int highestPheromoneLevel = 0;
        for (int[] dir : DIRECTIONS) {
            int newX = getxPosition() + dir[0];
            int newY = getyPosition() + dir[1];
            if (environment.isValidCoordinate(newX, newY) && !isPreviousSquare(newX, newY)) {
                GridCell adjacentCell = environment.getGridCell(newX, newY);
                if (adjacentCell != null && adjacentCell.isExplored()) {
                    int pheromoneLevel = adjacentCell.getPheromoneLevel();
                    if (pheromoneLevel > highestPheromoneLevel) {
                        highestPheromoneLevel = pheromoneLevel;
                        possibleMoves.clear();
                    }
                    if (pheromoneLevel == highestPheromoneLevel) {
                        possibleMoves.add(new int[]{newX, newY});
                    }
                }
            }
        }

        if (!possibleMoves.isEmpty()) {
            int[] nextMove = possibleMoves.get(rand.nextInt(possibleMoves.size()));
            environment.moveAnt(this, nextMove[0], nextMove[1]);
            setPosition(nextMove[0], nextMove[1]);
            movementHistory.push(new int[]{getxPosition(), getyPosition()});

            // Check and pick up food after moving
            GridCell newCell = environment.getGridCell(getxPosition(), getyPosition());
            if (newCell != null && newCell.getFoodAmount() > 0 && !isAtColonyEntrance(newCell)) {
                newCell.setFoodAmount(newCell.getFoodAmount() - 1);
                carryingFood = true;
                movementHistory.pop();
            }
        } else {
            moveRandomly(grid, environment);
        }
    }


    private boolean isPreviousSquare(int x, int y) {
        if (movementHistory.size() > 1) {
            int[] previousPosition = movementHistory.get(movementHistory.size() - 2);
//            System.out.println(Arrays.toString(previousPosition));
            return previousPosition[0] == x && previousPosition[1] == y;
        }
        return false;
    }

    private boolean isAtColonyEntrance(GridCell cell) {
      return cell.getQueenAntAcount() > 0;
    }

    private void moveRandomly(GridCell grid, Environment environment) {
        Random rand = new Random();
        // Randomly choose a direction to move
        int[] direction = DIRECTIONS[rand.nextInt(DIRECTIONS.length)];
        int newX = this.getxPosition() + direction[0];
        int newY = this.getyPosition() + direction[1];
        // Check if the new position is within the grid bounds
        if (grid.isValidMove(newX, newY)) {
            environment.moveAnt(this, newX, newY);
            setPosition(newX, newY);
            movementHistory.push(new int[]{newX, newY});
        }
    }

    private void returnToNest(GridCell grid, Environment environment) {
        if (movementHistory.isEmpty()) {
            carryingFood = false;
            return;
        }

        int[] lastStep = movementHistory.pop();
//        System.out.println(Arrays.toString(lastStep));
        environment.moveAnt(this, lastStep[0], lastStep[1]);


        if (!isAtColonyEntrance(grid)) {
            if (grid.getPheromoneLevel() < 1000) {
                grid.setPheromoneLevel(Math.min(grid.getPheromoneLevel() + 10, 1000));
            }
        }

        isNewGridQueen(environment.getGridCell(lastStep[0], lastStep [1]));
        }


    private void isNewGridQueen (GridCell gridCell){
        if (isAtColonyEntrance(gridCell)) {
            gridCell.setFoodAmount(gridCell.getFoodAmount() + 1);
            carryingFood = false;
            movementHistory.clear();
            firstMove = true;
        }
    }
}


