package pit.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import pit.tools.ArtReader;
import pit.tools.ConsoleHelper;
import pit.tools.MenuItemList;
import pit.tools.SimpleFiles;

public class ScoreScreen implements Screen {

   String[] title;
   public MenuItemList menuItems = new MenuItemList("Play", "Menu", "Info");
   Score[] scores;
   private Color titleColor;
   
   // When entering from game, you have a new score
   public ScoreScreen(String name, int level)
   {
      titleColor = new Color((int)(Math.random() * 100 + 155), (int)(Math.random() * 255), (int)(Math.random() * 100));
      title = ArtReader.get("highscores");
      Score score = new Score(name, level);
      scores = loadScores();
      scores = addScore(scores, score);
      String[] scoresInString = new String[scores.length];
      for (int i = 0; i < scores.length; i++)
         scoresInString[i] = scores[i].toString();
      try
      {
      SimpleFiles.writeArray("scores.txt", scoresInString);
      } catch(Exception e){}
   }
   
   // WHen entering from a menu, no new score just view
   public ScoreScreen()
   {
      title = ArtReader.get("highscores");
      scores = loadScores();
   }
   
   // bootleg code eh
   public Score[] loadScores()
   {
      String[] scores = new String[0];
      
      try
      {
         scores = SimpleFiles.readArray("scores.txt");
      }
      catch (Exception e)
      {
         System.out.println("can't find scores file");
      }
      
      Score[] scores2 = new Score[scores.length];
      for (int i = 0; i < scores2.length; i++)
      {
         scores2[i] = new Score(scores[i]);
      }
      
      return scores2;
   }
   
   @Override
   public void displayOutput(AsciiPanel terminal)
   {
      ConsoleHelper.writeCenterArray(terminal, title, 5, titleColor);

      for (int i = 0; i < scores.length; i++)
      {
         terminal.write(scores[i].name + " died on level " + scores[i].level, 30, i + 20);
      }
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
                  return new NameScreen();
               case 1:
                  return new StartScreen();
               case 2:
                  return new InfoScreen();
            }
      }
      /* /menu code */
   	
      return this;
   }
   
   public static class Score
   {
      public String name;
      public int level;
      public Score(String name, int level)
      {
         this.name = name;
         this.level = level;
      }
      
      public String toString()
      {
         return this.level + "~" + this.name;
      }
      
      public Score(String scoreString)
      {
         String[] scoreStringA = scoreString.split("~");
         this.level = Integer.parseInt(scoreStringA[0]);
         this.name = scoreStringA[1];
      }
   }
   
   public static Score[] addScore(Score[] scores, Score score)
   {
      // If Score is TOO LOW
      if (scores[scores.length - 1].level > score.level)
         return scores;
      // If Score is NOT TOO LOW
      else
      {
         int position = 0;
         for (int i = 0; i < scores.length; i++)
         {
            if (score.level > scores[i].level)
            {
               position = i; 
               break;
            }
         }
         
         for (int i = scores.length - 1; i > position; i--)
         {
            scores[i] = scores[i - 1];
         } 
         
         scores[position] = score;
      }
      return scores;
   }
}
