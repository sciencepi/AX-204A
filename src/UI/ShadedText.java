package UI;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;


public class ShadedText{
    Color front;
    Color back;

    public int x;
    public int y;
    public int count;
    public int direction;
    public double h;
    public int d = 0;

    public String text;

    public ShadedText(int[] ShadedColor, int[] backColor, int _x, int _y, int count_, String _text, int dir){

        front = new Color(ShadedColor[0], ShadedColor[1], ShadedColor[2]);

        back = new Color(backColor[0], backColor[1], backColor[2]);

        x = _x;
        y = _y;

        count = count_;
        text = _text;
        direction = dir;

        h = 30;
    }

    public void draw(Graphics g, Font f){
        g.setFont(f);
        g.setColor(back);
        g.drawString(text, x+2, y+2);
        g.setColor(front);
        g.drawString(text, x, y);
    }
}