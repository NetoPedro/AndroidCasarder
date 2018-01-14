package mainapplication.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import mainapplication.R;
import mainapplication.adapter.EmailsAdapter;

public class InviteFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_friends);

        RecyclerView equipmentView = (RecyclerView) findViewById(R.id.emails_view);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        equipmentView.setLayoutManager(layoutManager);
        equipmentView.setHasFixedSize(true);
        equipmentView.setNestedScrollingEnabled(false);
        final EmailsAdapter adapter = new EmailsAdapter(this);
        equipmentView.setAdapter(adapter);
        final EditText emailField = (EditText) findViewById(R.id.email_field);
        Button newInvite = (Button) findViewById(R.id.confirm_button);
        newInvite.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                adapter.updateData(emailField.getText().toString());
                emailField.setText("");
            }
        });
        final Intent extras = getIntent();
        Button continueButton = (Button) findViewById(R.id.continue_button);
        continueButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(InviteFriendsActivity.this, FinishBookingActivity.class).putExtra("facility", extras.getSerializableExtra("facility"))
                        .putExtra("timeslot", extras.getSerializableExtra("timeslot"))
                        .putExtra("equipments", extras.getSerializableExtra("equipments"))
                        .putExtra("invites", adapter.mEmails.toArray(new String[adapter.mEmails.size()])));
            }
        });
    }
}
