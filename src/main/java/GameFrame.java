import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {

    GamePanel gamePanel;

    GameFrame (){
        gamePanel = new GamePanel();
        this.add(gamePanel);
        this.setTitle("Ping Pong Game");
        this.setResizable(false);
        this.setBackground(Color.black);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.pack();
        this.setVisible(true);
        this.setLocationRelativeTo(null);


    }
}
