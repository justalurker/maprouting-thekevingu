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
 * DAView.java: Dijkstra's algorithm viewing client for Deerfield Academy
 */
public class DAView extends JPanel {

    // file location
    private static final String TEXT_LOCATION = "src/txt/da.txt";
    private static final String IMAGE_LOCATION = "src/images/da_campus.png";

    // graphics
    private static JFrame f;
    private static JLayeredPane layers = new JLayeredPane();
    private static JTextArea displayText;
    private static JLabel background;
    private static JLabel sourceLabel;
    private static JLabel destinationLabel;
    private static int[][] pixels = new int[850][1100];
    private static final int APPLICATION_WIDTH = 850;
    private static final int APPLICATION_HEIGHT = 1100;
    private static final String TITLE = "DAView.java";

    // source and destination
    private static int source = -1;
    private static int destination = -1;

    // graph and solution list
    private static GraphV graph;
    private static LinkedList<Node> solution = new LinkedList<>();

    // storing label locations
    private static Point offsetS = new Point(0, 0);
    private static Point offsetD = new Point(0, 0);

    // directions
    private static final Location[] DIRECTIONS = {new Location(-1, 0), new Location(0, 1), new Location(1, 0), new Location(0, -1)};

    // formatting
    private static DecimalFormat df = new DecimalFormat("0.00");

    // main
    public static void main(String[] args) {
        // instructions
        System.out.println("Kevin's DAView.java");
        System.out.println("- Dijkstra's algorithm viewing client for Deerfield Academy");
        System.out.println("- Drag and drop blue (source) and green (destination) markers on dots to find shortest path between points");
        System.out.println("- The TextArea displays the shortest route");

        // loads graph
        loadGraph();

        // start gui
        SwingUtilities.invokeLater(DAView::start);
    }

    // graphics settings
    private DAView() {
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
        f.add(new DAView(), BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

    // initializes and creates all JObjects
    private void initializeJObjects() {
        layers.setPreferredSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));

        // source marker
        sourceLabel = new JLabel();
        sourceLabel.setBounds(50, 100, 30, 46);
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
            }

            public void mouseReleased(MouseEvent e) {
                Point location = sourceLabel.getLocation();
                Location l = new Location(location.x + 15, location.y + 46);
                if (isValid(l)) {
                    int s1 = bfs(l);
                    if (s1 != -1) {
                        source = s1;
                        if (destination != -1)
                            solution = graph.dijkstra(source, destination);
                    } else
                        solution = new LinkedList<>();
                    background.repaint();
                    updateText();
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
        destinationLabel.setBounds(90, 100, 30, 46);
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
            }

            public void mouseReleased(MouseEvent e) {
                Point location = destinationLabel.getLocation();
                Location l = new Location(location.x + 15, location.y + 46);
                if (isValid(l)) {
                    int d1 = bfs(l);
                    if (d1 != -1) {
                        destination = d1;
                        if (source != -1)
                            solution = graph.dijkstra(source, destination);
                    } else
                        solution = new LinkedList<>();
                    background.repaint();
                    updateText();
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

        // displaying route
        displayText = new JTextArea();
        displayText.setLineWrap(true);
        displayText.setWrapStyleWord(true);
        displayText.setEditable(false);
        displayText.setBounds(25, 25, 200, 50);
        displayText.setText("(Intersection, Distance)");

        // implement scrolling
        JScrollPane scroll = new JScrollPane(displayText);
        scroll.setBounds(25, 25, 220, 50);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        layers.add(scroll);

        background = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < graph.vertices(); i++) {
                    Vertex v = graph.getVertex(i);
                    g.drawRect(v.location.x - 1, v.location.y - 1, 2, 2);
                }
                g.setColor(Color.RED);
                for (int i = 0; i < solution.size() - 1; i++)
                    g.drawLine(graph.getVertex(solution.get(i).index).location.x, graph.getVertex(solution.get(i).index).location.y, graph.getVertex(solution.get(i + 1).index).location.x, graph.getVertex(solution.get(i + 1).index).location.y);
            }
        };
        background.setBounds(0, 0, APPLICATION_WIDTH, APPLICATION_HEIGHT);
        background.setIcon(new ImageIcon(IMAGE_LOCATION));
        layers.add(background);

        f.getContentPane().add(layers);
        f.pack();
    }

    // updates text in displayText
    private static void updateText() {
        String s = "";
        for (Node n : solution) {
            s += n.index + "\t" + df.format(n.weight) + "\n";
        }
        s += "(Intersection, Distance)";
        displayText.setText(s);
    }

    // bfs around a location to find closest vertex
    private static int bfs(Location location) {
        Queue<Location> queue = new Queue<>();
        queue.enqueue(location);

        // for tracking whether visited
        boolean[][] visited = new boolean[850][1100];

        int counter = 0;

        // until queue is empty
        while (!queue.isEmpty() && counter++ < 1000) {
            Location current = queue.dequeue();
            if (pixels[current.x][current.y] != -1) {
                return pixels[current.x][current.y];
            }

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
                graph.addAdjacent(Integer.parseInt(st.nextToken()), Integer.parseInt(st.nextToken()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
