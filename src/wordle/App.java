package wordle;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.sql.SQLException;
import java.time.Duration;
import java.time.Instant;
import javax.swing.JFrame;
import javax.swing.JPanel;


public final class App extends JFrame {

    class WordleGame extends JPanel implements KeyListener{
        Board board;
        boolean stageBuilt = false;

        public WordleGame() throws SQLException{
            setPreferredSize(new Dimension(500, 490));
            this.addKeyListener(this);
            board = new Board();
            stageBuilt = true;
            this.setFocusable(true);
            this.requestFocus();
        }

        @Override
        public void paint(Graphics g) {
          if (stageBuilt && isVisible()) {
            board.paint(g, getSize().width, getSize().height);
          }
        }

        @Override
        public void keyPressed (KeyEvent e) {
            //No content needed
        }    
        
        @Override
        public void keyReleased (KeyEvent e) {
            try {
                board.keyPressed(e);
            } catch (SQLException e1) {
                e1.printStackTrace();
            }
        }    

        @Override
        public void keyTyped (KeyEvent e) {
            //No content needed
        }    

    }

    public static void main(String[] args) throws Exception {
        App window = new App();
        window.run();
    }

    private App() throws SQLException {
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        WordleGame canvas = new WordleGame();
        this.setContentPane(canvas);
        this.pack(); 
        this.setVisible(true);
    }

    public void run() {
        while (true) {
            Instant startTime = Instant.now();
            this.repaint();
            Instant endTime = Instant.now();
            long howLong = Duration.between(startTime, endTime).toMillis();
            try {
                Thread.sleep(20L - howLong);
            } catch (InterruptedException e) {
                System.out.println("thread was interrupted, but who cares?");
            } catch (IllegalArgumentException e) {
                System.out.println("application can't keep up with framerate");
            }
        }
    }


}
