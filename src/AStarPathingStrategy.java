import java.util.*;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;


class AStarPathingStrategy
        implements PathingStrategy
{




    public List<Point> computePath(Point start, Point end,
                                   Predicate<Point> canPassThrough,
                                   BiPredicate<Point, Point> withinReach,
                                   Function<Point, Stream<Point>> potentialNeighbors)
    {
        Comparator<Node> comp = Comparator.comparingDouble(Node::getF);
        List<Point> path = new LinkedList<Point>();
        Queue<Node> openList = new PriorityQueue<>(comp);
        HashSet<Node> closedList = new HashSet<>();
        openList.add(new Node(start, null, end));
        Node finalN = null;


        whileLoop:
        while(openList.size() > 0) {
            Node current = openList.poll();
            List<Point> neighbors = potentialNeighbors.apply(current.getPos()).filter(canPassThrough).filter(pt -> !pt.equals(start)).collect(Collectors.toList());
            for(Point p: neighbors) {
                Node temp = new Node(p, current, end);
                if(withinReach.test(p, end)) {
                    finalN = temp;
                    break whileLoop;
                }
                if (!closedList.contains(temp)) {
                    if (openList.removeIf(node -> node.equals(temp) && temp.getG() < node.getG())) {
                        openList.add(temp);
                        continue;
                    }
                    if(!openList.contains(temp))
                        openList.add(temp);


                }
            }
            closedList.add(current);
        }


        if(finalN == null) return null;
        while(!finalN.getPos().equals(start)) {
            path.add(0, finalN.getPos());
            finalN = finalN.getPrior();
        }
        return path;
    }
}
