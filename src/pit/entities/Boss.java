package pit.entities;

import java.awt.Color;

import pit.World;

public class Boss extends Monster
{
   Boss(World world)
   {
      super(world);
      this.symbol = 'B';
      this.color = new Color(200, 200, 200);
      this.health = 15;
      this.strength = 3;
      this.name = "Boss";
   }
   
   Boss(World world, int x, int y)
   {
      super(world, x, y, 15, 3, 'B', new Color(150, 150, 150));
      this.name = "Boss";
   }
   public Boss(World world, int x, int y, int health, int strength)
   {
      super(world, x, y, health, strength, 'B', new Color(150, 150, 150));
      this.name = "Boss";
   }
   
   public void update()
   {
      int x = (world.player.x - this.x);
      if (x != 0)
         x /= Math.abs(world.player.x - this.x);
      int y = (world.player.y - this.y);
      if (y != 0)
         y /= Math.abs(world.player.y - this.y);
      this.move(x, 0);
      this.move(0, y);
   }
}
