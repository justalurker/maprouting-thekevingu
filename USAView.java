import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.StringTokenizer;

/*
 * Kevin Gu
 * March 5, 2020
 * USAView.java: Dijkstra's algorithm viewing client for usa.txt
 */
public class USAView extends JPanel {

    // file location
    private static final String TEXT_LOCATION = "src/txt/usa.txt";
    private static final String IMAGE_LOCATION = "src/images/map_usa.png";

    // graphics
    private static JFrame f;
    private static JLayeredPane layers = new JLayeredPane();
    private static JTextArea displayText;
    private static JLabel background;
    private static JLabel sourceLabel;
    private static JLabel destinationLabel;
    private static int[][] pixels = new int[10005][4330];
    private static final int APPLICATION_WIDTH = 1388;
    private static final int APPLICATION_HEIGHT = 743;
    private static final String TITLE = "USAView.java";

    // source and destination
    private static int source = -1;
    private static int destination = -1;

    // graph and solution list
    private static GraphV graph;
    private static LinkedList<Node> solution = new LinkedList<>();

    // directions
    private static final Location[] DIRECTIONS = {new Location(-1, 0), new Location(0, 1), new Location(1, 0), new Location(0, -1)};

    // storing label locations
    private static Point offsetS = new Point(0, 0);
    private static Point offsetD = new Point(0, 0);
    private static Pressed pressed = Pressed.Source;

    // formatting
    private static DecimalFormat df = new DecimalFormat("0.00");

    // which button is last pressed
    private enum Pressed {
        Source, Destination
    }

    // main
    public static void main(String[] args) {
        // instructions
        System.out.println("Kevin's USAView.java");
        System.out.println("- Dijkstra's algorithm viewing client for USA");
        System.out.println("- Drag and drop blue (source) and green (destination) markers on dots to find shortest path between points");
        System.out.println("- Click a marker and enter value in textfield to set it to a specific vertex");
        System.out.println("- The TextArea displays the shortest route");

        // loads graph
        loadGraph();

        // start gui
        SwingUtilities.invokeLater(USAView::start);
    }

    // graphics settings
    private USAView() {
        setPreferredSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));
        setFocusable(true);
        initializeJObjects();
    }

    // jframe settings
    private static void start() {
        f = new JFrame();
        f.setTitle(TITLE);
        f.setResizable(false);
        f.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                System.exit(0);
            }
        });
        f.add(new USAView(), BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

    // initializes and creates all JObjects
    private void initializeJObjects() {
        layers.setPreferredSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));

        // input for selecting vertex
        JTextField input = new JTextField();
        input.setBounds(21, 638, 228, 25);
        input.addActionListener(e -> {
            if (isNumber(input.getText())) {
                int i = Integer.parseInt(input.getText());
                if (pressed.equals(Pressed.Source) && isVertexValid(i)) {
                    Location location2 = graph.getVertex(i).location;
                    sourceLabel.setLocation(getXPixelCoordinate(location2.x) - 15, getYPixelCoordinate(location2.y) - 46);
                    source = i;
                    if (destination != -1) {
                        solution = graph.dijkstra(source, destination);
                        background.repaint();
                        updateText();
                    }
                } else if (pressed.equals(Pressed.Destination) && isVertexValid(i)) {
                    Location location2 = graph.getVertex(i).location;
                    destinationLabel.setLocation(getXPixelCoordinate(location2.x) - 15, getYPixelCoordinate(location2.y) - 46);
                    destination = i;
                    if (source != -1) {
                        solution = graph.dijkstra(source, destination);
                        background.repaint();
                        updateText();
                    }
                } else {
                    System.out.println("Not within bounds.");
                }
            } else {
                System.out.println("Not a number.");
            }
        });
        layers.add(input);

        // displaying route
        displayText = new JTextArea();
        displayText.setLineWrap(true);
        displayText.setWrapStyleWord(true);
        displayText.setEditable(false);
        displayText.setBounds(25, 668, 200, 50);
        displayText.setText("(Intersection, Distance)");

        // implement scrolling
        JScrollPane scroll = new JScrollPane(displayText);
        scroll.setBounds(25, 668, 220, 50);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        layers.add(scroll);

        // source marker
        sourceLabel = new JLabel();
        sourceLabel.setBounds(1333, 680, 30, 46);
        sourceLabel.setIcon(new ImageIcon("src/images/source.png"));
        sourceLabel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getPoint().x - offsetS.x;
                int y = e.getPoint().y - offsetS.y;
                Point location = sourceLabel.getLocation();
                location.x += x;
                location.y += y;
                sourceLabel.setLocation(location);
            }
        });
        sourceLabel.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {
                offsetS = e.getPoint();
                layers.moveToFront(sourceLabel);
                pressed = Pressed.Source;
            }

            public void mouseReleased(MouseEvent e) {
                Point location = sourceLabel.getLocation();
                Location l = new Location(getGraphXCoordinate(location.x + 15), getGraphYCoordinate(location.y + 46));
                if (isValid(l)) {
                    int s1 = bfs(l);
                    if (s1 != -1) {
                        source = s1;
                        if (destination != -1) {
                            solution = graph.dijkstra(source, destination);
                            background.repaint();
                            updateText();
                        }
                    } else {
                        solution = new LinkedList<>();
                        background.repaint();
                        updateText();
                    }
                }
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        layers.add(sourceLabel);

        // destination marker
        destinationLabel = new JLabel();
        destinationLabel.setBounds(1280, 680, 30, 46);
        destinationLabel.setIcon(new ImageIcon("src/images/destination.png"));
        destinationLabel.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseDragged(MouseEvent e) {
                int x = e.getPoint().x - offsetD.x;
                int y = e.getPoint().y - offsetD.y;
                Point location = destinationLabel.getLocation();
                location.x += x;
                location.y += y;
                destinationLabel.setLocation(location);
            }
        });
        destinationLabel.addMouseListener(new MouseListener() {
            public void mousePressed(MouseEvent e) {
                offsetD = e.getPoint();
                layers.moveToFront(destinationLabel);
                pressed = Pressed.Destination;
            }

            public void mouseReleased(MouseEvent e) {
                Point location = destinationLabel.getLocation();
                Location l = new Location(getGraphXCoordinate(location.x + 15), getGraphYCoordinate(location.y + 46));
                if (isValid(l)) {
                    int d1 = bfs(l);
                    if (d1 != -1) {
                        destination = d1;
                        if (source != -1) {
                            solution = graph.dijkstra(source, destination);
                            background.repaint();
                            updateText();
                        } else {
                            solution = new LinkedList<>();
                            background.repaint();
                            updateText();
                        }
                    }
                }
            }

            public void mouseClicked(MouseEvent e) {
            }

            public void mouseEntered(MouseEvent e) {
            }

            public void mouseExited(MouseEvent e) {
            }
        });
        layers.add(destinationLabel);

        // background image
        background = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < graph.vertices(); i++) {
                    Vertex v = graph.getVertex(i);
                    g.drawRect(getXPixelCoordinate(v.location.x), getYPixelCoordinate(v.location.y), 1, 1);
                }
                g.setColor(Color.RED);
                for (int i = 0; i < solution.size() - 1; i++)
                    g.drawLine(getXPixelCoordinate(graph.getVertex(solution.get(i).index).location.x), getYPixelCoordinate(graph.getVertex(solution.get(i).index).location.y), getXPixelCoordinate(graph.getVertex(solution.get(i + 1).index).location.x), getYPixelCoordinate(graph.getVertex(solution.get(i + 1).index).location.y));
            }
        };
        background.setBounds(0, 0, 1388, 743);
        background.setIcon(new ImageIcon(IMAGE_LOCATION));
        layers.add(background);

        // adding in everything
        f.getContentPane().add(layers);
        f.pack();
    }

    // updates text in displayText
    private static void updateText() {
        String s = "";
        for (Node n : solution)
            s += n.index + "\t" + df.format(n.weight) + "\n";
        s += "(Intersection, Distance)";
        displayText.setText(s);
    }

    // bfs around a location to find closest vertex
    private static int bfs(Location location) {
        Queue<Location> queue = new Queue<>();
        queue.enqueue(location);

        // for tracking whether visited
        boolean[][] visited = new boolean[10005][4330];
        int counter = 0;

        // until queue is empty
        while (!queue.isEmpty() && counter++ < 50000) {
            Location current = queue.dequeue();
            if (pixels[current.x][current.y] != -1)
                return pixels[current.x][current.y];

            // set visited
            visited[current.x][current.y] = true;

            // iterate through the different directions
            // if valid, enqueue
            for (Location direction : DIRECTIONS)
                if (!visited[current.x + direction.x][current.y + direction.y] && isValid(new Location(current.x + direction.x, current.y + direction.y))) {
                    queue.enqueue(new Location(current.x + direction.x, current.y + direction.y));
                    visited[current.x + direction.x][current.y + direction.y] = true;
                }
        }
        return -1;
    }

    // returns whether pixel location is within n-bounds
    private static boolean isValid(Location l) {
        return l.x >= 0 && l.x < pixels.length && l.y >= 0 && l.y < pixels[0].length;
    }

    // returns whether index is a valid vertex
    private static boolean isVertexValid(int i) {
        return i >= 0 && i < graph.vertices();
    }

    // returns whether a string is a number
    private static boolean isNumber(String str) {
        try {
            Integer.parseInt(str);
        } catch (NumberFormatException nfe) {
            return false;
        }
        return true;
    }

    // loads graph
    private static void loadGraph() {
        try {
            BufferedReader br = new BufferedReader(new FileReader(TEXT_LOCATION));
            StringTokenizer st = new StringTokenizer(br.readLine(), " ");

            for (int i = 0; i < pixels.length; i++)
                for (int j = 0; j < pixels[0].length; j++)
                    pixels[i][j] = -1;

            int vertices = Integer.parseInt(st.nextToken());
            int edges = Integer.parseInt(st.nextToken());
            graph = new GraphV(vertices);

            for (int i = 0; i < vertices; i++) {
                st = new StringTokenizer(br.readLine(), " ");
                int index = Integer.parseInt(st.nextToken());
                int x = Integer.parseInt(st.nextToken());
                int y = Integer.parseInt(st.nextToken());
                pixels[x][y] = index;
                graph.addVertex(index, new Location(x, y));
            }

            for (int i = 0; i < edges; i++) {
                st = new StringTokenizer(br.readLine(), " ");
                int s = Integer.parseInt(st.nextToken());
                int d = Integer.parseInt(st.nextToken());
                graph.addAdjacent(s, d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // returns x pixel conversion of graph coordinate
    private static int getXPixelCoordinate(int x) {
        return 35 + (int) ((double) x / 10250 * 1388);
    }

    // returns graph x coordinate conversion of pixel coordinate
    private static int getGraphXCoordinate(int x) {
        return (int) ((x - 35) * (double) 10250 / 1388);
    }

    // returns y pixel conversion of graph coordinate
    private static int getYPixelCoordinate(int y) {
        return 750 - (int) ((double) y / 4400 * 743);
    }

    // returns graph y coordinate conversion of pixel coordinate
    private static int getGraphYCoordinate(int y) {
        return (int) ((750 - y) * (double) 4400 / 743);
    }

}
