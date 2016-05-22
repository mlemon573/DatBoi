import java.util.ArrayList;
import javax.swing.table.AbstractTableModel;
public class DataTable extends AbstractTableModel
{
    //Table elements
    public String[] columnNames;
    public ArrayList<DShapeModel> dataModels;



    public DataTable() {
        dataModels = new ArrayList<>();
        //columnNames = new String[]{"X", "Y", "Width", "Height", "ID"};
        columnNames = new String[]{"X", "Y", "Width", "Height"};
    }

    @Override
    public int getRowCount() {
        return dataModels.size(); //The total amount of objects that are on the screen.
    }

    @Override
    public int getColumnCount() {
        return columnNames.length; //The total amount of columns, which is fixed at 5 (4?).
    }

    @Override
    //public Object getValueAt(int row, int col) {
    public Object getValueAt(int selectedShape, int property) {
        switch(property) {
            case 0:
                return dataModels.get(selectedShape).getX();
            case 1:
                return dataModels.get(selectedShape).getY();
            case 2:
                return dataModels.get(selectedShape).getWidth();
            case 3:
                return dataModels.get(selectedShape).getHeight();
            //case 4:
            //    return dataModels.get(selectedShape).getID();
        }
        return null;
    }

    @Override
    public String getColumnName(int col){
        return columnNames[col];
    }

    /**
    @Override
    public void setValueAt(Object value, int row, int col){
        fireTableCellUpdated(row,col);
    }
    */

    //This is for when the clear function of the Whiteboard is called.
    public void reset() {
        dataModels.clear(); //Empties the arrayList.
        fireTableDataChanged();
    }

    //This is for when a new shape is added to the screen.
    public void addNewRow(DShapeModel shapeToAdd) {
        dataModels.add(shapeToAdd);
        fireTableDataChanged();
    }

    //This is for when an element is moved or resized.
    public void updateRow(DShapeModel shapeModel) {
        for(int i = 0; i < dataModels.size(); i++) {
            if(dataModels.get(i).getID() == shapeModel.getID()) {
                dataModels.set(i, shapeModel);
                fireTableRowsUpdated(i, i);
                break;
            }
        }
    }

    //This is for when a new shape is removed from the screen.
    public void removeRow(DShapeModel shapeToRemove) {
        for(int i = 0; i < dataModels.size(); i++) {
            if(dataModels.get(i).getID() == shapeToRemove.getID()) {
                for(DShapeModel model : dataModels) {
                    if(model.getID() > shapeToRemove.getID()) {
                        model.setID(model.getID() - 1);
                    }
                }
                dataModels.remove(i);
                break;
            }
        }
        fireTableDataChanged();
    }

    //This is for when a shape has been moved (to the front).
    public void moveRowUp(DShapeModel shapeChanged) {
        for(int i = 1; i < dataModels.size(); i++) {
            if(dataModels.get(i).getID() == shapeChanged.getID()) {
                DShapeModel tempModel = dataModels.get(i);
                dataModels.set(i, dataModels.get(i - 1));
                dataModels.set(i - 1, tempModel);
                fireTableCellUpdated(i - 1, i);
                break;
            }
        }
    }

    //This is for when a shape has been moved (to the back).
    public void moveRowDown(DShapeModel shapeChanged) {
        for(int i = 0; i < dataModels.size() - 1; i++) {
            if(dataModels.get(i).getID() == shapeChanged.getID()) {
                DShapeModel tempModel = dataModels.get(i);
                dataModels.set(i, dataModels.get(i + 1));
                dataModels.set(i + 1, tempModel);
                fireTableCellUpdated(i, i + 1);
                break;
            }
        }
    }
}