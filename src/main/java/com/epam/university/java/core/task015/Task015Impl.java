package com.epam.university.java.core.task015;

import java.util.LinkedList;
import java.util.List;

public class Task015Impl implements Task015 {
    public static final PointFactory pointFactory = new PointFactoryImpl();

    @Override
    public double getArea(Square first, Square second) {
        List<Point> intersectionPointsList = new LinkedList<>();
        List<Point> firstSquarePoints = getAllPointsOfTheSquare(first);
        List<Point> secondSquarePoints = getAllPointsOfTheSquare(second);
        for (Point point : firstSquarePoints) {
            if (isPointInsideThePolygon(secondSquarePoints, point)) {
                intersectionPointsList.add(point);
            }
        }
        for (Point point : secondSquarePoints) {
            if (isPointInsideThePolygon(firstSquarePoints, point)) {
                intersectionPointsList.add(point);
            }
        }

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                Point startFirst = firstSquarePoints.get(i);
                Point endFirst = firstSquarePoints.get((i + 1) % 4);
                Point startSecond = secondSquarePoints.get(j);
                Point endSecond = secondSquarePoints.get((j + 1) % 4);
                Point intersection = getIntersection(startFirst, endFirst, startSecond, endSecond);
                if (intersection != null) {
                    intersectionPointsList.add(intersection);
                }
            }
        }
        List<Point> points = vertSort(intersectionPointsList);
        double result = 0;
        for (int i = 1; i < points.size() - 1; i++) {
            result += getTriangleArea(points.get(0),
                    points.get(i),
                    points.get(i + 1));
        }
        return result;
    }

    /**
     * Method for getting all four vertexes of square.
     *
     * @param square is the given square with two opposite points.
     * @return list of all points of the square.
     */
    public List<Point> getAllPointsOfTheSquare(Square square) {
        List<Point> listOfPoints = new LinkedList<>();
        double x1 = square.getFirst().getX();
        double y1 = square.getFirst().getY();
        double x3 = square.getSecond().getX();
        double y3 = square.getSecond().getY();
        Point vertexA = square.getFirst();
        Point vertexB = pointFactory.newInstance((x1 + x3 + y1 - y3) / 2, (x3 - x1 + y1 + y3) / 2);
        Point vertexC = square.getSecond();
        Point vertexD = pointFactory.newInstance((x1 + x3 + y3 - y1) / 2, (x1 - x3 + y1 + y3) / 2);
        listOfPoints.add(vertexA);
        listOfPoints.add(vertexB);
        listOfPoints.add(vertexC);
        listOfPoints.add(vertexD);
        return listOfPoints;
    }

    /**
     * Get the point of intersection of two line segments.
     *
     * @param startFirst  is the first point of first segment.
     * @param endFirst    is the second point of first segment.
     * @param startSecond is the first point of second segment.
     * @param endSecond   is the second point of second segment.
     * @return point of intersection.
     */
    public static Point getIntersection(Point startFirst,
                                        Point endFirst,
                                        Point startSecond,
                                        Point endSecond) {

        Point firstVector = pointFactory
                .newInstance(endFirst.getX() - startFirst.getX(),
                        endFirst.getY() - startFirst.getY());
        Point secondVector = pointFactory
                .newInstance(endSecond.getX() - startSecond.getX(),
                        endSecond.getY() - startSecond.getY());

        //check if lines are parallel
        if (vectorsMultiplication(firstVector, secondVector) == 0) {
            return null;
        }
        double resultX;
        double resultY;
        double x1 = startFirst.getX();
        double y1 = startFirst.getY();
        double x2 = endFirst.getX();
        double y2 = endFirst.getY();
        double x3 = startSecond.getX();
        double y3 = startSecond.getY();
        double x4 = endSecond.getX();
        double y4 = endSecond.getY();

        // get lines equations
        double a1 = (y2 - y1) / (x2 - x1);
        double b1 = y1 - a1 * x1;
        double a2 = (y4 - y3) / (x4 - x3);
        double b2 = y3 - a2 * x3;
        if (firstVector.getX() == 0) {
            resultX = x1;
            resultY = a2 * x1 + b2;
        } else if (secondVector.getX() == 0) {
            resultX = x3;
            resultY = a1 * x3 + b1;
        } else {
            resultX = (b2 - b1) / (a1 - a2);
            resultY = resultX * a2 + b2;
        }
        Point intersection = pointFactory.newInstance(resultX, resultY);

        if (bothSegmentsContainsIntersection(resultX, resultY, x1, y1, x2, y2, x3, y3, x4, y4)) {
            return intersection;
        }
        return null;
    }

    private static boolean bothSegmentsContainsIntersection(double resultX,
                                                            double resultY,
                                                            double x1, double y1,
                                                            double x2, double y2,
                                                            double x3, double y3,
                                                            double x4, double y4) {
        return (resultX >= Math.min(x1, x2) && resultX <= Math.max(x1, x2)
                && resultX >= Math.min(x3, x4) && resultX <= Math.max(x3, x4)
                && resultY >= Math.min(y1, y2) && resultY <= Math.max(y1, y2)
                && resultY >= Math.min(y3, y4) && resultY <= Math.max(y3, y4));
    }

    /**
     * Method for multiply the vectors.
     *
     * @param first  is the first Point.
     * @param second is the second Point.
     * @return result of the multiplication.
     */
    public static double vectorsMultiplication(Point first, Point second) {
        return first.getX() * second.getY() - second.getX() * first.getY();
    }

    /**
     * Method to check whether a point is inside or outside.
     * 1) Draw a horizontal line to the right of each point and extend it to infinity
     * 2) Count the number of times the line intersects with polygon edges.
     * 3) A point is inside the polygon if either count of intersections is odd .
     *
     * @param polygon is the given polygon to check.
     * @param point   is the given point to check.
     * @return true if counter is odd, false otherwise.
     */
    public boolean isPointInsideThePolygon(List<Point> polygon, Point point) {
        // Define Infinite (Using INT_MAX
        // caused overflow problems.)
        int inf = 10000;
        Point extreme = new PointImpl(inf, point.getY());
        int count = 0;
        int i = 0;
        do {
            int next = (i + 1) % polygon.size();
            if ((polygon.get(i).getY() > point.getY())
                    != (polygon.get(next).getY() > point.getY())) {
                if ((getIntersection(polygon.get(i), polygon.get(next), point, extreme)) != null) {
                    count++;
                }
            }
            i = next;

        } while (i != 0);
        return (count % 2 == 1);
    }

    /**
     * Calculate the area of the triangle.
     *
     * @param p1 is the first point.
     * @param p2 is the second point.
     * @param p3 is the third point.
     * @return area.
     */
    public static double getTriangleArea(Point p1, Point p2, Point p3) {
        double a = getSideLength(p1, p2);
        double b = getSideLength(p2, p3);
        double c = getSideLength(p3, p1);
        double sp = (a + b + c) / 2;
        double area = Math.sqrt(sp * (sp - a) * (sp - b) * (sp - c));
        return area;
    }

    /**
     * Get the length of the triangle side.
     *
     * @param first  is the first point.
     * @param second is the second point.
     * @return distance.
     */
    public static double getSideLength(Point first, Point second) {
        double deltaX = first.getX() - second.getX();
        double deltaY = first.getY() - second.getY();
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    /**
     * Find center vertex.
     *
     * @param vertexes list of vertexes
     * @return center vertex
     */
    public Point getCenter(List<Point> vertexes) {
        double x = 0.0;
        double y = 0.0;
        for (Point vert : vertexes) {
            x += vert.getX();
            y += vert.getY();
        }
        return new PointImpl(x / vertexes.size(), y / vertexes.size());
    }

    /**
     * Sort vertices clockwise.
     *
     * @param vertexes list of vertexes
     * @return sorted list of vertexes
     */
    public List<Point> vertSort(List<Point> vertexes) {
        Point center = getCenter(vertexes);
        vertexes.sort((a, b) -> {
            double a1 =
                    (Math.toDegrees(Math.atan2(a.getX() - center.getX(), a.getY() - center.getY()))
                            + 360) % 360;
            double a2 =
                    (Math.toDegrees(Math.atan2(b.getX() - center.getX(), b.getY() - center.getY()))
                            + 360) % 360;
            return (int) (a1 - a2);
        });
        return vertexes;
    }
}
