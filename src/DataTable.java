import javax.swing.table.AbstractTableModel;
import java.util.LinkedList;
import java.util.List;

/**
 * Subclass of the AbstractTableModel.
 * Utilizing the adapter pattern, we dynamically pull the properties of the shape models.
 * The DataTable will show the shapes in the same order as the user has placed them on
 * canvas.
 * Adding and removing shapes will alter the table by adding or removing their
 * respective rows.
 */
public class DataTable extends AbstractTableModel
{
   //Table elements
   public String[] columnNames;
   public List<DShapeModel> dataModels;

   /**
    * Constructor for the Data Table.
    * Can be adapted in future to include more properties.
    */
   public DataTable()
   {
      dataModels = new LinkedList<>();
      columnNames = new String[]{"X", "Y", "Width", "Height"};
   }

   /**
    * Overrides the getRowCount method of the AbstractTableModel.
    *
    * @return dataModels.size - the total amount of objects that are on the screen.
    */
   @Override
   public int getRowCount()
   {
      return dataModels.size();
   }

   /**
    * Overrides the getColumnCount method of the AbstractTableModel.
    *
    * @return columnNames.length - the total amount of columns (a fixed value of 4).
    */
   @Override
   public int getColumnCount()
   {
      return columnNames.length;
   }

   /**
    * Overrides the getValueAt method of the AbstractTableModel.
    *
    * @param selectedShape - the DShape that is selected on the canvas.
    * @param property      - the x coordinate, y coordinate, width, or height of the
    *                      selected shape.
    * @return the selected shape's respective property.
    */
   @Override
   public Object getValueAt(int selectedShape, int property)
   {
      switch (property)
      {
         case 0:
            return dataModels.get(selectedShape).getX();
         case 1:
            return dataModels.get(selectedShape).getY();
         case 2:
            return dataModels.get(selectedShape).getWidth();
         case 3:
            return dataModels.get(selectedShape).getHeight();
      }
      return null;
   }

   /**
    * Overrides the getColumnName method of the AbstractTableModel.
    *
    * @param col - the column index in the array of column names.
    * @return columnNames[col] - the name of the column.
    */
   @Override
   public String getColumnName(int col)
   {
      return columnNames[col];
   }

   /**
    * Clears the table of all elements.
    */
   public void reset()
   {
      dataModels.clear(); //Empties the arrayList.
      fireTableDataChanged();
   }

   /**
    * Adds a new row to the table.
    *
    * @param shapeToAdd - the newly constructed shape to add.
    */
   public void addNewRow(DShapeModel shapeToAdd)
   {
      dataModels.add(0, shapeToAdd);
      fireTableDataChanged();
   }

   /**
    * Updates the rows from the table when a change occurs.
    *
    * @param shapeModel - the DShapeModel that has been changed.
    */
   public void updateRow(DShapeModel shapeModel)
   {
      //Finds the specific index of the table to change
      for (int i = 0; i < dataModels.size(); i++)
      {
         if (dataModels.get(i).getID() == shapeModel.getID())
         {
            dataModels.set(i, shapeModel);
            fireTableRowsUpdated(i, i);
            break;
         }
      }
   }

   /**
    * Removes a row from the table when a shape is removed.
    *
    * @param shapeToRemove - the shape to remove from the table.
    */
   public void removeRow(DShapeModel shapeToRemove)
   {
      //Finds the specific index of the table to change
      for (int i = 0; i < dataModels.size(); i++)
      {
         if (dataModels.get(i).getID() == shapeToRemove.getID())
         {
            dataModels.remove(i);
            break;
         }
      }
      fireTableDataChanged();
   }

   /**
    * Moves a row up when a shape is move to the front layer on the canvas.
    *
    * @param shapeChanged - the shape's row to move up the table.
    */
   public void moveRowUp(DShapeModel shapeChanged)
   {
      //Finds the specific index of the table to change
      for (int i = 1; i < dataModels.size(); i++)
      {
         if (dataModels.get(i).getID() == shapeChanged.getID())
         {
            DShapeModel tempModel = dataModels.remove(i);
            dataModels.add(0, tempModel);
            fireTableRowsUpdated(0, 0);
            fireTableRowsUpdated(i, i);
            break;
         }
      }
   }

   /**
    * Moves a row down when a shape is moved to the bottom layer on the canvas.
    *
    * @param shapeChanged - the shape's row to move down the table.
    */
   public void moveRowDown(DShapeModel shapeChanged)
   {
      //Finds the specific index of the table to change
      for (int i = 0; i < dataModels.size() - 1; i++)
      {
         if (dataModels.get(i).getID() == shapeChanged.getID())
         {
            DShapeModel tempModel = dataModels.remove(i);
            dataModels.add(tempModel);
            fireTableRowsUpdated(dataModels.size(), dataModels.size());
            fireTableRowsUpdated(i, i);
            break;
         }
      }
   }
}
