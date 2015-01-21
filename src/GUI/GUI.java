package GUI;

import Audio.Audio;
import SystemAction.SystemAction;
import javafx.stage.*;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.swing.*;

public class GUI extends JFrame{
    static final long RECORD_TIME = 6000;  // 6 sec
    TrayIcon trayIcon;
    SystemTray tray;
    GUI(){
        super("Voice Recognition");
        System.out.println("creating instance");
        try{
            System.out.println("setting look and feel");
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        }catch(Exception e){
            System.out.println("Unable to set LookAndFeel");
        }
        if(SystemTray.isSupported()){
            System.out.println("system tray supported");
            tray = SystemTray.getSystemTray();

            final Image image = Toolkit.getDefaultToolkit().getImage("images/icon32x32.png");
            final Image imageRed = Toolkit.getDefaultToolkit().getImage("images/icon32x32_red.png");
            ActionListener exitListener = new ActionListener() {
                public void actionPerformed(ActionEvent e) {
                    System.out.println("Exiting....");
                    System.exit(0);
                }
            };
            PopupMenu popup = new PopupMenu();
            MenuItem defaultItem = new MenuItem("Exit");
            defaultItem.addActionListener(exitListener);
            popup.add(defaultItem);
            trayIcon = new TrayIcon(image, "SystemTray Demo", popup);
            trayIcon.setImageAutoSize(true);
            trayIcon.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {      //пока по нажатию, TODO по конпке в фоновом режиме
                    if (e.getButton() != MouseEvent.BUTTON1) {return;}
                    Audio audio = new Audio();

                    Thread stopper = new Thread(new Runnable() {
                        public void run() {
                            try {
                                System.out.println("Start");
                                trayIcon.setImage(imageRed);
                                Thread.sleep(RECORD_TIME);
                                trayIcon.setImage(image);
                                System.out.println("End");
                            } catch (InterruptedException ex) {
                                ex.printStackTrace();
                            }
                        }
                    });

                    stopper.start();
                    try {
                        String recorded = audio.record();
                        System.out.println("Записал: " + recorded);
                        if (!SystemAction.act(recorded)) {
                            System.err.println("Couldn't recognise a command");
                        }
                    } catch (IOException i) {
                        i.printStackTrace();
                    }
                }
            });
        }else{
            System.out.println("system tray not supported");
        }

        setVisible(false);
        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            e.printStackTrace();
        }
        setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
    public static void main(String[] args){
        new GUI();
    }
}