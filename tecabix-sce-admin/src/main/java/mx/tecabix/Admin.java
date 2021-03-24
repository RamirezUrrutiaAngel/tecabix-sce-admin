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
package mx.tecabix;

import static java.awt.Frame.MAXIMIZED_BOTH;
import java.awt.Image;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.UIManager;
import mx.tecabix.view.ErrorLog;
import mx.tecabix.view.Home;
import mx.tecabix.view.sesion.InicioSession;

/**
 * 
 * @author Ramirez Urrutia Angel Abinadi
 * 
 */
public class Admin {

    private static Home home = null;
    private static final String VERCION = "2021.02.23 BETA";
    
    public static void main(String[] args) {
        
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            home = new Home();
            home.setExtendedState(MAXIMIZED_BOTH);
            home.setTitle("Tecabix SCE "+VERCION);
            home.setIconImage(new ImageIcon(home.getClass().getResource("/img/logo/tecabix-simple.png")).getImage());
            home.setLocationRelativeTo(null);
            home.setVisible(true);
            InicioSession inicioSession;
            do {
                inicioSession = new InicioSession(home);
                inicioSession.setLocationRelativeTo(home);
                inicioSession.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
                inicioSession.setVisible(true);
                if(inicioSession.isExit()){
                    System.exit(0);
                    return;
                }
            } while (!inicioSession.isAutentificado());
            home.cargarDatos();
        } catch (Exception ex) {
            Logger.getLogger(Admin.class.getName()).log(Level.SEVERE, null, ex);
            ErrorLog errorLog = new ErrorLog(home, ex);
        }
    }
}
