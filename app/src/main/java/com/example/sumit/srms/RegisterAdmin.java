package com.example.sumit.srms;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RegisterAdmin extends AppCompatActivity {


    private FirebaseDatabase db;
    private DatabaseReference table_reg, table_login;

    String userId;
    EditText txt_enrollment, txt_name, txt_email, txt_password;
    EditText spinner_role, spinner_course, spinner_semester, spinner_division, spinner_batch, spinner_electivesubject;
    Button btn_add_user, btn_update_user, btn_delete_user, btn_search_user;
    LinearLayout student_views;
    Map<String, Object> values_reg, values_login;
    List<String> semester_array, division_array, batch_array, electivesubject_array;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_admin);

        //Initialization Part
        db = FirebaseDatabase.getInstance();
        table_reg = db.getReference("registration_m");
        table_login = db.getReference("login_m");
        txt_enrollment = findViewById(R.id.txt_enrollment);
        txt_name = findViewById(R.id.txt_name);
        txt_email = findViewById(R.id.txt_email);
        txt_password = findViewById(R.id.txt_password);
        spinner_role = findViewById(R.id.spinner_role);
        spinner_course = findViewById(R.id.spinner_course);
        spinner_semester = findViewById(R.id.spinner_semester);
        spinner_division = findViewById(R.id.spinner_division);
        spinner_batch = findViewById(R.id.spinner_batch);
        spinner_electivesubject = findViewById(R.id.spinner_elective_subject);
        btn_add_user = findViewById(R.id.btn_AddUser);
        btn_update_user = findViewById(R.id.btn_UpdateUser);
        btn_delete_user = findViewById(R.id.btn_DeleteUser);
        btn_search_user = findViewById(R.id.btn_SearchUser);
        student_views = findViewById(R.id.student_views);


        btn_add_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                    if (txt_name.getText().toString().trim().length() == 0 || txt_email.getText().toString().trim().length() == 0 || txt_password.getText().toString().trim().length() == 0)
                    {
                        Toast.makeText(RegisterAdmin.this, "Error : Please Fill The Required Fields!!", Toast.LENGTH_SHORT).show();
                    }
                    else
                    {
                        String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";
                        if(txt_email.getText().toString().matches(emailPattern)) {
                            InitializeData();
                            String key = table_reg.push().getKey();
                            table_login.child(key).setValue(values_login);
                            table_reg.child(key).setValue(values_reg).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(RegisterAdmin.this, "Record Inserted Successfully!!", Toast.LENGTH_SHORT).show();
                                   // clear();
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Toast.makeText(RegisterAdmin.this, "Error : Something Went Wrong While Inserting Record!!", Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                        else
                        {
                            Toast.makeText(RegisterAdmin.this,"Error : Please Enter Valid Email Address!!", Toast.LENGTH_SHORT).show();
                        }
                    }
                }
        });



       // btn_search_user.setOnClickListener(this);
//        btn_update_user.setOnClickListener(this);
//        btn_delete_user.setOnClickListener(this);
//        spinner_role.setOnItemSelectedListener(this);
//        spinner_course.setOnItemSelectedListener(this);
//        spinner_semester.setOnItemSelectedListener(this);
    }

    private void InitializeData() {
        values_reg = new HashMap<>();
        values_login = new HashMap<>();
        String selectedItem = spinner_role.getText().toString();

        switch (selectedItem)
        {
            case "Student" :
                values_reg.put("user_role", spinner_role.getText().toString());
                values_reg.put("enrollment_number", txt_enrollment.getText().toString());
                values_reg.put("name", txt_name.getText().toString());
                values_reg.put("email", txt_email.getText().toString());
                values_login.put("email", txt_email.getText().toString());
                values_login.put("password", txt_password.getText().toString());
                values_reg.put("course_name", spinner_course.getText().toString());
                values_reg.put("semester_name", spinner_semester.getText().toString());
                values_reg.put("division_name", spinner_division.getText().toString());
                values_reg.put("batch_name", spinner_batch.getText().toString());
                values_reg.put("elective_subject", spinner_electivesubject.getText().toString());
                break;

            case "Faculty" :
                values_reg.put("user_role", spinner_role.getText().toString());
                values_reg.put("name", txt_name.getText().toString());
                values_reg.put("email", txt_email.getText().toString());
                values_login.put("email", txt_email.getText().toString());
                values_login.put("password", txt_password.getText().toString());
                values_reg.put("course_name", spinner_course.getText().toString());
                break;

            case "Director" :
                values_reg.put("user_role", spinner_role.getText().toString());
                values_reg.put("name", txt_name.getText().toString());
                values_reg.put("email", txt_email.getText().toString());
                values_login.put("email", txt_email.getText().toString());
                values_login.put("password", txt_password.getText().toString());
                values_reg.put("course_name", spinner_course.getText().toString());

                Query dir_name = db.getReference("course_m").orderByChild("course_name").equalTo(spinner_course.getText().toString());
                dir_name.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot)
                    {
                        if(snapshot.exists())
                        {
                            Map<String,Object> course_name = new HashMap<String,Object>();
                            course_name.put("director_name", txt_name.getText().toString());
                            for (DataSnapshot data : snapshot.getChildren()) {
                                db.getReference("course_m").child(data.getKey()).updateChildren(course_name).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        Toast.makeText(RegisterAdmin.this, "Director Allocated To Course!!", Toast.LENGTH_LONG);
                                    }
                                });
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });


                break;

            default :
                Toast.makeText(RegisterAdmin.this, "Error : Please Select Correct User Role!!", Toast.LENGTH_SHORT).show();
        }
    }

    public void clear()
    {
        spinner_role.setText("");
        txt_name.setText("");
        txt_email.setText("");
        txt_password.setText("");
        txt_enrollment.setText("");
        spinner_course.setText("");
        spinner_semester.setText("");
        spinner_division.setText("");
    }


}