import java.awt.Graphics2D;
import java.nio.FloatBuffer;

/**
 * A system for modelling a bezier surface in 3d.
 * 
 * @author gb21
 */
public class BezSurfModel
{
    /**
     * The control point rendering mode.
     */
    public static final int CONTROL_POINT = 10;
    
    /**
     * The bezier basis matrix.
     */
    public Matrixf44 bezierMatrix = new Matrixf44();

    /**
     * The control points of the bezier surface, freely movable.
     */
    private Vectorf4[] controlPointGrid = new Vectorf4[16];

    /**
     * The center of the control point and therefore surface grid.
     */
    private Vectorf4 gridCenter = null;

    /**
     * The size of the bezier surface grid, the amount of points in the grid is gridSize^2.
     */
    private int gridSize = 10;

    /**
     * The control points of the bezier surface, as positioned on the view plane.
     */
    private Vectorf4[] projectedControlPointGrid = new Vectorf4[16];

    /**
     * The rasterizer for rendering the control point and surface grids.
     */
    private GridRasterizer rasterizer = new GridRasterizer(new Dimension(640, 340));

    /**
     * The rendering mode to use.
     */
    private int renderingMode = CONTROL_POINT;

    /**
     * The index of the selected control point, or -1 if no control point is selected.
     */
    private int selectedPoint = -1;

    /**
     * True if this model is currently able to have a control point shown as selected, false otherwise.
     */
    private boolean selectionVisible = false;

    /**
     * The bezier surface grid, determined by the position of the control points.
     */
    private Vectorf4[] surfaceGrid = new Vectorf4[gridSize * gridSize];

    /**
     * Creates an instance of BezSurfModel.
     */
    public BezSurfModel()
    {
        // Initialize bezier basis matrix.
        FloatBuffer bezBuf = bezierMatrix.getBuffer();

        bezBuf.put(0, 1.0f);
        bezBuf.put(1, 3.0f);
        bezBuf.put(2, -3.0f);
        bezBuf.put(3, 1.0f);
        bezBuf.put(4, 3.0f);
        bezBuf.put(5, -6.0f);
        bezBuf.put(6, 3.0f);
        bezBuf.put(8, -3.0f);
        bezBuf.put(9, 3.0f);
        bezBuf.put(12, 1.0f);

        // Initialize control point grid.
        int count = 1;
        int sqrtTotalPoints = (int) Math.round(Math.sqrt(controlPointGrid.length));
        float x = -150.0f;
        float y = 150.0f;

        for (int index = 0; index < controlPointGrid.length; index++)
        {
            controlPointGrid[index] = new Vectorf4(x, y, 500.0f, 1.0f);

            if (count != sqrtTotalPoints)
            {
                x += 100.0f;
                count++;
            }
            else
            {
                y -= 100.0f;
                x = -150.0f;
                count = 1;
            }
        }

        gridCenter = new Vectorf4(0.0f, 0.0f, 500.0f, 1.0f);

        //Initialize surface grid.
        deriveSurfaceGrid();
    }
    
    /**
     * Determines a square mesh of arbitrary size of a polynomial bicubic surface using 16 control points.
     */
    private void deriveSurfaceGrid()
    {
        int sqrtTotalSurfacePoints = (int) Math.round(Math.sqrt(surfaceGrid.length));
        int sqrtTotalControlPoints = (int) Math.round(Math.sqrt(controlPointGrid.length));
        Vectorf4[][] controlPointsA = new Vectorf4[sqrtTotalControlPoints][sqrtTotalControlPoints];
        Vectorf4[] controlPointsB = new Vectorf4[sqrtTotalControlPoints];

        // Create control point arrays for the four curves that are the control points of the surface.
        for (int rowIndex = 0; rowIndex < sqrtTotalControlPoints; rowIndex++)
        {
            for (int columnIndex = 0; columnIndex < sqrtTotalControlPoints; columnIndex++)
            {
                controlPointsA[rowIndex][columnIndex] = controlPointGrid[(rowIndex * sqrtTotalControlPoints)
                        + columnIndex];
            }
        }

        // For every column in the surface grid.
        for (int columnIndex = 0; columnIndex < sqrtTotalSurfacePoints; columnIndex++)
        {
            // Derive a curve representing this column.
            for (int curveIndex = 0; curveIndex < sqrtTotalControlPoints; curveIndex++)
            {
                controlPointsB[curveIndex] = getPointOnCurve(controlPointsA[curveIndex],
                        (1.0f / (sqrtTotalSurfacePoints - 1)) * columnIndex);
            }

            // Interpolate points on curve for this column.
            for (int rowIndex = 0; rowIndex < sqrtTotalSurfacePoints; rowIndex++)
            {
                surfaceGrid[(rowIndex * sqrtTotalSurfacePoints) + columnIndex] = getPointOnCurve(controlPointsB,
                        (1.0f / (sqrtTotalSurfacePoints - 1)) * rowIndex);
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
        return (rasterizer.getCOPDistance());
    }

    /**
     * Returns the size of the bezier surface grid.
     * 
     * @return The size of the bezier surface grid.
     */
    public int getGridSize()
    {
        return (gridSize);
    }

    /**
     * Returns the interpolated point on the bezier curve defined by the control points given.
     * 
     * @param controlPoints The control points that define the bezier curve.
     * @param t A value between 0 and 1. This defines the distance along the curve to interpolate the point.
     * 
     * @return The interpolated point on the bezier curve defined by the control points given.
     */
    private Vectorf4 getPointOnCurve(Vectorf4[] controlPoints, float t)
    {
        Vectorf4 pointOnCurve = new Vectorf4();
        
        FloatBuffer pBuf = pointOnCurve.getBuffer();
        FloatBuffer p1Buf = controlPoints[0].getBuffer();
        FloatBuffer p2Buf = controlPoints[1].getBuffer();
        FloatBuffer p3Buf = controlPoints[2].getBuffer();
        FloatBuffer p4Buf = controlPoints[3].getBuffer();
        
        for (int index = 0; index < 4; index++)
        {
            pBuf.put(index,
                    (1 - t) * (1 - t) * (1 - t) * p1Buf.get(index) +
                    3 * t * (1 - t) * (1 - t) * p2Buf.get(index) +
                    3 * t * t * (1 - t) * p3Buf.get(index) +
                    t * t * t * p4Buf.get(index));
        }
        
        pointOnCurve.homogenize();

        return (pointOnCurve);
    }

    /**
     * Returns the rendering mode to use.
     * 
     * @return The rendering mode to use.
     */
    public int getRenderingMode()
    {
        return (renderingMode);
    }

    /**
     * Returns the index of the selected control point, or -1 if no control point is selected.
     * 
     * @return The index of the selected control point, or -1 if no control point is selected.
     */
    public int getSelectedPoint()
    {
        return (selectedPoint);
    }

    /**
     * Returns the size of the view plane.
     * 
     * @return The size of the view plane.
     */
    public float getViewPlaneSize()
    {
        return (rasterizer.getViewPlaneSize());
    }
    
    /**
     * Returns true if this model is currently able to have a control point shown as selected, false otherwise.
     * 
     * @return True if this model is currently able to have a control point shown as selected, false otherwise.
     */
    public boolean isSelectionVisible()
    {
        return (selectionVisible);
    }

    /**
     * Paints the model to the graphics object g2d.
     * 
     * @param g2d The graphics object to paint the model on.
     */
    public void paint(Graphics2D g2d)
    {
        if (selectionVisible)
        {
            rasterizer.drawGridWithSelection(controlPointGrid, g2d, selectedPoint);
        }
        else if (renderingMode == CONTROL_POINT)
        {
            rasterizer.drawGrid(controlPointGrid, g2d, GridRasterizer.WIRE_FRAME);
        }
        else
        {
            rasterizer.drawGrid(surfaceGrid, g2d, renderingMode);
        }

        projectedControlPointGrid = rasterizer.getProjectedPoints(controlPointGrid);
    }

    /**
     * Rotates the model about the x axis.
     * 
     * @param angle The angle to rotate the model (in radians).
     */
    public void rotatePhi(float angle)
    {
        for (int index = 0; index < controlPointGrid.length; index++)
        {
            rotatePointPhi(controlPointGrid, index, angle);
        }
        
        for (int index = 0; index < surfaceGrid.length; index++)
        {
            rotatePointPhi(surfaceGrid, index, angle);
        }
    }

    /**
     * Rotates a point about the x axis.
     * 
     * @param grid The grid the point belongs to.
     * @param index The index of the point in the grid.
     * @param angle The angle to rotate the point (in radians).
     */
    private void rotatePointPhi(Vectorf4[] grid, int index, float angle)
    {
        if (index < 0 || index >= grid.length)
        {
            return;
        }
        
        Vectorf4 temp = Vectorf4.subtract(grid[index], gridCenter);

        Matrixf44 xRot = new Matrixf44();
        xRot.getBuffer().put(5, (float) Math.cos(angle));
        xRot.getBuffer().put(6, (float) Math.sin(angle) * -1);
        xRot.getBuffer().put(9, (float) Math.sin(angle));
        xRot.getBuffer().put(10, (float) Math.cos(angle));

        temp = Matrixf44.multiply(temp, xRot);

        grid[index] = Vectorf4.add(temp, gridCenter);
    }

    /**
     * Rotates a point about the z axis.
     * 
     * @param grid The grid the point belongs to.
     * @param index The index of the point in the grid.
     * @param angle The angle to rotate the point (in radians).
     */
    private void rotatePointTheta(Vectorf4[] grid, int index, float angle)
    {
        if (index < 0 || index >= grid.length)
        {
            return;
        }
        
        Vectorf4 temp = Vectorf4.subtract(grid[index], gridCenter);

        Matrixf44 zRot = new Matrixf44();
        zRot.getBuffer().put(0, (float) Math.cos(angle));
        zRot.getBuffer().put(1, (float) Math.sin(angle) * -1);
        zRot.getBuffer().put(4, (float) Math.sin(angle));
        zRot.getBuffer().put(5, (float) Math.cos(angle));

        temp = Matrixf44.multiply(temp, zRot);

        grid[index] = Vectorf4.add(temp, gridCenter);
    }

    /**
     * Rotates the model about the z axis.
     * 
     * @param angle The angle to rotate the model (in radians).
     */
    public void rotateTheta(float angle)
    {
        for (int index = 0; index < controlPointGrid.length; index++)
        {
            rotatePointTheta(controlPointGrid, index, angle);
        }
        
        for (int index = 0; index < surfaceGrid.length; index++)
        {
            rotatePointTheta(surfaceGrid, index, angle);
        }
    }

    /**
     * Selects any control point that projects onto the graphics object within 5 pixels of
     * (<code>x</code>, <code>y</code>).
     * 
     * @param x The x coordinate to select the control point at.
     * @param y The y coordinate to select the control point at.
     */
    public void selectControlPoint(int x, int y)
    {
        for (int index = 0; index < projectedControlPointGrid.length; index++)
        {
            float xDiff = Math.abs(projectedControlPointGrid[index].getBuffer().get(0) - x);
            float yDiff = Math.abs(projectedControlPointGrid[index].getBuffer().get(1) - y);

            if (xDiff < 5.0f && yDiff < 5.0f)
            {
                selectedPoint = index;

                break;
            }
        }
    }

    /**
     * Sets the distance between the center of projection and the view plane.
     * 
     * @param distance The new distance between the center of projection and the view plane.
     */
    public void setCOPDistance(float distance)
    {
        rasterizer.setCOPDistance(distance);
    }

    /**
     * Sets the size of the bezier surface grid.
     * 
     * @param newSize The new size of the bezier surface grid.
     */
    public void setGridSize(int newSize)
    {
        gridSize = newSize;
        
        surfaceGrid = new Vectorf4[gridSize * gridSize];
        
        deriveSurfaceGrid();
    }

    /**
     * Sets the rendering mode to use.
     * 
     * @param newMode The new rendering mode to use.
     */
    public void setRenderingMode(int newMode)
    {
        renderingMode = newMode;
    }

    /**
     * Sets the state of the model to allow a point to be shown as selected or disallow it.
     * 
     * @param newSelectionVisible The new value for the state of the model.
     */
    public void setSelectionVisible(boolean newSelectionVisible)
    {
        selectionVisible = newSelectionVisible;
    }

    /**
     * Sets the size of the view plane.
     * 
     * @param newSize The new size of the view plane.
     */
    public void setViewPlaneSize(float newSize)
    {
        rasterizer.setViewPlaneSize(newSize);
    }
    
    /**
     * Changes the projection mode to be used between perspective and parallel.
     */
    public void toggleProjectionMode()
    {
        if (rasterizer.getProjectionMode() == GridRasterizer.PERSPECTIVE)
        {
            rasterizer.setProjectionMode(GridRasterizer.PARALLEL);
        }
        else
        {
            rasterizer.setProjectionMode(GridRasterizer.PERSPECTIVE);
        }
    }

    /**
     * Translates the control point at the index given by the vector <code>translation</code>.
     * 
     * @param translation The vector to translate the control point by.
     * @param index The index of the control point to translate.
     */
    public void translateControlPoint(Vectorf4 translation, int index)
    {
        if (index < 0 || index >= controlPointGrid.length)
        {
            return;
        }
        
        controlPointGrid[index] = Vectorf4.add(controlPointGrid[index], translation);

        deriveSurfaceGrid();
    }
}

