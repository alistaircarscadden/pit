package pit.entities;

import java.awt.Color;

import pit.World;

public class Wizard extends Monster
{   
   public Wizard(World world)
   {
      super(world);
      this.symbol = 'w';
      this.color = new Color(153, 50, 204);
      this.health = 8;
      this.strength = 2;
      this.name = "Wizard";
   }
   
   Wizard(World world, int x, int y)
   {
      super(world, x, y, 6, 1, 'w', new Color(153, 50, 204));
      this.name = "Wizard";
   }
   
   /* Needs to be fixed so that wizard doesn't teleport before attacking and always teleports after */
   public void update()
   {
      int x = (world.player.x - this.x);
      int y = (world.player.y - this.y);
      if (x != 0)
         x /= Math.abs(world.player.x - this.x);
      if (y != 0)
         y /= Math.abs(world.player.y - this.y);
      this.move(x, 0);
      this.move(0, y);
      
      int secondX = (world.player.x - this.x);
      int secondY = (world.player.y - this.y);
      if((secondX == 0 && secondY == 1) || (secondX == 1 && secondY == 0) || (secondX == -1 && secondY == 0) || (secondX == 0 && secondY == -1))
      {
         if(secondX == x && secondY == y)
         { 
            int newX = (int)(Math.random() * 20 - 10);
            int newY = (int)(Math.random() * 20 - 10);
            this.move(newX, 0);
            this.move(0, newY);
         }
      }
   }
}