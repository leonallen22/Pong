import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Rectangle;

public class Board extends JPanel implements Runnable
{
    private Player player;
    private Ball ball;
    private String gameOver = "Game Over";
    private Thread animator;
    private boolean inGame = true;
    private final int BOARD_WIDTH = 840;
    private final int BOARD_HEIGHT = 900;
    private final int DELAY = 5;
    
    public Board()
    {
        addKeyListener(new TAdapter());
        setFocusable(true);
        setBackground(Color.black);
        
        gameInit();
        setDoubleBuffered(true);
    }
    
    public void addNotify()
    {
        super.addNotify();
        gameInit();
    }
    
    public void gameInit()
    {
        player = new Player();
        ball = new Ball();
        
        if(animator == null || !inGame)
            animator = new Thread(this);
    }
        
    public void drawPlayer(Graphics g)
    {
        if(player.isVisible())
            g.fillRect((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight());
    }
    
    public void drawBall(Graphics g)
    {
        if(ball.isVisible())
            g.fillRect((int)ball.getX(), (int)ball.getY(), ball.getWidth(), ball.getHeight());
        
        else
            resetRound();
    }
    
    public void resetRound()
    {
        animator = new Thread();
        player.reset();
        ball.reset();
    }
    
    public void gameOver()
    {
        
    }
    
    public void paint(Graphics g)
    {
        super.paint(g);
        
        g.setColor(Color.black);
        g.fillRect(0,  0, BOARD_WIDTH, BOARD_HEIGHT);
        g.setColor(Color.green);
                
        if(inGame)
        {
            drawPlayer(g);
            drawBall(g);
        }
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void cycle()
    {
        player.move();
        ball.move();
        checkCollision();
    }
    
    public void checkCollision()
    {
        Rectangle playerbound = player.getBounds();
        Rectangle ballbound = ball.getBounds();
        double player_x = player.getX();
        double ball_x = ball.getX();
        double ball_y = ball.getY();
        
        if(player_x >= BOARD_WIDTH)
            player.setX(BOARD_WIDTH);
        
        else if(player_x <= 0)
            player.setX(1);
        
        if(ball_x >= BOARD_WIDTH+40)
        {
            int dx = ball.getdx();
            
            ball.setdx(-dx);
        }
        
        else if(ball_x <= 0)
        {
            int dx = ball.getdx();
            
            ball.setdx(-dx);
        }
        
        else if(ball_y <= 0)
        {
            int dy = ball.getdy();
            
            ball.setdy(-dy);
        }
        
        else if(ballbound.intersects(playerbound))
        {
            int dy = ball.getdy();
            double relativeintersect = (player.getWidth()/2 + (player_x-1)) - ball_x - 5;
            
            if(relativeintersect <= -30)
            {
                ball.setdy(-dy);
                ball.setdx(6);
            }
            
            else if(relativeintersect <= -25)
            {
                ball.setdy(-dy);
                ball.setdx(5);
            }
            
            else if(relativeintersect <= -19)
            {
                ball.setdy(-dy);
                ball.setdx(4);
            }
            
            else if(relativeintersect <= -13)
            {
                ball.setdy(-dy);
                ball.setdx(3);
            }
            
            else if(relativeintersect <= -7)
            {
                ball.setdy(-dy);
                ball.setdx(2);
            }
            
            else if(relativeintersect <= -1)
            {
                ball.setdy(-dy);
                ball.setdx(1);
            }
            
            else if(relativeintersect <= 2)
            {
                ball.setdy(-dy);
                ball.setdx(0);
            }
            
            else if(relativeintersect <= 7)
            {
                ball.setdy(-dy);
                ball.setdx(-1);
            }
            
            else if(relativeintersect <= 13)
            {
                ball.setdy(-dy);
                ball.setdx(-2);
            }
            
            else if(relativeintersect <= 19)
            {
                ball.setdy(-dy);
                ball.setdx(-3);
            }
            
            else if(relativeintersect <= 25)
            {
                ball.setdy(-dy);
                ball.setdx(-4);
            }
            
            else if(relativeintersect <= 30)
            {
                ball.setdy(-dy);
                ball.setdx(-5);
            }
            
            else
            {
                ball.setdy(-dy);
                ball.setdx(-6);
            }
        }
    }
    
    public void run()
    {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while (inGame)
        {
            repaint();
            cycle();

            timeDiff = System.currentTimeMillis() - beforeTime;
            sleep = DELAY - timeDiff;

            if (sleep < 0) 
                sleep = 2;
            
            try
            {
                Thread.sleep(sleep);
            }
            catch (InterruptedException e)
            {
                System.out.println("interrupted");
            }
            
            beforeTime = System.currentTimeMillis();
        }
        
        gameOver();
    }
    
    private class TAdapter extends KeyAdapter
    {
        public void keyReleased(KeyEvent e)
        {
            player.keyReleased(e);
        }
        
        public void keyPressed(KeyEvent e)
        {
            int key = e.getKeyCode();
            player.keyPressed(e);
            
            if(key == KeyEvent.VK_SPACE && !animator.isAlive())
                animator.start();
            
            else if(key == KeyEvent.VK_R)
                resetRound();
        }
    }
}
