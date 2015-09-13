import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.nio.FloatBuffer;

/**
 * A rasterizer for square grids of points.
 * 
 * @author gb21
 */
public class GridRasterizer
{
    /**
     * The flat shading model.
     */
    public static final int FLAT = 1;

    /**
     * The gouraud shading model.
     */
    public static final int GOURAUD = 2;

    /**
     * The parallel projection.
     */
    public static final int PARALLEL = 0;

    /**
     * The perspective projection.
     */
    public static final int PERSPECTIVE = 1;

    /**
     * The wire frame shading model.
     */
    public static final int WIRE_FRAME = 0;

    /**
     * The didtance to the view plane from the center of projection (when using perspective projection.)
     */
    private float copDistance = 1000.0f;

    /**
     * The size of the graphics object to draw grids onto.
     */
    private Dimension graphicsObjectSize = null;

    /**
     * The matrix to multiply points by when using parallel projection.
     */
    private Matrixf44 parallelMatrix = new Matrixf44();

    /**
     * The matrix to multiply points by when using perspective projection.
     */
    private Matrixf44 perspectiveMatrix = new Matrixf44();

    /**
     * The mode of projection to be used when rasterizing.
     */
    private int projectionMode = PERSPECTIVE;

    /**
     * The size of the view plane.
     */
    private float viewPlaneSize = 1.0f;

    /**
     * A buffer containing the z values corresponding to all the drawn pixels.
     */
    private float[][] zBuffer;

    /**
     * Creates an instance of GridRasterizer.
     */
    public GridRasterizer()
    {
        graphicsObjectSize = new Dimension();
        zBuffer = new float[graphicsObjectSize.width][graphicsObjectSize.height];

        // Initialize perspective projection matrix. (view plane at z = 0, COP at z = -COPDistance)
        perspectiveMatrix.getBuffer().put(14, 1.0f / copDistance);
        perspectiveMatrix.getBuffer().put(10, 0.0f);

        // Initialize parallel projection matrix.
        parallelMatrix.getBuffer().put(10, 0.0f);
    }

    /**
     * Creates an instance of GridRasterizer.
     * 
     * @param graphicsObjectSize The size of the graphics object to draw grids onto.
     */
    public GridRasterizer(Dimension graphicsObjectSize)
    {
        this.graphicsObjectSize = graphicsObjectSize;
        zBuffer = new float[graphicsObjectSize.width][graphicsObjectSize.height];

        // Initialize perspective projection matrix. (view plane at z = 0, COP at z = -COPDistance)
        perspectiveMatrix.getBuffer().put(14, 1.0f / copDistance);
        perspectiveMatrix.getBuffer().put(10, 0.0f);

        // Initialize parallel projection matrix.
        parallelMatrix.getBuffer().put(10, 0.0f);
    }

    /**
     * Draws a grid using flat shading.
     * 
     * Uses the z buffer algorithm.
     * 
     * @param grid The grid to draw.
     * @param g2d The graphics object to draw the grid to.
     */
    private void drawFlat(Vectorf4[] grid, Graphics2D g2d)
    {
        g2d.setStroke(new BasicStroke(1));

        // Clear the z buffer.
        for (int xIndex = 0; xIndex < zBuffer.length; xIndex++)
        {
            for (int yIndex = 0; yIndex < zBuffer[xIndex].length; yIndex++)
            {
                zBuffer[xIndex][yIndex] = Float.MAX_VALUE;
            }
        }

        int count = 1;
        int sqrtTotalPoints = (int) Math.round(Math.sqrt(grid.length));

        for (int index = 0; index < grid.length - sqrtTotalPoints; index++)
        {
            // If there is a point to the right and below (relative to matrix entries).
            if (count != sqrtTotalPoints)
            {
                Vectorf4 across = Vectorf4.subtract(grid[index + 1], grid[index]);
                across.scale(0.5f);
                Vectorf4 up = Vectorf4.subtract(grid[index], grid[index + sqrtTotalPoints]);
                up.scale(0.5f);

                Vectorf4 center = Vectorf4.add(grid[index + sqrtTotalPoints], across);
                center = Vectorf4.add(center, up);

                Vectorf4 normal = Vectorf4.crossProduct(across, up);

                center.normalize();
                normal.normalize();
                
                // The colour is set to the length of the difference between the center and normal vectors / 2.
                // It is divided by 2 because this length could be a maximum of 2 and the colour value must be a
                // maximum value of 1. The shortest or longest lengths represent small angles of reflection and so
                // they create the most light. This is acheived by way of the max() function below.
                float red = Vectorf4.subtract(center, normal).getLength() / 2.0f;
                g2d.setColor(new Color(Math.min(Math.max(red, 1.0f - red), 1.0f), 0.0f, 0.0f));

                drawTriangle(new Vectorf4[] {grid[index], grid[index + 1], grid[index + sqrtTotalPoints + 1]}, g2d);
                drawTriangle(new Vectorf4[] {grid[index], grid[index + sqrtTotalPoints],
                        grid[index + sqrtTotalPoints + 1]}, g2d);

                count++;
            }
            else
            {
                count = 1;
            }
        }
    }

    /**
     * Draws the given grid in the given shading model.
     * 
     * @param grid The grid to draw.
     * @param g2d The graphics object to draw the grid to.
     * @param shadingModel The shading model to use.
     */
    public void drawGrid(Vectorf4[] grid, Graphics2D g2d, int shadingModel)
    {
        if (shadingModel == WIRE_FRAME)
        {
            drawWireFrame(grid, g2d);
        }
        else if (shadingModel == FLAT)
        {
            drawFlat(grid, g2d);
        }
        else if (shadingModel == GOURAUD)
        {
            //drawGouraud(grid, g2d);
        }
    }

    /**
     * Draws the given grid with a circle around the given selected point.
     * 
     * @param grid The grid to draw.
     * @param g2d The graphics object to draw the grid to.
     * @param selectedPoint The point to circle.
     */
    public void drawGridWithSelection(Vectorf4[] grid, Graphics2D g2d, int selectedPoint)
    {
        drawWireFrame(grid, g2d);

        if (selectedPoint >= 0 && selectedPoint < grid.length)
        {
            Vectorf4 projectedPoint = projectPoint(grid[selectedPoint]);

            g2d.setStroke(new BasicStroke(1));
            g2d.setColor(Color.white);

            g2d.drawOval((int) Math.round(projectedPoint.getBuffer().get(0)) - 4, (int) Math.round(projectedPoint
                    .getBuffer().get(1)) - 4, 8, 8);
        }
    }

    /**
     * Draws a line from point <code>a</code> to point <code>b</code> on the graphics object <code>g2d</code>.
     * 
     * @param a The first point.
     * @param b The second point.
     * @param g2d The graphics object to draw the line to.
     */
    private void drawLine(Vectorf4 a, Vectorf4 b, Graphics2D g2d)
    {
        if (a == null || b == null)
        {
            return;
        }

        FloatBuffer bufA = a.getBuffer();
        FloatBuffer bufB = b.getBuffer();

        g2d.drawLine(Math.round(bufA.get(0)), Math.round(bufA.get(1)), Math.round(bufB.get(0)), Math.round(bufB
                .get(1)));
    }

    /**
     * Draws a triangle within the area defined by the points <code>triangle</code> on the graphics object
     * <code>g2d</code>.
     * 
     * @param triangle The points that define the triangle.
     * @param g2d The graphics object to draw the triangle to.
     */
    private void drawTriangle(Vectorf4[] triangle, Graphics2D g2d)
    {
        Vectorf4[] projectedTriangle = getProjectedPoints(triangle);
        reorderTriangle(projectedTriangle, triangle);

        int yMax = (int) Math.round(projectedTriangle[0].getBuffer().get(1));
        int yMin = (int) Math.round(projectedTriangle[2].getBuffer().get(1));

        int yMax2 = (int) Math.round(triangle[0].getBuffer().get(1));
        int yMin2 = (int) Math.round(triangle[2].getBuffer().get(1));

        for (int y = yMax; y >= yMin; y--)
        {
            int x0 = getDualEdgeIntersect(y, 'x', projectedTriangle[0], projectedTriangle[1], projectedTriangle[2]);
            int x1 = getEdgeIntersect(y, 'x', projectedTriangle[0], projectedTriangle[2]);

            int y2 = (int) Math.round(yMax2 - (yMax2 - yMin2) * ((float) (yMax - y) / (float) (yMax - yMin)));

            int z0 = getDualEdgeIntersect(y2, 'z', triangle[0], triangle[1], triangle[2]);
            int z1 = getEdgeIntersect(y2, 'z', triangle[0], triangle[2]);

            // If one of the edges was horizontal at this value of y, set the intersection points to be its end
            // points.
            if (x0 == -1)
            {
                x0 = (int) Math.round(projectedTriangle[0].getBuffer().get(0));
                x1 = (int) Math.round(projectedTriangle[1].getBuffer().get(0));

                z0 = (int) Math.round(triangle[0].getBuffer().get(2));
                z1 = (int) Math.round(triangle[1].getBuffer().get(2));
            }
            else if (x0 == -2)
            {
                x0 = (int) Math.round(projectedTriangle[1].getBuffer().get(0));
                x1 = (int) Math.round(projectedTriangle[2].getBuffer().get(0));

                z0 = (int) Math.round(triangle[1].getBuffer().get(2));
                z1 = (int) Math.round(triangle[2].getBuffer().get(2));
            }
            else if (x1 == -1)
            {
                x0 = (int) Math.round(projectedTriangle[0].getBuffer().get(0));
                x1 = (int) Math.round(projectedTriangle[2].getBuffer().get(0));

                z0 = (int) Math.round(triangle[0].getBuffer().get(2));
                z1 = (int) Math.round(triangle[2].getBuffer().get(2));
            }

            // Ensure x0 is greater than x1.
            if (x0 < x1)
            {
                int temp = x0;
                x0 = x1;
                x1 = temp;

                temp = z0;
                z0 = z1;
                z1 = temp;
            }

            for (int x = x0; x >= x1; x--)
            {
                // Do not attempt to draw pixels outside of the graphics object.
                if (x >= 0 && x < graphicsObjectSize.width && y >= 0 && y < graphicsObjectSize.height)
                {
                    // Interpolate z.
                    float z;
                    if (z0 < z1)
                    {
                        z = z0 - (z0 - z1) * (1.0f - ((float) (x0 - x) / (float) (x0 - x1)));
                    }
                    else
                    {
                        z = z0 - (z0 - z1) * ((float) (x0 - x) / (float) (x0 - x1));
                    }

                    if (z < zBuffer[x][y])
                    {
                        zBuffer[x][y] = z;

                        g2d.drawLine(x, y, x, y);
                    }
                }
            }
        }
    }

    /**
     * Draws a grid in wire frame.
     * 
     * @param grid The grid to draw.
     * @param g2d The graphics object to draw the grid to.
     */
    private void drawWireFrame(Vectorf4[] grid, Graphics2D g2d)
    {
        g2d.setStroke(new BasicStroke(1));
        g2d.setColor(Color.white);

        int count = 1;
        Vectorf4[] projectedGrid = getProjectedPoints(grid);
        int sqrtTotalPoints = (int) Math.round(Math.sqrt(grid.length));

        for (int index = 0; index < grid.length; index++)
        {
            // Horizontal line. (relative to matrix entries.)
            if (count != sqrtTotalPoints)
            {
                drawLine(projectedGrid[index], projectedGrid[index + 1], g2d);
                count++;
            }
            else
            {
                count = 1;
            }

            // Vertical line. (relative to matrix entries.)
            if (index < grid.length - sqrtTotalPoints)
            {
                drawLine(projectedGrid[index], projectedGrid[index + sqrtTotalPoints], g2d);
            }
        }
    }

    /**
     * Returns the distance between the center of projection and the view plane.
     * 
     * @return The distance between the center of projection and the view plane.
     */
    public float getCOPDistance()
    {
        return (copDistance);
    }

    /**
     * Returns the point on <code>axis</code> where the line from <code>top</code> to <code>middle</code> or the line
     * from <code>middle</code> to <code>bottom</code> intersects the plane y = <code>y</code>.
     * 
     * @param y The value of y that represents the plane the line is intersecting.
     * @param axis The axis of the point of intersection to return.
     * @param top The point on the upper line with the maximum y value.
     * @param middle The intermediate point on both lines.
     * @param bottom The point on the lower line with the minimum y value.
     * 
     * @return The point on <code>axis</code> where the line from <code>top</code> to <code>middle</code> or the line
     * from <code>middle</code> to <code>bottom</code> intersects the plane y = <code>y</code>.
     */
    private int getDualEdgeIntersect(int y, char axis, Vectorf4 top, Vectorf4 middle, Vectorf4 bottom)
    {
        if (y > (int) Math.round(middle.getBuffer().get(1)))
        {
            return (getEdgeIntersect(y, axis, top, middle));
        }
        else
        {
            int intY = getEdgeIntersect(y, axis, middle, bottom);

            if (intY == -1)
            {
                return (-2);
            }
            else
            {
                return (intY);
            }
        }
    }

    /**
     * Returns the point on <code>axis</code> where the line from <code>top</code> to <code>bottom</code> intersects
     * the plane y = <code>y</code>.
     * 
     * @param y The value of y that represents the plane the line is intersecting.
     * @param axis The axis of the point of intersection to return.
     * @param top The point on the line with the maximum y value.
     * @param bottom The point on the line with the minimum y value.
     * 
     * @return The point on <code>axis</code> where the line from <code>top</code> to <code>bottom</code> intersects
     * the plane y = <code>y</code>.
     */
    private int getEdgeIntersect(int y, char axis, Vectorf4 top, Vectorf4 bottom)
    {
        int index = -1;
        if (axis == 'x')
        {
            index = 0;
        }
        else if (axis == 'z')
        {
            index = 2;
        }

        int topAxis = (int) Math.round(top.getBuffer().get(index));
        int topY = (int) Math.round(top.getBuffer().get(1));
        int bottomAxis = (int) Math.round(bottom.getBuffer().get(index));
        int bottomY = (int) Math.round(bottom.getBuffer().get(1));

        if (topY == bottomY)
        {
            return (-1);
        }

        return ((int) Math.round(topAxis - (topAxis - bottomAxis) * ((float) (topY - y) / (float) (topY - bottomY))));
    }

    /**
     * Returns the distance to offset points when placing them on the graphics object.
     * 
     * @return The distance to offset points when placing them on the graphics object.
     */
    public Dimension getGraphicsObjectSize()
    {
        return (graphicsObjectSize);
    }

    /**
     * Returns the projected points of the given points.
     * 
     * @param grid The points to project.
     * 
     * @return The projected points of the given points.
     */
    public Vectorf4[] getProjectedPoints(Vectorf4[] points)
    {
        Vectorf4[] projectedPoints = new Vectorf4[points.length];

        for (int index = 0; index < points.length; index++)
        {
            projectedPoints[index] = projectPoint(points[index]);
        }

        return (projectedPoints);
    }

    /**
     * Returns the mode of projection to be used when rasterizing.
     * 
     * @return The mode of projection to be used when rasterizing.
     */
    public int getProjectionMode()
    {
        return (projectionMode);
    }

    /**
     * Returns the size of the view plane.
     * 
     * @return The size of the view plane.
     */
    public float getViewPlaneSize()
    {
        return (viewPlaneSize);
    }

    /**
     * Projects a point onto the view plane and returns a new vector representing this point on the view plane.
     * 
     * @param point The point to project onto the view plane.
     * 
     * @return A new vector representing <code>point</code> when it is projected onto the view plane.
     */
    private Vectorf4 projectPoint(Vectorf4 point)
    {
        Vectorf4 projectedPoint = null;

        if (projectionMode == PARALLEL)
        {
            projectedPoint = Matrixf44.multiply(parallelMatrix, point);
        }
        else if (projectionMode == PERSPECTIVE)
        {
            projectedPoint = Matrixf44.multiply(perspectiveMatrix, point);
        }

        projectedPoint.homogenize();
        projectedPoint.scale(1.0f / viewPlaneSize);
        projectedPoint.getBuffer().put(0, projectedPoint.getBuffer().get(0) + graphicsObjectSize.width / 2);
        projectedPoint.getBuffer().put(1, projectedPoint.getBuffer().get(1) + graphicsObjectSize.height / 2);

        return (projectedPoint);
    }

    /**
     * Reorders the vertices or <code>projectedTriangle</code> so that index 0 has the maximum y value and index 2 has
     * the minimum y value.
     * 
     * Also reorders <code>triangle</code> in the same way but reorders using the same ordering as
     * <code>projectedTriangle</code> to ensure the same vertices are in the same indices respectively.
     * 
     * @param projectedTriangle The triangle to reorder using the y values of its vertices.
     * @param triangle The triangle to reorder using the y values of <code>projectedTriangle</code>.
     */
    private void reorderTriangle(Vectorf4[] projectedTriangle, Vectorf4[] triangle)
    {
        float yMax = Float.MIN_VALUE;
        int yMaxIndex = 0;
        float yMin = Float.MAX_VALUE;
        int yMinIndex = 0;

        // Find indices of minimum and maximum values of y.
        for (int index = 0; index < projectedTriangle.length; index++)
        {
            float y = projectedTriangle[index].getBuffer().get(1);

            if (y > yMax)
            {
                yMax = y;
                yMaxIndex = index;
            }

            if (y < yMin)
            {
                yMin = y;
                yMinIndex = index;
            }
        }

        // If the index of the minimum value of y is 0, set it to the index of the maximum value of y.
        //
        // The value at index 0 and the value at the index of the maximum value will be swapped first so if the
        // minimum value is at index 0 it will be placed at the index of the maximum value.
        if (yMinIndex == 0)
        {
            yMinIndex = yMaxIndex;
        }

        // The two steps below will implicitly put the intermediate value in index 1.
        //
        // Put the maximum value in index 0.
        Vectorf4 temp = projectedTriangle[0];
        projectedTriangle[0] = projectedTriangle[yMaxIndex];
        projectedTriangle[yMaxIndex] = temp;

        temp = triangle[0];
        triangle[0] = triangle[yMaxIndex];
        triangle[yMaxIndex] = temp;

        // Put the minimum value in index 2.
        temp = projectedTriangle[2];
        projectedTriangle[2] = projectedTriangle[yMinIndex];
        projectedTriangle[yMinIndex] = temp;

        temp = triangle[2];
        triangle[2] = triangle[yMinIndex];
        triangle[yMinIndex] = temp;
    }

    /**
     * Sets the distance between the center of projection and the view plane.
     * 
     * @param distance The new distance between the center of projection and the view plane.
     */
    public void setCOPDistance(float distance)
    {
        copDistance = distance;

        perspectiveMatrix.getBuffer().put(14, 1.0f / copDistance);
    }

    /**
     * Sets the mode of projection to be used when rasterizing.
     * 
     * @param newMode The new mode of projection to be used when rasterizing.
     */
    public void setProjectionMode(int newMode)
    {
        projectionMode = newMode;
    }

    /**
     * Sets the size of the view plane.
     * 
     * @param newSize The new size of the view plane.
     */
    public void setViewPlaneSize(float newSize)
    {
        viewPlaneSize = newSize;
    }
}

