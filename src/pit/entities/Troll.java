package pit.entities;

import java.awt.Color;

import pit.Tile;
import pit.World;

public class Troll extends Monster
{
   public Troll(World world)
   {
      super(world);
      this.symbol = 't';
      this.color = new Color(0, 128, 255);
      this.health = 10;
      this.strength = 1;
      this.name = "Troll";
   }
   
   Troll(World world, int x, int y)
   {
      super(world, x, y, 10, 1, 't', new Color(0, 128, 255));
      this.name = "Troll";
   }
   
   /* Needs to be fixed so that trolls move multiple spaces without jumping over player */
   public void update()
   {
      int x = (world.player.x - this.x);
      if (Math.abs(x) != 1 && Math.abs(x) != 2)
      {
         if((x < 0 && world.tile(this.x - 1, this.y) != Tile.WALL) || (x > 0 && world.tile(this.x + 1, this.y) != Tile.WALL))   
            x *= 2;
      }
      if (x != 0)
         x /= Math.abs(world.player.x - this.x);
      int y = (world.player.y - this.y);
      if(Math.abs(y) != 1 && Math.abs(y) != 2)
      {
         if((y < 0 && world.tile(this.x, this.y - 1) != Tile.WALL) || (y > 0 && world.tile(this.x, this.y + 1) != Tile.WALL))   
            y *= 2;
      }
      if (y != 0)
         y /= Math.abs(world.player.y - this.y);
         
      this.move(x, 0);
      this.move(0, y);
   }
   
}
