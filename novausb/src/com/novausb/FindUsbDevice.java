
package com.novausb;

import java.io.File;
import javax.swing.filechooser.FileSystemView;

/**
 *
 * @author David Bastidas <espabastidas19@gmail.com>
 */
public class FindUsbDevice {

    public static void main(String argv[]) {
        FileSystemView fsv = FileSystemView.getFileSystemView();
         
        File[] drives = File.listRoots();
        if (drives != null && drives.length > 0) {
            for (File aDrive : drives) {
                System.out.println("Drive Letter: " + aDrive);
                System.out.println("\tType: " + fsv.getSystemTypeDescription(aDrive));
                System.out.println("\tName: " + aDrive.getName());
                System.out.println("\tTotal space: " + aDrive.getTotalSpace());
                System.out.println("\tFree space: " + aDrive.getFreeSpace());
                System.out.println();
            }
        }
    }
}
