package co.chatsdk.firebase.push;

import co.chatsdk.core.session.NM;
import co.chatsdk.core.session.NetworkManager;

/**
 * Created by ben on 9/1/17.
 */

public class FirebasePushModule  {

    public static void activateForFirebase () {
        FirebasePushModule.activate(() -> NM.core().pushUser().doOnError(throwable -> throwable.printStackTrace()).subscribe());
    }

    public static void activateForXMPP () {
        FirebasePushModule.activate(() -> NM.core().pushUser().doOnComplete(() -> NM.core().goOnline()).doOnError(throwable -> throwable.printStackTrace()).subscribe());
    }


    public static void activate (FirebasePushHandler.TokenPusher pusher) {
        NetworkManager.shared().a.push = new FirebasePushHandler(pusher);
    }

}