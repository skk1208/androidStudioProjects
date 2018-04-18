package co.chatsdk.firebase;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;

import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.Date;

import co.chatsdk.core.base.AbstractCoreHandler;
import co.chatsdk.core.dao.User;
import co.chatsdk.core.events.EventType;
import co.chatsdk.core.events.NetworkEvent;
import co.chatsdk.core.session.NM;
import co.chatsdk.core.types.ChatError;
import co.chatsdk.core.types.FileUploadResult;
import co.chatsdk.core.utils.DisposableList;
import co.chatsdk.firebase.wrappers.UserWrapper;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.CompletableSource;
import io.reactivex.Observer;
import io.reactivex.Single;
import io.reactivex.SingleEmitter;
import io.reactivex.SingleOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Action;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by benjaminsmiley-andrews on 02/05/2017.
 */

public class FirebaseCoreHandler extends AbstractCoreHandler {

    private DisposableList disposableList = new DisposableList();

    public FirebaseCoreHandler () {
        // When the user logs out, turn off all the existing listeners
        FirebaseEventHandler.shared().source()
                .filter(NetworkEvent.filterType(EventType.Logout))
                .subscribe(networkEvent -> disposableList.dispose());
    }

    /** Unlike the iOS code the current user need to be saved before you call this method.*/
    public Completable pushUser () {
        return Single.create((SingleOnSubscribe<User>) e -> {
            // Check to see if the avatar URL is local or remote
            File avatar = new File(NM.currentUser().getAvatarURL());
            if (avatar.exists() && NM.upload() != null) {
                // Upload the image
                Bitmap bitmap = BitmapFactory.decodeFile(avatar.getPath());
                NM.upload().uploadImage(bitmap).subscribe(new Observer<FileUploadResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                    }

                    @Override
                    public void onNext(@NonNull FileUploadResult fileUploadResult) {
                        if (fileUploadResult.urlValid()) {
                            NM.currentUser().setAvatarURL(fileUploadResult.url);
                            NM.currentUser().update();
                            NM.events().source().onNext(NetworkEvent.userMetaUpdated(NM.currentUser()));
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable ex) {
                        ex.printStackTrace();
                        e.onSuccess(NM.currentUser());
                    }

                    @Override
                    public void onComplete() {
                        e.onSuccess(NM.currentUser());
                    }
                });
            } else {
                e.onSuccess(NM.currentUser());
            }
        }).flatMapCompletable(user -> new UserWrapper(user).push()).subscribeOn(Schedulers.single());
    }

    public Completable setUserOnline() {

        User current = NM.currentUser();
        if (current != null && StringUtils.isNotEmpty(current.getEntityID())) {
            return UserWrapper.initWithModel(currentUserModel()).goOnline();
        }
        return Completable.complete();
    }

    public Completable setUserOffline() {
        User current = NM.currentUser();
        if (current != null && StringUtils.isNotEmpty(current.getEntityID()))
        {
            // Update the last online figure then go offline
            return updateLastOnline()
                    .concatWith(UserWrapper.initWithModel(currentUserModel()).goOffline());
        }
        return Completable.complete();
    }

    public void goOffline() {
        NM.core().save();
        disposableList.add(setUserOffline().subscribe(() -> DatabaseReference.goOffline()));
    }

    public void goOnline() {
        FirebasePaths.firebaseRawRef().child(".info/connected").addListenerForSingleValueEvent(new FirebaseEventListener().onValue((snapshot, hasValue) -> {
            if(hasValue) {
                Timber.v("Already online!");
            }
            else {
                DatabaseReference.goOnline();
                disposableList.add(setUserOnline().subscribe());
            }
        }));
    }

    public Completable updateLastOnline () {
        return Completable.create(e -> {
            User currentUser = NM.currentUser();
            currentUser.setLastOnline(new Date());
            currentUser.update();
            e.onComplete();
        }).concatWith(pushUser()).subscribeOn(Schedulers.single());
    }

    public Single<Boolean> isOnline() {
        return Single.create((SingleOnSubscribe<Boolean>) e -> {
            if (NM.currentUser() == null) {
                e.onError(ChatError.getError(ChatError.Code.NULL, "Current user is null"));
                return;
            }

            FirebasePaths.userOnlineRef(NM.currentUser().getEntityID()).addListenerForSingleValueEvent(new FirebaseEventListener().onValue((snapshot, hasValue) -> {
                disposableList.add(updateLastOnline().subscribe());
                e.onSuccess((Boolean) snapshot.getValue());
            }));

        }).subscribeOn(Schedulers.single());
    }

    public void userOn (final User user) {
        final UserWrapper wrapper = new UserWrapper(user);
        disposableList.add(wrapper.onlineOn().doOnDispose(() -> wrapper.onlineOff()).subscribe(aBoolean -> NM.events().source().onNext(NetworkEvent.userPresenceUpdated(user))));
        disposableList.add(wrapper.metaOn().doOnDispose(() -> wrapper.metaOff()).subscribe(user1 -> NM.events().source().onNext(NetworkEvent.userMetaUpdated(user1))));
    }

    public void userOff (final User user) {
        UserWrapper wrapper = new UserWrapper(user);
        wrapper.onlineOff();
        wrapper.metaOff();
    }

    public void save () {

    }
}
