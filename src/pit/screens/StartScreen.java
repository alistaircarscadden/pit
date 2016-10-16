package pit.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import pit.World;
import pit.tools.ArtReader;
import pit.tools.ConsoleHelper;
import pit.tools.MenuItemList;

public class StartScreen implements Screen {
   public MenuItemList menuItems = new MenuItemList("Singleplayer", "Scores", "Info", "Exit");
   private String[] title;
   private Color titleColor;
   private World world;
   
   // Think of this as if its setup() in Processing
   public StartScreen()
   {
         titleColor = new Color((int)(Math.random() * 100 + 155), (int)(Math.random() * 255), (int)(Math.random() * 100));
         title = ArtReader.get("title");
         world = new World(100, 50);
         //world.tiles = world.smooth(8);
   }

   // Think of this as if its draw() in Processing
   @Override 
   public void displayOutput(AsciiPanel terminal)
   {
      drawWorldTiles(terminal);
      ConsoleHelper.rectDouble(terminal, 3, 3, 94, 20);
      ConsoleHelper.writeCenterArray(terminal, title, 5, titleColor);
      menuItems.drawList(terminal, 15);
   }

   // This is the KeyListener
   @Override
   public Screen respondToUserInput(KeyEvent key) {
      /* Code for choosing menu items. */
      
      /* ! HELP SCREENS ! */
      /*
      if (key.getKeyCode() == KeyEvent.VK_S)
         return new LetterScreen();
      if (key.getKeyCode() == KeyEvent.VK_R)
         return new RectangleScreen();
      */
      /* end HELP SCREENS */
      
      switch (key.getKeyCode()){
         case KeyEvent.VK_UP: 
            this.menuItems.select(-1);
            break;
         case KeyEvent.VK_DOWN: 
            this.menuItems.select(1);
            break;
         case KeyEvent.VK_ENTER:
            switch(menuItems.selected){
               case 0:
                  return new NameScreen();
               case 1:
                  return new ScoreScreen();
               case 2:
                  return new InfoScreen();
               case 3:
                  System.exit(0);
            }
      }
      /* /menu code */
   	
      return this;
   }
   
   public void drawWorldTiles(AsciiPanel terminal)
   {
      for (int o = 0; o < 100; o++)
      {
         for (int p = 0; p < 50; p++)
         {
            terminal.write(world.tile(o, p).symbol, o, p, world.tile(o, p).color);
         }
      }
   }
}
