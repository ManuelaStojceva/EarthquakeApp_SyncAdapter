package sync;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.ContentResolver;
import android.content.Context;
import android.os.Bundle;

/**
 * Created by Manuela.Stojceva on 3/17/2017.
 */
public class SyncUtils {
    private static final long SYNC_FREQUENCY = 60 * 6;
    private static final String CONTENT_AUTHORITY = "sync";
    private static final String PREF_SETUP_COMPLETE = "setup_complete";
    // Value below must match the account type specified in res/xml/syncadapter.xml
    public static final String ACCOUNT_TYPE = "sync.account";
    public static final String ACCOUNT = "dummyaccount";

    public static Account CreateSyncAccount(Context context) {
        boolean newAccount = false;

        // Create account, if it's missing.
        Account account = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        AccountManager accountManager =
                (AccountManager) context.getSystemService(Context.ACCOUNT_SERVICE);
        if (accountManager.addAccountExplicitly(account, null, null)) {
            // this account supports sync
            ContentResolver.setIsSyncable(account, CONTENT_AUTHORITY, 1);
            // this account is eligible for auto sync when the network is up
            ContentResolver.setSyncAutomatically(account, CONTENT_AUTHORITY, true);
            // Recommend a schedule for automatic synchronization. The system may modify this based
            // on other scheduled syncs and network utilization.
            ContentResolver.addPeriodicSync(
                    account, CONTENT_AUTHORITY, new Bundle(),SYNC_FREQUENCY);
            newAccount = true;
        }

        if (newAccount) {
            TriggerRefresh(account);
        }
        return account;
    }

    /**
     * Helper method to trigger an immediate sync ("refresh").
     *
     * <p>This should only be used when we need to preempt the normal sync schedule. Typically, this
     * means the user has pressed the "refresh" button.
     */
    public static void
    TriggerRefresh(Account account) {
        Bundle b = new Bundle();
        // perform sync NOW!
        b.putBoolean(ContentResolver.SYNC_EXTRAS_MANUAL, true);
        b.putBoolean(ContentResolver.SYNC_EXTRAS_EXPEDITED, true);
        ContentResolver.requestSync(
                account, // Sync account
                CONTENT_AUTHORITY,                 // Content authority
                b);                                             // Extras
    }
}
