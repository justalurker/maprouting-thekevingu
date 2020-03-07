## Kevin's PathFinder Project

**Assignment:** Write a program to find the shortest path using Dijkstra's Algorithm from any one point to any other point in the graph network described in the file usa.txt.

**Objectives**: We are to determine what the data in usa.txt means and how we will turn it into a graph. After learning Dijkstra's Algorithm, we code the relevant data structures and create a console test client and a Swing app that finds the shortest path between two points.

## Project Files

**Project specific files:** `DijkstraClient`, `KruskalClient`, `DAView`, `USAView`, `KruskalView`

**Relevant data structures:** `GraphV,` `GraphE`, `MinPQ`, `Vertex`, `Edge`, `Node`, `UnionFind`

**Text files:** `usa.txt`, `da.txt`

**Others:** `LinkedList`, `Location`, `Queue`, `Stack`

I followed the basic requirements outlined in the Canvas website, and decided to pursue some of the things that came to mind/were interesting while I was programming. I implemented Kruskal's Algorithm for minimum spanning trees and used it with `usa.txt`. For all my programs, I created a very functional and minimalistic GUI.

Additionally, I created my own file, `da.txt`, creating a graph network for Deerfield Academy. I thought it would be interesting seeing the project apply to something concrete in real-life.

## Dijkstra's Algorithm

**High level overview:** Dijkstra's Algorithm is a breadth first search widely used to find the shortest path in an edge/vertex-weighted graph. It finds the shortest path from an initial given vertex to every other vertex, including the destination. The problem has a dynamic programming strategy, determining the shortest path to a vertex and checking all outgoing edges/vertices to see whether there is a shorter path to that vertex. We implemented this program using a minimum priority queue.

For my implementation choice, I decided to use a vertex-weighted graph. Note that I calculate the weight with the pythagorean equation. The following photo represents Dijkstra's Algorithm in action:

<img src="https://i.imgur.com/QeVtLec.jpg" alt="View.java"
	title="View.java" width="320" height="272" />
	
**Psuedocode:**

```
1. Set all nodes as unvisited and the distance to infinity.
2. Set the distance of initial node as 0.
3. Add all nodes and distances into the minimum priority queue.
3. For the current node, look at the unvisited neighbors and calculate their distances.
4. Compare the distance with the previuos minimum, and update the minimum value if it is smaller.
5. Update the priority queue as needed.
6. Mark current node as visited
7. Keep repeating steps 3-6 until minimum priority queue is empty or there are no connections left.
```

## USA.txt

**What the file contains:** `usa.txt` lists the number of vertices and edges in its first line, then lists the vertices by index then its x and y coordinates, followed by the edges.

Note that the x and y coordinates are most likely abstract, as the U.S.' horizontal distance is 4313 km, which is significantly less than the largest x coordinate of 10,000.

```
6 7
0  1000 2400
1  2800 3000
2  2400 2500
3  4000    0
4  4500 3800
5  6000 1500
0 1
0 3
1 2
1 4
2 4
2 3
2 5
```

**Interpretation:**

*First line:* The graph contains 6 vertices and 7 edges.

*Lines 2-7:* These are the vertices, listed by the vertex number and the x and y coordinates.

*Afterwards:* These are the edges, listed by the source and destination vertices.

The vertices listed in `usa.txt` are coordinates representing the road intersections in the U.S. So, by using Dijkstra's Algorithm, I can find the shortest path using roads between two points.

## Overview of Data Structures

My `Vertex` class contains an index, a location, and a list of adjacent vertex indices and is used by the `GraphV` class. The graph class contains an array of vertices.

Given the nature of this problem and the algorithm I need to use, I need to create a graph class for Dijkstra's algorithm to use. The high level structure is as such: after I add in the vertices in my vertex array, I use the edge connections from the text file to add adjacent vertex indices to each connected vertex.

The minimum priority queue is implemented with a heap via array. Alex, Elven, and I coded this last project. I use it to retrieve the minimum weighted vertices in Dijkstra's Algorithm.

For Kruskal's Algorithm, I created a separate graph class called `GraphE` which is implemented with edge weights. I made this choice because Kruskal's Algorithm naturally needs to find the minimum weighted edge each loop, so it would be much simpler to implemented it with edges rather than vertices.

Note that the `Node` class is simply used in the minimum priority queue. I implemented it with a comparable so that the priority queue can sort itself by weight.

## MinPQ

A priority queue is an abstract data type. We implemented it with heaps in an array. The following is the API for `MinPQ.java`.

```
size()			// returns size of PQ
isEmpty()		// whether PQ is empty
min()			// get minimum in queue
insert()		// insert new value
delMin()		// delete minimum
```

For my insert function, I made several decisions in implementing the PQ. Instead of replacing existing keys within the PQ, what I did was insert a new value every time with the lowered minimum. In Dijkstra's Algorithm, I had an array tracking whether each vertex visited to avoid repeats after the lowest had been popped off and there were duplicates. I found this method to be much more efficient time-wise.

## USAView

This class if the GUI visualization for the Dijkstra's Algorithm for usa.txt. The two markers represent the source (blue) and destination (green). Please drag them accordingly to find the paths between points.

The textfield toward the bottom left is for selecting a certain vertex. You do this by clicking on the marker you want to set and entering a valid vertex number.

The texarea on the bottom left shows the path taken to get to that location as well as their corresponding distances.

An interesting problem that I noticed with the GUI initially was that before, I had to click pixel perfect to obtain a vertex. However, I made this process easier by creating a BFS to expand radially outward in an array representing the various locations in the graph to get closest vertex until a certain point. This way, I can still have innacuracies in clicking while being able to select the point I'm trying to select.

<img src="https://i.imgur.com/hKwesBp.png" alt="View.java"
	title="View.java" width="750" height="438" />

## DAView

DAView uses the same basic GUI visualization tools. To expand on this, I actually created another Swing document so that I could click individually the intersections I wanted to put in the graph and generate the vertices and edge connections accordingly. I thought it would be interesting to see a real-world application of Dijkstra's Algorithm.

<img src="https://i.imgur.com/KK6BRnN.png" alt="View.java"
	title="View.java" width="481" height="617" />

## KruskalView

Please note that the graphics for this program takes an extremely long time to load. It may take over a minute or so—drawing and calculating 85,000 or so points is a pretty difficult task.

As noted above, I created a different graph class for this problem to facilitate code-writing. Kruskal's Algorithm uses the edges in the priority queue, so I created `GraphE` for this reason. This program shows  the minimum spanning tree for `usa.txt`.

Note that I used my UnionFind classes from the previous assignment to check whether the edges formed cycles.

Kruskal's Algorithm is quite intuitive. Here is the psuedocode for it:

```
1. Create a set of all the edges in the graph
2. Remove the lowest weighted edge from the priority queue
3. Add the edge if it does not create a cycle
4. Repeat steps 2 and 4 until size is satisfied

```

<img src="https://i.imgur.com/vg0f2d3.png" alt="View.java"
	title="View.java" width="750" height="438" />

## Future Work

I can further optimize Dijkstra's Algorithm by perhaps incorporating a modified best-first search. Instead of only using the weight as the distance from the origin, I can include a metric (or heuristic) calculating how close each vertex is from the destination node as well, which might improve performance.

## Sources
Canvas, Medium, Wikipedia.
