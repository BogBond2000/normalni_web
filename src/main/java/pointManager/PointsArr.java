package pointManager;

import java.util.ArrayList;
import java.util.List;

public class PointsArr
{
    private final List<PointManager> points = new ArrayList<>();

    public void addPoint(PointManager point)
    {
        points.add(point);
    }

    public List<PointManager> getPoints() {
        return points;
    }
}