

2
3
4
5
6
7
8
9
10
11
12
13
14
15
16
17
18
19
20
21
22
23
24
25
26
27
28
29
30
 
import java.awt.Font;
 
public class DTextModel extends DShapeModel {
    private String text;
    private Font font;
     
    public DTextModel() {
        super();
        this.text = "";
        this.font = null;
    }
     
    public void setText(String text) {
        this.text = text;
        notifyListeners();
    }
     
    public String getText() {
        return this.text;
    }
     
    public void setFont(String font) {
        this.font = new Font(font, Font.PLAIN, 14);
        notifyListeners();
    }
     
    public Font getFont() {
        return this.font;
    }
}
 
