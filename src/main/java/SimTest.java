import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JOptionPane;
import javax.swing.Timer;

/**
 * Written by Roger West
 * University of Illinois at Springfield
 * Jan 9, 2013
 */
public class SimTest implements SimulationEventListener, ActionListener {
    private Environment environment; // Add this attribute


    //-------------------------------------------------------------------------
    //	Constants
    //-------------------------------------------------------------------------

    //-------------------------------------------------------------------------
    //	Attributes
    //-------------------------------------------------------------------------

    private int turnCounter;
    private int dayCounter;

    private AntSimGUI gui;

    private Timer swingTimer;

    //-------------------------------------------------------------------------
    //	Constructors
    //-------------------------------------------------------------------------

    public SimTest(AntSimGUI gui) {
        this.gui = gui;
        this.turnCounter = 1;
        this.dayCounter = 1;

        gui.addSimulationEventListener(this);

        ColonyView colonyView = new ColonyView(27, 27);

        // Initialize the Environment with the ColonyView
        this.environment = new Environment(colonyView);

        // Initialize GUI with the ColonyView
        gui.initGUI(colonyView);

    }

    //-------------------------------------------------------------------------
    //	Methods
    //-------------------------------------------------------------------------

    /**
     * @param args
     */
    public static void main(String[] args) {
        AntSimGUI gui = new AntSimGUI();
        SimTest st = new SimTest(gui);
    }

    @Override
    public void actionPerformed(ActionEvent arg0) {
        if (!environment.isSimulationRunning()) {
            // Stop the timer and end the simulation
            swingTimer.stop();
            JOptionPane.showMessageDialog(null, "Simulation ended: The Queen has died.");
            return;
        }

        environment.incrementTurnCounter();
        environment.startNewTurn();
        environment.trySpawnBalaAnt();
        environment.performTurnEvents();
        environment.updateColonyNodeViews();

        if(turnCounter % 10 == 0) {
            dayCounter++;
        }
        gui.setTime("Day: " + dayCounter + ", Turn: " + turnCounter++);
    }

    @Override
    public void simulationEventOccurred(SimulationEvent simEvent) {
        if (simEvent.getEventType() == SimulationEvent.NORMAL_SETUP_EVENT) {
            // set up initial state of the simulation
            JOptionPane.showMessageDialog(null, "Normal Setup Event", "Normal Setup", JOptionPane.INFORMATION_MESSAGE);
        } else if (simEvent.getEventType() == SimulationEvent.QUEEN_TEST_EVENT) {
            // set up antSim.simulation for testing the queen ant
            JOptionPane.showMessageDialog(null, "Queen Test Event", "Queen Test", JOptionPane.INFORMATION_MESSAGE);
        } else if (simEvent.getEventType() == SimulationEvent.SCOUT_TEST_EVENT) {
            // set up antSim.simulation for testing the scout ant
            JOptionPane.showMessageDialog(null, "Scout Test Event", "Scout Test", JOptionPane.INFORMATION_MESSAGE);
        } else if (simEvent.getEventType() == SimulationEvent.FORAGER_TEST_EVENT) {
            // set up antSim.simulation for testing the forager ant
            JOptionPane.showMessageDialog(null, "Forager Test Event", "Forager Test", JOptionPane.INFORMATION_MESSAGE);
        } else if (simEvent.getEventType() == SimulationEvent.SOLDIER_TEST_EVENT) {
            // set up antSim.simulation for testing the soldier ant
            JOptionPane.showMessageDialog(null, "Soldier Test Event", "Soldier Test", JOptionPane.INFORMATION_MESSAGE);
        } else if (simEvent.getEventType() == SimulationEvent.RUN_EVENT) {
            swingTimer = new Timer(100, this);

            swingTimer.start();
        } else if (simEvent.getEventType() == SimulationEvent.STEP_EVENT) {
            performSingleSimulationStep();
        } else {
            // invalid event occurred
        }
    }

    private void performSingleSimulationStep() {
        if (!environment.isSimulationRunning()) {
            // End the simulation
            JOptionPane.showMessageDialog(null, "Simulation ended: The Queen has died.");
            return;
        }

        environment.incrementTurnCounter();
        environment.startNewTurn();
        environment.performTurnEvents();
        environment.updateColonyNodeViews();

        if(turnCounter % 10 == 0) {
            dayCounter++;
        }
        gui.setTime("Day: " + dayCounter + ", Turn: " + turnCounter++);
    }
}


