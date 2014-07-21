import java.awt.Rectangle;

public class Sprite
{
    private boolean visible;
    protected boolean hit;
    protected Rectangle bounds;
    protected int dx;
    protected int dy;
    protected int width;
    protected int height;
    protected int recoil;
    
    public Sprite()
    {
        visible = true;
        hit = false;
        recoil = 0;
    }
    
    public double getX()
    {
        return bounds.getX();
    }
    
    public double getY()
    {
        return bounds.getY();
    }
    
    public Rectangle getBounds()
    {
        return bounds;
    }
    
    public boolean isVisible()
    {
        return visible;
    }
    
    public boolean isHit()
    {
        return hit;
    }
    
    public int getWidth()
    {
        return width;
    }
    
    public int getHeight()
    {
        return height;
    }
    
    public void setHit(boolean hit)
    {
        this.hit = hit;
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
