package com.example.sumit.IncomeTaxCalculator;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import  android.widget.RadioButton;
import android.widget.RadioGroup;

public class Main2Activity extends AppCompatActivity {
    private Button button_calc;
    private EditText salary;
    private RadioGroup radioGroup;
    private RadioButton radioButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main2);
        onListnerOnButton();
    }
    public void onListnerOnButton(){
        radioGroup = (RadioGroup)findViewById(R.id.radioGroup);
        salary = (EditText)findViewById(R.id.editText);
        button_calc = (Button)findViewById(R.id.button3);
        button_calc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedid = radioGroup.getCheckedRadioButtonId();
                radioButton = (RadioButton)findViewById(selectedid);
                String sal = salary.getText().toString();
                double sal1 = Double.parseDouble(sal);
                TextView t1 = (TextView)findViewById(R.id.editText5);

                TaxCalcalutor tc = new TaxCalcalutor();
                double tax = tc.getTaxFromSalary(sal1, (String) radioButton.getText());
                //Toast.makeText(Main2Activity.this , tax + "" , Toast.LENGTH_SHORT).show();
                t1.setText(tax+"");
            }
        });}


    public class TaxCalcalutor {

        double ONE_LAKH = 100*1000;
        double TEN_LAKHS = 10*ONE_LAKH;
        double FIVE_LAKHS = 5*ONE_LAKH;
        double TWO_AND_A_HALF_LAKHS = 2.5*ONE_LAKH;

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
            if (annualSalary > TWO_AND_A_HALF_LAKHS) {
                taxPayable += (annualSalary-TWO_AND_A_HALF_LAKHS)*0.05; // Apply 5% tax on the salary between 2.5 lakhs and 5 lakhs
                annualSalary = TWO_AND_A_HALF_LAKHS; //This line is not required but still, for continuity it is there.
            }
            return taxPayable;
        }

    }

}
