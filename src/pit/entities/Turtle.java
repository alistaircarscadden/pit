package pit.entities;

import java.awt.Color;

import pit.World;

public class Turtle extends Monster
{
   public Turtle(World world)
   {
      super(world);
      this.symbol = 'b';
      this.color = new Color(255,0,127);
      this.health = 30;
      this.strength = 0;
      this.name = "Turtle";
   }
   
   Turtle(World world, int x, int y)
   {
      super(world, x, y, 30, 0, 'b', new Color(255,0, 127));
      this.name = "Turtle";
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
