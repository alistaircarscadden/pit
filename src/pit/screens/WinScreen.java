package pit.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import pit.tools.ArtReader;
import pit.tools.ConsoleHelper;
import pit.tools.MenuItemList;

public class WinScreen implements Screen {
   public MenuItemList menuItems = new MenuItemList("Continue");
   public String name;
   public int level;
   public String[] winMessage;
   
   public WinScreen(String name, int level)
   {
      this.name = name;
      this.level = level;
      winMessage = ArtReader.get("win");
   }
   
	@Override
	public void displayOutput(AsciiPanel terminal) {
      ConsoleHelper.writeCenterArray(terminal, winMessage, 4, new Color(255, 0, 0));
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
