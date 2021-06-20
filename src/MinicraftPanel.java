import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.Point;
import java.awt.image.BufferedImage;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.Timer;
import javax.swing.WindowConstants;

import java.awt.Font;
import java.io.InputStream;
import java.lang.Exception;

import java.awt.Color;

import Render.BufferedBlock;
import Block.BlockAlias;
import World.World;
import Player.PlayerSprite;
import Util.Logger;
import UI.HUD;
import Block.Entity.BlockEntity;
import Block.BlockTile;


class MinicraftPanel extends JPanel implements ActionListener, KeyListener {
    private static final Dimension PANEL_SIZE = new Dimension(960, 720);

    private static final int REFRESH_RATE = (int)(1000.0 / 60.0);
    //private static final int REFRESH_RATE = 0;
    private Timer timer = new Timer(REFRESH_RATE, this);

    public World world = new World(512, 512, 25, 15);
    public PlayerSprite playerSprite = new PlayerSprite("Resources/player/player_right.png",
                                                        "Resources/player/player_left.png",
                                                        "Resources/player/player_front.png",
                                                        "Resources/player/player_back.png");
    public Logger logger = new Logger(Logger._prefix);

    public HUD hud = new HUD();
    public BlockEntity runnableBlockEntity = null;
    public int blockEntityOpen = 0;
    

    public MinicraftPanel() {
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        setBackground(Color.BLACK);
        
        this.setCursor(this.getToolkit().createCustomCursor(
            new BufferedImage(3, 3, BufferedImage.TYPE_INT_ARGB), new Point(0, 0),
            "null"));

        world.generate(hud);
        timer.start();
    }

    public Dimension getPreferredSize() {
        return PANEL_SIZE;
    }

    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        world.draw(this, g);
        if (world.getPlayer().cdown_timer > 0){
            world.getPlayer().cdown_timer--;
        }
        hud.draw(g, world, this);
        
        playerSprite.drawSpriteImage((12*32)+80, (7*32)+64, g, this, world.getBufferedBlock(), world.getPlayer().getCurrentDirection());

        if (blockEntityOpen == 1){
            runnableBlockEntity.draw(g, this, hud.c128_font.deriveFont(Font.PLAIN, 16f), hud.getInventory());
        }
    }

    public void actionPerformed(ActionEvent e) {
        repaint();
    }

    public void keyPressed(KeyEvent e) {
        char code = e.getKeyChar();
        boolean bf_containsEntity = false;
        if (hud.getInventory().isInventoryOpen != 1 && blockEntityOpen == 0){
            world.getPlayer().move(code, world);
        }
        hud.getInventory().update(code, world);
        if (code == 'e'){
            BlockTile bt = world.getCurrentPlayerBlockTile();
            if (bt.containsEntity){
                bf_containsEntity = true;
                switch(blockEntityOpen){
                    case 0:
                        blockEntityOpen = 1;
                        break;
                    case 1:
                        blockEntityOpen = 0;
                        break;
                }
                runnableBlockEntity = bt.getBlockEntity();
            }
            if (blockEntityOpen == 0 && !bf_containsEntity){
                switch(hud.getInventory().isInventoryOpen){
                    case 0:
                        hud.getInventory().isInventoryOpen = 1;
                        break;
                    case 1:
                        hud.getInventory().isInventoryOpen = 0;
                        break;
                }
            }
            
        }
        if (blockEntityOpen == 1){
            runnableBlockEntity.update(code, hud.getInventory());
        }
    }

    public void keyTyped(KeyEvent e) {}
    public void keyReleased(KeyEvent e) {
    }
}