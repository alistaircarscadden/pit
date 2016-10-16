package pit.screens;

import java.awt.Color;
import java.awt.event.KeyEvent;

import asciiPanel.AsciiPanel;
import pit.Tile;
import pit.World;
import pit.entities.Boss;
import pit.entities.Entity;
import pit.entities.Goblin;
import pit.entities.Player;
import pit.entities.Troll;
import pit.entities.Turtle;
import pit.entities.Wizard;
import pit.items.AntidotePotion;
import pit.items.HealthPotion;
import pit.items.Item;
import pit.items.StrengthPotion;
import pit.items.Weapon;
import pit.tools.ArtReader;
import pit.tools.ConsoleHelper;
import pit.tools.Point;
 
public class PlayScreen implements Screen {
   private World world;
   private String name;
   
   private int level; 
   private int worldDispWidth = 100;
   private int worldDispHeight = 40;
   private int worldDispX;
   private int worldDispY;
   
   private String[] gui;
   
   public PlayScreen(String name)
   {
      this.name = name;
      level = 1;
      initializeWorld(level);
      gui = ArtReader.get("playscreeninfo");
   }
   
   @Override
   public void displayOutput(AsciiPanel terminal) {
      drawWorldTiles(terminal);
      drawWorldItems(terminal);
      drawWorldEntities(terminal);
      drawGui(terminal);
      drawItems(terminal);
      world.player.inventory.drawInventory(terminal);
      terminal.write(world.message, 0, 49);
   }

   @Override
   public Screen respondToUserInput(KeyEvent key) {
      switch (key.getKeyCode()){
         /* Ingame player controls (moving, manipulating the world) */
         case KeyEvent.VK_NUMPAD1:
            world.player.move(-1, 1);
            world.update();
            break;
         case KeyEvent.VK_DOWN:
         case KeyEvent.VK_NUMPAD2: 
            world.player.move(0, 1);
            world.update();
            break;
         case KeyEvent.VK_NUMPAD3:
            world.player.move(1, 1);
            world.update();
            break;
         case KeyEvent.VK_LEFT: 
         case KeyEvent.VK_NUMPAD4:
            world.player.move(-1, 0);
            world.update();
            break;
         case KeyEvent.VK_W:
         case KeyEvent.VK_NUMPAD5:
               // No move: wait
            world.update();
            break;
         case KeyEvent.VK_RIGHT: 
         case KeyEvent.VK_NUMPAD6:
            world.player.move(1, 0);
            world.update();
            break;
         case KeyEvent.VK_NUMPAD7:
            world.player.move(-1, -1);
            world.update();
            break;
         case KeyEvent.VK_UP:
         case KeyEvent.VK_NUMPAD8:
            world.player.move(0, -1);
            world.update();
            break;
         case KeyEvent.VK_NUMPAD9:
            world.player.move(1, -1);
            world.update();
            break;
         case KeyEvent.VK_G:
            world.player.pickUp();
            break;
         case KeyEvent.VK_E:
            world.player.use();
            break;
         case KeyEvent.VK_Q:
            world.player.drop();
            break;
        /* end Player ingame controls */
      }
      /* GUI controls */
      if ((int)key.getKeyChar() >= 48 && (int)key.getKeyChar() <= 57)
      {
         world.player.inventory.select((int)key.getKeyChar() - 49);
         /* Since the 0 key is on the right side, it shouldn't choose the left inventory slot.
            this fixes that */
         if ((int)key.getKeyChar() == 48)
            world.player.inventory.select(9);
      }
      if (world.player.health <= 0)
         return new LoseScreen(name, level);
      if(world.tiles[world.player.x][world.player.y] == Tile.STAIRCASE)
      {
         Player temp = world.player;
         level++;
         initializeWorld(level);
         if(level % 5 == 0)
            world.player = new Player(world, temp, new Point(25, 48));
         else
            world.player = new Player(world, temp, new Point((int)(world.width/2), (int)(world.height/2)));
         world.message = "You descended to level " + level;
      }
      if(level > 20)
      {
         return new WinScreen(name, level);
      }
         
      return this;
   }
    
   public void drawWorldEntities(AsciiPanel terminal)
   {
      for (Entity entity : world.entities)
      {
         /* if the entity is within the region being displayed => print it */
         if (entity.x >= worldDispX && entity.x < worldDispX+worldDispWidth && entity.y >= worldDispY && entity.y < worldDispY+worldDispHeight)
            terminal.write(entity.symbol, entity.x-worldDispX, entity.y-worldDispY, entity.color);
      }
      
      /* Draw the player
         the player is drawn in the center of the screen
         and the world moves around him
         */
      terminal.write(world.player.symbol, worldDispWidth/2, worldDispHeight/2, world.player.color);
   }
   
   public void drawWorldItems(AsciiPanel terminal)
   {
      for (Item item : world.items)
      {
         /* if the item is within the region being displayed => print it */
         if (item.x >= worldDispX && item.x < worldDispX+worldDispWidth && item.y >= worldDispY && item.y < worldDispY+worldDispHeight)
            terminal.write(item.symbol, item.x-worldDispX, item.y-worldDispY, item.color);
      }
   }
   
   public void drawWorldTiles(AsciiPanel terminal)
   {
      worldDispX = world.player.x - (worldDispWidth/2);
      worldDispY = world.player.y - (worldDispHeight/2);
      
      for (int o = 0; o < worldDispWidth; o++)
      {
         for (int p = 0; p < worldDispHeight; p++)
         {
            terminal.write(world.tile(worldDispX + o, worldDispY + p).symbol, o, p, world.tile(worldDispX + o, worldDispY + p).color);
         }
      }
   }

   public void drawGui(AsciiPanel terminal)
   {
      /* Text at below the map */
      ConsoleHelper.writeArray(terminal, gui, 0, 40);
      
      /* Draw player name beside "name:" */
      terminal.write(name.length() > 30 ? name.substring(0, 30) : name, 13, 40, new Color(130, 255, 130));
      
      /* Draw level # beisde "level:" */
      terminal.write(level + "", 13, 41);
      
      /* Draw health hearts,
         if the health hearts exceed the size of the screen
         don't draw the health hearts, draw a #
         */
      for (int i = 0; i < world.player.health; i++)
         if (65+i < 100)
            terminal.write((char)3, 65+i, 40, AsciiPanel.red);
         else
         {
            for (int j = 65; j < 100; j++)
               terminal.write(' ', j, 40);
            terminal.write(world.player.health + "   ", 65, 40, AsciiPanel.red);
         }
     
      /* Draw strength numbers */
      terminal.write(world.player.strength + "", 65, 41, new Color(255, 128, 0));
      terminal.write(" +" + ((Weapon)world.player.equipSword).damage + "", 66 + (int)Math.log(world.player.strength), 41, new Color(255, 50, 0));
   }

   public void drawItems(AsciiPanel terminal)
   {
      /* There are 3 items to draw
         item equipped
         item selected
         item on floor
         */
      Item equipped = world.player.equipSword;
      Item selected = world.player.inventory.item(world.player.inventory.selected);
      Item floor = world.item(world.player.x, world.player.y);
      int y = 42;
      int xEquipped = 0;
      int xSelected = (int)(100 * 0.33);
      int xFloor = (int)(100 * 0.66);
      for (int i = 0; i < 4; i++)
      {
         terminal.write((char)179, xSelected - 1, y + i);
         terminal.write((char)179, xFloor - 1, y + i);
      }
      
      if (equipped != null)
      {
         if (equipped.name.length() < 30)
            terminal.write(equipped.name, xEquipped, y, equipped.color);
         else
            terminal.write(equipped.name.substring(0, 29), xEquipped, y, equipped.color);
         if (equipped.tooltip.length() < 30)
            terminal.write(equipped.tooltip, xEquipped, y+1, equipped.color);
         else
            terminal.write(equipped.tooltip.substring(0, 29), xEquipped, y+1, equipped.color);
         if (equipped instanceof Weapon)
         {
            terminal.write("Req. strength: " + ((Weapon)equipped).strength + "", xEquipped, y+2);
            terminal.write("Damage: " + ((Weapon)equipped).damage + "", xEquipped, y+3);
         }
      }
      
      if (selected != null)
      {
         if (selected.name.length() < 30)
            terminal.write(selected.name, xSelected, y, selected.color);
         else
            terminal.write(selected.name.substring(0, 29), xSelected, y, selected.color);
         if (selected.tooltip.length() < 30)
            terminal.write(selected.tooltip, xSelected, y+1, selected.color);
         else
            terminal.write(selected.tooltip.substring(0, 29), xSelected, y+1, selected.color);
         if (selected instanceof Weapon)
         {
            Color strengthColor = ((Weapon)selected).strength > world.player.strength ? new Color(255, 0, 0) : new Color(0, 255, 0);
            terminal.write("Req. strength: " + ((Weapon)selected).strength + "", xSelected, y+2, strengthColor);
            Color damageColor = ((Weapon)selected).damage < ((Weapon)world.player.equipSword).damage ? new Color(255, 0, 0) : new Color(0, 255, 0);
            terminal.write("Damage: " + ((Weapon)selected).damage + "", xSelected, y+3, damageColor);
         }
      }
      
      if (floor != null)
      {
         if (floor.name.length() < 30)
            terminal.write(floor.name, xFloor, y, floor.color);
         else
            terminal.write(floor.name.substring(0, 29), xFloor, y, floor.color);
         if (floor.tooltip.length() < 30)
            terminal.write(floor.tooltip, xFloor, y+1, floor.color);
         else
            terminal.write(floor.tooltip.substring(0, 29), xFloor, y+1, floor.color);
         if (floor instanceof Weapon)
         {
            Color strengthColor = ((Weapon)floor).strength > world.player.strength ? new Color(255, 0, 0) : new Color(0, 255, 0);
            terminal.write("Req. strength: " + ((Weapon)floor).strength + "", xFloor, y+2, strengthColor);
            Color damageColor = ((Weapon)floor).damage < ((Weapon)world.player.equipSword).damage ? new Color(255, 0, 0) : new Color(0, 255, 0);
            terminal.write("Damage: " + ((Weapon)floor).damage + "", xFloor, y+3, damageColor);
         }
      }
   }
   
   private void initializeWorld(int level)
   {
      // i had a stroke so i had to move these out of sight
      double gInit = 1.0;
      double wInit = 0.4; 
      double tInit = 0.7;
      double tuInit = 1.0; 
      double apInit = 8;
      double spInit = 8.0;
      double hpInit = 10.0; 
      int gOnScreen;
      int wOnScreen;
      int tOnScreen;
      int tuOnScreen;
      int apOnScreen;
      int spOnScreen;
      int hpOnScreen;
      double spMultiplier = 0.9;
      double apMultiplier = 0.9;
      double hpMultiplier = 0.9;
      double tMultiplier = 1.5;
      double gMultiplier = 1.5;
      double tuMultiplier = 1.5;
      double wMultiplier = 1.2; 
        
      world = new World(100, 100);
      // Empty room for boss battle
      if(level %  5 == 0)
      {
         world.tiles = world.emptyFloorGenerator(world.width, world.height);
         int bossStrength = (level/5) + 2;
         int bossHealth = (level/5 + 3) * 4;
         world.addEntity(new Boss(world, 25, 6, bossHealth, bossStrength), new Point(25, 6));
      }
      // Regular room
      else
      {
         world.tiles = world.realFloorGenerator(world.width, world.height);
         // Level 2 it would be from 2-4, max 7
         // Level 3 it would be from 3-6, max 7
         // Level 4 it would be from 3-8, max 7, thus 3-7 (more likely to be 7)
         // Level 8 it would be from 8-16, max 7, thus 7 (100%)
         int numberOfWeapons = Math.min(7, ((int)(Math.random() * level) + level));
         for(int i = 0; i < numberOfWeapons; i++)
         {
            int typeOfWeapon = (int)(Math.random() * (level/3));
            switch(typeOfWeapon)
            {
               case 0:  world.addItemRandomPoint(new Weapon('D', "Dagger", "Light and easy to use!", new Color(255, 255, 255), 2, 2));
                  break;
               case 1:  world.addItemRandomPoint(new Weapon('H', "Hammer", "A hefty hammer!", new Color(255, 255, 255), 4, 4));
                  break;
               case 2:  world.addItemRandomPoint(new Weapon('S', "Sword", "A popular weapon of choice!", new Color(255, 255, 255), 6, 6));
                  break;
               case 3:  world.addItemRandomPoint(new Weapon('M', "Mace", "A most peculiar weapon!", new Color(255, 255, 255), 8, 8));
                  break;
               case 4:
               default: world.addItemRandomPoint(new Weapon('A', "Battle Axe", "Very heavy and strong!", new Color(255, 255, 255), 10, 10));
                  break;
            }
         }
          
         gOnScreen = (int)(Math.pow(gMultiplier, level - 1) * gInit);
         tOnScreen = (int)(Math.pow(tMultiplier, level - 1) * tInit);
         tuOnScreen = (int)(Math.pow(tuMultiplier, level - 1) * tuInit);
         wOnScreen = (int)(Math.pow(wMultiplier, level - 1) * wInit); 
         spOnScreen = (int)(Math.pow(spMultiplier, level - 1) * spInit);
         hpOnScreen = (int)(Math.pow(hpMultiplier, level - 1) * hpInit);
         apOnScreen = (int)(Math.pow(apMultiplier, level - 1) * apInit);
         for (int i = 0; i < hpOnScreen; i++)
            world.addItemRandomPoint(new HealthPotion());
         for (int i = 0; i < spOnScreen; i++)
            world.addItemRandomPoint(new StrengthPotion());
         for (int i = 0; i < apOnScreen; i++)
            world.addItemRandomPoint(new AntidotePotion(1));
         for (int i = 0; i < gOnScreen; i++)
            world.addEntity(new Goblin(world), world.findEmptySpace());
         for (int i = 0; i < tOnScreen; i++)
            world.addEntity(new Troll(world), world.findEmptySpace());
         for (int i = 0; i < tuOnScreen; i++)
            world.addEntity(new Turtle(world), world.findEmptySpace());
         for (int i = 0; i < wOnScreen; i++)
            world.addEntity(new Wizard(world), world.findEmptySpace());
      }
      // Algorithms for enemies
   }
}
