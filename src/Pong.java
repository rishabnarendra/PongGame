import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class Pong extends JPanel implements KeyListener, ActionListener {

    // Instead of a point system, reduce the width of the paddle on each miss
    private final int POINT_LOSS_CONSEQUENCE = 5;

    // Game timer
    private Timer t = new Timer(5, this);

    // Width and height of the frame
    private int WIDTH, HEIGHT;

    // Velocity of the user paddle (basically which direction is it moving, left or right)
    private int userVelocity = 0;

    // Computer paddle width
    private int COMPUTER_PADDLE_WIDTH = 40;

    // Speed, width, height, position of paddles
    private final int SPEED = 2;
    private int PADDLE_WIDTH = 40, PADDLE_HEIGHT = 10;
    private int xUser, xComputer;
    
    // Place the paddles slightly inwards of the screen for better visibility 
    private int paddlePadding = 10;

    // Ball position, velocity and size
    private double xBall, yBall;
    private double xVel = 2, yVel = 2;
    private double ballSize = 20;

    public Pong(int width, int height) {
        WIDTH = width;
        HEIGHT = height;
        // Center the paddles
        xUser = WIDTH / 2 - PADDLE_WIDTH / 2;
        xComputer = WIDTH / 2 - PADDLE_WIDTH / 2;
        // Serve to the user
        xBall = WIDTH / 2;
        yBall = 0;
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        t.setInitialDelay(2000);
        t.start();
    }

    @Override
    protected void paintComponent(Graphics g) {
        // Paint the playing table
        WIDTH = getWidth();
        HEIGHT = getHeight();
        g.setColor(Color.black);
        g.fillRect(0, 0, WIDTH, HEIGHT);
        
        // Draw the paddles, ball and net
        g.setColor(Color.white);
        g.drawRect(xUser, HEIGHT - PADDLE_HEIGHT - paddlePadding, PADDLE_WIDTH, PADDLE_HEIGHT);
        g.drawRect(xComputer, paddlePadding, COMPUTER_PADDLE_WIDTH, PADDLE_HEIGHT);
        g.drawOval((int)xBall, (int)yBall, (int)ballSize, (int)ballSize);
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // Bounce the ball between the side walls
        if(xBall < 0 || xBall > WIDTH - ballSize) {
            xVel = -xVel;
        }

        // If computer loses round:
        // 1. Reset ball position
        // 2. Subtract paddle width
        if(yBall < 0) {
            xBall = WIDTH / 2;
            yBall = HEIGHT - PADDLE_HEIGHT - paddlePadding;
            COMPUTER_PADDLE_WIDTH -= POINT_LOSS_CONSEQUENCE;
        }

        // If user loses round:
        // 1. Reset ball position
        // 2. Subtract paddle width
        if(yBall + ballSize > HEIGHT) {
            xBall = WIDTH / 2;
            yBall = 0;
            PADDLE_WIDTH -= POINT_LOSS_CONSEQUENCE;
        }

        // If any of the paddle lengths are <= 0 the game is over
        if(PADDLE_WIDTH <= 0) {
            System.out.println("YOU LOST!!!");
            System.exit(0);
        }
        if(COMPUTER_PADDLE_WIDTH <= 0) {
            System.out.println("YOU WON!!!");
            System.exit(0);
        }

        // Revert y direction of ball if computer paddle has hit it
        if(yBall + ballSize >= HEIGHT - PADDLE_HEIGHT - paddlePadding && yVel > 0)
            if(xBall + ballSize >= xUser && xBall <= xUser + PADDLE_WIDTH)
                yVel = -yVel;

        // Revert y direction of ball if user paddle has hit it
        if(yBall <= PADDLE_HEIGHT + paddlePadding && yVel < 0)
            if(xBall + ballSize >= xComputer && xBall <= xComputer + PADDLE_WIDTH)
                yVel = -yVel;

        xBall += xVel;
        yBall += yVel;

        // Move the paddle based upon the key pressed (we don't want it to move too quickly, so reset the value to base speed if it exceeds threshold 
        if(userVelocity > 2)
            userVelocity = 2;
        if(userVelocity < -2)
            userVelocity = -2;

        // Make sure the paddle stays within the walls
        xUser += userVelocity;
        if(xUser < 0)
            xUser = 0;
        if(xUser >= WIDTH - PADDLE_WIDTH)
            xUser = WIDTH - PADDLE_WIDTH - 2;

        // Move the computer paddle
        xComputer += xBall - xComputer > 0 ? SPEED : -SPEED;

        repaint();
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_LEFT) {
            userVelocity -= 2;
        }
        else if(key == KeyEvent.VK_RIGHT) {
            userVelocity += 2;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if(key == KeyEvent.VK_LEFT) {
            userVelocity += 2;
        }
        else if(key == KeyEvent.VK_RIGHT) {
            userVelocity -= 2;
        }
    }
}