package controller;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.earthquakeapp.syncadapter.R;

/**
 * Created by Manuela.Stojceva on 3/17/2017.
 */
public class Engine {

    private static Engine me = null;
    private Dialog dialog;
    private static Context context;

    public Engine(final Context context){
        this.context = context;
    }
    public static synchronized Engine getInstance() {
        if (me == null) {
            me = new Engine(context);
        }
        return me;
    }

    public Dialog showDialog(final Activity activity, final String title, final String message) {
        final Dialog dialog = new Dialog(activity);
        dialog.setContentView(R.layout.general_dialog);
        dialog.setTitle(title);
        TextView tv = (TextView) dialog.findViewById(R.id.textDialog);
        TextView tvMsg = (TextView) dialog.findViewById(R.id.textDialogMsg);
        tv.setText(title);
        tvMsg.setText(message);
        Button close = (Button) dialog.findViewById(R.id.declineButton);

        close.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });
        dialog.show();
        return dialog;

    }
}
