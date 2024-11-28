package com.example.hitopm;

public class Configuration {
    private static boolean useFirebase = false;

    public static boolean isUseFirebase() {
        return useFirebase;
    }

    public static void setUseFirebase(boolean useNewFirebase) {
        useFirebase = useNewFirebase;
    }


}
