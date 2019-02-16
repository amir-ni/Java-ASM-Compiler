package Core.View.TableModels;

import javax.swing.table.AbstractTableModel;

public class RegisterTableModel extends AbstractTableModel {
    String[] columnNames = {"Name", "Number", "Value"};
    Object[][] data = {{"$zero", "$0", 0}, {"$at", "$1", 0},
            {"$v0", "$2", 0}, {"$v1", "$3", 0},
            {"$a0", "$4", 0}, {"$a1", "$5", 0}, {"$a2", "$6", 0}, {"$a3", "$7", 0},
            {"$t0", "$8", 0}, {"$t1", "$9", 0}, {"$t2", "$10", 0}, {"$t3", "$11", 0}, {"$t4", "$12", 0},
            {"$t5", "$13", 0}, {"$t6", "$14", 0}, {"$t7", "$15", 0},
            {"$s0", "$16", 0}, {"$s1", "$17", 0}, {"$s2", "$18", 0}, {"$s3", "$19", 0}, {"$s4", "$20", 0},
            {"$s5", "$21", 0}, {"$s6", "$22", 0}, {"$s7", "$23", 0},
            {"$t8", "$24", 0}, {"$t9", "$25", 0},
            {"$k0", "$26", 0}, {"$k1", "$27", 0},
            {"$gp", "$28", 0}, //TODO
            {"$sp", "$29", 0}, //TODO
            {"$fp", "$30", 0},
            {"$ra", "$31", 0},
            {"pc", "", 0}, //TODO
            {"hi", "", 0}, {"lo", "", 0}};

    @Override
    public int getRowCount() {
        return data.length;
    }

    @Override
    public int getColumnCount() {
        return columnNames.length;
    }

    @Override
    public String getColumnName(int column) {
        return columnNames[column];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        return false;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        return data[rowIndex][columnIndex];
    }

    public void setValueAt(Object value, int rowIndex, int columnIndex) {
        data[rowIndex][columnIndex] = value;
        fireTableCellUpdated(rowIndex, columnIndex);
    }
}
