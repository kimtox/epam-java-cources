package com.epam.university.java.core.task021;

import com.epam.university.java.core.task015.Point;
import com.epam.university.java.core.task015.PointImpl;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Collection;

public class Task021Impl implements Task021 {
    @Override
    public Point calculate(Collection<Point> minePositions) {
        if (minePositions == null || minePositions.size() == 0) {
            throw new IllegalArgumentException();
        }
        ArrayList<Point> listOfVertices = new ArrayList<>(minePositions);

        PointImpl pointA = (PointImpl) listOfVertices.get(0);
        PointImpl pointB = (PointImpl) listOfVertices.get(1);
        PointImpl pointC = (PointImpl) listOfVertices.get(2);

        double angle120 = 2 * Math.PI / 3;

        double angleA = getAngle(pointA, pointC, pointB);
        if (angleA >= angle120) {
            return pointA;
        }
        double angleB = getAngle(pointB, pointA, pointC);

        if (angleB >= angle120) {
            return pointB;
        }

        double angleC = getAngle(pointC, pointA, pointB);
        if (angleC >= angle120) {
            return pointC;
        }

        BigDecimal ax = BigDecimal.valueOf(listOfVertices.get(0).getX());
        BigDecimal ay = BigDecimal.valueOf(listOfVertices.get(0).getY());
        BigDecimal bx = BigDecimal.valueOf(listOfVertices.get(1).getX());
        BigDecimal by = BigDecimal.valueOf(listOfVertices.get(1).getY());
        BigDecimal cx = BigDecimal.valueOf(listOfVertices.get(2).getX());
        BigDecimal cy = BigDecimal.valueOf(listOfVertices.get(2).getY());

        BigDecimal firstX = cx.add(bx).add(cy.subtract(by)
                .multiply(BigDecimal.valueOf(StrictMath.sqrt(3))))
                .divide(new BigDecimal("2.0"), 20, RoundingMode.HALF_UP);

        BigDecimal firstY = cy.add(by).add(bx.subtract(cx)
                .multiply(BigDecimal.valueOf(StrictMath.sqrt(3))))
                .divide(new BigDecimal("2.0"), 20, RoundingMode.HALF_UP);


        BigDecimal secondX = ax.add(bx).add(ay.subtract(by)
                .multiply(BigDecimal.valueOf(StrictMath.sqrt(3))))
                .divide(new BigDecimal("2.0"), 20, RoundingMode.HALF_UP);

        BigDecimal secondY = ay.add(by).add(bx.subtract(ax)
                .multiply(BigDecimal.valueOf(StrictMath.sqrt(3))))
                .divide(new BigDecimal("2.0"), 20, RoundingMode.HALF_UP);


        BigDecimal a1 = cy.subtract(secondY);

        BigDecimal b1 = secondX.subtract(cx);

        BigDecimal a2 = ay.subtract(firstY);

        BigDecimal b2 = firstX.subtract(ax);


        BigDecimal d = a1.multiply(b2).subtract(a2.multiply(b1));

        BigDecimal c1 = secondY.multiply(cx).subtract(secondX.multiply(cy));

        BigDecimal c2 = firstY.multiply(ax).subtract(firstX.multiply(ay));


        BigDecimal resultX = b1.multiply(c2).subtract(b2.multiply(c1));
        resultX = resultX.divide(d, 15, RoundingMode.HALF_UP);

        BigDecimal resultY = a2.multiply(c1).subtract(a1.multiply(c2));
        resultY = resultY.divide(d, 15, RoundingMode.HALF_UP);

        return new PointImpl(resultX.doubleValue(), resultY.doubleValue());
    }

    private double getSideLength(Point first, Point second) {
        double deltaX = second.getX() - first.getX();
        double deltaY = second.getY() - first.getY();
        return Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));
    }

    private double getAngle(Point center, Point first, Point second) {
        double lineA = getSideLength(center, first);
        double lineB = getSideLength(center, second);
        double lineC = getSideLength(first, second);

        return Math.acos(
                (Math.pow(lineA, 2) + Math.pow(lineB, 2) - Math.pow(lineC, 2))
                        / (2 * lineA * lineB));
    }
}
