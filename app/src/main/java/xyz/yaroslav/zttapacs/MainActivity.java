package xyz.yaroslav.zttapacs;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.nfc.tech.IsoDep;
import android.nfc.tech.MifareClassic;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.nfc.tech.NfcA;
import android.nfc.tech.NfcB;
import android.nfc.tech.NfcV;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    NfcManager nfcManager;
    NfcAdapter nfcAdapter;

    ImageView settingsButton;
    ImageView nfcImage;
    TextView nfcText;

    private boolean is_nfc_available = false;

    private final String[][] techList = new String[][]{
            new String[]{
                    NfcA.class.getName(),
                    NfcB.class.getName(),
                    NfcV.class.getName(),
                    IsoDep.class.getName(),
                    MifareClassic.class.getName(),
                    MifareUltralight.class.getName(),
                    Ndef.class.getName()
            }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nfcImage = findViewById(R.id.nfcImage);
        nfcText = findViewById(R.id.nfcTextUnderImage);

        settingsButton = findViewById(R.id.mainMenu);
        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CustomDialogFragment settingsDialog = new CustomDialogFragment();
                settingsDialog.show(getSupportFragmentManager(), "settings");
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        nfcManager = (NfcManager) getApplicationContext().getSystemService(Context.NFC_SERVICE);
        nfcAdapter = nfcManager.getDefaultAdapter();
        if (nfcAdapter != null && nfcAdapter.isEnabled()) {
            is_nfc_available = true;
        } else {
            is_nfc_available = false;
        }

        if (is_nfc_available) {
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, new Intent(this, getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
            IntentFilter filter = new IntentFilter();
            filter.addAction(NfcAdapter.ACTION_TAG_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_NDEF_DISCOVERED);
            filter.addAction(NfcAdapter.ACTION_TECH_DISCOVERED);
            nfcAdapter = NfcAdapter.getDefaultAdapter(this);
            nfcAdapter.enableForegroundDispatch(this, pendingIntent, new IntentFilter[]{filter}, this.techList);
            nfcImage.setImageResource(R.drawable.ic_default);
            nfcText.setText(getString(R.string.label_default));
        } else {
            nfcImage.setImageResource(R.drawable.ic_nfc_off);
            nfcText.setText(getString(R.string.label_nfc_off));
        }
    }

    private void displayMainFragment(String value) {
        Fragment prev = getSupportFragmentManager().findFragmentByTag("tag_fragment");
        if (prev != null) {
            DialogFragment df = (DialogFragment) prev;
            df.dismiss();
        }
        TagDialogFragment tagDialogFragment = new TagDialogFragment();
        tagDialogFragment.setUid(value);
        tagDialogFragment.show(getSupportFragmentManager(), "tag_fragment");
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        if (intent.getAction().equals(NfcAdapter.ACTION_TAG_DISCOVERED)) {
            String tag_id = ByteArrayToHexString(intent.getByteArrayExtra(NfcAdapter.EXTRA_ID));
            displayMainFragment(tag_id);
        }
    }

    private String ByteArrayToHexString(byte [] inarray) {
        int i, j, in;
        String [] hex = {"0","1","2","3","4","5","6","7","8","9","A","B","C","D","E","F"};
        String out= "";
        for(j = 0 ; j < inarray.length ; ++j)
        {
            in = (int) inarray[j] & 0xff;
            i = (in >> 4) & 0x0f;
            out += hex[i];
            i = in & 0x0f;
            out += hex[i];
        }
        return out;
    }



}




















