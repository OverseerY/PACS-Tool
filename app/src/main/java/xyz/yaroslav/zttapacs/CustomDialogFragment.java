package xyz.yaroslav.zttapacs;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CustomDialogFragment extends DialogFragment {
    public static final String SYS_PREFERENCES = "SystemPreferences";
    public static final String SERVER_PROTOCOL = "srv_protocol";
    public static final String SERVER_IP = "srv_ip";
    public static final String SERVER_PORT = "srv_port";
    public static final String SERVER_POSTFIX = "srv_postfix";

    private String server_protocol;
    private String server_ip;
    private String server_port;
    private String server_postfix;

    SharedPreferences systemPref;

    Button saveButton;
    Button cancelButton;
    EditText ipAddressField;
    EditText portField;
    EditText postfixField;
    EditText protocolField;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        systemPref = getActivity().getSharedPreferences(SYS_PREFERENCES, Context.MODE_PRIVATE);
        server_protocol = systemPref.getString(SERVER_PROTOCOL, "http");
        server_ip = systemPref.getString(SERVER_IP, "192.168.0.14");
        server_port = systemPref.getString(SERVER_PORT, "5002");
        server_postfix = systemPref.getString(SERVER_POSTFIX, "add");

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_layout, container, false);

        saveButton = view.findViewById(R.id.settingsSave);
        cancelButton = view.findViewById(R.id.settingsCancel);
        ipAddressField = view.findViewById(R.id.ipField);
        portField = view.findViewById(R.id.portField);
        postfixField = view.findViewById(R.id.postfixField);
        protocolField = view.findViewById(R.id.protocolField);

        ipAddressField.setText(server_ip);
        portField.setText(server_port);
        postfixField.setText(server_postfix);
        protocolField.setText(server_protocol);

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveSettings();
                getDialog().dismiss();
            }
        });

        return view;
    }

    public void saveSettings() {
        String protocol_value = protocolField.getText().toString().trim();
        String ip_value = ipAddressField.getText().toString().trim();
        String port_value = portField.getText().toString().trim();
        String postfix_value = postfixField.getText().toString().trim();

        SharedPreferences.Editor editor = systemPref.edit();
        if (!protocol_value.equals("") && !protocol_value.equals(server_protocol)) {
            editor.putString(SERVER_PROTOCOL, protocolField.getText().toString().trim());
        }
        if (!ip_value.equals("") && !ip_value.equals(server_ip)) {
            editor.putString(SERVER_IP, ipAddressField.getText().toString().trim());
        }
        if (!port_value.equals("") && !port_value.equals(server_port)) {
            editor.putString(SERVER_PORT, portField.getText().toString().trim());
        }
        if (!postfix_value.equals("") && !postfix_value.equals(server_postfix)) {
            editor.putString(SERVER_POSTFIX, postfixField.getText().toString().trim());
        }
        editor.apply();
        Toast.makeText(getContext(), getString(R.string.toast_settings_saved), Toast.LENGTH_SHORT).show();
    }
}

































