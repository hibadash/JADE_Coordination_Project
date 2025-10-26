package agents;

import jade.core.Agent;
import jade.core.behaviours.CyclicBehaviour;
import jade.lang.acl.ACLMessage;
import ui.GridGUI;

public class SupervisorAgent extends Agent {

    // Predefined non-adjacent zones for 4 robots
    private int[][] zones = {
            {0,0,2,2},
            {5,0,2,2},
            {0,5,2,2},
            {5,5,2,2}
    };

    protected void setup() {
        System.out.println(getLocalName() + " ready.");

        addBehaviour(new CyclicBehaviour() {
            public void action() {
                ACLMessage msg = receive();
                if (msg != null && msg.getPerformative() == ACLMessage.PROPOSE) {
                    String robotName = msg.getSender().getLocalName();
                    int id = Integer.parseInt(robotName.replaceAll("[^0-9]", "")); // Robot0->0
                    int[] zone = zones[id];

                    GridGUI.getInstance().setZoneAssignment(robotName, zone[0], zone[1], zone[2], zone[3]);

                    ACLMessage reply = msg.createReply();
                    reply.setPerformative(ACLMessage.ACCEPT_PROPOSAL);
                    reply.setContent(zone[0]+","+zone[1]+","+zone[2]+","+zone[3]);
                    send(reply);
                } else {
                    block();
                }
            }
        });
    }
}
