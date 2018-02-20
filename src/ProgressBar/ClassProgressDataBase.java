/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ProgressBar;

import javax.swing.JLabel;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;

/**
 *
 * @author Orlando
 */
public class ClassProgressDataBase extends SwingWorker<Integer, String> {

    public JLabel getLabel() {
        return label;
    }

    public void setLabel(JLabel label) {
        this.label = label;
    }

    public JProgressBar getJpbar() {
        return jpbar;
    }

    public void setJpbar(JProgressBar jpbar) {
        this.jpbar = jpbar;
    }

    public ClassProgressDataBase(JLabel label, JProgressBar jpbar) {
        this.label = label;
        this.jpbar = jpbar;
    }

    private JLabel label;
    private JProgressBar jpbar;

    @Override
    protected Integer doInBackground() throws Exception {
        getJpbar().setIndeterminate(true);
        try {
          for (int i = 0; i < 11; i++) {
            Thread.sleep(1000); 
        }  
        } catch (Exception e) {
            System.out.println("error: " + e);
        }
        
        
        
        
        getJpbar().setIndeterminate(false); 
        return 0;
    }

}
