public abstract class Ant {
    protected static int nextId = 1; // Start from 1, as 0 is reserved for the Queen
    protected final int id;
    protected int age;
    protected int lifespan = 3650;
    protected int xPosition;
    protected int yPosition;
    protected boolean hasActedThisTurn;

    public Ant( int lifespan, int xPosition, int yPosition) {
        this.id = getNextId();
        this.xPosition = xPosition;
        this.yPosition = yPosition;
        this.age = 0;
        this.hasActedThisTurn = false;
        this.lifespan = lifespan;
    }

    private static synchronized int getNextId() {
        return nextId++;
    }

    public void setPosition(int x, int y) {
            this.xPosition = x;
            this.yPosition = y;
        }

    // Method to reset action status for a new turn
    public void resetActionStatus() {
        this.hasActedThisTurn = false;
    }

    // Setter for the hasActedThisTurn flag
    public void setHasActedThisTurn(boolean hasActed) {
        this.hasActedThisTurn = hasActed;
    }

    // Getter for the hasActedThisTurn flag
    public boolean hasActedThisTurn() {
        return hasActedThisTurn;
    }

    public int getId() {
        return id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getLifespan() {
        return lifespan;
    }

    public void setLifespan(int lifespan) {
        this.lifespan = lifespan;
    }

    public int getxPosition() {
        return xPosition;
    }

    public void setxPosition(int xPosition) {
        this.xPosition = xPosition;
    }

    public int getyPosition() {
        return yPosition;
    }

    public void setyPosition(int yPosition) {
        this.yPosition = yPosition;
    }

    public boolean isAlive() {
        return age < lifespan;
    }

    public abstract void performAction(GridCell grid, Environment environment);

    public void incrementAge() {
        age++;
    }

    public boolean checkIfDead() {
        if (this.age > this.lifespan) {
            return true;
        }
        return false;
    }
}

