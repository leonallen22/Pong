import java.awt.Rectangle;

public class Opponent extends Sprite
{
    private final int INITIAL_X = 420;
    private final int INITIAL_Y = 10;
    
    public Opponent()
    {
        bounds = new Rectangle(60, 20);
        width = (int)bounds.getWidth();
        height = (int)bounds.getHeight();
        setX(INITIAL_X);
        setY(INITIAL_Y);
    }
    
    public void setdx(int dx)
    {
        this.dx = dx;
    }
    
    public void move()
    {
        bounds.translate(dx, 0);
    }
    
    public void reset()
    {
        bounds.setLocation(INITIAL_X, INITIAL_Y);
    }
}
