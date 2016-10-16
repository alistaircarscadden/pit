package pit.tools;

import asciiPanel.AsciiPanel;
import java.awt.*;

public class MenuItem
{
   public String text;
   public Color color;
   public MenuItem(String text)
   {
      this.text = text;
      this.color = AsciiPanel.brightWhite;
   }
}