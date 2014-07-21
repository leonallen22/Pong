import java.awt.EventQueue;
import javax.swing.JFrame;

public class WallBall extends JFrame
{
    public WallBall()
    {
        add(new Board());
        setTitle("Pong");
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setSize(900, 800);
        setResizable(false);
    }
    
    public static void main(String[] args)
    {
        EventQueue.invokeLater(new Runnable()
        {
            public void run()
            {
                JFrame frame = new WallBall();
                frame.setVisible(true);
            }
        });
    }
}
