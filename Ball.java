import java.awt.Rectangle;

public class Ball extends Sprite
{
    private final int INITIAL_X = 450;
    private final int INITIAL_Y = 300;
    private boolean returned;
    
    public Ball()
    {
        bounds = new Rectangle(10, 10);
        width = (int)bounds.getWidth();
        height = (int)bounds.getHeight();
        returned = false;
        setX(INITIAL_X);
        setY(INITIAL_Y);
        dy = 3;
    }
    
    public int getdx()
    {
        return dx;
    }
    
    public int getdy()
    {
        return dy;
    }
    
    public boolean isReturned()
    {
        return returned;
    }
    
    public void setdx(int dx)
    {
        this.dx = dx;
    }
    
    public void setdy(int dy)
    {
        this.dy = dy;
    }
    
    public void setReturned(boolean returned)
    {
        this.returned = returned;
    }
    
    public void move()
    {
        bounds.translate(dx, dy);
    }
    
    public void reset()
    {
        dy = 3;
        dx = 0;
        bounds.setLocation(INITIAL_X, INITIAL_Y);
    }
}
