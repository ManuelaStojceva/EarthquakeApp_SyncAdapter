package sync;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

/**
 * Created by Manuela.Stojceva on 3/17/2017.
 */
public class AuthenticatorService extends Service {

    // Instance field that stores the authenticator object
    private Authenticator authenticator;

    @Override
    public void onCreate() {
        // Create a new authenticator object
        authenticator = new Authenticator(this);
    }

    /*
     * When the system binds to this Service to make the RPC call return the authenticator's
     * IBinder.
     */
    @Override
    public IBinder onBind(Intent intent) {
        return authenticator.getIBinder();
    }
}
