public class Node {
    private final Point pos;
    private Node prior;
    private int g;
    private int h;
    private double f;

    public Node(Point pos, Node prior, Point target) {
        this.pos = pos;
        this.prior = prior;

        if(prior == null)
            this.g = 0;
        else
            this.g = prior.getG() + 1;

        this.h = Math.abs(pos.x - target.x) + Math.abs(pos.y - target.y);
        this.f = g + h;
    }
    public Point getPos() {return pos;}

    public Node getPrior() {return prior;}

    public int getG() {return g;}

    public double getF() {return f;}

    @Override
    public String toString() {
        return "(" + this.pos.getX() + "," + this.pos.getY() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Node node = (Node) o;

        return pos.equals(node.pos);
    }

    @Override
    public int hashCode() {
        return pos.hashCode();
    }
}
