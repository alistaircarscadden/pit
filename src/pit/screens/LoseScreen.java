package pit.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import pit.tools.ArtReader;
import pit.tools.ConsoleHelper;
import pit.tools.MenuItemList;

public class LoseScreen implements Screen {

   public MenuItemList menuItems = new MenuItemList("Continue");
   String name;
   int level;
   
   public LoseScreen(String name, int level)
   {
      this.name = name;
      this.level = level;
   }
   
	@Override
	public void displayOutput(AsciiPanel terminal) {
      String[] loseArt = ArtReader.get("lose");
      String[] skull = ArtReader.get("loseskull");
      ConsoleHelper.writeArray(terminal, skull, 5, 5, new Color(50, 50, 50), false);
      ConsoleHelper.writeArray(terminal, loseArt, 29, 10, new Color(255, 200, 80), false);
      menuItems.drawList(terminal, 40);
	}

	@Override
	public Screen respondToUserInput(KeyEvent key) {
		/* Code for choosing menu items. */
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
                  return new ScoreScreen(name, level);
            }
      }
      /* /menu code */
      return this;
	}
}
