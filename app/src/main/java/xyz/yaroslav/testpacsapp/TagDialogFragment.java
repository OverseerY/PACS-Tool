package xyz.yaroslav.testpacsapp;

import android.app.Dialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.Collections;
import java.util.Timer;
import java.util.TimerTask;

public class TagDialogFragment extends DialogFragment {

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

    private String default_url = "";
    private String employeeVal = "";
    private String departmentVal = "";
    private String decUidVal = "";

    private String uid_value;

    public void setUid(String uid_value) {
        this.uid_value = uid_value;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);

        systemPref = getActivity().getSharedPreferences(SYS_PREFERENCES, Context.MODE_PRIVATE);
        server_protocol = systemPref.getString(SERVER_PROTOCOL, "http");
        server_ip = systemPref.getString(SERVER_IP, "192.168.0.14");
        server_port = systemPref.getString(SERVER_PORT, "5003");
        server_postfix = systemPref.getString(SERVER_POSTFIX, "add");

        default_url = server_protocol + "://" + server_ip + ":" + server_port + "/" + server_postfix;

        return dialog;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.tag_layout, container, false);

        final TextView uidField = view.findViewById(R.id.tagUidValue);
        final EditText tagDepartment = view.findViewById(R.id.tagDepartment);
        final EditText tagEmployee = view.findViewById(R.id.tagEmployee);
        Button tagSave = view.findViewById(R.id.tagSave);
        Button tagCancel = view.findViewById(R.id.tagCancel);

        if (uid_value != null && !uid_value.equals("")) {
            String decUid = convertToDecimal(uid_value);
            uidField.setText(decUid);
        }

        tagCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().dismiss();
            }
        });

        tagSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!uidField.getText().toString().trim().equals("")) {
                    decUidVal = uidField.getText().toString();
                }
                if (!tagDepartment.getText().toString().trim().equals("")) {
                    departmentVal = tagDepartment.getText().toString();
                }
                if (!tagEmployee.getText().toString().trim().equals("")) {
                    employeeVal = tagEmployee.getText().toString();
                }
                if (!decUidVal.equals("") && !departmentVal.equals("") && !employeeVal.equals("")) {
                    new HTTPAsyncTask().execute(default_url);
                    getDialog().dismiss();
                } else {
                    Toast.makeText(getContext(), getString(R.string.toast_empty_fields), Toast.LENGTH_SHORT).show();
                }
            }
        });

        return view;
    }

    private String convertToDecimal(String uid) {
        int num_of_pairs = uid.toCharArray().length/2;
        String dec_uid = "";

        String [] inv_uid = new String [num_of_pairs];

        int k = 0;
        for(int i = 0;i < uid.length() - 1; i = i + 2) {
            String pair = uid.substring(i, i + 2);
            inv_uid[k++] = pair;
        }

        Collections.reverse(Arrays.asList(inv_uid));

        for (String s : inv_uid) {
            dec_uid += s;
        }

        long decimal = Long.parseLong(dec_uid, 16);

        return Long.toString(decimal);
    }

    private class HTTPAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            // params comes from the execute() call: params[0] is the url.
            try {
                try {
                    return HttpPost(urls[0]);
                } catch (JSONException e) {
                    e.printStackTrace();
                    return "Error!";
                }
            } catch (IOException e) {
                e.printStackTrace();
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            Log.i("TAG", result);
        }
    }

    private String HttpPost(String myUrl) throws IOException, JSONException {
        String result = "";

        URL url = new URL(myUrl);

        // 1. create HttpURLConnection
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        // 2. build JSON object
        JSONObject jsonObject = buidJsonObject();

        // 3. add JSON content to POST request body
        setPostRequestContent(conn, jsonObject);

        // 4. make POST request to the given URL
        conn.connect();

        // 5. return response message
        return conn.getResponseMessage()+"";

    }

    private JSONObject buidJsonObject() throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("card", decUidVal);
        jsonObject.put("name", employeeVal);
        jsonObject.put("dept", departmentVal);

        return jsonObject;
    }

    private void setPostRequestContent(HttpURLConnection conn, JSONObject jsonObject) throws IOException {
        OutputStream os = conn.getOutputStream();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os, "UTF-8"));
        writer.write(jsonObject.toString());
        Log.i(MainActivity.class.toString(), jsonObject.toString());
        writer.flush();
        writer.close();
        os.close();
    }
}


























