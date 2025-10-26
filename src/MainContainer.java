import jade.core.Profile;
import jade.core.ProfileImpl;
import jade.core.Runtime;
import jade.wrapper.AgentContainer;
import jade.wrapper.AgentController;
import ui.GridGUI;

public class MainContainer {
    public static void main(String[] args) {
        // Launch the GUI first
        GridGUI.getInstance();

        try {
            // Create JADE runtime and main container with GUI
            Runtime rt = Runtime.instance();
            Profile p = new ProfileImpl();
            p.setParameter(Profile.GUI, "true");
            AgentContainer mainContainer = rt.createMainContainer(p);

            // Create Supervisor agent
            AgentController supervisor = mainContainer.createNewAgent(
                    "Supervisor",
                    "agents.SupervisorAgent",
                    null
            );
            supervisor.start();

            // Create 4 Robot agents
            for (int i = 0; i < 4; i++) {
                AgentController robot = mainContainer.createNewAgent(
                        "Robot" + i,
                        "agents.RobotAgent",
                        null
                );
                robot.start();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
