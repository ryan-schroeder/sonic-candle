package com.soniccandle.util;

import java.io.File;
import javax.swing.filechooser.*;

public class ImageFilter extends FileFilter {

    // Accept all directories and all image files.
    public boolean accept(File f) {
        if (f.isDirectory()) {
            return true;
        }

        String extension = Utils.getExtension(f);
        if (extension != null) {
            if (extension.equals(Utils.png) || extension.equals(Utils.jpg)) {
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
            if (extension.equals(Utils.png) || extension.equals(Utils.jpg)) {
                return true;
            } else {
                return false;
            }
        }

        return false;
    }

    // The description of this filter
    public String getDescription() {
        return "Image Files (.png or .jpg)";
    }
}
