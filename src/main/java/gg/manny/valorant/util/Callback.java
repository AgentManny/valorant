package gg.manny.valorant.util;

public interface Callback<T> {

    /**
     * Called when the request is successfully completed
     *
     * @param data the data received from the call
     */
    void callback(T data);

}

