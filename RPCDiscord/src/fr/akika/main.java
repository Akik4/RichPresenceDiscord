package fr.akika;

import club.minnced.discord.rpc.DiscordEventHandlers;
import club.minnced.discord.rpc.DiscordRPC;
import club.minnced.discord.rpc.DiscordRichPresence;
import java.awt.*;
import java.awt.event.*;
import java.lang.management.ManagementFactory;
import java.net.URL;

public class main {

    public static void main(String[] args){
        info.icn();
        com.sun.management.OperatingSystemMXBean os = (com.sun.management.OperatingSystemMXBean) ManagementFactory.getOperatingSystemMXBean();

        DiscordRPC lib = DiscordRPC.INSTANCE;
        String applicationId = "";
        String steamId = "";
        DiscordEventHandlers handlers = new DiscordEventHandlers();
        lib.Discord_Initialize(applicationId, handlers, true, steamId);
        DiscordRichPresence presence = new DiscordRichPresence();
        presence.startTimestamp = System.currentTimeMillis() /1000;
        presence.state = "RAM USAGE ON DISCORD";
        lib.Discord_UpdatePresence(presence);

        new Thread(() -> {
            System.out.println("Attention");
            while (!Thread.currentThread().isInterrupted()){
                lib.Discord_RunCallbacks();
                try{
                    presence.details = "Mémoire " + (Math.floor(info.TotalRam(os.getTotalPhysicalMemorySize()) * 100) / 100 - Math.floor(info.TotalRam(os.getFreePhysicalMemorySize() *100)) /100) + "/" + Math.floor(info.TotalRam(os.getTotalPhysicalMemorySize()) * 100) / 100 + " GB" ;
                    lib.Discord_UpdatePresence(presence);
                    Thread.sleep(2000);
                } catch (InterruptedException ignored){}
            }
        }, "DISCORD-RICHPRESENCE").start();
    }
}

class info{
    public static double TotalRam(double value){
        double size_bytes= value;
        double size_kb = size_bytes /1024;
        double size_mb = size_kb / 1024;
        double size_gb = size_mb / 1024 ;
        return size_gb;
    }
    public static void icn(){
        if (!SystemTray.isSupported()) {
            System.out.println("SystemTray is not supported");
            return;
        }
        final PopupMenu popup = new PopupMenu();

        URL url = System.class.getResource("/fr/akika/img/téléchargement.png");
        Image image = Toolkit.getDefaultToolkit().getImage(url);

        final TrayIcon trayIcon = new TrayIcon(image);

        final SystemTray tray = SystemTray.getSystemTray();

        MenuItem exitItem = new MenuItem("Exit");
        MenuItem otherItem = new MenuItem("Other");


        //Add components to pop-up menu
        popup.add(otherItem);
        popup.addSeparator();
        popup.add(exitItem);
        popup.addSeparator();

        trayIcon.setPopupMenu(popup);

        try {
            tray.add(trayIcon);
        } catch (AWTException e) {
            System.out.println("TrayIcon could not be added.");
        }

        ActionListener al;
        al = new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if(e.getActionCommand().equals("Exit")){
                    System.exit(-1);
                }
                System.out.println(e.getActionCommand());
            }
        };
        popup.addActionListener(al);
    }
}



