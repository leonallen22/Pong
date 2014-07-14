import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Rectangle;

public class Board extends JPanel implements Runnable
{
    private Player player;
    private Opponent opponent;
    private Ball ball;
    private Ball possiblePath;
    private String gameOver = "Game Over";
    private Thread animator;
    private boolean inGame = true;
    private final int BOARD_WIDTH = 840;
    private final int BOARD_HEIGHT = 900;
    private final int DELAY = 5;
    private int playerScore;
    private int opponentScore;
    
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
        opponent = new Opponent();
        ball = new Ball();
        possiblePath = new Ball();
        playerScore = 0;
        opponentScore = 0;
        
        if(animator == null || !inGame)
            animator = new Thread(this);
    }
        
    public void drawPlayer(Graphics g)
    {
        if(player.isVisible())
            g.fillRect((int)player.getX(), (int)player.getY(), player.getWidth(), player.getHeight());
    }
    
    public void drawOpponent(Graphics g)
    {
        if(opponent.isVisible())
            g.fillRect((int)opponent.getX(), (int)opponent.getY(), opponent.getWidth(), opponent.getHeight());
    }
    
    public void drawBall(Graphics g)
    {
        if(ball.isVisible())
            g.fillRect((int)ball.getX(), (int)ball.getY(), ball.getWidth(), ball.getHeight());
        
        else
            resetRound();
    }
    
    public void drawScore(Graphics g)
    {
        Font small = new Font("Helvetica", Font.BOLD, 24);
        FontMetrics m = getFontMetrics(small);
        
        g.setColor(Color.green);
        g.setFont(small);
        g.drawString(Integer.toString(playerScore), 830, 850);
        g.drawString(Integer.toString(opponentScore), 20, 30);
    }
    
    public void resetRound()
    {
        animator = new Thread();
        player.reset();
        opponent.reset();
        ball.reset();
    }
    
    public void gameOver(Graphics g)
    {
        Font small = new Font("Helvetica", Font.BOLD, 60);
        FontMetrics m = getFontMetrics(small);
        
        g.setColor(Color.green);
        g.setFont(small);
        g.drawString(gameOver, (BOARD_WIDTH - m.stringWidth(gameOver)) / 2, BOARD_HEIGHT / 2);
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
            drawOpponent(g);
            drawBall(g);
            drawScore(g);
            
            Toolkit.getDefaultToolkit().sync();
            g.dispose();
        }
        
        else
            gameOver(g);
    }
    
    public void cycle()
    {
        player.move();
        opponent.move();
        ball.move();
        checkCollision();
        predictPath();
    }
    
    public void checkCollision()
    {
        Rectangle playerbound = player.getBounds();
        Rectangle opponentbound = opponent.getBounds();
        Rectangle ballbound = ball.getBounds();
        double player_x = player.getX();
        double opponent_x = opponent.getX();
        double ball_x = ball.getX();
        double ball_y = ball.getY();
        
        if(player_x >= BOARD_WIDTH)
            player.setX(BOARD_WIDTH);
        
        else if(player_x <= 0)
            player.setX(1);
        
        if(opponent_x >= BOARD_WIDTH)
            opponent.setX(BOARD_WIDTH);
        
        else if(opponent_x <= 0)
            opponent.setX(1);
        
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
            ball.setVisible(false);
            ++playerScore;
        }
        
        else if(ball_y > BOARD_HEIGHT)
        {
            ball.setVisible(false);
            ++opponentScore;
        }
        
        else if(ballbound.intersects(playerbound))
        {
            int dy = ball.getdy();
            double relativeintersect = (player.getWidth()/2 + (player_x-1)) - ball_x - 5;
            ball.setReturned(true);
            
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
                double rand = Math.random();
                ball.setdy(-dy);
                
                if(rand <= 0.5)
                    ball.setdx(-1);
                
                else
                    ball.setdx(1);
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
        
        else if(ballbound.intersects(opponentbound))
        {
            int dy = ball.getdy();
            double relativeintersect = (opponent.getWidth()/2 + (opponent_x-1)) - ball_x - 5;
            ball.setReturned(true);
            
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
                double rand = Math.random();
                ball.setdy(-dy);
                
                if(rand <= 0.5)
                    ball.setdx(-1);
                
                else
                    ball.setdx(1);
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
    
    public void predictPath()
    {
        if(ball.isReturned())
        {
            int x = (int)ball.getX();
            int y = (int)ball.getY();
            int dx = ball.getdx();
            int dy = ball.getdy();
            
            possiblePath.setX(x);
            possiblePath.setY(y);
            possiblePath.setdx(dx);
            possiblePath.setdy(dy);
            opponent.setdx(possiblePath.getdx());
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
