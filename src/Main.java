import javax.swing.*;

public class Main {

    private static final int WIDTH = 400, HEIGHT = 600;

    public static void main(String[] args) {
        JFrame frame = new JFrame();
        frame.setTitle("Pong Game");
        Pong g = new Pong(WIDTH, HEIGHT);
        frame.setContentPane(g);
        frame.setSize(WIDTH, HEIGHT);
        frame.setResizable(false);
        frame.setVisible(true);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
    }
}
