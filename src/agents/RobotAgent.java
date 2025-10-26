package agents;

import jade.core.Agent;
import jade.core.behaviours.TickerBehaviour;
import jade.lang.acl.ACLMessage;
import ui.GridGUI;

import java.util.Random;

public class RobotAgent extends Agent {

    private int x, y;
    private int zoneX, zoneY, zoneWidth, zoneHeight;
    private Random rand = new Random();

    protected void setup() {
        System.out.println(getLocalName() + " ready.");

        // Request zone from supervisor
        try {
            ACLMessage msg = new ACLMessage(ACLMessage.PROPOSE);
            msg.addReceiver(getAID("Supervisor"));
            msg.setContent("Request zone");
            send(msg);

            ACLMessage reply = blockingReceive();
            if (reply != null && reply.getPerformative() == ACLMessage.ACCEPT_PROPOSAL) {
                String[] parts = reply.getContent().split(",");
                zoneX = Integer.parseInt(parts[0]);
                zoneY = Integer.parseInt(parts[1]);
                zoneWidth = Integer.parseInt(parts[2]);
                zoneHeight = Integer.parseInt(parts[3]);

                x = zoneX;
                y = zoneY;

                // Update GUI immediately
                GridGUI.getInstance().updateRobot(getLocalName(), x, y);

                // staggered start based on robot ID
                int id = Integer.parseInt(getLocalName().replaceAll("[^0-9]", ""));
                Thread.sleep(id * 4000); // 4 seconds between each robot

                addBehaviour(new TickerBehaviour(this, 500) { // move every 0.5s
                    protected void onTick() {
                        moveInsideZone();
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void moveInsideZone() {
        int direction = rand.nextInt(4);
        switch(direction) {
            case 0: if(x < zoneX + zoneWidth - 1) x++; break;
            case 1: if(x > zoneX) x--; break;
            case 2: if(y < zoneY + zoneHeight - 1) y++; break;
            case 3: if(y > zoneY) y--; break;
        }
        GridGUI.getInstance().updateRobot(getLocalName(), x, y);
    }
}
