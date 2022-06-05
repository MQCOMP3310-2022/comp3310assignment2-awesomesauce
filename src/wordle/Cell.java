package wordle;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Cell extends Rectangle{
    private static int size = 70;
    protected int col;
    protected int row;
    protected Color backbgroundColor;
    protected Color textColor;
    protected char displayCharacter;
    protected boolean isEmpty;
    protected boolean isActive;

    public Cell(){
        super(0,0,0,0);
        col = -1;
        row = -1;
        displayCharacter = ' ';
        backbgroundColor = Color.DARK_GRAY;
        textColor = Color.WHITE;
        isEmpty = true;
    }

    public Cell(int columnIndex, int rowIndex, int inX, int inY){
        super(inX,inY,size,size);
        col = columnIndex;
        row = rowIndex;
        displayCharacter = ' ';
        backbgroundColor = Color.DARK_GRAY;
        textColor = Color.WHITE;
        isEmpty = true;
    }

    public void setCharacter(char letter, int cellState){
        //cell state 0 is empty
        //cell state 1 is letter entered
        //cell state 2 is letter entered, yellow background
        //cell state 3 is letter entered, green background
        switch(cellState){
            case 1:
                displayCharacter = letter;
                backbgroundColor = Color.DARK_GRAY;
                textColor = Color.WHITE;
                isEmpty = false;
                break;
            case 2:
                displayCharacter = letter;
                backbgroundColor = Color.YELLOW;
                textColor = Color.BLACK;
                isEmpty = false;
                break;
            case 3:
                displayCharacter = letter;
                backbgroundColor = Color.GREEN;
                textColor = Color.BLACK;
                isEmpty = false;
                break; 
            case 4:
                displayCharacter = letter;
                backbgroundColor = Color.RED;
                textColor = Color.WHITE;
                isEmpty = false;
                break; 
            default:
                displayCharacter = ' ';
                backbgroundColor = Color.DARK_GRAY;
                textColor = Color.WHITE;
                isEmpty = true;
                break;
        }
    }

    void setState(int cellState){
        setCharacter(displayCharacter,cellState);
    }

    void paint(Graphics g, int width, int height){
        if(isActive){
            g.setColor(Color.LIGHT_GRAY);
        } else {
            g.setColor(backbgroundColor);
        }
        g.fillRect((int)((double)(x * width)/500),(int)((double)(y * height)/490), (int)((double)(size * width)/500), (int)((double)(size * height)/490));
        
        if(isActive){
            g.setColor(Color.YELLOW);
        } else {
            g.setColor(Color.BLACK);
        }
        g.drawRect((int)((double)(x * width)/500),(int)((double)(y * height)/490), (int)((double)(size * width)/500), (int)((double)(size * height)/490));
        
        
        if(isActive){
            g.setColor(Color.BLACK);
        } else {
            g.setColor(textColor);
        }
        
        Font f = new Font("Arial", Font.PLAIN, (int)((double)(40 * height)/490));
        FontMetrics metrics = g.getFontMetrics(f);
        int drawXPos = (int)((double)(x * width)/500) + ((int)((double)(size * width)/500) - metrics.stringWidth(""+displayCharacter))/2;
        int drawYPos = (int)((double)(y * height)/490) + ((int)((double)(size * height)/490) + metrics.getHeight())/2 - 10;
        
        g.setFont(f); 
        g.drawString(""+displayCharacter, drawXPos, drawYPos);
    }

    void setActive(){
        isActive = true;
    }

    void setInactive(){
        isActive = false;
    }

    void reset(){
        setCharacter(' ', 0);
    }

    public String getStoredCharacter(){
        return "" + displayCharacter;
    }

    public String toString(){
        return Integer.toString(col) + Integer.toString(row) + ":'" + displayCharacter + "'";
    }

    public void setX(int width) {
        x = width;
    }
    
    public void setY(int height) {
        y = height;
    }
}
