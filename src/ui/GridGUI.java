package ui;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class GridGUI extends JFrame {
    private static GridGUI instance;
    private Map<String, RobotInfo> robots = new ConcurrentHashMap<>();
    private Color[] palette = {Color.RED, Color.BLUE, Color.GREEN, Color.ORANGE, Color.MAGENTA};

    private GridPanel gridPanel;
    private JTextArea infoPanel;

    private final int rows = 10;
    private final int cols = 10;

    private GridGUI() {
        setTitle("SMA - Robots Coordination");
        setSize(800, 600);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        gridPanel = new GridPanel();
        add(gridPanel, BorderLayout.CENTER);

        infoPanel = new JTextArea();
        infoPanel.setEditable(false);
        infoPanel.setFont(new Font("Monospaced", Font.PLAIN, 14));
        add(new JScrollPane(infoPanel), BorderLayout.EAST);

        setVisible(true);
    }

    public static GridGUI getInstance() {
        if (instance == null) {
            instance = new GridGUI();
        }
        return instance;
    }

    public synchronized void updateRobot(String robotName, int xCell, int yCell) {
        robots.compute(robotName, (k, v) -> {
            if (v == null) {
                Color color = palette[robots.size() % palette.length];
                return new RobotInfo(xCell, yCell, color, robotName);
            } else {
                v.x = xCell;
                v.y = yCell;
                return v;
            }
        });
        updateInfoPanel();
        SwingUtilities.invokeLater(gridPanel::repaint);
    }

    public synchronized void setZoneAssignment(String robotName, int startX, int startY, int zoneWidth, int zoneHeight) {
        robots.compute(robotName, (k, v) -> {
            if (v == null) {
                Color color = palette[robots.size() % palette.length];
                return new RobotInfo(startX, startY, color, robotName, startX, startY, zoneWidth, zoneHeight);
            } else {
                v.zoneX = startX;
                v.zoneY = startY;
                v.zoneWidth = zoneWidth;
                v.zoneHeight = zoneHeight;
                return v;
            }
        });
        updateInfoPanel();
    }

    private void updateInfoPanel() {
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, RobotInfo> entry : robots.entrySet()) {
            RobotInfo r = entry.getValue();
            sb.append(String.format("%s -> Zone: (%d,%d,%dx%d) | Pos: (%d,%d)%n",
                    entry.getKey(), r.zoneX, r.zoneY, r.zoneWidth, r.zoneHeight, r.x, r.y));
        }
        infoPanel.setText(sb.toString());
    }

    private class GridPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int cellWidth = getWidth() / cols;
            int cellHeight = getHeight() / rows;

            // Draw grid
            g.setColor(Color.LIGHT_GRAY);
            for (int i = 0; i <= cols; i++) g.drawLine(i * cellWidth, 0, i * cellWidth, getHeight());
            for (int i = 0; i <= rows; i++) g.drawLine(0, i * cellHeight, getWidth(), i * cellHeight);

            // Draw robots
            for (RobotInfo r : robots.values()) {
                g.setColor(r.color);
                g.fillRect(r.x * cellWidth, r.y * cellHeight, cellWidth, cellHeight);
                g.setColor(Color.BLACK);
                g.drawString(r.name, r.x * cellWidth + 3, r.y * cellHeight + cellHeight / 2);
            }
        }
    }

    private static class RobotInfo {
        int x, y;
        int zoneX, zoneY, zoneWidth, zoneHeight;
        Color color;
        String name;

        RobotInfo(int x, int y, Color c, String name) {
            this.x = x; this.y = y; this.color = c; this.name = name;
            this.zoneX = x; this.zoneY = y; this.zoneWidth = 1; this.zoneHeight = 1;
        }

        RobotInfo(int x, int y, Color c, String name, int zx, int zy, int zw, int zh) {
            this.x = x; this.y = y; this.color = c; this.name = name;
            this.zoneX = zx; this.zoneY = zy; this.zoneWidth = zw; this.zoneHeight = zh;
        }
    }
}
