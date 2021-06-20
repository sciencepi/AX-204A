package UI;

import java.util.Arrays;
import java.util.ArrayList;
import java.awt.Font;
import java.awt.Graphics;

import java.awt.image.BufferedImage;
import javax.swing.JPanel;


public class ItemDamageHandler{
    private ArrayList<ShadedText> itemDamageQueue;
    private Font inUseFont;
    private BufferedImage breakItem;
    public boolean id_true = false;

    public int id_x = 0;
    public int id_y = 0;
    public int id_timer = 0;

    public ItemDamageHandler(BufferedImage bi){
        itemDamageQueue = new ArrayList<ShadedText>();
        breakItem = bi;
    }

    public void addItemDamage(int x, int y, String l, int dir, int lx, int ly){
        itemDamageQueue.add(
            new ShadedText(
                new int[] {255, 0, 0},
                new int[] {20, 20, 20},
                x,
                y,
                17,
                l,
                dir
            )
        );

       id_x = lx;
       id_y = ly; 

       id_true = true;
       id_timer = 5;
    }

    public void draw(Graphics g, JPanel panel){
        boolean delete_g = false;
        ShadedText slm = null;

        for (ShadedText st : itemDamageQueue){
            if (st.count - 1 == 0){
                delete_g = true;
                slm = st;
            }else{
                st.draw(g, inUseFont);
                st.count--;
            }

            switch(st.direction){
                case 0:
                    st.x+=2;
                    break;
                case 1:
                    st.x-=2;
                    break;
            }

            if (st.d == 0){
                st.h = st.h - (st.h / 2);
            }else{
                st.h = st.h + (st.h / 2);
            }
            st.y -= (int)st.h;

            if (st.h < 0.5 && st.d != 1){
                st.h = -1;
                st.d = 1;
            }
        }

        if (delete_g){
            itemDamageQueue.remove(slm);
        }

        if (id_true){
            g.drawImage(breakItem, id_x, id_y, panel);
            id_timer--;
            if (id_timer == -1)id_true = false;
        }
    }
}