import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

/**
 * THIS CLASS IS BASED ON THE CLASS OF THE SAME NAME PROVIDED ON THE COURSE WEBSITE.
 * 
 * The text menu component of a Bezier Surface Viewer.
 * 
 * Displays the text menu.
 * 
 * Reacts to key events to select menu items.
 * 
 * @author gb21
 */
class BezSurfTextMenu extends Canvas implements KeyListener
{
    /**
     * The graphics component this text menu is controlling.
     */
    private BezSurfGraphics graphics;

    /**
     * The menu to display in this text menu component.
     */
    private Menu menu;

    /**
     * Creates an instance of BezSurfTextMenu.
     * 
     * @param xDim The size of the x dimension of this text menu component.
     * @param yDim The size of the y dimension of this text menu component.
     * @param graphics The graphics component this text menu is controlling.
     */
    public BezSurfTextMenu(int xDim, int yDim, BezSurfGraphics graphics)
    {
        this.graphics = graphics;

        setSize(xDim, yDim);
        setBackground(Color.black);
        addKeyListener(this);

        menu = initMenu();
    }

    /**
     * Draws the contents of this text menu component.
     * 
     * @param w Width of this text menu component.
     * @param h Height of this text menu component.
     * @param g2 Graphics drawer for this text menu component.
     */
    private void drawBezSurfTextMenu(int w, int h, Graphics2D g2)
    {
        int offsetX1 = 10;
        int offsetX2 = w / 2;

        g2.setColor(Color.white);

        g2.drawString(menu.getName() + " Menu", offsetX1, 20);
        g2.drawLine(0, 30, w, 30);

        Iterator itemIter = menu.getItems().iterator();
        int positionY = 50;
        boolean left = true;

        while (itemIter.hasNext())
        {
            if (left)
            {
                g2.drawString(itemIter.next().toString(), offsetX1, positionY);

                left = false;
            }
            else
            {
                g2.drawString(itemIter.next().toString(), offsetX2, positionY);

                positionY += 20;
                left = true;
            }
        }
    }

    /**
     * Creates the menu heirarchy.
     * 
     * @return The menu to display in this text menu upon creation.
     */
    private Menu initMenu()
    {
        Menu main = new Menu("Main", 'm');
        Menu alterViewMenu = new Menu("Alter View", 'v');
        Menu viewModeMenu = new Menu("View Mode", 'm');
        Menu controlPointsMenu = new Menu("Edit Control Points", 'c');
        Menu meshMenu = new Menu("Set Mesh Size", 's');
        
        MenuItem.auxiliaryObjects.add(graphics);
        
        // Init main menu.
        main.addItem(alterViewMenu);
        main.addItem(viewModeMenu);
        main.addItem(controlPointsMenu);
        main.addItem(meshMenu);
        main.addItem(new QuitMenuItem("Quit", 'q'));

        // Init alter view menu.
        alterViewMenu.addItem(new IncrThetaMenuItem("Increase Theta", 'a', false));
        alterViewMenu.addItem(new IncrPhiMenuItem("Increase Phi", 'w', false));
        alterViewMenu.addItem(new IncrThetaMenuItem("Decrease Theta", 'd', true));
        alterViewMenu.addItem(new IncrPhiMenuItem("Decrease Phi", 's', true));
        alterViewMenu.addItem(new IncrCOPMenuItem("Increase COP", 'z', false));
        alterViewMenu.addItem(new IncrViewPlaneMenuItem("Increase View Plane", 'Z', false));
        alterViewMenu.addItem(new IncrCOPMenuItem("Decrease COP", 'x', true));
        alterViewMenu.addItem(new IncrViewPlaneMenuItem("Decrease View Plane", 'X', true));
        alterViewMenu.addItem(main);

        // Init view mode menu.
        viewModeMenu.addItem(new SetViewModeMenuItem("Control Points", 'c', BezSurfModel.CONTROL_POINT));
        viewModeMenu.addItem(new SetViewModeMenuItem("Wire Frame", 'w', GridRasterizer.WIRE_FRAME));
        viewModeMenu.addItem(new SetViewModeMenuItem("Flat Shading", 'f', GridRasterizer.FLAT));
        viewModeMenu.addItem(new SetViewModeMenuItem("Gouraud Shading", 'g', GridRasterizer.GOURAUD));
        viewModeMenu.addItem(new ChangeProjectionMenuItem("Toggle Projection Mode", 'p'));
        viewModeMenu.addItem(main);
        
        // Init edit control points menu.
        controlPointsMenu.addItem(new IncrAxisMenuItem("Increase in X axis", 'a', true, IncrAxisMenuItem.X));
        controlPointsMenu.addItem(new IncrAxisMenuItem("Increase in Y axis", 'w', true, IncrAxisMenuItem.Y));
        controlPointsMenu.addItem(new IncrAxisMenuItem("Decrease in X axis", 'd', false, IncrAxisMenuItem.X));
        controlPointsMenu.addItem(new IncrAxisMenuItem("Decrease in Y axis", 's', false, IncrAxisMenuItem.Y));
        controlPointsMenu.addItem(new IncrAxisMenuItem("Increase in Z axis", 'z', true, IncrAxisMenuItem.Z));
        controlPointsMenu.addItem(main);
        controlPointsMenu.addItem(new IncrAxisMenuItem("Decrease in Z axis", 'x', false, IncrAxisMenuItem.Z));
        
        // Init set mesh size menu.
        meshMenu.addItem(new IncrMeshMenuItem("Increase Mesh Size", 'w', false));
        meshMenu.addItem(new IncrMeshMenuItem("Decrease Mesh Size", 's', true));
        meshMenu.addItem(main);

        return (main);
    }

    public void keyPressed(KeyEvent e)
    {/* unused */}

    public void keyReleased(KeyEvent e)
    {/* unused */}

    public void keyTyped(KeyEvent e)
    {
        char keyPressed = e.getKeyChar();
        Iterator itemIter = menu.getItems().iterator();
        Object nextItem;

        while (itemIter.hasNext())
        {
            nextItem = itemIter.next();

            if (((MenuElement) nextItem).getSelector() == keyPressed)
            {
                if (nextItem instanceof Menu)
                {
                    menu = (Menu) nextItem;
                    
                    if (menu.getName().equals("Edit Control Points"))
                    {
                        graphics.getModel().setSelectionVisible(true);
                        
                        graphics.repaint();
                    }
                    else
                    {
                        graphics.getModel().setSelectionVisible(false);
                        
                        graphics.repaint();
                    }
                }
                else if (nextItem instanceof MenuItem)
                {
                    ((MenuItem) nextItem).performAction();
                }
            }
        }

        repaint();
    }

    public void paint(Graphics g)
    {
        Graphics2D g2 = (Graphics2D) g;
        Dimension d = getSize();

        g2.setBackground(getBackground());
        g2.clearRect(0, 0, d.width, d.height);
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        drawBezSurfTextMenu(d.width, d.height, g2);
    }
}

