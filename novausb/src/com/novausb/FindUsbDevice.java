package com.novausb;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author David Bastidas <espabastidas19@gmail.com>
 */
public class FindUsbDevice {

    private final String baseDirLocal = "todo";

    public static void main(String argv[]) {
        FindUsbDevice findUsbDevice = new FindUsbDevice();
        findUsbDevice.listar();
    }

    public void listar() {
        FileSystemView fsv = FileSystemView.getFileSystemView();

        File[] drives = File.listRoots();
        if (drives != null && drives.length > 0) {
            for (File aDrive : drives) {
                if (fsv.getSystemTypeDescription(aDrive).toLowerCase().indexOf("usb") != -1) {
                    System.out.println("Drive Letter: " + aDrive.toString());
                    System.out.println("\tType: " + fsv.getSystemTypeDescription(aDrive));
                    System.out.println("\tDysplay NAm0: " + fsv.getSystemDisplayName(aDrive));
                    System.out.println("\tName: " + aDrive.getName());
                    System.out.println("\tTotal space: " + aDrive.getTotalSpace());
                    System.out.println("\tFree space: " + aDrive.getFreeSpace());
                    System.out.println();
                    //backup(aDrive.toString());
                    //speed /q para quick o normal en blanco
                    //type /fs:FAT32 o /fs:NTFS
                    //formatear(aDrive.toString().replace("\\", ""), "/q", "/fs:NTFS");
                    //copiarAUsb(aDrive.toString());
                    //permisoSoloLectura(aDrive.toString().replace("\\", ""));
                    ejecutarDiskPart();
                    break;
                }
            }
        }
    }

    public void copiarAUsb(String unidad) {
        File source = new File(baseDirLocal);
        File target = new File(unidad + "\\" + baseDirLocal);
        if (!target.exists()) {
            target.mkdir();
        }
        try {
            copyDirectory(source, target);
        } catch (IOException ex) {
            Logger.getLogger(FindUsbDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void backup(String unidad) {
        File source = new File(unidad);
        File target = new File(baseDirLocal);
        try {
            copyDirectory(source, target);
        } catch (IOException ex) {
            Logger.getLogger(FindUsbDevice.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void permisoSoloLectura(String drive) {
        Process p;
        try {
            p = Runtime.getRuntime().exec("CMD /C attrib +r " + drive);
            p.waitFor();
            //File f1 = new File("formato.bat");
            //f1.delete();
        } catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        } finally {

        }
    }

    public void ejecutarDiskPart() {
        Process p;
        try {
            p = Runtime.getRuntime().exec("CMD diskpart");

            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(p.getOutputStream()));
            BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
            System.out.println("cmd: "+input.readLine());
            /*writer.write("cmd diskpart");
            writer.flush();
            System.out.println("cmd: "+input.readLine());
            writer.write("list disk");
            writer.flush();
            System.out.println("cmd: "+input.readLine());*/
            
            input.close();
            writer.close();
            
        } catch (IOException e) {
            System.out.println(e);
        } finally {

        }
    }

    public void copy(File sourceLocation, File targetLocation) throws IOException {
        if (sourceLocation.isDirectory()) {
            copyDirectory(sourceLocation, targetLocation);
        } else {
            copyFile(sourceLocation, targetLocation);
        }
    }

    private void copyDirectory(File source, File target) throws IOException {
        if (!target.exists()) {
            target.mkdir();
        }
        String[] files = source.list();
        if (files != null) {
            for (String f : files) {
                copy(new File(source, f), new File(target, f));
            }
        }
    }

    private void copyFile(File source, File target) throws IOException {
        try (
                InputStream in = new FileInputStream(source);
                OutputStream out = new FileOutputStream(target)) {
            byte[] buf = new byte[1024];
            int length;
            while ((length = in.read(buf)) > 0) {
                out.write(buf, 0, length);
            }
        }
    }

    /*formatenado*/
    public void formatear(String drive, String speed, String type) {
        Process p;
        try {
            p = Runtime.getRuntime().exec("CMD /C format /y" + " " + drive + " " + speed + " " + type + " " + "/v:" + "NUeva");
            p.waitFor();
            //File f1 = new File("formato.bat");
            //f1.delete();
        } catch (IOException e) {
            System.out.println(e);
        } catch (InterruptedException ex) {
            System.out.println(ex);
        } finally {

        }
    }
}
