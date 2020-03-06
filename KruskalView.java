import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;

/*
 * Kevin Gu
 * March 5, 2020
 * KruskalView.java: Kruskal's algorithm viewing client
 */
public class KruskalView extends JPanel {

    // file location
    private static final String TEXT_LOCATION = "src/txt/usa.txt";
    private static final String IMAGE_LOCATION = "src/images/map_usa.png";

    // graphics
    private static JFrame f;
    private static JLayeredPane layers = new JLayeredPane();
    private static JLabel background;
    private static int[][] pixels = new int[10005][4330];
    private static final int APPLICATION_WIDTH = 1388;
    private static final int APPLICATION_HEIGHT = 743;
    private static final String TITLE = "KruskalView.java";

    // graph and solution list
    private static GraphE graph;
    private static LinkedList<Edge> kruskal = new LinkedList<>();

    // display
    private static boolean display = true;

    // main
    public static void main(String[] args) {
        // instructions
        System.out.println("Kevin's KruskalView.java");
        System.out.println("- Displays Kruskal's algorithm for shortest spanning tree on usa.txt");
        System.out.println("- Note that the graphics for this class takes an extremely long time to load/process (>45 seconds)");
        loadGraph();
        SwingUtilities.invokeLater(KruskalView::start);
    }

    // graphics settings
    private KruskalView() {
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
        f.add(new KruskalView(), BorderLayout.CENTER);
        f.pack();
        f.setVisible(true);
        f.setLocationRelativeTo(null);
    }

    // initializes and creates all JObjects
    private void initializeJObjects() {
        layers.setPreferredSize(new Dimension(APPLICATION_WIDTH, APPLICATION_HEIGHT));

        // background image
        background = new JLabel() {
            @Override
            protected void paintComponent(Graphics g) {
                if (display) {
                super.paintComponent(g);
                    g.setColor(Color.RED);
                    for (int i = 0; i < kruskal.size(); i++)
                        g.drawLine(getXPixelCoordinate(graph.getVertex(kruskal.get(i).source).location.x), getYPixelCoordinate(graph.getVertex(kruskal.get(i).source).location.y), getXPixelCoordinate(graph.getVertex(kruskal.get(i).destination).location.x), getYPixelCoordinate(graph.getVertex(kruskal.get(i).destination).location.y));
                    display = false;
                }
            }
        };
        background.setBounds(0, 0, 1388, 743);
        background.setIcon(new ImageIcon(IMAGE_LOCATION));
        layers.add(background);

        // adding in everything
        f.getContentPane().add(layers);
        f.pack();
    }

    // calculates weight
    private static double calculateWeight(Location l1, Location l2) {
        return Math.sqrt(Math.pow(l2.x - l1.x, 2) + Math.pow(l2.y - l1.y, 2));
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
            graph = new GraphE(vertices, edges);

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
                graph.addAdjacent(s, d, calculateWeight(graph.getVertex(s).location, graph.getVertex(d).location));
            }
            kruskal = graph.kruskal();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // returns x pixel conversion of graph coordinate
    private static int getXPixelCoordinate(int x) {
        return 35 + (int) ((double) x / 10250 * 1388);
    }

    // returns y pixel conversion of graph coordinate
    private static int getYPixelCoordinate(int y) {
        return 750 - (int) ((double) y / 4400 * 743);
    }
}