import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;

public class Particle
{
    private int x;
    private int y;
    private int dx;
    private int dy;
    private int size;
    private int life;
    private Color color;
    private String symbol;
    
    public Particle(int x, int y, int dx, int dy, int size, int life, Color color)
    {
        this.x = x;
        this.y = y;
        this.dx = dx;
        this.dy = dy;
        this.size = size;
        this.life = life;
        this.color = color;
        
        if(Math.random() < 0.5)
            symbol = "0";
        
        else
            symbol = "1";
    }
    
    public boolean update()
    {
        x += dx;
        y += dy;
        
        --life;
        
        if(life <= 0)
            return true;
        
        return false;
    }
    
    public void render(Graphics g)
    {
        Font tiny = new Font("Helvetica", Font.BOLD, size);
        
        Graphics2D g2d = (Graphics2D) g.create();
        g2d.setColor(color);
        g2d.setFont(tiny);
        //g2d.fillRect(x-(size/2), y-(size/2), size, size);
        g2d.drawString(symbol, x, y);
        
        g2d.dispose();
    }
}
