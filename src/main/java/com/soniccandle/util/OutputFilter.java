package com.soniccandle.util;

import java.io.File;
import javax.swing.filechooser.*;

public class OutputFilter extends FileFilter {

    // Accept all directories and all mp4 and png files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.mp4) || extension.equals(Utils.png)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    public static boolean supportedType(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.mp4) || extension.equals(Utils.png)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    // The description of this filter
    public String getDescription() {
        return "Output files (.mp4 or .png)";
    }
}
