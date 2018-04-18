package com.example.sumit.IncomeTaxCalculator.Tax;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;


import com.example.sumit.IncomeTaxCalculator.R;

public class Main4Activity extends AppCompatActivity {
    private Button button_calc;
    private EditText salary;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);
        onListnerOnButton();
    }

    private void onListnerOnButton() {radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        salary = (EditText)findViewById(R.id.editText);
        button_calc = (Button)findViewById(R.id.button3);
        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Toast.makeText(Main2Activity.this , salary.getText(), Toast.LENGTH_SHORT).show();
                int selectedid = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)findViewById(selectedid);
                String sal = salary.getText().toString();
                double sal1 = Double.parseDouble(sal);
                TextView t1 = (TextView)findViewById(R.id.editText5);

                Main4Activity.TaxCalcalutor tc = new Main4Activity.TaxCalcalutor();
                double tax = tc.getTaxFromSalary(sal1, (String) radioButton.getText());
                //Toast.makeText(Main4Activity.this , tax + "" , Toast.LENGTH_SHORT).show();
                t1.setText(tax+"");
            }
        });}


    public class TaxCalcalutor {

        double ONE_LAKH = 100*1000;
        double TEN_LAKHS = 10*ONE_LAKH;
        double FIVE_LAKHS = 5*ONE_LAKH;

        public double getTaxFromSalary(double salary, String salaryType){
            double annualSalary;

            switch (salaryType){
                case "WEEKLY" :  annualSalary = salary * 52;  //Assuming 1 year has 52 weeks.
                    break;

                case "MONTHLY" : annualSalary = salary * 12;
                    break;

                case "YEARLY"  : annualSalary = salary;
                    break;

                default: annualSalary = salary; break;
            }

            double taxPayable = 0;
            if (annualSalary > TEN_LAKHS){
                taxPayable += (annualSalary-TEN_LAKHS)*0.3; //Apply 30% tax on the salary above 10 lakhs
                annualSalary = TEN_LAKHS;
            }
            if (annualSalary > FIVE_LAKHS){
                taxPayable += (annualSalary-FIVE_LAKHS)*0.2; // Apply 20% tax on salary between 5 lakh and 10 lakhs
                annualSalary = FIVE_LAKHS;
            }
            return taxPayable;
        }

    }


}