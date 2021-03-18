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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public abstract class BotonPagina extends JButton implements ActionListener{

    public static final byte PAGINA = 0;
    public static final byte IZQUIERDA = -1;
    public static final byte DERECHA = -2;
    public static final byte IZQUIERDA_TOTAL = -3;
    public static final byte DERECHA_TOTAL = -4;
    public static final byte ACTUAL = -5;
    public short value;
    public byte status;
    public BotonPagina(short value){
        this(value,PAGINA);
    }
    
    public BotonPagina(short value, byte status){
        addActionListener(this);
        if(status == IZQUIERDA){
            setText("<");
        }else if(status == IZQUIERDA_TOTAL){
            setText("<<");
        }else if(status == DERECHA){
            setText(">");
        }else if(status == DERECHA_TOTAL){
            setText(">>");
        }else if(status == ACTUAL){
            setText("-"+(value+1)+"-");
        }else{
            setText(String.valueOf(value+1));
        }
        this.status=status;
        this.value = value;
    }
    
    @Override
    public void actionPerformed(ActionEvent e) {
        if(value!=ACTUAL){
            action(value);
        }
    }
    
    public abstract void action(short x);
    
}
