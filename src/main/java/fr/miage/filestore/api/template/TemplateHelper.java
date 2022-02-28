package fr.miage.filestore.api.template;

public class TemplateHelper {

    public String mimetypeToIcon(String mimetype) {
        String icon = "fa-file";
        if ( mimetype.startsWith("image") ) icon =  "fa-file-image";
        if ( mimetype.startsWith("audio") ) icon =  "fa-file-audio";
        if ( mimetype.startsWith("video") ) icon =  "fa-file-video";
        if ( mimetype.equals("application/fs-folder") ) icon =  "fa-folder";
        if ( mimetype.equals("application/pdf") ) icon =  "fa-file-pdf";
        if ( mimetype.equals("text/plain") ) icon =  "fa-file-text";
        if ( mimetype.equals("text/html") ) icon =  "fa-file-code";
        if ( mimetype.equals("application/json") ) icon =  "fa-file-code";
        if ( mimetype.equals("application/gzip") ) icon =  "fa-file-archive";
        if ( mimetype.equals("application/zip") ) icon =  "fa-file-archive";

        if ( mimetype.equals("application/msword") ) icon =  "fa-file-word";
        if ( mimetype.equals("application/vnd.openxmlformats-officedocument.wordprocessingml.document") ) icon =  "fa-file-word";
        if ( mimetype.equals("application/vnd.ms-excel") ) icon =  "fa-file-excel";
        if ( mimetype.equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet") ) icon =  "fa-file-excel";
        if ( mimetype.equals("application/vnd.ms-powerpoint") ) icon =  "fa-file-powerpoint";
        if ( mimetype.equals("application/vnd.openxmlformats-officedocument.presentationml.presentation") ) icon =  "fa-file-powerpoint";

        return icon;
    }

    public static String sizeToBytes(long bytes, boolean si) {
        int unit = si ? 1000 : 1024;
        if (bytes < unit) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(unit));
        String pre = (si ? "kMGTPE" : "KMGTPE").charAt(exp-1) + (si ? "" : "i");
        return String.format("%.1f %sB", bytes / Math.pow(unit, exp), pre);
    }

}
