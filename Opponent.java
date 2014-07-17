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
    
    public void move(Ball ball)
    {
        int x = (int)bounds.getX();
        int y = (int)bounds.getY();
        
        if(ball.isReturned())
        {
            int ball_x = (int)ball.getX();
            int ball_y = (int)ball.getY();
            int ball_dx = ball.getdx();
            int diff_x = Math.abs(x - ball_x);
            int diff_y = Math.abs(y - ball_y);
           
            if(ball_dx > 2 || ball_dx < -2 || diff_x > 150 || (diff_y < 150 && diff_x > 50))
            {
                if(x > ball_x)
                    setdx(-3);
               
                else if(x < ball_x)
                    setdx(3);
            }
            
            else if(ball_dx > 1 || ball_dx < -1 || diff_x > 50)
            {
                if(x > ball_x)
                    setdx(-2);
               
                else if(x < ball_x)
                    setdx(2);
            }
            
            else if(ball_dx == 1 || ball_dx == -1 || diff_x != 0)
            {
                if(x > ball_x)
                    setdx(-1);
               
                else if(x < ball_x)
                    setdx(1);
            }
            
            else
                setdx(0);
        }
        
        else
        {
            if(x != 420)
            {
                if(x > 420)
                    setdx(-3);
                
                else
                    setdx(3);
            }
            
            else
                setdx(0);
        }
        
        bounds.translate(dx, 0);
    }
    
    public void reset()
    {
        bounds.setLocation(INITIAL_X, INITIAL_Y);
    }
}
