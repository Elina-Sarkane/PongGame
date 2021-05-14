import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

public class GamePanel extends JPanel implements Runnable {

    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * (0.5555));
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;
    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    GamePaddle gamePaddle1;
    GamePaddle gamePaddle2;
    Ball ball;
    GameScore gameScore;

    GamePanel(){
        newPaddles();
        newBall();
        gameScore = new GameScore(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new ActionListener());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();

    }

    public void newBall(){
        random = new Random();
        ball = new Ball((GAME_WIDTH/2)-(BALL_DIAMETER/2), random.nextInt(GAME_HEIGHT-BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);

    }
    public void newPaddles(){

        gamePaddle1 = new GamePaddle(0, (GAME_HEIGHT/2) - (PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        gamePaddle2 = new GamePaddle(GAME_WIDTH-PADDLE_WIDTH, (GAME_HEIGHT/2) - (PADDLE_HEIGHT/2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }
    public void paint(Graphics g){

        image = createImage(getWidth(), getHeight());
        graphics = image.getGraphics();
        draw(graphics);
        g.drawImage(image, 0, 0,this);
    }
    public void draw(Graphics g){
        gamePaddle1.draw(g);
        gamePaddle2.draw(g);
        ball.draw(g);
        gameScore.draw(g);
    }
    public void move(){
        gamePaddle1.move();
        gamePaddle2.move();
        ball.move();
    }
    public void checkCollision(){
        //bounce ball positions from bottom and top
        if(ball.y <= 0){
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= GAME_HEIGHT-BALL_DIAMETER){
            ball.setYDirection(-ball.yVelocity);
        }

        //bounce ball position from right and left
        if (ball.intersects(gamePaddle1)){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if (ball.yVelocity>0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if (ball.intersects(gamePaddle2)){
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if (ball.yVelocity>0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        //stops paddles at window edges
        if(gamePaddle1.y <=0)
            gamePaddle1.y=0;
        if(gamePaddle1.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            gamePaddle1.y = GAME_HEIGHT-PADDLE_HEIGHT;
        if(gamePaddle2.y <=0)
            gamePaddle2.y=0;
        if(gamePaddle2.y >= (GAME_HEIGHT-PADDLE_HEIGHT))
            gamePaddle2.y = GAME_HEIGHT-PADDLE_HEIGHT;

        //give player 1 point, creates new paddles and ball
        if(ball.x <= 0){
            gameScore.player2++;
            newPaddles();
            newBall();
            System.out.println("Player 2 scored: " + gameScore.player2);
        }

        if(ball.x >= GAME_WIDTH-BALL_DIAMETER){
            gameScore.player1++;
            newPaddles();
            newBall();
            System.out.println("Player 1 scored: " + gameScore.player1);
        }
    }
    public void run(){
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double nanoSeconds = 1000000000 / amountOfTicks;
        double delta = 0;
        while (true){
            long now = System.nanoTime();
            delta += (now - lastTime) / nanoSeconds;
            lastTime = now;
            if(delta>= 1){
                move();
                checkCollision();
                repaint();
                delta--;
            }
        }

    }
    public class ActionListener extends KeyAdapter {
        public void keyPressed(KeyEvent e){

            gamePaddle1.keyPressed(e);
            gamePaddle2.keyPressed(e);
        }
        public void keyReleased(KeyEvent e){
            gamePaddle1.keyReleased(e);
            gamePaddle2.keyReleased(e);
        }
    }
}
