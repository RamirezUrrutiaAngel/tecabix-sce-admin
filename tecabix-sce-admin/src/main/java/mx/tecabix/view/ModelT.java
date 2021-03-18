/*
 *   This file is part of Foobar.
 *
 *   Foobar is free software: you can redistribute it and/or modify
 *   it under the terms of the GNU General Public License as published by
 *   the Free Software Foundation, either version 3 of the License, or
 *   (at your option) any later version.
 *
 *   Foobar is distributed in the hope that it will be useful,
 *   but WITHOUT ANY WARRANTY; without even the implied warranty of
 *   MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 *   GNU General Public License for more details.
 *
 *   You should have received a copy of the GNU General Public License
 *   along with Foobar.  If not, see <https://www.gnu.org/licenses/>.
 *
 */
package mx.tecabix.view;

import javax.swing.table.DefaultTableModel;

/**
 * 
 * @author Sammy Guergachi <sguergachi at gmail.com>
 */
public class ModelT extends DefaultTableModel{

    public ModelT( Object[] columnNames,Class [] types) {
        super(new Object[][]{}, columnNames);
        this.types=types;
    }


    private Class[] types ;
    
    private boolean[] canEdit =null;

    @Override
    public Class getColumnClass(int columnIndex) {
        return types [columnIndex];
    }

    @Override
    public boolean isCellEditable(int rowIndex, int columnIndex) {
        if(canEdit==null)return false;
        return canEdit [columnIndex];
    }

    public void setTypes(Class[] types) {
        this.types = types;
    }

    public void setCanEdit(boolean[] canEdit) {
        this.canEdit = canEdit;
    }
}
