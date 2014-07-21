import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.FontMetrics;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Toolkit;
import java.awt.Color;
import javax.swing.JPanel;
import java.awt.Rectangle;
import java.util.ArrayList;

public class Board extends JPanel implements Runnable
{
    private AudioPlayer audio;
    private ArrayList<Particle> particles;
    private Player player;
    private Opponent opponent;
    private Ball ball;
    private Thread animator;
    private boolean inGame = true;
    private boolean showHint = true;
    private final int BOARD_WIDTH = 840;
    private final int BOARD_HEIGHT = 800;
    private final int DELAY = 6;
    private final int BURST = 25;
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
        audio = new AudioPlayer();
        particles = new ArrayList<Particle>(BURST);
        player = new Player();
        opponent = new Opponent();
        ball = new Ball();
        playerScore = 0;
        opponentScore = 0;
        
        if(animator == null || !inGame)
        {
            animator = new Thread(this);
            animator.start();
        }
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
        {
            g.fillRect((int)ball.getX(), (int)ball.getY(), ball.getWidth(), ball.getHeight());
            
            if(Math.random() < 0.3)
                drawTrail();
        }
        
        else
            resetRound();
    }
    
    public void drawTrail()
    {
        int x = (int)ball.getX()+1;
        int y = (int)ball.getY()+8;
        int life = 0;
        double rand = Math.random();
        
        if(rand < 0.3)
            x += 1;
        
        else if(rand < 0.6)
            x += 2;
        
        if(Math.random() < 0.5)
            y += 2;
        
        if(ball.getdx() == 0)
            life = (int)(Math.random()*50);
        
        else
            life = (int)(Math.random()*40);
        
        particles.add(new Particle(x, y, 0, 0, 10, life, Color.green));
    }
    
    public void drawScore(Graphics g)
    {
        Font small = new Font("Helvetica", Font.BOLD, 24);
        
        g.setColor(Color.green);
        g.setFont(small);
        g.drawString(Integer.toString(playerScore), 830, 750);
        g.drawString(Integer.toString(opponentScore), 20, 30);
    }
    
    public void resetRound()
    {
        player.reset();
        opponent.reset();
        ball.reset();
        ball.setVisible(true);
    }
    
    public void resetGame()
    {
        player.reset();
        opponent.reset();
        ball.reset();
        ball.setVisible(true);
        playerScore = 0;
        opponentScore = 0;
    }
    
    public void gameOver(Graphics g)
    {
        Font small = new Font("Helvetica", Font.BOLD, 46);
        FontMetrics m = getFontMetrics(small);
        String gameOver = "Game Over";
        
        if(playerScore > opponentScore)
            gameOver = "You Win!";
        
        else
            gameOver = "You Lose!";
        
        g.setColor(Color.black);
        g.fillRect(0, 0, BOARD_WIDTH, BOARD_HEIGHT);
        g.setColor(Color.green);
        g.setFont(small);
        g.drawString(gameOver, ((BOARD_WIDTH - m.stringWidth(gameOver)) / 2)+30, BOARD_HEIGHT / 2);
    }
    
    public void drawHint(Graphics g)
    {
        Font small = new Font("Helvetica", Font.BOLD, 14);
        FontMetrics m = getFontMetrics(small);
        String hint_1 = "Press Space to serve";
        String hint_2 = "Press R to restart game";
        String hint_3 = "Use Left & Right Arrows to control paddle";
        
        g.setColor(Color.green);
        g.setFont(small);
        g.drawString(hint_1, ((BOARD_WIDTH - m.stringWidth(hint_1)) / 2)+30, BOARD_HEIGHT/2 - 25);
        g.drawString(hint_2, ((BOARD_WIDTH - m.stringWidth(hint_2)) / 2)+30, BOARD_HEIGHT/2);
        g.drawString(hint_3, ((BOARD_WIDTH - m.stringWidth(hint_3)) / 2)+30, BOARD_HEIGHT/2 + 25);
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
            renderParticles(g);
            
            if(showHint)
                drawHint(g);
        }
        
        else
        {
            gameOver(g);
            drawPlayer(g);
            drawOpponent(g);
            drawScore(g);
            renderParticles(g);
        }
        
        Toolkit.getDefaultToolkit().sync();
        g.dispose();
    }
    
    public void addParticle(int x, int y, int direction)
    {
        int particle_dx = 0;
        int particle_dy = 0;
        int ball_dx = ball.getdx();
        int life = (int)(Math.random()*40);
        
        switch(ball_dx)
        {
            case 6:
                if(Math.random() < 0.95)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case 5:
                if(Math.random() < 0.9)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case 4:
                if(Math.random() < 0.8)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case 3:
                if(Math.random() < 0.7)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case 2:
                if(Math.random() < 0.6)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case 1:
                if(Math.random() < 0.5)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case -1:
                if(Math.random() < 0.5)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case -2:
                if(Math.random() < 0.4)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case -3:
                if(Math.random() < 0.3)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case -4:
                if(Math.random() < 0.2)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case -5:
                if(Math.random() < 0.1)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
                
            case -6:
                if(Math.random() < 0.05)
                    particle_dx = (int)(Math.random()*5);
                
                else
                    particle_dx = (int)(Math.random()*-5);
                break;
        }
        
        if(direction == 0)
            particle_dy = (int)(Math.random()*-5);
                
        else
            particle_dy = (int)(Math.random()*5);
        
        particles.add(new Particle(x, y, particle_dx, particle_dy, 10, life, Color.green));
    }
    
    public void addBurst(int x, int y, int direction)
    {
        int particle_dx = 0;
        int particle_dy = 0;
        int life = 0;
        
        for(int i=0; i < 1000; ++i)
        {
            life = (int)(Math.random()*60);
            
            if(Math.random() < 0.5)
                particle_dx = (int)(Math.random()*-4);
                    
            else
                particle_dx = (int)(Math.random()*4);
            
            if(direction == 0)
                particle_dy = (int)(Math.random()*-4);
                    
            else
                particle_dy = (int)(Math.random()*4);
            
            particles.add(new Particle(x, y, particle_dx, particle_dy, 10, life, Color.green));
        }
    }
    
    public void updateParticles()
    {
        for(int i=0; i < particles.size(); ++i)
        {
            if(particles.get(i).update())
                particles.remove(i);
        }
    }
    
    public void renderParticles(Graphics g)
    {        
        for(int i=0; i < particles.size(); ++i)
            particles.get(i).render(g);
    }
    
    public void cycle()
    {
        if(inGame)
        {
            if(ball.isServed())
            {
                player.move();
                opponent.move(ball);
                ball.move();
            }
            
            if(playerScore >= 10 || opponentScore >= 10)
                inGame = false;
        }
        
        updateParticles();
        checkCollision();
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
            
            audio.playSound(0);
            ball.setdx(-dx);
        }
        
        else if(ball_x <= 0)
        {
            int dx = ball.getdx();
            
            audio.playSound(0);
            ball.setdx(-dx);
        }
        
        else if(ball_y <= 0)
        {
            ball.setVisible(false);
            audio.playSound(2);
            
            addBurst((int)ball_x, (int)ball_y, 1);
            
            ++playerScore;
        }
        
        else if(ball_y > BOARD_HEIGHT)
        {
            ball.setVisible(false);
            audio.playSound(2);
            
            addBurst((int)ball_x, (int)ball_y, 0);
            
            ++opponentScore;
        }
        
        else if(ballbound.intersects(playerbound))
        {
            int player_y = (int)player.getY();
            int dy = ball.getdy();
            double relativeintersect = (player.getWidth()/2 + (player_x-1)) - ball_x-5;
            ball.setReturned(true);
            audio.playSound(1);
            
            if(!player.isHit())
                player.setHit(true);
            
            if(relativeintersect <= -30)
            {
                if(ball_y <= player_y+2)
                {
                    ball.setdy(-dy);
                    ball.setdx(6);
                }
                
                else
                    ball.setdx(6);
            }
            
            else if(relativeintersect <= -25)
            {
                if(ball_y <= player_y+2)
                {
                    ball.setdy(-dy);
                    ball.setdx(5);
                }
                
                else
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
                if(ball_y <= player_y+2)
                {
                    ball.setdy(-dy);
                    ball.setdx(-5);
                }
                
                else
                    ball.setdx(-5);
            }
            
            else
            {
                if(ball_y <= player_y+2)
                {
                    ball.setdy(-dy);
                    ball.setdx(-6);
                }
                
                else
                    ball.setdx(-6);
            }
            
            for(int i=0; i < BURST; ++i)
                addParticle((int)ball_x, (int)ball_y, 0);
        }
        
        else if(ballbound.intersects(opponentbound))
        {
            int opponent_y = (int)(opponent.getY()-19);
            int dy = ball.getdy();
            double relativeintersect = (opponent.getWidth()/2 + (opponent_x-1)) - ball_x - 5;
            ball.setReturned(false);
            audio.playSound(1);
            
            if(!opponent.isHit())
                opponent.setHit(true);
            
            if(relativeintersect <= -30)
            {
                if(ball_y >= opponent_y+2)
                {
                    ball.setdy(-dy);
                    ball.setdx(6);
                }
                
                else
                    ball.setdx(6);
            }
            
            else if(relativeintersect <= -25)
            {
                if(ball_y >= opponent_y+2)
                {
                    ball.setdy(-dy);
                    ball.setdx(5);
                }
                
                else
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
                if(ball_y >= opponent_y-2)
                {
                    ball.setdy(-dy);
                    ball.setdx(-5);
                }
                
                else
                    ball.setdx(-5);
            }
            
            else
            {
                if(ball_y >= opponent_y-2)
                {
                    ball.setdy(-dy);
                    ball.setdx(-6);
                }
                
                else
                    ball.setdx(-6);
            }
            
            for(int i=0; i < BURST; ++i)
                addParticle((int)ball_x, (int)ball_y, 1);
        }
    }
    
    public void run()
    {
        long beforeTime, timeDiff, sleep;

        beforeTime = System.currentTimeMillis();

        while(true)
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
            
            if(key == KeyEvent.VK_SPACE)
            {
                ball.setServed(true);
                showHint = false;
            }
            
            else if(key == KeyEvent.VK_R)
            {
                resetGame();
                inGame = true;
                showHint = true;
            }
        }
    }
}
