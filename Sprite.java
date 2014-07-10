import java.awt.Rectangle;

public class Sprite
{
    private boolean visible;
    protected Rectangle bounds;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;
    
    public Sprite()
    {
        visible = true;
    }
    
    public int getX()
    {
        return (int)bounds.getX();
    }
    
    public int getY()
    {
        return (int)bounds.getY();
    }
    
    public Rectangle getBound()
    {
        return bounds;
    }
    
    public boolean isVisible()
    {
        return visible;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void setVisible(boolean visible)
    {
        this.visible = visible;
    }
    
    public void setX(int x)
    {
        int y = (int)bounds.getY();
        bounds.setLocation(x, y);
    }
    
    public void setY(int y)
    {
        int x = (int)bounds.getX();
        bounds.setLocation(x, y);
    }
    
    public void setLocation(int x, int y)
    {
        bounds.setLocation(x, y);
    }
}
