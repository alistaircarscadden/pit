package pit;

import java.util.ArrayList;
import java.util.List;

import pit.entities.Entity;
import pit.entities.Player;
import pit.items.Item;
import pit.tools.Line;
import pit.tools.Point;
   
public class World
{
   public int width, height;
   public Tile[][] tiles;
   public List<Entity> entities;
   public List<Item> items;
   public Player player;
   public String message;
   
   public World(int x, int y)
   {
      this.width = x;
      this.height = y;
      this.tiles = realFloorGenerator(x, y);
      this.entities = new ArrayList<Entity>();
      this.items = new ArrayList<Item>();
      this.player = new Player(this, new Point((int)(x/2), (int)(y/2)));
      this.message = new String();
   }
   
   /* Method returns a selected tile in the map,
      if the tile is out of bounds then it returns the BOUNDS tile
      reserved for out of bounds tiles.
      */
   public Tile tile(int x, int y, Tile[][] tile)
   {
      if(x < 0 || x >= width || y < 0 || y >= height)
         return Tile.NONE;
      else
         return tile[x][y];
   }
   
   public Tile tile(int x, int y)
   {
      if(x < 0 || x >= width || y < 0 || y >= height)
         return Tile.NONE;
      else
         return tiles[x][y];
   }
   
   /* Method returns a newly generated level layout
      of size x by y.
      */
   public Tile[][] floorGenerator(int x, int y)
   {
      Tile[][] tiles = new Tile[x][y];
      for (int i = 0; i < x; i++)
      {
         for (int ii = 0; ii < y; ii++)
         {
            if (i == 0 || ii == 0 || i == x-1 || ii == y-1)
               tiles[i][ii] = Tile.WALL;
            else
               tiles[i][ii] = Math.random() > 0.6 ? Tile.WALL : Tile.FLOOR;
         }
      }
      return tiles;
   }
   
   public Tile[][] emptyFloorGenerator(int x, int y)
   {
      Tile[][] tiles = new Tile[x][y];
      for (int i = 0; i < x; i++)
      {
         for (int ii = 0; ii < y; ii++)
         {
            tiles[i][ii] = Tile.WALL;
         }
      }
      for (int i = 1; i < (x-1); i++)
      {
         for (int ii = 1; ii < (y-1); ii++)
         {
               tiles[i][ii] = Tile.FLOOR;
         }
      }
      tiles[25][3] = Tile.STAIRCASE;
      return tiles;
   }
   
   public Point findEmptySpace()
   {
      int x;
      int y;
      do
      {
         x = (int)(Math.random() * width);
         y = (int)(Math.random() * height);
      } while(this.tiles[x][y] != Tile.FLOOR);
      return new Point(x, y);
   }
   
   public Point findEmptySpace(Tile[][] tile)
   {
      int x;
      int y;
      do
      {
         x = (int)(Math.random() * width);
         y = (int)(Math.random() * height);
      } while(tile[x][y] != Tile.FLOOR);
      return new Point(x, y);
   }
      
   public void remove(Entity entity)
   {
      entities.remove(entity);
   }
   
   public void remove(Item item)
   {
      items.remove(item);
   }
   
   public void addItem(Item item, int x, int y)
   {
      item.x = x;
      item.y = y;
      items.add(item);
   }
   
   public void addEntity(Entity entity, int x, int y)
   {
      entity.x = x;
      entity.y = y;
      entities.add(entity);
   }
   
   public void addEntity(Entity entity, Point point)
   {
      entity.x = point.x;
      entity.y = point.y;
      entities.add(entity);
   }
   
   public void addEntity(Entity entity)
   {
      int x, y;
      do
      {
         x = (int)(Math.random() * width);
         y = (int)(Math.random() * height);
      } while (tile(x,y,tiles).solid);
      entity.x = x;
      entity.y = y;
      entities.add(entity);
   }
   
   public Entity entity(int x, int y)
   {
      for (Entity entity : entities)
      {
         if (entity != null)
         {
            if (entity.x == x && entity.y == y)
            // there should not be 2 entities on 1 spot
            // this method returns the first found
               return entity;
         }
      }
      // if there is no entity
      return null;
   }
   
   public Item item(int x, int y)
   {
      for (Item item : items)
      {
         if (item != null)
         {
            if (item.x == x && item.y == y)
            // there should not be 2 entities on 1 spot
            // this method returns the first found
               return item;
         }
      }
      // if there is no entity
      return null;
   }
   
   /* This is Trystan's code from his roguelike tutorial
   http://trystans.blogspot.ca/2011/08/roguelike-tutorial-03-scrolling-through.html
   */
   public Tile[][] smooth(int times) {
      Tile[][] tiles2 = new Tile[width][height];
      for (int time = 0; time < times; time++) {
      
         for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
               int floors = 0;
               int rocks = 0;
            
               for (int ox = -1; ox < 2; ox++) {
                  for (int oy = -1; oy < 2; oy++) {
                     if (x + ox < 0 || x + ox >= width || y + oy < 0
                        || y + oy >= height)
                        continue;
                  
                     if (tiles[x + ox][y + oy] == Tile.FLOOR)
                        floors++;
                     else
                        rocks++;
                  }
               }
               tiles2[x][y] = floors >= rocks ? Tile.FLOOR : Tile.WALL;
            }
         }
      }
      Point portal = findEmptySpace();
      tiles2[portal.x][portal.y] = Tile.STAIRCASE;
      return tiles2;
   }
   
   public void update()
   {
      List<Entity> updateQueue = new ArrayList<Entity>(entities);
      
      for (Entity entity : updateQueue)
      {
         entity.update();
      }
   }
   
   public boolean canSee(int x0, int y0, int x1, int y1)
   {
      Line l = new Line(x0, x1, y0, y1);
      for (Point p : l.points)
      {
         if (tile(p.x, p.y, tiles).solid)
            return false;
      }
      return true;
   }
   
   public void addItemRandomPoint(Item item)
   {
      Point p = findEmptySpace();
      addItem(item, p.x, p.y);
   }
   
   // How many points around a tile are not solid (floor)
   public int numberOfFreeSurroundingTiles(int x, int y, Tile[][] tile)
   {
      int numberOfTiles = 0;
      if(x != 0 && !tile(x-1, y, tile).solid)
         numberOfTiles++;
      if(x != width && !tile(x+1, y, tile).solid)
         numberOfTiles++;
      if(y != 0 && !tile(x, y-1, tile).solid)
         numberOfTiles++;
      if(y != height && !tile(x, y+1, tile).solid)
         numberOfTiles++;
      return numberOfTiles;
   }
   
   public Tile[][] realFloorGenerator(int x, int y)
   {
      Tile[][] tiles = new Tile[x][y];
      int xMod = 0;
      int yMod = 0;
      // Step 1: Fill whole level with solid blocks/walls
      for(int i = 0; i < x; i++)
      {
         for(int ii = 0; ii < y; ii++)
         {
            tiles[i][ii] = Tile.WALL;
         }
      }
      
      // Step 2: Dig one central room
      for(int i = (x/2 - 3); i < (x/2 + 3); i++)
      {
         for(int ii = (y/2 - 3); ii < (y/2 + 3); ii++)
         {
            tiles[i][ii] = Tile.FLOOR;
         }
      }
      // Runs 250 times
      for(int times = 0; times < 250; times++)
      {
         // Step 3: Find a wall in the dungeon
         int i;
         int ii;
         do
         {
            i = (int)(Math.random() * width + 1) - 1;
            ii = (int)(Math.random() * height + 1) - 1;
            //System.out.println("i = " + i + ", ii = " + ii);
         } while(numberOfFreeSurroundingTiles(i, ii, tiles) != 1 || !tile(i, ii, tiles).solid);
         
         // Find out which direction the room is in
         /*
         1: Floor tile is above the wall tile
         2: Floor tile is right of the wall tile
         3: Floor tile is below the wall tile
         4: Floor tile is left of the wall tile
         */
         int freeTile = directionOfFreeTile(i, ii, tiles);         
         
         // Initiates stats for new feature          
         int newFeature = (int)(Math.random() * 3);
         // 2/3 chance that newFeature will be a corridor
         int newFeatureXSize = 0;
         int newFeatureYSize = 0;
         
         // Sets dimensions for new feature
         if(newFeature == 0 && (freeTile == 1 || freeTile == 3))
         {
            newFeatureXSize = 3;
            newFeatureYSize = (int)(Math.random() * 6 + 4);
         }
         else if(newFeature == 0 && (freeTile == 2 || freeTile == 4))
         {
            newFeatureYSize = 3;
            newFeatureXSize = (int)(Math.random() * 6 + 4);
         }
         else
         {
            newFeatureXSize = (int)(Math.random() * 6 + 4);
            newFeatureYSize = (int)(Math.random() * 6 + 4);
         }
         // Determines placement of feature in relation to the door
         // Makes sure that the tile after the door is not always the top left tile of the corridor/room
         int xStartChange = 0;
         int yStartChange = 0;
         
         // Depending on location of wall with respect to previous room, it finds out whether
         // the new feature can be made and makes it
         /*
         Key point: dimensions of the new feature include the walls of that feature
         Meaning that xMod and yMod are used to reposition the feature so that the tile connected to the door
         will be a floor tile, not a wall tile
         */
         switch(freeTile)
         {
            case 1:  xMod = -1;
                     yMod = 0;
                     /*
                     xStartChange = (int)(Math.random() * newFeatureXSize - 2);
                     //You don't want this to be negative
                     if(xStartChange < 0)
                        xStartChange = 0;
                     */
                     // Just...try drawing a picture, it might make sense
                     if(canCreate(i + (xMod + xStartChange), ii + (yMod + yStartChange), newFeatureXSize, newFeatureYSize, tiles))
                     {
                        create(i + (xMod + xStartChange), ii + (yMod + yStartChange), newFeatureXSize, newFeatureYSize, tiles);
                        tiles[i][ii] = Tile.DOOR;
                     }   
                     break;
            case 2:  xMod = 1;
                     yMod = -1;
                     /*
                     yStartChange = (int)(Math.random() * newFeatureYSize - 2);
                     if(yStartChange < 0)
                        yStartChange = 0;
                     */
                     if(canCreate(i + (xMod + xStartChange - newFeatureXSize), ii + (yMod + yStartChange), newFeatureXSize, newFeatureYSize, tiles))
                     {
                        create(i + (xMod + xStartChange - newFeatureXSize), ii + (yMod + yStartChange), newFeatureXSize, newFeatureYSize, tiles);
                        tiles[i][ii] = Tile.DOOR;
                     }
                     break;
            case 3:  xMod = -1;
                     yMod = 1;
                     /*
                     xStartChange = (int)(Math.random() * newFeatureXSize - 2);
                     if(xStartChange < 0)
                        xStartChange = 0;
                     */
                     if(canCreate(i + (xMod + xStartChange), ii + (yMod + yStartChange - newFeatureYSize), newFeatureXSize, newFeatureYSize, tiles))
                     {
                        create(i + (xMod + xStartChange), ii + (yMod + yStartChange - newFeatureYSize), newFeatureXSize, newFeatureYSize, tiles);
                        tiles[i][ii] = Tile.DOOR;
                     }
                     break;
            case 4:  xMod = 0;
                     yMod = -1;
                     /*
                     yStartChange = (int)(Math.random() * newFeatureYSize - 2);
                     if(yStartChange < 0)
                        yStartChange = 0;
                     */
                     if(canCreate(i + (xMod + xStartChange), ii + (yMod + yStartChange), newFeatureXSize, newFeatureYSize, tiles))
                     {
                        create(i + (xMod + xStartChange), ii + (yMod + yStartChange), newFeatureXSize, newFeatureYSize, tiles);
                        tiles[i][ii] = Tile.DOOR;
                     }
                     break;
         }
      }
      
      Point portal = findEmptySpace(tiles);
      tiles[portal.x][portal.y] = Tile.STAIRCASE;

      return tiles;
   }
   
   // Tells you how many tiles aren't being used (Number of wall tiles remaining)
   // Might not be needed depending on whether we base the generation off how many times it goes through the loop
   // Or on how many tiles are left
   public int numberOfFreeTiles(Tile[][] layout)
   {
      int numberOfWallBlocks = 0;
      for(int i = 0; i < layout.length; i++)
      {
         for(int ii = 0; ii < layout[i].length; ii++)
         {
            if(layout[i][ii] == Tile.WALL)
            {
               numberOfWallBlocks++;
            }
         }
      }
      return (layout.length * layout[0].length - numberOfWallBlocks);
   } 
   
   public int directionOfFreeTile (int x, int y, Tile[][] tile)
   {
      int direction = 0;
      // Above block
      if(!tile(x, y-1, tile).solid)
         direction = 1;
      // Right of block
      if(!tile(x+1, y, tile).solid)
         direction = 2;
      // Below block
      if(!tile(x, y+1, tile).solid)
         direction = 3;
      // Left of block
      if(!tile(x-1, y, tile).solid)
         direction = 4;
         
      return direction;
   }
   
   // Checks whether the rectangle you want for your feature is currently unused
   public boolean canCreate(int x, int y, int xSize, int ySize, Tile[][] tile)
   {
      boolean possible = true;
      for(int i = x; i < (x + xSize) && possible; i++)
      {
         for(int ii = y; ii < (y + ySize) && possible; ii++)
         {
            if(tile(i, ii, tile) != Tile.WALL)
               possible = false;
         }
      }
      
      return possible;
   }
   
   // Makes a room/corridor
   // Again, the dimensions account for walls surrounding the room
   public void create(int x, int y, int xSize, int ySize, Tile[][] tile)
   {
      for(int i = x + 1; i < (x + xSize - 1); i++)
      {
         for(int ii = y + 1; ii < (y + ySize - 1); ii++)
         {
            tile[i][ii] = Tile.FLOOR;
         }
      }
   }
}
