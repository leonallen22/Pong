import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JPanel;

public class Board extends JPanel implements Runnable
{
    private Player player;
    private Ball ball;
    private String gameOver = "Game Over";
    private Thread animator;
    private boolean inGame = true;
    private final int BOARD_WIDTH = 900;
    private final int BOARD_HEIGHT = 900;
    private final int DELAY = 3;
    
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
            g.fillRect(player.getX(), player.getY(), player.getWidth(), player.getHeight());
    }
    
    public void drawBall(Graphics g)
    {
        if(ball.isVisible())
            g.fillRect(ball.getX(), ball.getY(), ball.getWidth(), ball.getHeight());
        
        else
            resetRound();
    }
    
    public void resetRound()
    {
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
            player.keyPressed(e);
            
            if(e.getKeyCode() == KeyEvent.VK_SPACE && !animator.isAlive())
                animator.start();
        }
    }
}
